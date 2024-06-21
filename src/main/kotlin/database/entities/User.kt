package database.entities

import auth.models.ClientAuthDataDto

class User(
    val id: Long? = 0L,
    val displayName: String,
    val email: String,
) {
    companion object {
        const val TABLE = "users"
        const val ID = "id"
        const val DISPLAY_NAME = "displayName"
        const val EMAIL = "email"

        fun fromAuthDataDto(clientAuthDataDto: ClientAuthDataDto): User {
            return User(
                displayName = clientAuthDataDto.displayName,
                email = clientAuthDataDto.email,
            )
        }
    }
}
