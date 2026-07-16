# Rate Limiting in Spring Boot

Step-by-step practice repo for implementing rate limiting in Spring Boot using different approaches.

## Stack

- Java 25 (Temurin)
- Spring Boot 4.1.0
- Maven

## Prerequisites

- JDK 25 installed at `/usr/lib/jvm/temurin-25-jdk-amd64`
- Maven (or use the included `./mvnw` wrapper)

## Run

```bash
./mvnw spring-boot:run
```

App starts on `http://localhost:8080`.

## Java environment

This project uses Java 25. Other projects on the machine can stay on Java 17/21.

**Cursor / VS Code** — `.vscode/settings.json` sets `JAVA_HOME` and Java 25 for the integrated terminal and language server automatically.

**External terminal (optional)** — install [direnv](https://direnv.net/), add `eval "$(direnv hook bash)"` to `~/.bashrc`, then run `direnv allow` once in this directory. The `.envrc` file handles the rest.
