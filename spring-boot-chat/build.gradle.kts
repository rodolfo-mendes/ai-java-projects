plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ai.rodolfomendes"
version = "0.0.1-SNAPSHOT"
description = "A simple Spring Boot chat application"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Langchain4j and Spring integration dependencies
	implementation("dev.langchain4j:langchain4j-spring-boot-starter:1.6.0-beta12")
	implementation("dev.langchain4j:langchain4j-open-ai-spring-boot-starter:1.6.0-beta12")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
