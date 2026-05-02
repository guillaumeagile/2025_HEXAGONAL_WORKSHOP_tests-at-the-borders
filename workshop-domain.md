# The CUTE DDD Workshop in Kotlin

## Objectives

### 🎯 CUTE DDD Principles

#### **C** - **Contextual**
Design decisions based on specific domain context

#### **U** - **Ubiquitous**
Shared language understood by all stakeholders

#### **T** - **Testable**
Easy to test at all levels (unit, integration, acceptance)

#### **E** - **Expressive**
Clearly communicates business intent, through an internal DSL used to write tests
as human-readable specifications

---

## Avoid legacy OO code

Bad habits we want to fight against:

- Several controllers and models, all mixed up
    - product (all concerns embedded: transport, stock, catalog front end, suppliers)
    - suppliers management
    - stock management
    - catalog front end
    - transport management
- Several services as well
- All models with inter-dependencies (relationships in DB)

---

## 🖋️ Domain Storytelling + Context Mapping

While creating our own DSL, write stories about Products, Suppliers and Stocks.

**Stories:**
- A supplier adds a product to the catalog, then ships it into stock later
- A product is in stock, then sold: the supplier must be advised, the stock must be updated
- A product is in stock but deprecated: the supplier must be advised, the stock must be updated,
  the customer must be advised (product label must change)

**Goal:** split the domain into bounded contexts, let events emerge.

---

## Architectural decisions

### Bounded contexts

There are **3 bounded contexts**. Each has its own interpretation of `Produit` — **no shared kernel**.

```
┌─────────────────────┐     events     ┌─────────────────────┐
│       Catalog       │ ─────────────► │       Stock         │
│  (reference impl)   │                │  (workshop exercise) │
└─────────────────────┘                └─────────────────────┘
          │
          │ events
          ▼
┌─────────────────────┐
│     Fournisseur     │
│  (workshop exercise) │
└─────────────────────┘
```

- **Catalog** — owns the product lifecycle (actif → déprécié). **Full reference implementation.**
- **Stock** — owns inventory quantities. Reacts to catalog events. Workshop exercise.
- **Fournisseur** — owns supplier relationships and notifications. Reacts to catalog events. Workshop exercise.

### Events

Domain events are modeled explicitly. Delivery is **in-process** (modular monolith).
Events are dispatched synchronously at the application service boundary, after aggregate state changes.

```
application service
  → aggregate produces event
  → service publishes via PourPublierUnEvenement port
  → in-process bus dispatches to registered handlers in other bounded contexts
```

Async delivery (Kafka, RabbitMQ) is a future adapter swap — domain and application layers don't change.

### Always-valid domain

See `ARCHITECTURE.md`. No public constructors, no sentinel instances, factories return `Either`.

### Abstractions for every infrastructure concern

Every infrastructure dependency gets a port, even small ones:

| Port | Responsibility |
|------|---------------|
| `PourGenererUnSlug` | Decoupled from any slug library |
| `PourAvoirHeure` | Decoupled from system clock |
| `PourGenererUnId` | Decoupled from ULID/UUID library |
| `PourPublierUnEvenement` | Decoupled from event bus implementation |
| `PourPersister<Aggregate>` | Decoupled from storage technology |

### Language

Ubiquitous language is **French**. Domain terms in code: `Produit`, `Fournisseur`, `Stock`,
`Slug`, `EtatDuProduit`, `ProduitAjouté`, `ProduitDéprécié`.
Technical/infrastructure terms may be in English (`Either`, `fun interface`, `suspend`, etc.).

---

## 🚀 Strategic changes

1. **Contextual Design** — align code structure with domain boundaries, no cross-context dependencies
2. **Ubiquitous Language** — use domain terms in code, bridge technical and business understanding
3. **Testable Models** — design for testability from the start, respect test pyramid
4. **Expressive Code** — intent-revealing names, self-documenting structure, fluent DSL for tests

---

## CUTE DDD: tactical changes

### Technical concerns / code easier to maintain

- Nulls and exceptions → `Either` (Arrow)
- Mapping problem: entities exposed vs dedicated DTO → adapters own their DTOs
- Anemic Domain Model → rich aggregates with invariants
- Too much logic in constructor → factory methods returning `Either`
- Slug logic → decoupled via `PourGenererUnSlug` port (Inversion of Control)
- ORM is heavy → use DTOs, adapters own the mapping
- Port and adapters for persistence

### So many changes! So little time! (workshop exercises — intentionally incomplete)

- Primitive Obsession → value objects: `Slug`, `NomDeProduit`, `Prix`
- Aggregates, Entities, Value Objects
- Events → `ProduitAjouté`, `ProduitDéprécié`
- Validations vs invariants
- Always-valid model
- Make illegal states unrepresentable
