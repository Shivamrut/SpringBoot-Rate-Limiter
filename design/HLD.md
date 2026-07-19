# High-Level Design (Reference)

> **Purpose:** Correct / PRD-aligned reference for learning.  
> **Companion:** `design/hld.md` = your proposed draft (keep iterating there).  
```text
Author: Cursor(Auto)
Version: 1
Application: PulseAPI
```
---

## 1. Problem Statement

### 1.1 What we are building
PulseAPI is a developer-facing HTTP API that serves inspirational quotes and a small set of related operations (lookup, create, batch fetch, and lightweight text processing). Third-party applications integrate via simple, versioned endpoints (`/v1/...`).

### 1.2 Who it is for
- **Primary:** API consumers (developers) integrating quotes/processing into their apps.
- **Same interface, different behavior:** automated clients (scripts/bots) calling the same APIs — often at higher volume and less polite about retries.
- **Operators (internal):** run and observe the service (logs/metrics). They are not a separate product audience with admin APIs in v1.

### 1.3 Core pain / goal
As usage grows, a few clients can starve others, raise cost, and destabilize the service. The product must remain **useful and fair**: predictable access, clear contracts, and transparent feedback when a client is throttled.

**Product goal:** a reliable quotes/processing API.  
**System-design goal (primary learning focus):** enforce **per-client, per-endpoint, per-tier** usage limits without breaking the experience for well-behaved clients.

---

## 2. Functional Requirements

### 2.1 Must-have capabilities

**A. Product surface (what the API does)**
1. Return a random quote.
2. Return a quote by id (or not-found).
3. Accept a new quote (with validation).
4. Run a lightweight text operation on input (`reverse` / `uppercase` / `wordcount`) with intentional processing cost.
5. Batch-fetch quotes by a list of ids (partial success allowed).
6. Expose a liveness/health check for infrastructure (no auth).

**B. Identity**
7. Authenticate protected endpoints with an API key that maps to a **client identity** and **tier** (Free / Standard / Premium). Missing/invalid key → reject before business work.

**C. Fair access (rate limiting)**
8. Enforce independent limits per **client × endpoint × tier** (e.g. exhausting convert quota must not block quote reads).
9. Evaluate limits **before** business logic; a rejected write must not create side effects (e.g. no quote created on 429).
10. On throttle: HTTP 429 plus client guidance (`Retry-After` and rate-limit headers). Same headers should appear on successful protected responses so clients can slow down proactively.
11. Let a client inspect **their own** current-window usage/remaining quotas via a usage API (counts must match enforcement). Reading usage must not consume quotas of other business endpoints (usage may have its own limit).
12. Health checks are **exempt** from per-client rate limits.

**D. Response quality**
13. Consistent JSON error shape with stable machine-readable codes and a request id for correlation.

### 2.2 Nice-to-have (out of v1 if needed)
1. Quotes (and optionally keys) survive process restart (durable store) — v1 may use seed/in-memory data.
2. Limits and related knobs adjustable without a full redesign of the API (configurability).
3. Richer operator tooling (dashboards, key rotation APIs) — explicitly later.

### 2.3 Explicit non-features
1. Billing, payments, or subscription management  
2. Self-service API key signup or admin dashboard UI  
3. Geographic routing or multi-region deployment  
4. OAuth or end-user login (API key only)  
5. Content moderation beyond basic validation  
6. Contractual SLA / uptime commitments  

---

## 3. Non-Functional Requirements

### 3.1 Performance & latency
1. Rate-limit check overhead: **&lt; 5 ms p99** (excluding business logic).
2. `GET /health`: **&lt; 50 ms p99**.
3. Quote read paths: **&lt; 100 ms p99** (excluding rate-limit check).
4. Convert path: **200–300 ms artificial delay** + small overhead (delay is a product requirement to make “expensive” endpoints meaningful).

### 3.2 Availability & reliability
1. Sustained max-rate traffic from one client must not take down the service or destroy experience for other clients.
2. Deployable as **multiple app instances** behind a load balancer.
3. Define behavior when rate-limit state is temporarily unavailable (**fail-open vs fail-closed** — decide later in design; requirement is that behavior is explicit and tested).
4. Process restart or mild clock skew must not permanently lock out a client or grant unbounded access.
5. Concurrent requests from the same client must not produce unbounded overshoot of the published limit (document acceptable tolerance later).

### 3.3 Scale & consistency targets
1. **Client isolation:** Client A’s usage/limits must not affect or leak into Client B’s.
2. **Cross-instance consistency:** With multiple instances, a single client’s effective limit remains correct within the agreed tolerance (requirement = consistency of enforcement).
3. **Initial production scale:** support on the order of **~1,000 active API clients** with concurrent requests per client (see §5).
4. Hot-key awareness: design must remain correct when one client hammers one endpoint at the limit.

---

## 4. Assumptions & Constraints

> PRD did not specify target client counts. Numbers in §5 are **design assumptions** for initial production — revisit when real traffic exists.

### 4.1 Assumptions
1. **Read-heavy** traffic (~90%+ reads: random/by-id/batch/usage vs writes/convert).
2. Callers are **third-party apps** identified by **API keys** (not end-user logins).
3. **Initial production target:** ~**1,000 monthly active API clients** (keys with meaningful traffic), growing later; not a toy/sandbox-only deploy.
4. **Tier mix (assumed):** ~80% Free, ~15% Standard, ~5% Premium.
5. Organic traffic has **diurnal peaks** (e.g. morning/evening); use peak factor **~10×** average for provisioning headroom.
6. Some clients will **retry aggressively** after 429 (bots/scripts); the system must stay up under that behavior.
7. **Single region** for initial production; multi-region is out of scope (see §2.3).
8. Clocks are roughly synchronized (NTP); designs should still tolerate mild skew.

### 4.2 Constraints
1. **One deployable application service** for v1 (no microservice split required).
2. **API-key auth only** — no OAuth/user login.
3. Limits are **per client × per endpoint × per tier**, enforced before business logic.
4. Must be **horizontally scalable** (multiple instances behind a load balancer) with correct-enough limit enforcement across instances.
5. **Convert** remains intentionally expensive (artificial delay); capacity planning must treat it as a separate bottleneck from cheap reads.
6. No billing/subscription platform in-process (tiers are configuration, not a payments system).

### 4.3 Non-goals
Same as **§2.3** (billing, self-serve key signup/admin UI, multi-region, OAuth, deep moderation, contractual SLAs). Not repeated here.

---

## 5. Capacity Estimation

**Framing:** Build for **real production**, starting at **small scale**. Unit of scale = **API client (key)**, not consumer “DAU.”

| Planning target | Value | Notes |
|---|---|---|
| Monthly active clients | **1,000** | Initial production assumption |
| Horizon | Design so **10× (10k clients)** does not require a total rewrite | Exact 10k sizing is future |

### 5.1 Traffic (QPS / peak)

**Organic load**

| Step | Value |
|---|---|
| Active clients | **1,000** |
| Avg requests / client / day | **200** (integrations polling quotes + light writes) |
| Daily requests | 1,000 × 200 = **200,000** |
| **Average QPS** | 200,000 ÷ 86,400 ≈ **2.3 QPS** |
| **Organic peak QPS** | 2.3 × 10 ≈ **23 QPS** |
| Read : write(+convert) | ≈ **10 : 1** → peak writes/convert ≈ **~2 QPS** organic |

**Limit-derived / abuse load (must survive)**

Organic peak is easy; rate limiting exists for the ugly case.

| Scenario | Rough math | Implication |
|---|---|---|
| 1× Premium at cap on `GET .../random` | 500 / min ≈ **8.3 RPS** | Single hot client |
| 50× Free at cap on same endpoint | 50 × 30 / min ≈ **25 RPS** | Many small clients |
| Aggressive retries after 429 | Can multiply attempt rate; most should fail cheaply at the limiter | Limiter path must stay fast (&lt; 5 ms) |
| **Design headroom (initial prod)** | Survive **~100–200 QPS** of mostly limited traffic without collapse | Far above organic ~23 QPS |

**Convert caution:** even low RPS is costly (200–300 ms each). Use Little’s Law: **in-flight ≈ arrival rate × service time**.

| Step | Math |
|---|---|
| 10 Premium clients at convert cap | 10 × 50 / min = **500 convert / min** |
| Arrival rate | 500 ÷ 60 ≈ **8.3 req/s** |
| Service time per convert | ~**0.25 s** (artificial delay) |
| Avg concurrent convert work | 8.3 req/s × 0.25 s ≈ **~2** in-flight handlers |

Still fine at small scale, but convert concurrency is the first resource to watch as clients grow.

### 5.2 Storage & growth

**What we store**
1. **Quotes** — catalog content  
2. **Client identity** — key → clientId, tier (and static limit config by tier/endpoint)  
3. **Rate-limit / usage state** — counters (or equivalent) per client × endpoint for the active window(s)  
4. **Logs/metrics** — operational; size depends on retention (treat as ops concern, not core DB sizing for v1)

**Quotes footprint**
- ~700 bytes/quote (text + author + id overhead)
- Seed **50**; plan **~10k** quotes in early production → 10,000 × 700 ≈ **7 MB**
- Growth: low (user-generated creates are rate-limited); even **100k** quotes ≈ **~70 MB** — not the scaling bottleneck

**Client metadata**
- ~1 KB/client rough → **1,000 clients ≈ 1 MB**; at 10k clients ≈ 10 MB

**Rate-limit state (the interesting one)**
- ~6 limited endpoints × ~64–256 bytes/counter record (order-of-magnitude)
- Active state ∝ **clients × endpoints** (and window strategy)
- At 1,000 clients: on the order of **≤ a few MB** of hot counter state  
- At 10k clients: still typically **tens of MB** — architecture choice (in-process vs shared store) is driven by **multi-instance consistency**, not raw size at this scale

**Conclusion:** data volume is small. Capacity risk is **QPS + convert concurrency + correct shared counters**, not terabytes of quotes.

### 5.3 Bandwidth / payload
Payloads are small JSON (quotes, short convert I/O). At ~23–200 QPS, bandwidth is **not** a bottleneck for initial production. Skip detailed bandwidth math unless batch sizes or payloads grow large.

## 6. High-Level Architecture Diagram

### 6.1 Context (external actors)
### 6.2 Container / component view
### 6.3 Key interactions (arrows labeled)

## 7. Component Responsibilities

### 7.1 Component list
### 7.2 Ownership boundaries (what each does / does not do)
### 7.3 Dependencies between components

## 8. API Design

### 8.1 Resources & endpoints
### 8.2 Auth & error contract
### 8.3 Important request/response shapes

## 9. Data Model & Storage Design

### 9.1 Core entities / state
### 9.2 What must persist vs ephemeral
### 9.3 Storage placement (local vs shared; durability)

## 10. Core Request Flows (Sequence Diagrams)

### 10.1 Happy path
### 10.2 Auth failure
### 10.3 Rate-limited / rejected path

## 11. Scalability & Reliability

### 11.1 Bottlenecks & scaling approach
### 11.2 Multi-instance / shared state
### 11.3 Failure modes & recovery

## 12. Security

### 12.1 Authentication & identity
### 12.2 Authorization / abuse boundaries
### 12.3 Sensitive data handling

## 13. Observability

### 13.1 Logs
### 13.2 Metrics
### 13.3 Tracing / correlation (if needed)

## 14. Deployment

### 14.1 Runtime topology
### 14.2 Config & environments
### 14.3 Rollout / ops notes (brief)

## 15. Trade-offs / Future Improvements

### 15.1 Decisions & alternatives considered
### 15.2 Known limitations
### 15.3 Next scale / Phase 2+ ideas
