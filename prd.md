# PRD: PulseAPI — Developer API Platform

**Version:** 1.0  
**Status:** Draft  
**Owner:** Product  
**Last updated:** July 2026

---

## 1. Problem Statement

PulseAPI is a developer-facing HTTP API that third-party applications call to fetch content, submit data, and run lightweight processing jobs. As adoption grows, a small number of clients can overwhelm the service, degrade experience for everyone, and drive up infrastructure cost.

The platform needs **fair, predictable access control** so that:

- No single client can monopolize capacity
- Expensive operations are protected more aggressively than cheap reads
- Legitimate clients understand when and why they are throttled
- Operators can reason about traffic patterns and abuse

This document defines the **product surface** (endpoints, behavior, constraints). It intentionally does not prescribe implementation. Technical design (LLD/HLD) follows separately.

---

## 2. Product Vision

PulseAPI should feel like a real public API product: clear contracts, consistent responses, and transparent usage boundaries. A developer integrating with PulseAPI should be able to build, test, and operate their integration without surprises at scale.

---

## 3. Goals

| # | Goal |
|---|------|
| G1 | Protect service stability under traffic spikes and abusive usage |
| G2 | Enforce different usage policies for different types of operations |
| G3 | Give API consumers clear feedback when limits are exceeded |
| G4 | Allow operators to observe per-client usage patterns |
| G5 | Keep the API simple enough to build incrementally, rich enough to exercise real system design tradeoffs |

---

## 4. Non-Goals (v1)

- Billing, payments, or subscription management
- Self-service API key signup or admin dashboard UI
- Geographic routing or multi-region deployment
- OAuth or user login flows (API key only)
- Content moderation workflows beyond basic validation
- SLA guarantees or contractual uptime commitments

---

## 5. Users & Personas

### 5.1 API Consumer (Developer)
Integrates PulseAPI into their application. Cares about reliability, clear error messages, and knowing how many calls they have left.

### 5.2 API Operator (Internal)
Runs the service. Cares about abuse prevention, observability, and the ability to tune limits without redeploying the entire application.

### 5.3 Automated Client (Bot / Script)
High-volume, repetitive caller. May or may not respect retry guidance. The system must remain stable even if this actor misbehaves.

---

## 6. Authentication Model

All business endpoints require an API key.

| Rule | Detail |
|------|--------|
| Header | `X-API-Key: <key>` |
| Missing key | Request is rejected before any business logic runs |
| Invalid key | Request is rejected; response must not reveal whether the key format was wrong vs unknown |
| Key identity | Each key maps to a single **client identity** used for usage tracking and limit enforcement |

### Pre-seeded clients (for development & testing)

| Client Name | API Key | Tier |
|-------------|---------|------|
| Free Dev | `pk_free_dev_001` | Free |
| Startup | `pk_startup_002` | Standard |
| Enterprise | `pk_enterprise_003` | Premium |

Tier affects limits (see Section 9). Keys are static in v1 — no rotation or revocation API.

---

## 7. API Endpoints

Base path: `/v1`  
All responses use `application/json` unless stated otherwise.

---

### 7.1 `GET /health`

**Purpose:** Liveness check for load balancers and monitoring. Confirms the process is up.

**Authentication:** None

**Request:** No parameters

**Success response (200):**
```json
{
  "status": "UP",
  "timestamp": "2026-07-16T12:00:00Z"
}
```

**Notes:**
- Must be extremely lightweight
- Must not depend on downstream systems
- Used by infrastructure; not a developer-facing product endpoint

---

### 7.2 `GET /v1/quotes/random`

**Purpose:** Return one random inspirational quote. Primary "happy path" read endpoint — expected to be the highest-volume call.

**Authentication:** Required

**Success response (200):**
```json
{
  "id": "q-1042",
  "text": "Simplicity is the soul of efficiency.",
  "author": "Austin Freeman"
}
```

**Error responses:**
| Code | Condition |
|------|-----------|
| 401 | Missing or invalid API key |
| 429 | Client has exceeded their rate limit |
| 500 | Unexpected server error |

---

### 7.3 `GET /v1/quotes/{id}`

**Purpose:** Fetch a specific quote by ID. Predictable read with path parameter.

**Authentication:** Required

**Path parameter:** `id` — quote identifier (e.g. `q-1042`)

**Success response (200):**
```json
{
  "id": "q-1042",
  "text": "Simplicity is the soul of efficiency.",
  "author": "Austin Freeman"
}
```

**Error responses:**
| Code | Condition |
|------|-----------|
| 401 | Missing or invalid API key |
| 404 | Quote not found |
| 429 | Rate limit exceeded |
| 500 | Unexpected server error |

---

### 7.4 `POST /v1/quotes`

**Purpose:** Submit a new quote to the platform. Write operation — costlier than reads, higher abuse risk.

**Authentication:** Required

**Request body:**
```json
{
  "text": "The best way to predict the future is to invent it.",
  "author": "Alan Kay"
}
```

**Validation rules:**
| Field | Rule |
|-------|------|
| `text` | Required, 10–500 characters |
| `author` | Required, 1–100 characters |

**Success response (201):**
```json
{
  "id": "q-2048",
  "text": "The best way to predict the future is to invent it.",
  "author": "Alan Kay",
  "status": "accepted"
}
```

**Error responses:**
| Code | Condition |
|------|-----------|
| 400 | Validation failure |
| 401 | Missing or invalid API key |
| 429 | Rate limit exceeded |
| 500 | Unexpected server error |

---

### 7.5 `POST /v1/convert`

**Purpose:** Run a lightweight text processing job. Simulates a **compute-heavy** endpoint (processing delay is intentional product behavior, not a bug).

**Authentication:** Required

**Request body:**
```json
{
  "text": "Rate limiting protects shared systems from unfair usage.",
  "operation": "reverse"
}
```

**Supported operations (v1):**

| Operation | Output |
|-----------|--------|
| `reverse` | Reverses the input string |
| `uppercase` | Converts to uppercase |
| `wordcount` | Returns word count |

**Success response (200):**
```json
{
  "operation": "reverse",
  "input": "Rate limiting protects shared systems from unfair usage.",
  "result": ".egasu riarf morf smetsys derahs stcetorp gnitimil etaR",
  "processingTimeMs": 250
}
```

**Behavioral note:** The service must introduce an artificial delay of **200–300 ms** on every successful request to simulate real compute cost. This delay is a product requirement — it exists so that rate limiting on expensive endpoints has meaningful system design consequences.

**Error responses:**
| Code | Condition |
|------|-----------|
| 400 | Missing `text`, unsupported `operation`, or `text` exceeds 10,000 characters |
| 401 | Missing or invalid API key |
| 429 | Rate limit exceeded |
| 500 | Unexpected server error |

---

### 7.6 `POST /v1/batch/quotes`

**Purpose:** Fetch multiple quotes in a single request. Reduces HTTP overhead for clients but concentrates load — a classic rate limiting design challenge.

**Authentication:** Required

**Request body:**
```json
{
  "ids": ["q-1001", "q-1002", "q-1003"]
}
```

**Validation rules:**
| Field | Rule |
|-------|------|
| `ids` | Required, array of 1–10 quote IDs |

**Success response (200):**
```json
{
  "results": [
    { "id": "q-1001", "text": "...", "author": "..." },
    { "id": "q-1002", "text": "...", "author": "..." },
    { "id": "q-1003", "error": "not_found" }
  ],
  "found": 2,
  "notFound": 1
}
```

**Partial success:** A batch request succeeds (200) even if some IDs are not found. Individual items carry their own result or error.

**Error responses:**
| Code | Condition |
|------|-----------|
| 400 | Validation failure (empty array, more than 10 IDs) |
| 401 | Missing or invalid API key |
| 429 | Rate limit exceeded |
| 500 | Unexpected server error |

---

### 7.7 `GET /v1/usage`

**Purpose:** Let a client inspect their own consumption. Supports developer debugging and operator transparency goals.

**Authentication:** Required (client can only see their own usage)

**Success response (200):**
```json
{
  "clientId": "free-dev",
  "tier": "free",
  "currentWindow": {
    "windowStart": "2026-07-16T12:00:00Z",
    "windowEnd": "2026-07-16T12:01:00Z",
    "endpoints": {
      "GET /v1/quotes/random": { "limit": 30, "used": 18, "remaining": 12 },
      "POST /v1/convert": { "limit": 5, "used": 5, "remaining": 0 }
    }
  }
}
```

**Notes:**
- Returns data for the **current active window** only
- Must reflect the same limits and counts that enforcement uses
- If a client has made no requests in the current window, return zero usage with full remaining quota

**Error responses:**
| Code | Condition |
|------|-----------|
| 401 | Missing or invalid API key |
| 500 | Unexpected server error |

---

## 8. Standard Error Response Format

All error responses (4xx, 5xx) must follow this shape:

```json
{
  "error": {
    "code": "RATE_LIMIT_EXCEEDED",
    "message": "Human-readable explanation of what went wrong.",
    "requestId": "req-abc123"
  }
}
```

| Field | Requirement |
|-------|-------------|
| `code` | Machine-readable, stable string (e.g. `VALIDATION_ERROR`, `UNAUTHORIZED`, `RATE_LIMIT_EXCEEDED`, `NOT_FOUND`, `INTERNAL_ERROR`) |
| `message` | Human-readable; safe to show to developers |
| `requestId` | Unique per request; must appear in server logs for correlation |

---

## 9. Rate Limiting — Functional Requirements

These are **product rules**. How they are enforced is a design decision (LLD/HLD).

### 9.1 Limit Dimensions

| Dimension | Requirement |
|-----------|-------------|
| Per client | Every limit is scoped to the authenticated API key |
| Per endpoint | Different endpoints may have different limits |
| Per tier | Client tier affects limit values |

### 9.2 Endpoint Limit Policy

Limits are expressed as **maximum requests per 60-second rolling window**.

| Endpoint | Free | Standard | Premium |
|----------|------|----------|---------|
| `GET /v1/quotes/random` | 30 / min | 100 / min | 500 / min |
| `GET /v1/quotes/{id}` | 30 / min | 100 / min | 500 / min |
| `POST /v1/quotes` | 5 / min | 20 / min | 100 / min |
| `POST /v1/convert` | 5 / min | 15 / min | 50 / min |
| `POST /v1/batch/quotes` | 10 / min | 30 / min | 100 / min |
| `GET /v1/usage` | 10 / min | 30 / min | 60 / min |
| `GET /health` | Exempt | Exempt | Exempt |

### 9.3 Enforcement Behavior

| # | Requirement |
|---|-------------|
| R1 | When a client is within limits, the request proceeds normally |
| R2 | When a client exceeds a limit, the request is rejected with HTTP 429 |
| R3 | Rate limiting is evaluated **before** business logic executes |
| R4 | A rejected request must **not** count as a successful business operation (e.g. a throttled `POST /v1/quotes` must not create a quote) |
| R5 | A rejected request **may** count toward the limit (design decision — document your choice in LLD) |
| R6 | Limits for `POST /v1/batch/quotes` count as **one request** regardless of how many IDs are in the batch |
| R7 | Each endpoint's limit is independent — exhausting the convert quota must not block quote reads |
| R8 | Unauthenticated requests to protected endpoints are rejected with 401 and are not subject to per-client rate limits |

### 9.4 Client Communication on Throttle

When returning 429, the response must help the client self-correct:

| Field | Location | Requirement |
|-------|----------|-------------|
| `Retry-After` | Response header | Seconds until the client should retry (integer) |
| `X-RateLimit-Limit` | Response header | Maximum requests allowed in the window for this endpoint |
| `X-RateLimit-Remaining` | Response header | Requests remaining in the current window |
| `X-RateLimit-Reset` | Response header | Unix epoch seconds when the current window resets |

These headers should also be present on **successful responses** (2xx) for protected endpoints so clients can proactively slow down.

### 9.5 Usage Endpoint Consistency

The counts shown by `GET /v1/usage` must be **consistent** with the headers on the most recent request and with enforcement decisions. A client must never see `remaining: 5` and then immediately receive 429.

---

## 10. Non-Functional Requirements

### 10.1 Performance

| # | Requirement | Target |
|---|-------------|--------|
| NF1 | Rate limit check overhead per request | < 5 ms at p99 (excluding business logic) |
| NF2 | `GET /health` response time | < 50 ms at p99 |
| NF3 | Read endpoints (`/quotes/*`) response time | < 100 ms at p99 (excluding rate limit check) |
| NF4 | `POST /v1/convert` response time | 200–300 ms artificial delay + < 50 ms overhead |

### 10.2 Availability & Resilience

| # | Requirement |
|---|-------------|
| NF5 | The service must remain responsive when a single client sends sustained max-rate traffic |
| NF6 | The service must define behavior when rate limit state is temporarily unavailable (fail-open vs fail-closed — document in LLD) |
| NF7 | The service must be deployable as multiple instances behind a load balancer with **consistent** limit enforcement across instances |

### 10.3 Correctness

| # | Requirement |
|---|-------------|
| NF8 | Limit enforcement must be correct under concurrent requests from the same client (no overshoot beyond an agreed tolerance — document tolerance in LLD) |
| NF9 | Clock changes or process restarts must not permanently lock out a client or grant unlimited access |
| NF10 | Rate limit state must not leak between clients |

### 10.4 Observability

| # | Requirement |
|---|-------------|
| NF11 | Every request must emit a structured log entry containing: `requestId`, `clientId`, `endpoint`, `httpStatus`, `latencyMs` |
| NF12 | Rate-limited requests (429) must be logged distinctly so operators can identify abuse patterns |
| NF13 | Operators must be able to answer: "How many requests did client X make to endpoint Y in the last hour?" |

### 10.5 Security

| # | Requirement |
|---|-------------|
| NF14 | API keys must not appear in logs |
| NF15 | Error messages must not leak internal implementation details or other clients' data |
| NF16 | Rate limiting must not be bypassable by changing request format (e.g. varying headers, HTTP method) |

### 10.6 Scalability (Design Exercise)

| # | Requirement |
|---|-------------|
| NF17 | The design must identify what state needs to be stored, how it grows with clients and endpoints, and at what point a single-node approach breaks down |
| NF18 | The design must address the "hot key" scenario: one very active client hitting a single endpoint at max rate |

---

## 11. Abuse Scenarios to Design For

These are not test cases — they are real-world patterns the system must survive.

| # | Scenario | Expected System Behavior |
|---|----------|--------------------------|
| A1 | Client sends 1,000 requests/second to `GET /v1/quotes/random` | Service stays up; client receives 429s; other clients unaffected |
| A2 | Client exhausts convert quota, then switches to batch quotes to bypass convert limits | Batch and convert limits are independent; no bypass |
| A3 | Client retries immediately after every 429 without waiting | System remains stable; `Retry-After` is present on every 429 |
| A4 | Client uses multiple API keys from the same tier to multiply throughput | Each key is limited independently (acceptable in v1; flag as known limitation) |
| A5 | Client sends requests with a valid key to `/health` in a loop | Health endpoint remains fast and is not subject to per-client limits |
| A6 | Two application instances receive concurrent requests from the same client at window boundary | Total allowed requests must not significantly exceed the defined limit (document acceptable tolerance in LLD) |

---

## 12. Seed Data Requirements

The application must ship with enough data to exercise all endpoints without external setup.

| Data | Minimum |
|------|---------|
| Pre-loaded quotes | 50 quotes with unique IDs (`q-1001` through `q-1050`) |
| API keys | 3 keys as defined in Section 6 |
| Quote content | Static, in-memory is acceptable for v1 |

---

## 13. Delivery Phases

Work is intentionally phased so each stage produces a working, testable system.

### Phase 1 — API Surface (no rate limiting)
- All endpoints implemented and returning correct responses
- Authentication enforced
- Seed data loaded
- Standard error format in place
- `GET /health` operational

**Exit criteria:** All endpoints manually testable with `curl`. No rate limiting yet.

### Phase 2 — Rate Limiting
- Per-client, per-endpoint, per-tier limits enforced per Section 9
- 429 responses with required headers
- `GET /v1/usage` reflects live counts

**Exit criteria:** Abuse scenarios A1–A3 demonstrably handled.

### Phase 3 — Hardening & Observability
- Multi-instance consistency addressed
- Structured logging complete
- Failure mode behavior defined and tested
- Hot key and concurrency documented

**Exit criteria:** NF1–NF18 addressed in design docs and verified.

---

## 14. Success Metrics

| Metric | Target |
|--------|--------|
| Zero unthrottled abuse-induced downtime in load testing | 100% |
| p99 rate limit check latency | < 5 ms |
| 429 responses include all four required headers | 100% |
| Usage endpoint matches enforcement state | 100% consistency in manual testing |
| All endpoints have documented request/response contracts | 100% |

---

## 15. Open Questions (for LLD / HLD)

These are intentional design gaps. Resolve them in your technical design documents.

1. Where is rate limit state stored, and what happens when that store is down?
2. How is consistency achieved across multiple application instances?
3. What is the acceptable overshoot tolerance at the window boundary under concurrent load?
4. Should throttled requests (429) consume quota, or only successful requests?
5. How will limits be configured — hardcoded, config file, or externalized?
6. What happens when system clock jumps forward or backward?
7. How will you test rate limiting correctness without waiting 60 seconds per test?
8. At what client count does the chosen storage approach need re-architecture?

---

## 16. Appendix: Sample Quote Data

For reference during implementation. IDs `q-1001`–`q-1050` should exist at startup.

| ID | Text | Author |
|----|------|--------|
| q-1001 | Simplicity is the soul of efficiency. | Austin Freeman |
| q-1002 | The best way to predict the future is to invent it. | Alan Kay |
| q-1003 | Any fool can write code that a computer can understand. Good programmers write code that humans can understand. | Martin Fowler |
| q-1004 | First, solve the problem. Then, write the code. | John Johnson |
| q-1005 | Talk is cheap. Show me the code. | Linus Torvalds |

*(Remaining 45 quotes: generate or repeat variations — content does not matter for product correctness as long as IDs are unique and fields are populated.)*

---

## 17. Appendix: Quick Reference — Endpoint Summary

| Method | Path | Auth | Category | Rate Limited |
|--------|------|------|----------|--------------|
| GET | `/health` | No | Ops | No |
| GET | `/v1/quotes/random` | Yes | Read | Yes |
| GET | `/v1/quotes/{id}` | Yes | Read | Yes |
| POST | `/v1/quotes` | Yes | Write | Yes |
| POST | `/v1/convert` | Yes | Compute | Yes |
| POST | `/v1/batch/quotes` | Yes | Batch read | Yes |
| GET | `/v1/usage` | Yes | Meta | Yes |
