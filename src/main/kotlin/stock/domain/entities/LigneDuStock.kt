package stock.domain.entities

// TODO: implement the Stock aggregate
// This bounded context has its own view of a product — only what stock cares about:
// a product reference and a quantity. No name, no slug, no price.
//
// Workshop exercise:
//   - Define value objects: ReferenceDeProduigt, Quantite
//   - Always-valid: private constructor, factory returning Either<ErreurDeStock, LigneDuStock>
//   - Operations: approvisionner(quantite), vendre(quantite) -> Either<ErreurDeStock, LigneDuStock>
//   - Each operation produces an event: StockApprovisionne, StockVendu
