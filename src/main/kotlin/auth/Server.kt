package auth

import auth.models.ClientAuthDataDto
import database.DatabaseManager
import database.daos.UsersDao
import database.entities.User
import utils.faker

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
        val userStored = usersDao.findByEmail(clientAuthDataDto.email)
        if (userStored != null) throw Exception("Usuário já cadastrado.")

        val challengeBuffer = faker.random.randomString(8)
        val userId = faker.random.randomString(8)

        val user = User(
            id = userId,
            displayName = clientAuthDataDto.displayName,
            email = clientAuthDataDto.email,
            challengeBuffer = challengeBuffer,
            challengeVerification = ""
        )

        usersDao.createUser(user)
    }
}
