# Clean Architecture Guidelines — Kotlin BFF Seed (2026)

> This document is a **guideline for agentic coding**.
> When an AI agent adds code to this project, it must follow these rules.
> When in doubt, read this file before writing anything.

---

## Core principle

The domain knows nothing about the outside world.
Infrastructure knows about the domain. Never the reverse.

```
HTTP / CLI / Events          ← driver adapters      (left / primary)
        ↓
  [application services]     ← orchestration layer  (commands & queries)
        ↓
     [domain]                ← pure business logic
        ↓
Database / Cache / Clock     ← driven adapters      (right / secondary)
```

Dependency arrows always point **inward**, toward the domain.

---

## Package structure

```
src/main/kotlin/<bounded-context>/
│
├── domain/
│   ├── entities/            # Aggregates — always valid, constructed via factory methods only
│   ├── valueObjects/        # Immutable value types, self-validating (@JvmInline where zero-cost)
│   ├── errors/              # Sealed domain error hierarchy
│   ├── services/            # Pure domain services (logic that spans multiple aggregates)
│   └── factories/           # Aggregate factories (UsineDeTickets, etc.)
│
├── ports/
│   ├── driven/              # Interfaces the application calls outward (persistence, clock, ids)
│   └── driver/              # Interfaces the outside world calls inward (optional, for testability)
│
├── application/             # Thin orchestration — no business logic here
│   ├── commands/            # Plain data classes: AcheterUnTicketCmd, ProlongerUnTicketCmd
│   ├── queries/             # Plain data classes: ObtenirTicketQuery, TousLesTicketsQuery
│   └── services/            # Application services: one class per aggregate root
│
├── adapters/
│   ├── driven/              # Implementations of driven ports
│   │   ├── storage/
│   │   │   ├── mongo/
│   │   │   ├── postgres/
│   │   │   └── valkey/
│   │   └── time/
│   └── driver/              # Implementations of driver ports
│       └── http/            # HTTP handlers, DTOs, routing — one file per route group
│
└── bootstrap/               # Wiring only: DI (Koin modules), main(), server start
```

---

## Always-valid domain

**An invalid domain object must never exist.**
There is no such thing as a "default", "empty", or "failure" aggregate instance.
If construction can fail, the factory returns `Either<DomainError, T>` — not a nullable, not a sentinel value, not an exception.

### Rules

1. **No public constructors on aggregates.**
   All constructors are `private`. Creation goes through a factory method or companion object that validates invariants and returns `Either<DomainError, T>`.

   ```kotlin
   // WRONG
   data class Ticket(val id: String, val duree: DureeDeLocation, val prix: Monnaie)
   // → caller can construct Ticket("", DureeDeLocation(-1), Monnaie.Euros(-5.0)) — invalid

   // RIGHT
   class Ticket private constructor(
       val id: String,
       val duree: DureeDeLocation,
       val prix: Monnaie
   ) {
       companion object {
           fun creer(id: String, duree: DureeDeLocation, prix: Monnaie): Either<ErreurDeLocation, Ticket> =
               either {
                   ensure(id.isNotBlank()) { ErreurDeLocation.IdManquant }
                   Ticket(id, duree, prix)
               }
       }
   }
   ```

2. **No sentinel / null-object instances** (`enEchec()`, `empty()`, `default()`).
   These exist only to paper over the absence of `Either`. Remove them.

3. **Value objects enforce their own invariants at construction.**

   ```kotlin
   @JvmInline
   value class DureeDeLocation private constructor(val enMinutes: Int) {
       companion object {
           fun de(minutes: Int): Either<ErreurDeLocation, DureeDeLocation> =
               if (minutes > 0) DureeDeLocation(minutes).right()
               else ErreurDeLocation.DureeInvalide.left()
       }
   }
   ```

4. **`data class` is allowed for value objects**, but only when all fields are themselves always-valid types, so structural equality is always meaningful.
   Aggregates should **not** be `data class` — their identity is their ID, not structural equality.

5. **Domain operations that change state return a new instance or `Either`.**
   They never mutate in place, never return `Unit`, never throw.

   ```kotlin
   fun prolonger(dureeSupplementaire: DureeDeLocation): Either<ErreurDeLocation, Ticket>
   ```

6. **Reconstruction from persistence is exempt from validation.**
   Adapters reconstituting a stored aggregate use a dedicated `reconstituer(...)` factory that bypasses business rules (the data was already valid when saved). Mark it clearly:

   ```kotlin
   // For adapter use only — skips invariant checks
   internal fun reconstituer(id: String, duree: DureeDeLocation, prix: Monnaie): Ticket =
       Ticket(id, duree, prix)
   ```

---

## Application services (commands & queries)

An application service is a **thin orchestrator**. It:
- Receives a command or a query (plain data class)
- Loads the aggregate via a driven port
- Calls domain logic
- Persists the result via a driven port
- Returns `Either<DomainError, T>`

There is **one application service class per aggregate root**.
Each method handles exactly one command or one query.
Commands change state. Queries do not.

```kotlin
class ServiceDePaiementLocation(
    private val tickets: PourPersisterUnTicket,
    private val horloge: PourAvoirHeure,
    private val usine: UsineDeTickets
) {
    // --- Commands (write, state-changing) ---
    suspend fun acheterUnTicket(cmd: AcheterUnTicketCmd): Either<ErreurDeLocation, Ticket>
    suspend fun prolongerUnTicket(cmd: ProlongerUnTicketCmd): Either<ErreurDeLocation, Ticket>

    // --- Queries (read, no state change) ---
    suspend fun obtenirTicket(query: ObtenirTicketQuery): Either<ErreurDeLocation, Ticket>
    suspend fun tousLesTickets(): List<Ticket>
}
```

**No business logic in application services.**
If you find yourself writing an `if` that relates to domain rules, move it to the aggregate or a domain service.

**No mediator bus.** Direct method calls are explicit and traceable by agents and humans alike.
If multiple bounded contexts appear, introduce a bus at that point — not before.

---

## Rules by layer

### Domain (`domain/`)

- **No framework imports.** No Ktor, no Koin, no Jackson, no coroutine imports.
- **No coroutines.** Domain functions are pure and synchronous.
- No public constructors on aggregates (see Always-valid domain above).
- No nulls in signatures. Use `Either` or `Option` (Arrow).
- No exceptions for business errors. Use `Either`.
- Sealed interfaces for the error hierarchy:
  ```kotlin
  sealed interface ErreurDeLocation {
      data object DureeInvalide : ErreurDeLocation
      data object IdManquant : ErreurDeLocation
      data class TicketIntrouvable(val id: String) : ErreurDeLocation
  }
  ```

### Ports (`ports/`)

- One interface per capability, named from the domain's point of view.
- `fun interface` for single-method ports — they become lambdas in tests.
- No `reset()` or test helpers. Use a separate `Resettable` interface in test scope only.
- Driven ports may be `suspend`. Be consistent: if one is, all are.

### Application services (`application/services/`)

- Depends on: domain + driven ports.
- Does not depend on: adapters, HTTP, DI framework.
- Constructor-injected. Fully testable with fake adapters, no framework.

### Adapters — driven (`adapters/driven/`)

- Each adapter implements exactly one port.
- DTOs are **private inner classes** of the adapter. They never escape.
- Mapping lives inside the adapter, not in the domain.
- Use `reconstituer(...)` factory (not the validated constructor) when rehydrating from storage.
- No test helpers in production code.

### Adapters — driver / HTTP (`adapters/driver/http/`)

- HTTP DTOs are separate from domain types. Never serialize a domain object directly.
- Handlers are thin: parse input → call application service → map result to HTTP response.
- Error mapping (`Either` → HTTP status) lives in one dedicated function per bounded context.
- No business logic.

### Bootstrap (`bootstrap/`)

- Wiring only. Koin modules live here.
- `main()` reads config, builds the graph, starts the server.
- The domain and application services must be instantiable without Koin in tests.

---

## Contract testing for adapters

Every driven adapter must pass a shared contract test suite.

```kotlin
// Abstract contract — lives in test scope, alongside the port
abstract class ContratDeStockage : AnnotationSpec() {
    abstract fun adapter(): PourPersisterUnTicket

    @Test fun `enregistrer puis compter`() { ... }
    @Test fun `deux tickets avec le même id`() { ... }
}

// One subclass per adapter
class TestsAvecMongo : ContratDeStockage() {
    override fun adapter() = MongoAdapterPourTickets(mongoContainer.connectionString())
}

class TestsAvecFake : ContratDeStockage() {
    override fun adapter() = FakePourPersisterUnTicket()
}
```

The fake adapter is a first-class citizen — it passes the same contract as the real adapter.
It lives in `src/test/kotlin/.../adapters/driven/storage/fake/` and is the adapter used in application service tests.

---

## Error handling

Arrow `Either` end-to-end, with the `either { }` / `Raise` DSL (Arrow 2.x):

```kotlin
// Domain factory
fun creer(...): Either<ErreurDeLocation, Ticket> = either {
    ensure(duree.enMinutes > 0) { ErreurDeLocation.DureeInvalide }
    Ticket(id, duree, prix)
}

// Application service — using raise DSL
suspend fun acheterUnTicket(cmd: AcheterUnTicketCmd): Either<ErreurDeLocation, Ticket> = either {
    val ticket = usine.creer(cmd.duree).bind()      // propagates Left automatically
    tickets.enregistrer(ticket)
    ticket
}

// HTTP adapter
service.acheterUnTicket(cmd).fold(
    ifLeft  = { err -> Response(err.toHttpStatus()) },
    ifRight = { ticket -> Response(OK).with(ticketLens of ticket.toDTO()) }
)
```

Reserve `Result<T>` for infrastructure boundaries where Java exceptions are expected (JDBC, network calls).
Never use `Result` in domain or application service signatures.

---

## Coroutines

- Application services are `suspend`. Driven port interfaces are `suspend`.
- Domain functions are **not** `suspend` — they are pure and synchronous.
- If a driven adapter wraps a blocking library (JDBC, synchronous Mongo driver), it runs on `Dispatchers.IO` internally and exposes a `suspend` interface.

---

## Testing strategy

| Layer | Test type | Tools |
|-------|-----------|-------|
| Domain (entities, value objects, rules) | Unit | Kotest `FunSpec`, no mocks, no fakes |
| Application services | Unit | Fake adapters, no mocks, no TestContainers |
| Driven adapters | Contract (integration) | TestContainers + shared contract spec |
| Driver adapters (HTTP) | Integration | Ktor `testApplication` or http4k in-process client |
| Full stack | E2E (optional) | Docker Compose, real infra |

**No mocks for driven adapters.** Mocks couple tests to implementation details and allow the fake to drift from reality.

---

## What an agent must NOT do

- Import framework types into `domain/` or `ports/`
- Create a public constructor on an aggregate
- Create sentinel instances (`enEchec()`, `empty()`, `nul()`)
- Add `reset()` to a production port interface
- Put DTO mapping in the domain
- Use `!!` anywhere
- Throw exceptions for domain business errors
- Put business logic in an application service
- Create a layer not described in this document (`services/` at domain level is allowed; `managers/`, `helpers/`, `utils/` are not)
- Write a driven adapter without a corresponding contract test
- Add logic to the bootstrap layer

---

## Naming conventions

| Concept | Convention | Example |
|---------|------------|---------|
| Driven port | `Pour<WhatItDoes>` | `PourPersisterUnTicket` |
| Driver port | `Pour<WhatItDoes>` | `PourGererLaPaiementLocation` |
| Application service | `ServiceDe<AggregateName>` | `ServiceDePaiementLocation` |
| Command | `<Verb><Noun>Cmd` | `AcheterUnTicketCmd` |
| Query | `<Noun>Query` | `ObtenirTicketQuery` |
| Domain error | Sealed interface per aggregate | `ErreurDeLocation.DureeInvalide` |
| Aggregate factory | `Usine<Aggregate>` | `UsineDeTickets` |
| Adapter | `<Technology>AdapterPour<Port>` | `MongoAdapterPourTickets` |
| DTO (adapter-internal) | Private inner `data class` | `MongoAdapterPourTickets.DocumentTicket` |
| DTO (HTTP) | Suffix `DTO` in driver adapter | `TicketDTO` |
| Test contract | `ContratDe<Port>` | `ContratDeStockage` |
| Fake adapter | `FakePour<Port>` | `FakePourPersisterUnTicket` |
