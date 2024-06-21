import auth.Client
import auth.Server
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
import java.security.Security
import java.util.*

val scanner = Scanner(System.`in`)

fun main() {
    // Add BouncyCastle security provider so we can access its algorithms
    Security.addProvider(BouncyCastleFipsProvider())

    Server.init()

    val certificate = Client.storePasskeyAndCreateCertificate()
    Server.storeCertificate(certificate)

    while (true) {
        when (getAppMode()) {
            "1" -> runSignUpMode()
            "2" -> runLoginMode()
            "0" -> break
        }
    }

    Server.finish()
}

private fun getAppMode(): String {
    println("=====")
    println("Escolha um modo de uso:")
    println("[ 1 ] Registro")
    println("[ 2 ] Autenticação")
    println("[ 0 ] Sair")
    println("=====")

    return scanner.nextLine().trim()
}

private fun runSignUpMode() {
    println("[ Registro ]\n")

    val (displayName, email) = Client.inputDisplayNameAndEmail()

    try {
        Server.signUp(
            displayName = displayName,
            email = email,
        )
    } catch (e: Exception) {
        println(e.message)
    }
}

private fun runLoginMode() {
    println("[ Autenticação ]\n")

    val email = Client.inputEmail()

    try {
        val (userId, challengeBuffer) = Server.checkClientCredentials(email = email)

        val signedChallengeBuffer = Client.signChallengeBuffer(challengeBuffer)
        Server.login(
            userId = userId,
            signedChallengeBuffer = signedChallengeBuffer,
        )
    } catch (e: Exception) {
        println(e.message)
    }
}
