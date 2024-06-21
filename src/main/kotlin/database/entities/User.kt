package database.entities

class User(
    val id: String,
    val displayName: String,
    val email: String,
    val challengeBuffer: String,
    val challengeVerification: String,
) {
    companion object {
        const val TABLE = "users"
        const val ID = "id"
        const val DISPLAY_NAME = "displayName"
        const val EMAIL = "email"
        const val CHALLENGE_BUFFER = "challengeBuffer"
        const val CHALLENGE_VERIFICATION = "challengeVerification"
    }
}
