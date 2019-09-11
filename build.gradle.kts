import de.undercouch.gradle.tasks.download.Download

plugins {
    java
    application
    id("com.github.spotbugs") version "2.0.0"
    checkstyle
    pmd
    id("de.undercouch.download") version "4.0.0"
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
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
}

checkstyle {
    toolVersion = "8.24"
}

pmd {
    ruleSetFiles = files("config/pmd/ruleSet.xml")
}

spotbugs {
    effort = "max"
}
