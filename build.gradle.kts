import io.github.andreabrighi.gradle.gitsemver.conventionalcommit.ConventionalCommit

plugins {
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.dokka)
    alias(libs.plugins.taskTree)
    alias(libs.plugins.kotlin.jvm)
}

group = "org.example"

gitSemVer {
    maxVersionLength.set(20)
    buildMetadataSeparator.set("-")
    commitNameBasedUpdateStrategy(ConventionalCommit::semanticVersionUpdate)
}

buildscript {
    dependencies {
        classpath(libs.convetional)
    }
}

allprojects {
    // apply(plugin = "org.danilopianini.gradle-kotlin-qa")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
