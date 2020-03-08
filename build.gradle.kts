plugins {
    java
    application
    id ("com.github.spotbugs") version "4.0.1"
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
    implementation("org.ow2.asm:asm:7.3.1")
    implementation("com.google.guava:guava:28.2-jre")
    antlr("org.antlr:antlr4:4.8")
}

application {
    mainClassName = "jsf.App"
}

tasks {
    generateGrammarSource {
        arguments = arguments + listOf("-visitor", "-no-listener")
    }
}

tasks.withType<Checkstyle> {
    exclude("**jsf/jsfVisitor**", "**jsf/jsfParser**", "**jsf/jsfBaseVisitor*", "**jsf/jsfLexer**")
    ignoreFailures = false
    maxWarnings = 0
}

pmd {
    ruleSets = listOf()
    ruleSetFiles = files("config/pmd/ruleSet.xml")
}

spotbugs {
    setEffort("max")
    setProperty("toolVersion", "4.0.1")
    setProperty("excludeFilter", File("config/spotbugs/excludefilter.xml"))
}