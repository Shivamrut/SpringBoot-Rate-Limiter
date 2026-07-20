# Trivial Spring Boot learnings

Short, reusable notes. Numbered so new items can be appended.

---

## 1. Adding a shared path prefix to controllers (e.g. `/v1`)

Goal: expose controllers under a common prefix without repeating it on every method (or only on some controllers).

### Option A — Global context path (`application.yaml`)

```yaml
server:
  servlet:
    context-path: /v1
```

- `server` must be a **top-level** key (sibling of `spring`, not nested under it).
- Mapping `/quotes/random` is served as `/v1/quotes/random`.
- **Caveat:** applies to **every** endpoint in the app (health, actuator, error pages, etc. all sit under that prefix).

Use when the whole application should live under one base path.

### Option B — Class-level `@RequestMapping`

```java
@RestController
@RequestMapping("/v1")
public class QuoteController {

  @GetMapping("/quotes/random")
  public ... random() { ... }
}
```

- Final path: `/v1/quotes/random`.
- Controllers that should stay unprefixed simply omit `@RequestMapping("/v1")`.
- Or put the full path on the method: `@GetMapping("/v1/quotes/random")` (more repetition).

Use when only some APIs are versioned / prefixed.

### Option C — Selective prefix via `WebMvcConfigurer`

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix(
        "/v1",
        HandlerTypePredicate.forBasePackage("com.example.app.api")
    );
  }
}
```

- Spring prepends `/v1` to mappings for controllers that match the predicate (package, annotation, etc.).
- Controllers outside the predicate stay unprefixed.
- Import: `org.springframework.web.method.HandlerTypePredicate`.

Use when many controllers share a prefix and you want one rule instead of annotating each class.

### Quick choice

| Approach | Scope | Typical use |
|---|---|---|
| `server.servlet.context-path` | Entire app | Single base path for everything |
| `@RequestMapping` on controller | Per class / method | Mixed prefixed and unprefixed APIs |
| `PathMatchConfigurer.addPathPrefix` | Selected controller types | Many controllers, one central rule |

---

## 2. Java `record` (immutable data carriers)

### What it is

A `record` is a compact class for **just data**: immutable fields plus generated constructor, accessors, `equals`, `hashCode`, and `toString`.

```java
public record HealthResponse(HealthStatus status, Instant timestamp) {}
```

Roughly equivalent to a `final` class with:

- `private final` fields for each component
- Canonical constructor
- Accessors named after fields: `status()`, `timestamp()` (not `getStatus()`)
- Value-based `equals` / `hashCode` / `toString`

### Class vs record

| | Class | Record |
|---|---|---|
| Purpose | General type (can hold behavior + mutable state) | Immutable data carrier |
| Fields | You define; often mutable | Implicitly `final` |
| Boilerplate | Getters/setters (or Lombok) | Generated |
| Inheritance | Can extend a class | Cannot extend a class; can implement interfaces |
| Jackson (Spring) | Needs visible props / getters | Uses record components |

### When to use which

| Use a **record** | Use a **class** |
|---|---|
| API request/response DTOs | JPA/entities you mutate |
| Simple value objects | Types that need inheritance or non-final fields |
| “Bag” with no real behavior | Rich domain objects with methods that change state |

### Spring tip

Modern Spring/Jackson serializes records to JSON using component names (`status`, `timestamp`). No Lombok required for that DTO.

---
