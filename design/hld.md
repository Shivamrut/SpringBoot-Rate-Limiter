# High-Level Design
```text
Author: Shivamrut 
Version: 1
Application: PulseAPI
```
## 1. Problem Statement

### 1.1 What we are building
We are building a web api application that can be integrated by developers using the simple api endpoints we define. The core product of the application is to serve "Inspirational Quotes" and let user perform lightweight text processing on the quotes.

### 1.2 Who it is for
The applications main consumer are the developers that need random quotes in their application. So our application provides them the required features wrt to Quotes that their product will need. Other users can be operators like us or custom automation bots or scripts to test the product performance.

### 1.3 Core pain / goal
The core feature of this application to provide rate limiting per client per endpoint per tier. This along with good api structure with clean response and error handling with security will be primary goals.

## 2. Functional Requirements

### 2.1 Must-have capabilities
1. Limits are based on client id, each client have independent limits for different apis. The limits are different for different tier of subscription or client user.
2. Rate limiting once the quota has exceeded, the user must be notified with the appropriate message
3. User should be able to fetch current usage and remaining quota without affecting the limits of actual usage apis
4. A health endpoint without rate limiting to test the health of the application
5. Reliable auth to identify correctly the client id and tier. We can generate API keys that map to particular combination.
6. Clear instructions for client after 429 in the request headers
7. Consistency between backend api and database

### 2.2 Nice-to-have (out of v1 if needed)
1. Reliable storage to persist the quotes data. Application shutdown should not affect the data. V1 can have in memory quotes data. A consistent database like postgresql will be good to have.
2. Cache db like redis for faster request processing
3. Containerisation of the application
4. Limits and controlling variables to be set in external config file. To change application constraints without complete reload.

### 2.3 Explicit non-features (optional cross-link to §4)
1. Billing, payments, or subscription management
2. Self-service API key signup or admin dashboard UI
3. Geographic routing or multi-region deployment
4. OAuth or user login flows (API key only)
5. Content moderation workflows beyond basic validation
6. SLA guarantees or contractual uptime commitments

## 3. Non-Functional Requirements

### 3.1 Performance & latency
1. Overheads like checking the current quota and rate limits should be under 5ms at p99
2. Health endpoint should respond under 50ms at p99
3. Read endpoints should respond under 100ms at p99
4. Text processing api should have artificial delay of 200-300ms

### 3.2 Availability & reliability
1. Continuos normal or peak traffic from a single client should not throttle the application or cause shutdown.
2. Ability to deploy as multiple instances of the application behind a load balancer.
3. Concurrency should be handled. Race conditions to be avoided.
4. System clock consistency and disaster measures to be taken
5. Client 1 limits must be independent and consistent with Client 2 limits

### 3.3 Scale & consistency targets
1. Consistent logging with appropriate fields visible
2. Internal implementation details must not be leaked
3. Rate limiting must not be by passable. Take care of different kinds of injections.
4. Current v1 is targetted at single client user. But it should be able to take concurrent requests from single user and concurrent requests from mutliple users. 
5. If multiple application instances are deployed then the client sending concurrent requests should be mapped to sticky instance or other measures to prevent limit abuse by concurrent requests.

## 4. Assumptions & Constraints

### 4.1 Assumptions
1. This is a read heavy application
2. Run this on local PC with containerisation for v1
3. Developers with user facing apps integrate this application
4. It can experince heavy traffic during morning when people wake up and want to read inspirational quotes. Night also can experience a burst.
5. Clients will use API keys for request

### 4.2 Constraints
1. It will be a single service, no microservice
2. For v1 it will be scoped only for functionality and other features like observabiltiy will be added later

### 4.3 Non-goals
*Already discussed in 2.3*
## 5. Capacity Estimation
*Though v1 is sandboxed with no real users. We will make the application reliable for 100 DAU*

### 5.1 Traffic (QPS / peak)
1. Users - 100 DAU
2. Requests per client per day - 100
3. Average QPS - 100*100/86400 = 0.11 requests per second
4. Peak QPS (morning/night) = avg * 5 = 0.55 rps
5. Read, write requests split = 10:1

### 5.2 Storage & growth
1. We store the user metadata (limits, quotas, name/id, api key, tier)
2. We store the quotes
3. We store logs
4. Cost -
    * Bytes per quotes = max 700 bytes
    * Count of quotes = 1000 to 10000
    * Growth per day = negligible for v1. Assume 50 quotes seeded. 500% growth in initial deployment till it reaches 4000 quotes. Then addition at 0.5% per day
    * Retention until user deletes
    * Total cost = 700 KB to 7MB in memory

### 5.3 Bandwidth / payload
*No need for v1*

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
