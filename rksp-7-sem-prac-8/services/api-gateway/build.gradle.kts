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
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.1.3")

    implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.1.5")
    implementation("org.springframework.boot:spring-boot-starter-security:3.3.4")
    implementation("org.springframework.security:spring-security-oauth2-client:6.3.3")
    implementation("org.springframework.security:spring-security-oauth2-jose:6.3.3")
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")



    implementation("org.projectlombok:lombok:1.18.28")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

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
    archiveBaseName.set("api-gateway")
}