plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ai.rodolfomendes"
version = "0.0.1-SNAPSHOT"
description = "A friendly chat assistant to help you plan your next trip"

val langchain4jVersion = "1.4.0"

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
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("dev.langchain4j:langchain4j:$langchain4jVersion")
	implementation("dev.langchain4j:langchain4j-open-ai:$langchain4jVersion")
	implementation("dev.langchain4j:langchain4j-spring-boot-starter:${langchain4jVersion}-beta10")
	implementation("dev.langchain4j:langchain4j-open-ai-spring-boot-starter:${langchain4jVersion}-beta10")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	jvmArgs = listOf(
		"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005"
	)
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}
