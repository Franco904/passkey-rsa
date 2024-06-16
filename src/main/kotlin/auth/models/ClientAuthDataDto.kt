package auth.models

import utils.Certificate

class ClientAuthDataDto(
    val displayName: String,
    val email: String,
    val certificate: Certificate,
)
