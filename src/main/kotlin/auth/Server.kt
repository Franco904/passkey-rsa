package auth

import auth.models.ClientCredentialsDto
import database.DatabaseManager
import database.daos.UsersDao
import database.entities.User
import utils.KeyStoreManager
import utils.createSecureRandomString
import java.security.cert.Certificate

object Server {
    private val databaseManager = DatabaseManager()
    private val usersDao = UsersDao(databaseManager)

    fun init() {
        databaseManager.connect()
    }

    fun finish() {
        databaseManager.disconnect()
    }

    fun storeCertificate(certificate: Certificate) {
        KeyStoreManager.storeCertificate(
            alias = "public",
            certificate = certificate,
        )
    }

    fun signUpClient(clientCredentialsDto: ClientCredentialsDto) {
        val userStored = usersDao.findByEmail(clientCredentialsDto.email)
        if (userStored != null) throw Exception("[Erro] Server: Usuário já cadastrado.")

        val userId = createSecureRandomString(size = 16)
        val challengeBuffer = createSecureRandomString(size = 16)

        val user = User(
            id = userId,
            displayName = clientCredentialsDto.displayName,
            email = clientCredentialsDto.email,
            challengeBuffer = challengeBuffer,
        )

        usersDao.createUser(user)
    }
}
