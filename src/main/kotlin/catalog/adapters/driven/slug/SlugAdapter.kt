package catalog.adapters.driven.slug

import arrow.core.Either
import arrow.core.right
import catalog.domain.errors.ErreurDeCatalog
import catalog.domain.factories.PourGenererUnSlug
import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Slug

// Production slug generation: lowercase, replace spaces and special chars with hyphens.
// Decoupled from any external slug library — swap the implementation here without touching the domain.
class SlugAdapter : PourGenererUnSlug {
    override fun generer(nom: NomDeProduit): Either<ErreurDeCatalog, Slug> {
        val valeur = nom.valeur
            .lowercase()
            .trim()
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .replace(Regex("\\s+"), "-")
        return Slug.de(valeur)
    }
}
