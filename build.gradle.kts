plugins {
    java
    application
    id ("com.github.spotbugs") version "3.0.0"
    checkstyle
    pmd
    antlr
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    compileOnly("org.ow2.asm:asm:7.3.1")
    implementation("com.google.guava:guava:28.2-jre")
    antlr("org.antlr:antlr4:4.8")
}

application {
    mainClassName = "jsf.App"
}

java {
    sourceCompatibility = JavaVersion.VERSION_13
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }

    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks {
    spotbugsMain {
        reports.xml.isEnabled = false
        reports.html.isEnabled = true
    }

    generateGrammarSource {
        arguments = arguments + listOf("-visitor", "-no-listener")
    }
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
    toolVersion = "4.0.0-RC1"
}