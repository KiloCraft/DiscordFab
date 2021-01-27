package com.github.hansi132.discordfab.discordbot.util.depencies;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.DiscordFabMod;
import com.github.hansi132.discordfab.discordbot.util.Constants;
import net.fabricmc.loader.launch.common.FabricLauncherBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * Slightly changed copy of Dependency class from https://github.com/lucko/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/dependencies/DependencyManager.java
 * */
public class DependencyManager {

    /** The path where library jars are cached. */
    private final Path cacheDirectory;
    /** A map of dependencies which have already been loaded. */
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);


    public DependencyManager() {
        this.cacheDirectory = setupCacheDirectory();
    }

    private static Path setupCacheDirectory() {
        Path cacheDirectory = Constants.CONFIG_PATH.resolve("libs");
        try {
            createDirectoriesIfNotExists(cacheDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create libs directory", e);
        }
        return cacheDirectory;
    }

    public static void createDirectoriesIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return;
        }

        Files.createDirectories(path);
    }

    public void loadDependencies(Dependency... dependencies) {
        DiscordFabMod.LOGGER.info("Loading " + dependencies.length + " dependencies...");
        CountDownLatch latch = new CountDownLatch(dependencies.length);
        for (Dependency dependency : dependencies) {
            CompletableFuture.runAsync(() -> {
                try {
                    loadDependency(dependency);
                } catch (Throwable e) {
                    DiscordFabMod.LOGGER.warn("Unable to load dependency " + dependency.name() + ".");
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void loadDependency(Dependency dependency) throws Exception {
        DiscordFabMod.LOGGER.info("Loading " + dependency.getFileName());
        if (this.loaded.containsKey(dependency)) {
            return;
        }

        Path file = downloadDependency(dependency);

        this.loaded.put(dependency, file);
        FabricLauncherBase.getLauncher().propose(file.toUri().toURL());
    }

    private Path downloadDependency(Dependency dependency) throws DependencyDownloadException {
        Path file = this.cacheDirectory.resolve(dependency.getFileName() + ".jar");

        // if the file already exists, don't attempt to re-download it.
        if (Files.exists(file)) {
            return file;
        }

        DependencyDownloadException lastError = null;

        // attempt to download the dependency from each repo in order.
        for (DependencyRepository repo : DependencyRepository.values()) {
            try {
                DiscordFabMod.LOGGER.info("Downloading dependency " + file.getFileName().toString());
                repo.download(dependency, file);
                return file;
            } catch (DependencyDownloadException e) {
                lastError = e;
            }
        }

        throw Objects.requireNonNull(lastError);
    }



}
