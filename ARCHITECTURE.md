# Clean Architecture Guidelines — Kotlin BFF Seed (2026)

> This document is a **guideline for agentic coding**.
> When an AI agent adds code to this project, it must follow these rules.
> When in doubt, read this file before writing anything.

---

## Core principle

The domain knows nothing about the outside world.
Infrastructure knows about the domain. Never the reverse.

```
HTTP / CLI / Events          ← driver adapters    (left side)
        ↓
    [use cases]              ← domain core
        ↓
Database / Cache / Clock     ← driven adapters    (right side)
```

Dependency arrows always point **inward**, toward the domain.

---

## Package structure

```
src/main/kotlin/<bounded-context>/
├── domain/
│   ├── entities/            # Aggregates and entities (pure Kotlin, no framework)
│   ├── valueObjects/        # Immutable value types (@JvmInline where applicable)
│   ├── useCases/            # One class per use case, depends only on ports
│   └── usine/               # Factories and domain rules (pure functions preferred)
│
├── ports/
│   ├── driven/              # Interfaces the domain calls out through
│   │   ├── PourPersister*.kt
│   │   ├── PourAvoirHeure.kt
│   │   └── PourLesIdentifiants.kt
│   └── driver/              # Interfaces the outside world calls in through (optional)
│
├── adapters/
│   ├── driven/              # Implementations of driven ports
│   │   ├── storage/
│   │   │   ├── mongo/
│   │   │   ├── postgres/
│   │   │   └── valkey/
│   │   └── time/
│   └── driver/              # Implementations of driver ports
│       └── http/            # HTTP handlers, DTOs, routing
│
└── bootstrap/               # Wiring only: DI setup, main(), server start
```

---

## Rules by layer

### Domain

- **No framework imports.** No Spring, no Ktor, no Koin, no Jackson.
- **No coroutines.** Domain functions are pure and synchronous.
- Entities are `data class`. Value objects are `data class` or `@JvmInline value class`.
- Use `Either<DomainError, T>` (Arrow) for operations that can fail. Never throw exceptions for business errors.
- Sealed classes for domain errors:
  ```kotlin
  sealed interface ErreurDeLocation {
      data object DureeInvalide : ErreurDeLocation
      data class TicketIntrouvable(val id: String) : ErreurDeLocation
  }
  ```
- Factories (`UsineDeTickets`) take their dependencies as constructor parameters (ports/lambdas), never as singletons.
- No nulls in domain signatures. Use `Either` or `Option` (Arrow).

### Ports (interfaces)

- One interface per capability, named from the domain's point of view (`PourPersisterUnTicket`, not `TicketRepository`).
- `fun interface` for single-method ports — they become lambdas in tests.
- No `reset()` or test helpers in production port interfaces. Use a separate `Resettable` interface in test scope only.
- Driven ports can be `suspend` if the use case layer is coroutine-aware. Be consistent: either all driven ports are suspend, or none are.

### Adapters (driven)

- Each adapter implements exactly one port.
- DTOs (Mongo documents, SQL rows, Redis hashes) are **private inner classes** of the adapter. They never escape.
- Mapping between domain types and DTOs lives inside the adapter, not in the domain.
- TestContainers setup belongs in the test adapter, not in the production adapter.
- The production adapter must not know it is being tested.

### Adapters (driver — HTTP)

- HTTP DTOs are **separate from domain entities**. Never serialize a domain entity directly.
- One file per route group. Routing, DTO, and mapping are co-located in the same file only for small handlers; split when the handler grows.
- Handlers are thin: validate input → call use case → map result to response. No business logic.
- Error mapping (domain `Either` → HTTP status) lives in a single dedicated function, not scattered across handlers.

### Bootstrap

- Dependency injection wiring happens **only here**. No `@Inject`, no service locators anywhere else.
- The `main()` function reads config, builds the dependency graph, starts the server.
- Use Koin modules as a wiring DSL, not as a framework. The domain must be instantiable without Koin in tests.

---

## Contract testing for adapters

Every driven adapter must pass a shared contract test suite.
The contract is defined once as an abstract spec; each adapter provides a concrete subclass.

```kotlin
// The contract (in test scope, next to the port)
abstract class ContratDeStockage : AnnotationSpec() {
    abstract fun adapter(): PourPersisterUnTicket

    @Test fun `enregistrer puis compter`() { ... }
    @Test fun `deux tickets avec le même id`() { ... }
}

// Concrete subclass per adapter
class TestsAvecMongo : ContratDeStockage() {
    override fun adapter() = MongoAdapter(mongoContainer.connectionString())
}

class TestsAvecFake : ContratDeStockage() {
    override fun adapter() = FakeAdapter()
}
```

The **Fake adapter** (in-memory) must also pass the same contract.
If the fake passes but the real adapter does not, fix the real adapter.
If the real adapter passes but the fake does not, fix the fake.

---

## Error handling

Use Arrow `Either` end-to-end:

```kotlin
// Domain
fun payerLocation(duree: DureeDeLocation): Either<ErreurDeLocation, Ticket>

// Use case — Raise DSL (Arrow 2.x)
fun Either.Companion.catch { ... }  // for adapter calls that throw
raise(ErreurDeLocation.DureeInvalide)  // for domain rule violations

// HTTP adapter
useCase.payerLocation(duree).fold(
    ifLeft  = { err -> Response(err.toStatus()) },
    ifRight = { ticket -> Response(OK).with(ticketLens of ticket.toDTO()) }
)
```

Never use `Result<T>` in the domain — it hides the error type. Reserve `Result` for infrastructure boundaries where exceptions are expected (JDBC, network).

---

## Coroutines

- Use cases **may** be `suspend` if the driven adapters are genuinely async (e.g., reactive Mongo, non-blocking Redis).
- If adapters are blocking (JDBC, synchronous Mongo driver), keep use cases synchronous and run them on `Dispatchers.IO` at the driver adapter boundary.
- Do not mix blocking calls and coroutines inside the domain.

---

## Testing strategy

| Layer | Test type | What to use |
|-------|-----------|-------------|
| Domain (entities, value objects, rules) | Unit | Kotest `AnnotationSpec` or `FunSpec`, no mocks |
| Use cases | Unit | Fake adapters only, no mocks, no TestContainers |
| Driven adapters | Contract (integration) | TestContainers + shared contract spec |
| Driver adapters (HTTP) | Integration | `http4k` / Ktor in-process test client |
| Full stack | E2E (optional) | Docker Compose, real infra |

**No mocks for driven adapters.** Use fakes that implement the port. Mocks couple tests to implementation details.

The fake adapter is a first-class citizen:
- It lives in `src/test/kotlin/.../adapters/driven/storage/fake/`
- It passes the same contract tests as the real adapter
- It is the adapter used in use-case tests

---

## What an agent must NOT do

- Import framework types into `domain/` or `ports/`
- Add `reset()` to a production port interface
- Put DTO mapping logic in the domain
- Use `!!` (force unwrap) anywhere
- Throw exceptions for domain business errors (use `Either`)
- Create a new abstraction layer (e.g. `services/`, `managers/`) not described in this document
- Write a new adapter without a corresponding contract test
- Add logic to the bootstrap/wiring layer

---

## Naming conventions

| Concept | Convention | Example |
|---------|------------|---------|
| Driven port | `Pour<WhatItDoes>` | `PourPersisterUnTicket` |
| Driver port | `<UseCase>` or `Pour<WhatItDoes>` | `PaiementLocation` |
| Use case impl | Concrete name of the business | `LillePaiementLocation` |
| Domain error | Sealed interface + `data object` | `ErreurDeLocation.DureeInvalide` |
| Adapter | `<Technology>AdapterPour<Port>` | `MongoAdapterPourTickets` |
| DTO (adapter) | Private inner class | `RepositoryMongoDb.DTOMongoTicket` |
| DTO (HTTP) | Suffix `DTO` in driver adapter | `TicketDTO` |
| Test contract | `ContratDe<Port>` | `ContratDeStockage` |
| Fake adapter | `Fake<Port>` | `FakePourPersisterUnTicket` |
