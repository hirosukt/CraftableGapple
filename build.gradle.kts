import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask.JarUrl
import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "1.6.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.ben-manes.versions") version "0.41.0"
    id("com.palantir.git-version") version "0.12.3"
    id("dev.s7a.gradle.minecraft.server") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jmailen.kotlinter") version "3.8.0"
}

val gitVersion: Closure<String> by extra

val pluginVersion: String by project.ext

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)

dependencies {
    shadowImplementation(kotlin("stdlib"))
    compileOnly("org.spigotmc:spigot-api:$pluginVersion-R0.1-SNAPSHOT")
}

kotlinter {
    ignoreFailures = true
}

configure<BukkitPluginDescription> {
    main = "com.github.hirosukt.Main"
    version = gitVersion()
    apiVersion = "1.13"
}

tasks.withType<ShadowJar> {
    configurations = listOf(shadowImplementation)
    archiveClassifier.set("")
    relocate("kotlin", "com.github.hirosukt.libs.kotlin")
    relocate("org.intellij.lang.annotations", "com.github.hirosukt.libs.org.intellij.lang.annotations")
    relocate("org.jetbrains.annotations", "com.github.hirosukt.libs.org.jetbrains.annotations")
}

tasks.named("build") {
    dependsOn("shadowJar")
}

listOf(
    "8" to "1.8.8",
    "9" to "1.9.4",
    "10" to "1.10.2",
    "11" to "1.11.2",
    "12" to "1.12.2",
    "13" to "1.13.2",
    "14" to "1.14.4",
    "15" to "1.15.2",
    "16" to "1.16.5",
    "17" to "1.17.1",
    "18" to "1.18.2",
    "19" to "1.19.2"
).forEach { (name, version) ->
    task<LaunchMinecraftServerTask>("buildAndLaunchServer-$version") {
        dependsOn("build")
        doFirst {
            copy {
                from(buildDir.resolve("libs/${project.name}.jar"))
                into(buildDir.resolve("MinecraftServer-$version/plugins"))
            }
        }

        jarUrl.set(JarUrl.Paper(version))
        jarName.set("server.jar")
        serverDirectory.set(buildDir.resolve("MinecraftServer-$version"))
        nogui.set(true)
        agreeEula.set(true)
    }
}

task<SetupTask>("setup")