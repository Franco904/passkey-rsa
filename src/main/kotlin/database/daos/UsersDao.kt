package database.daos

import database.DatabaseManager
import database.entities.User

class UsersDao(
    private val databaseManager: DatabaseManager,
) {
    fun createUser(user: User) {
        databaseManager.insert(
            """
            INSERT INTO users(displayName, email) VALUES("${user.displayName}", "${user.email}");
            """
        )
    }
}
