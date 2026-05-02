package fournisseur.domain.entities

// TODO: implement the Fournisseur aggregate
// This bounded context has its own view of a product — only what a supplier cares about:
// which products they supply, and whether they've been notified of changes.
//
// Workshop exercise:
//   - Value objects: NomDeFournisseur, EmailDeFournisseur
//   - Always-valid: private constructor, factory returning Either<ErreurDeFournisseur, Fournisseur>
//   - Operations: notifierDepreciation(produitId) -> Either<ErreurDeFournisseur, Fournisseur>
//   - Produces event: FournisseurNotifie
