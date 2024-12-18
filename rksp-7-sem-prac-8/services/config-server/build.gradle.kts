plugins {
    application
    java
    id("org.springframework.boot") version "3.2.5"
}

apply(plugin = "io.spring.dependency-management")
apply(plugin = "java")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-config:4.1.3")
    implementation("org.springframework.cloud:spring-cloud-config-server:4.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.1.3")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.App"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.example.App"
    }
}

tasks.bootJar {
    archiveBaseName.set("config-server")
}