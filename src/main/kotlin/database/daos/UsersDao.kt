package database.daos

import database.DatabaseManager
import database.entities.User

class UsersDao(
    private val databaseManager: DatabaseManager,
) {
    fun createUser(user: User) {
        databaseManager.insert(
            """
            INSERT INTO ${User.TABLE}(${User.ID}, ${User.DISPLAY_NAME}, ${User.EMAIL}, ${User.CHALLENGE_BUFFER}, ${User.CHALLENGE_VERIFICATION})
            VALUES("${user.id}", "${user.displayName}", "${user.email}", "${user.challengeBuffer}", "${user.challengeVerification}");
            """
        )
    }

    fun findByEmail(email: String): User? {
        return databaseManager.findSingle(
            query = "SELECT * FROM ${User.TABLE} WHERE ${User.EMAIL} = ?;",
            args = listOf(email),
        )
    }
}
