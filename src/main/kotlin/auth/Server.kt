package auth

import auth.models.ClientAuthDataDto
import database.DatabaseManager
import database.daos.UsersDao
import database.entities.User

object Server {
    private val databaseManager = DatabaseManager()
    private val usersDao = UsersDao(databaseManager)

    fun init() {
        databaseManager.connect()
    }

    fun finish() {
        databaseManager.disconnect()
    }

    fun signUpClient(clientAuthDataDto: ClientAuthDataDto) {
        val user = User.fromAuthDataDto(clientAuthDataDto)

        usersDao.createUser(user)
    }
}
