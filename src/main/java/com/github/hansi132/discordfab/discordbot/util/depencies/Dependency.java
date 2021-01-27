package com.github.hansi132.discordfab.discordbot.util.depencies;

import com.google.common.collect.ImmutableList;

import java.util.Base64;

/**
* Slightly changed copy of Dependency class from https://github.com/lucko/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/dependencies/Dependency.java
* */
public enum Dependency {

    MYSQL_DRIVER(
            "mysql",
            "mysql-connector-java",
            "5.1.48"
    ),
    JDA(
            "net{}dv8tion",
            "JDA",
            "4.1.1_150"
    ),
    GOOGLE_CODE(
            "com{}google{}code{}findbugs",
            "jsr305",
            "3.0.2"
    ),
    ANNOTATIONS(
            "org{}jetbrains",
            "annotations",
            "16.0.1"
    ),
    LOGGER(
            "org{}slf4j",
            "slf4j-api",
            "1.7.25"
    ),
    NEOVISIONARIES(
            "com{}neovisionaries",
            "nv-websocket-client",
            "2.9"
    ),
    OKHTTP3(
            "com{}squareup{}okhttp3",
            "okhttp",
            "3.13.0"
    ),
    MINNCED(
            "club{}minnced",
            "discord-webhooks",
            "0.3.0"
    ),
    JSON(
            "org{}json",
            "json",
            "20180813"
    ),
/*    MINNCED(
            "club{}minnced",
            "opus-java",
            "1.0.4"
    ),*/
    APACHE_COMMONS(
            "org{}apache{}commons",
            "commons-collections4",
            "4.1"
    ),
    TROVE4J(
            "net{}sf{}trove4j",
            "trove4j",
            "3.0.3"
    ),
    FASTERXML(
            "com{}fasterxml{}jackson{}core",
            "jackson-databind",
            "2.10.1"
    );

    private final String mavenRepoPath;
    private final String version;

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    Dependency(String groupId, String artifactId, String version) {
        this.mavenRepoPath = String.format(MAVEN_FORMAT,
                rewriteEscaping(groupId).replace(".", "/"),
                rewriteEscaping(artifactId),
                version,
                rewriteEscaping(artifactId),
                version
        );
        this.version = version;
    }

    private static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }

    public String getFileName() {
        return name().toLowerCase().replace('_', '-') + "-" + this.version;
    }

    String getMavenRepoPath() {
        return this.mavenRepoPath;
    }

}
