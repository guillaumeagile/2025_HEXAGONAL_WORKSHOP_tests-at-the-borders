package catalog.domain.errors

sealed interface ErreurDeCatalog {
    data object NomVide : ErreurDeCatalog
    data object PrixInvalide : ErreurDeCatalog
    data object SlugVide : ErreurDeCatalog
    data class ProduitIntrouvable(val id: String) : ErreurDeCatalog
    data object ProduitDejaDeprecied : ErreurDeCatalog
}
