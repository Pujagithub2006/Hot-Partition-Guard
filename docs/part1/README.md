Save to: C:\Project_Practice\Hot_Partition_Guard\docs\part1\README.md

markdown
# Part 1 - Pure Spring Framework

Milestone branch: `feature/01-pure-spring-core`
Blueprint: The Spring Foundations - High-Concurrency Flash-Sale and Seat Reservation Engine

This milestone demonstrates Spring Framework mechanics without Spring Boot. The `hpg-core`
module is intentionally compiled with an enforcer rule that fails the build if any
`org.springframework.boot:*` artifact appears on the classpath. Every container, AOP,
transaction, and MVC behavior is wired explicitly.

## Scope

- IoC container bootstrap using `AnnotationConfigApplicationContext`
- Manual JPA infrastructure: `DataSource`, `EntityManagerFactory`, `JpaTransactionManager`
- Bean lifecycle interception via `@PostConstruct`, `@PreDestroy`, and `BeanPostProcessor`
- Dynamic strategy resolution using `@Primary`, `@Qualifier`, and map injection
- Spring AOP with custom annotations, `@Around` advice, and proxy mechanics profiling
- Transaction demarcation including self-invocation bypass and `REQUIRES_NEW` propagation
- Zero-XML Spring MVC with `AbstractAnnotationConfigDispatcherServletInitializer`
- Custom `HandlerMethodArgumentResolver` for typed request-header extraction

## Modules

| Module | Purpose |
|--------|---------|
| `hpg-domain` | JPA-annotated entities. No Spring, no Hibernate. |
| `hpg-core` | Pure Spring Framework 7. Container, AOP, transactions, MVC. |
| `hpg-platform` | Placeholder. Activated in Part 2 (Spring Boot 4). |
| `hpg-security` | Placeholder. Activated in Part 3 (Spring Security). |

## Issue Index

| Issue | Title | Category |
|-------|-------|----------|
| 1 | Scaffold multi-module Maven project and pure Spring zero-boot dependencies | chore |
| 2 | Bootstrap AnnotationConfigApplicationContext and manual JpaTransactionManager | feat |
| 3 | BeanFactory vs ApplicationContext lifecycle and instantiation benchmark | test |
| 4 | CustomVenuePostProcessor for seat grid layout metadata validation | feat |
| 5 | 3-level cache demonstration and constructor injection circular dependency defense | test |
| 6 | Dynamic SeatAllocationStrategy resolution via @Qualifier, @Primary, and map injection | feat |
| 7 | ReservationService with strict constructor DI and hold-release lifecycle hooks | feat |
| 8 | @RetryOnLockFailure runtime annotation and @Around retry aspect with backoff | feat |
| 9 | Profile proxy generation mechanics and latency overhead (JDK vs CGLIB) | test |
| 10 | Reproducible test for @Transactional self-invocation proxy bypass | test |
| 11 | Isolated audit logging via Propagation.REQUIRES_NEW boundary demarcation | feat |
| 12 | Zero-XML DispatcherServlet via AbstractAnnotationConfigDispatcherServletInitializer | feat |
| 13 | QueueSessionArgumentResolver for header extraction and queue validation | feat |
| 14 | Document pure Spring architecture, proxy boundaries, and transaction lifecycle findings | docs |

## Architecture Overview

The diagram below illustrates how the three layers - container, proxy, and transaction -
compose when a reservation request passes through the system. This composition is the
central teaching point of Part 1. Spring Boot would hide most of this behind
auto-configuration. Here, every arrow is explicit.

```mermaid
flowchart TB
    Client[HTTP Client]
    DS[DispatcherServlet]
    ARG[QueueSessionArgumentResolver]
    CTRL[SeatMapController]
    SVC[ReservationService]
    PROXY[Spring AOP Proxy]
    RETRY[ConcurrencyRetryAspect]
    TX[TransactionInterceptor]
    EM[EntityManager]
    DB[(PostgreSQL)]
    AUDIT[AuditLoggerService REQUIRES_NEW]
    CACHE[Seat Grid Cache PostConstruct]

    Client --> DS
    DS --> ARG
    ARG --> CTRL
    CTRL --> SVC
    SVC --> PROXY
    PROXY --> RETRY
    RETRY --> TX
    TX --> EM
    EM --> DB
    SVC -. failure path .-> AUDIT
    AUDIT -. independent tx .-> DB
    CACHE -. warmed at startup .-> SVC
Document Index
Each document is self-contained and cross-links to the others where relevant.

Document	Covers	Related Issue
beanfactory-vs-context.md	Lazy vs eager container initialization, benchmark results	3
circular-dependency-analysis.md	3-level cache, constructor injection failure, ArchUnit enforcement	5
container-lifecycle.md	Bean initialization order, BeanPostProcessor hooks, scope lifetimes	4, 5, 7
proxy-mechanics.md	JDK vs CGLIB generation, bytecode size, latency delta	9
self-invocation-trap.md	@Transactional proxy bypass, three sanctioned fixes	10
transaction-lifecycle.md	Aspect ordering, wrapping hierarchy, common failure modes	8, 10, 11
propagation-boundaries.md	Seven propagation modes, decision table, pool pressure notes	11
dispatcher-servlet-bootstrap.md	Zero-XML MVC bootstrap, servlet container lifecycle	12
custom-argument-resolvers.md	Resolver chain order, precedence rules, header validation	13
How to Run
Requirements:

Java 25

Maven 3.9 or later

Docker (for Testcontainers-backed integration tests)

Build and verify:

powershell
mvn -q clean verify
Confirm the Spring Boot ban is enforced on hpg-core:

powershell
mvn -q -pl hpg-core dependency:tree "-Dincludes=org.springframework.boot"
The command must produce empty output. Any org.springframework.boot:* line indicates
the enforcer rule is not active or a dependency has been introduced that violates the
Part 1 contract.

Run only the integration tests (Testcontainers required):

powershell
mvn -q -pl hpg-core failsafe:integration-test failsafe:verify
Constraints
No Spring Boot dependency in hpg-core. The enforcer rule enforces this at build time.

No field injection. Every service uses constructor injection with final fields.
Enforced by ArchUnit (issue 5).

No XML configuration. All wiring is Java-based via @Configuration classes.

No H2. Integration tests use PostgreSQL via Testcontainers to reproduce realistic
row-locking and rollback semantics.

Relationship to Parts 2 and 3
Part 2 migrates infrastructure configuration to Spring Boot 4 auto-configuration and
introduces Flyway-managed schema evolution, tuned async thread pools, and Micrometer
telemetry. The manually wired beans from Part 1 are replaced, and the differences are
documented.

Part 3 hardens the HTTP perimeter with Spring Security: a custom SecurityFilterChain,
an anti-bot rate-limiting filter, stateless JWT queue passes, and SpEL method-level
authorization.

Both parts reference the findings captured here. Any regression in the container, proxy,
or transaction behavior discovered in later milestones should be traced back to the
corresponding document in this directory.