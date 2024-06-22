package auth

import database.DatabaseManager
import database.daos.UsersDao
import database.entities.User
import utils.managers.CryptoManager.decrypt
import utils.managers.KeyStoreManager
import utils.createSecureRandomString
import java.security.cert.Certificate

object Server {
    private val databaseManager by lazy { DatabaseManager() }
    private val usersDao by lazy { UsersDao(databaseManager) }

    private val keyStoreManager by lazy { KeyStoreManager("server-ks") }

    fun init() {
        databaseManager.connect()
    }

    fun finish() {
        databaseManager.disconnect()
    }

    fun signUp(
        displayName: String,
        email: String,
    ): String {
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

        return userId
    }

    fun storeCertificate(
        certificate: Certificate,
        userId: String,
    ) {
        keyStoreManager.storeCertificate(
            alias = userId,
            certificate = certificate,
        )
    }

    fun checkClientCredentials(email: String): Pair<String, String> {
        val storedUser = usersDao.findByEmail(email) ?: throw Exception("[Erro] Server: Usuário não encontrado.")

        return Pair(storedUser.id, storedUser.challengeBuffer)
    }

    fun login(
        signedChallengeBuffer: String,
        userId: String,
    ) {
        val certificate = keyStoreManager.getCertificate(userId) ?: throw Exception("[Erro] Server: Certificado não encontrado.")

        val challengeBuffer = signedChallengeBuffer.decrypt(certificate)

        val storedUser = usersDao.findById(userId) ?: throw Exception("[Erro] Server: Usuário não encontrado.")
        val storedChallengeBuffer = storedUser.challengeBuffer

        val isAuthenticated = challengeBuffer == storedChallengeBuffer

        if (!isAuthenticated) {
            throw Exception("[Erro] Server: Challenge inválido.")
        }
    }
}
