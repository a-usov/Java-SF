import de.undercouch.gradle.tasks.download.Download

plugins {
    java
    application
    id("com.github.spotbugs") version "2.0.0"
    checkstyle
    pmd
    id("de.undercouch.download") version "4.0.0"
    antlr
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    compileOnly("org.ow2.asm:asm:5.0.3")
    implementation("com.google.guava:guava:23.5-jre")
    antlr("org.antlr:antlr4:4.5")
}

application {
    mainClassName = "jsf.App"
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }

    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
}

task<Download>("download-google-style") {
    src("https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml")
    dest(File("config/checkstyle/checkstyle.xml"))
    overwrite(false)
}

tasks {
    "checkstyleMain" {
        dependsOn("download-google-style")
    }

    spotbugsMain {
        reports.xml.isEnabled = false
        reports.html.isEnabled = true
    }

    generateGrammarSource {
        arguments = arguments + listOf("-visitor", "-no-listener")
    }
}

checkstyle {
    toolVersion = "8.24"
}

tasks.withType<Checkstyle> {
    exclude("**jsf/jsfVisitor**", "**jsf/jsfParser**", "**jsf/jsfBaseVisitor*", "**jsf/jsfLexer**", "**domain/type/TypeSpecificOpcodes**")
    ignoreFailures = false
    maxWarnings = 0
}

pmd {
    ruleSets = listOf()
    ruleSetFiles = files("config/pmd/ruleSet.xml")
}

spotbugs {
    effort = "max"
    excludeFilter = File("config/spotbugs/excludefilter.xml")
}