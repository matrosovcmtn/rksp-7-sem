plugins {
    application
    java
    id("org.springframework.boot") version "3.3.5"
}

apply(plugin = "io.spring.dependency-management")
apply(plugin = "java")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")
    implementation("org.springframework.cloud:spring-cloud-starter-config:4.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.1.3")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    implementation("org.springframework.boot:spring-boot-starter-test")



    implementation("org.projectlombok:lombok:1.18.28")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

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
    archiveBaseName.set("delivery-service")
}