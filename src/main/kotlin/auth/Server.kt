package auth

import database.DatabaseManager
import database.daos.UsersDao
import database.entities.User
import utils.CryptoManager.decrypt
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

    fun signUp(displayName: String, email: String) {
        val storedUser = usersDao.findByEmail(email)
        if (storedUser != null) throw Exception("[Erro] Server: Usuário já cadastrado.")

        val userId = createSecureRandomString(size = 16)
        val challengeBuffer = createSecureRandomString(size = 16)

        val user = User(
            id = userId,
            displayName = displayName,
            email = email,
            challengeBuffer = challengeBuffer,
        )

        usersDao.createUser(user)
    }

    fun checkClientCredentials(email: String): Pair<String, String> {
        // TODO: Tornar genérica a mensagem de erro
        val storedUser = usersDao.findByEmail(email) ?: throw Exception("[Erro] Server: Usuário não cadastrado.")

        return Pair(storedUser.id, storedUser.challengeBuffer)
    }

    fun login(
        userId: String,
        signedChallengeBuffer: String,
    ) {
        val certificate = KeyStoreManager.getCertificate("public") ?: throw Exception("[Erro] Server: Certificado não encontrado.")

        val challengeBuffer = signedChallengeBuffer.decrypt(certificate)

        val storedUser = usersDao.findById(userId) ?: throw Exception("[Erro] Server: Usuário não cadastrado.")
        val storedChallengeBuffer = storedUser.challengeBuffer

        val isAuthenticated = challengeBuffer == storedChallengeBuffer

        if (isAuthenticated) {
            println("[Sucesso] Server: Usuário autenticado com sucesso.")
        }
    }
}
