package catalog.application.commands

import catalog.domain.valueObjects.NomDeProduit
import catalog.domain.valueObjects.Prix

data class AjouterUnProduitCmd(
    val nom: NomDeProduit,
    val prix: Prix
)
