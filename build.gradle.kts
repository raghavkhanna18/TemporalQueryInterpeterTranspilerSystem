val group = "uk.ac.imperial.doc.rk4718.tsql"

plugins {
    application
    kotlin("jvm") version "1.3.70"
    antlr
    // id("org.jlleitschuh.gradle.ktlint") version "8.2.0"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

application {
    mainClassName = "tsql.MainKt"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.commons:commons-text:1.8")
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    runtime(group = "org.apache.commons", name = "commons-lang3", version = "3.9")
    antlr("org.antlr:antlr4:4.10.1")
    implementation(files("lib/antlr-4.10.1-complete.jar"))
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.5")
    implementation(group = "org.slf4j", name = "slf4j-log4j12", version = "1.7.5")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

// ktlint {
//     verbose.set(true)
//     outputToConsole.set(true)
//     coloredOutput.set(true)
// }

tasks {
    generateGrammarSource {
        maxHeapSize = "64m"
        outputDirectory = file("src/main/java/antlr")
        arguments = arguments + listOf("-visitor", "-long-messages", "-package", "antlr")
    }

    compileKotlin {
        // dependsOn(addKtlintFormatGitPreCommitHook)
        dependsOn(generateGrammarSource)
        // dependsOn(ktlintMainSourceSetFormat)
    }

    compileTestKotlin {
        dependsOn(generateGrammarSource)
        // dependsOn(ktlintTestSourceSetFormat)
    }

    // ktlintMainSourceSetCheck {
    //     // shouldRunAfter(compileKotlin)
    // }
    //
    // ktlintTestSourceSetCheck {
    //     // shouldRunAfter(compileTestKotlin)
    // }

    test {
        useJUnitPlatform()
    }
}
