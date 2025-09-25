# Travel Chat Assistant

A friendly AI-powered chat assistant to help you plan your next trip. It exposes a simple REST API backed by Spring Boot and LangChain4j, with chat memory stored in MongoDB and basic input/output guardrails.

## Overview
- Tech stack: Java 24, Spring Boot 3.5.5, Gradle (Kotlin DSL), LangChain4j 1.4.0
- AI provider: OpenAI via LangChain4j OpenAI starter
- Persistence: MongoDB (chat memory stored via a custom ChatMemoryStore)
- API: REST endpoints under /chat
- Containerization: Dockerfile and docker-compose.yml provided

Features
- Session-scoped chat with memory persisted to MongoDB
- Input guardrail (OffensiveLanguageGuardrail) blocks messages with offensive terms
- Output guardrail (PiiGuardrail) blocks model responses that contain PII such as emails and phone numbers

## Requirements
- JDK 24 (project config uses toolchain Java 24)
- Internet access (to call OpenAI APIs)
- OpenAI API key
- MongoDB (local instance or via Docker Compose)
- Optional: Docker and Docker Compose (to run everything in containers)

## Configuration (Environment Variables)
- OPENAI_API_KEY: Your OpenAI API key (required)
- SPRING_DATA_MONGODB_URI: Mongo connection string when running locally without docker-compose, e.g. mongodb://localhost:27017/travel
- Optional (container only): JAVA_OPTS for JVM flags (default -Xms256m -Xmx512m)

The application also sets the following in application.properties:
- langchain4j.open-ai.chat-model.model-name: gpt-4.1-nano
- langchain4j.open-ai.chat-model.log-requests: true
- langchain4j.open-ai.chat-model.log-responses: true

## Getting Started

### Clone and build
- On any platform with Git and JDK 24:
  - ./gradlew clean build (Linux/macOS)
  - gradlew.bat clean build (Windows)

This compiles the code and runs the test suite.

### Run locally (Gradle)
1) Start MongoDB separately or use an existing instance.
2) Export environment variables and start the app:
- Linux/macOS:
  - export OPENAI_API_KEY=your_key_here
  - export SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/travel"
  - ./gradlew bootRun
- Windows (PowerShell):
  - $env:OPENAI_API_KEY="your_key_here"
  - $env:SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/travel"
  - .\gradlew.bat bootRun

Notes
- The application listens on port 8080.
- The Gradle bootRun task enables remote debugging on port 5005 (address 0.0.0.0:5005).

### Run with Docker Compose
Docker Compose will build the app image, run MongoDB, and wire the app to the database.

Requirements
- Docker and Docker Compose installed

Commands
- Linux/macOS:
  - export OPENAI_API_KEY=your_key_here
  - docker compose up --build
- Windows (PowerShell):
  - $env:OPENAI_API_KEY="your_key_here"
  - docker compose up --build

This will:
- Build the app using the provided Dockerfile
- Start the app on localhost:8080
- Start MongoDB on localhost:27017 with a persisted docker volume

To stop and remove containers/volumes: docker compose down -v

### Run with plain Docker (without Compose)
1) Build the JAR and Docker image
- ./gradlew clean bootJar
- docker build -t travel-chat-assistant .

2) Run MongoDB
- docker run -d --name mongodb -p 27017:27017 -v mongo_data:/data/db mongo:8.0.14

3) Run the app
- docker run --rm -p 8080:8080 --env OPENAI_API_KEY=your_key_here --env SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/travel travel-chat-assistant

## API
Base URL: http://localhost:8080

- POST /chat/
  - Starts a new chat session (session-scoped). Returns HTTP 201 Created with empty body.

- POST /chat/messages
  - Sends a user message and appends the AI response to the session history.
  - Request body (application/json):
    { "text": "I want to visit Japan in spring." }
  - Success: HTTP 200 OK with empty body.
  - Errors: HTTP 400 with body { "message": "..." } when a guardrail blocks the input/output.

- GET /chat/messages
  - Returns the current session message history as a JSON array:
    [
      { "index": 0, "type": "AI", "text": "Hello! ..." },
      { "index": 1, "type": "USER", "text": "I want to visit Japan in spring." },
      { "index": 2, "type": "AI", "text": "Great choice! ..." }
    ]

Message type is one of: AI, USER.

Guardrails
- OffensiveLanguageGuardrail: returns 400 if user input contains an offensive term.
- PiiGuardrail: returns 400 if the AI response appears to contain an email address or phone number.

## Scripts and Tasks
Gradle wrapper commands
- ./gradlew bootRun – run the application (debug on 5005)
- ./gradlew test – run unit tests
- ./gradlew build – build with tests
- ./gradlew bootJar – build executable JAR at build/libs/app.jar

Shell scripts
- build-docker.sh – helper to build the Docker image (runs gradle and docker build). Note: verify and adapt to your environment if needed.

Containers
- Dockerfile – runs the Spring Boot fat JAR (app.jar) with optional JAVA_OPTS
- docker-compose.yml – brings up the app and MongoDB with appropriate environment variables

## Project Structure
- src/main/java/... – Spring Boot application, controller, models, services, guardrails
  - ai.rodolfomendes.travel.TravelChatAssistantApplication – main entry point
  - ai.rodolfomendes.travel.chat.controller.ChatController – REST endpoints
  - ai.rodolfomendes.travel.chat.service.TravelChatAssistant – LangChain4j AI service interface
  - ai.rodolfomendes.travel.chat.service.MemoryConfiguration – wires MongoDB chat memory provider
  - ai.rodolfomendes.travel.chat.service.MongoDbChatMemoryProvider/Store – Mongo-backed chat memory
  - ai.rodolfomendes.travel.chat.service.OffensiveLanguageGuardrail – input guardrail
  - ai.rodolfomendes.travel.chat.service.PiiGuardrail – output guardrail
  - ai.rodolfomendes.travel.chat.model.* – Chat, Message, etc.
- src/main/resources/
  - application.properties – Spring and LangChain4j configuration
  - ai/.../assistant-system-message.txt – system prompt used by the assistant
  - ai/.../greetings.txt – greeting template used at chat start
- src/test/java/... – unit and integration tests
- build.gradle.kts – Gradle configuration (plugins, dependencies, tasks)
- Dockerfile, docker-compose.yml – containerization
- gradlew, gradlew.bat, gradle/wrapper – Gradle wrapper

## Tests
- Run tests: ./gradlew test (or .\gradlew.bat test on Windows)
- Notable tests:
  - PiiGuardrailTest – verifies that PII in AI output is blocked
  - TravelChatAssistantApplicationTests – Spring Boot context test

## Notes
- The app uses session-scoped Chat; each client should maintain a session (e.g., with cookies) to keep a separate chat history per session. If you use a stateless client, you may see a new session on each request.
- To switch to in-memory chat memory (non-persistent), MemoryConfiguration shows a commented example using InMemoryChatMemoryProvider.

## License
TODO: Add a LICENSE file and specify the license (e.g., MIT, Apache-2.0). Until then, the default is unspecified.

## Roadmap / TODOs
- Decide and document the license
- Add API examples in OpenAPI/Swagger format
- Provide CI workflow for build and tests
- Add more robust moderation guardrails and configurable model name
- Provide a small frontend example to interact with the API
