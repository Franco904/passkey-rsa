package database.daos

import database.DatabaseManager
import database.entities.User

class UsersDao(
    private val databaseManager: DatabaseManager,
) {
    fun createUser(user: User) {
        databaseManager.insert(
            """
            INSERT INTO ${User.TABLE}(${User.DISPLAY_NAME}, ${User.EMAIL})
            VALUES("${user.displayName}", "${user.email}");
            """
        )
    }

    fun findByEmail(email: String): User? {
        return databaseManager.findById(
            query = "SELECT * FROM ${User.TABLE} WHERE ${User.EMAIL} = $email;"
        )
    }
}
