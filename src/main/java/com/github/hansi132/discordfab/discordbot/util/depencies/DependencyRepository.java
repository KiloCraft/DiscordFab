package com.github.hansi132.discordfab.discordbot.util.depencies;

/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Slightly changed copy of Dependency class from https://github.com/lucko/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/dependencies/DependencyRepository.java
 * */
public enum DependencyRepository {

    /**
     * Maven Central.
     */
    MAVEN_CENTRAL("https://repo1.maven.org/maven2/"),

    /*
    * JCenter
    * **/
    JCENTER("https://jcenter.bintray.com/");

    private final String url;

    DependencyRepository(String url) {
        this.url = url;
    }

    /**
     * Opens a connection to the given {@code dependency}.
     *
     * @param dependency the dependency to download
     * @return the connection
     * @throws IOException if unable to open a connection
     */
    protected URLConnection openConnection(Dependency dependency) throws IOException {
        URL dependencyUrl = new URL(this.url + dependency.getMavenRepoPath());
        return dependencyUrl.openConnection();
    }

    /**
     * Downloads the raw bytes of the {@code dependency}.
     *
     * @param dependency the dependency to download
     * @return the downloaded bytes
     * @throws DependencyDownloadException if unable to download
     */
    public byte[] downloadRaw(Dependency dependency) throws DependencyDownloadException {
        try {
            URLConnection connection = openConnection(dependency);
            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = ByteStreams.toByteArray(in);
                if (bytes.length == 0) {
                    throw new DependencyDownloadException("Empty stream");
                }
                return bytes;
            }
        } catch (Exception e) {
            throw new DependencyDownloadException(e);
        }
    }

    /**
     * Downloads the raw bytes of the {@code dependency}, and ensures the downloaded
     * bytes match the checksum.
     *
     * @param dependency the dependency to download
     * @return the downloaded bytes
     */
    public byte[] download(Dependency dependency) throws DependencyDownloadException {
        byte[] bytes = downloadRaw(dependency);

        return bytes;
    }

    /**
     * Downloads the the {@code dependency} to the {@code file}
     *
     * @param dependency the dependency to download
     * @param file the file to write to
     * @throws DependencyDownloadException if unable to download
     */
    public void download(Dependency dependency, Path file) throws DependencyDownloadException {
        try {
            Files.write(file, download(dependency));
        } catch (IOException e) {
            throw new DependencyDownloadException(e);
        }
    }

}

