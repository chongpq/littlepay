plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    jacoco
}

group = "com.littlepay"
version = "1.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
}

application {
    // Define the main class for the application.
    mainClass.set("com.littlepay.App")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to application.mainClass))
    }
}
