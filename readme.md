# Rate Limiting in Spring Boot

Step-by-step practice repo for implementing rate limiting in Spring Boot using different approaches.

## Stack

- Java 25 (Temurin)
- Spring Boot 4.1.0
- Maven

## Prerequisites

- JDK 25 installed at `/usr/lib/jvm/temurin-25-jdk-amd64`
- Maven (`mvn`) or the included `./mvnw` wrapper

## Run

This project needs **Java 25**. Your shell may default to Java 21 (`JAVA_HOME` in `~/.bashrc`). Point at Java 25 for this session, then start the app:

```bash
export JAVA_HOME=/usr/lib/jvm/temurin-25-jdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

./mvnw spring-boot:run
```

Or with system Maven:

```bash
export JAVA_HOME=/usr/lib/jvm/temurin-25-jdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

mvn spring-boot:run
```

App starts on `http://localhost:8080`.

Check versions:

```bash
java --version   # should show 25
mvn --version    # should show Java version: 25 (command is mvn, not maven)
```

## Java environment (optional)

**Cursor / VS Code** — `.vscode/settings.json` sets `JAVA_HOME` and Java 25 for the integrated terminal and language server automatically. Open a **new** terminal after opening this folder.

**External terminal (direnv)** — install [direnv](https://direnv.net/), then add this line to `~/.bashrc` (once):

```bash
eval "$(direnv hook bash)"
```

Reload the shell (`source ~/.bashrc` or open a new terminal), `cd` into this project, and run `direnv allow` once. The `.envrc` file sets Java 25 for this directory only.
