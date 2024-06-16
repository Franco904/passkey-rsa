import auth.Client
import auth.Server
import auth.models.ClientAuthDataDto
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
import java.security.Security
import java.util.*

val scanner = Scanner(System.`in`)

fun main() {
    // Add BouncyCastle security provider so we can access its algorithms
    Security.addProvider(BouncyCastleFipsProvider())

    while (true) {
        when (getAppMode()) {
            "1" -> runSignUpMode()
            "2" -> runAuthMode()
            "0" -> break
        }
    }
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
    val certificate = Client.createPasskeyAndCertificate()

    val authDataDto = ClientAuthDataDto(
        displayName = displayName,
        email = email,
        certificate = certificate,
    )

    try {
        Server.signUpClient(clientAuthDataDto = authDataDto)
    } catch (e: Exception) {
        println(e.message)
    }
}

private fun runAuthMode() {
    println("[ Autenticação ]\n")
}
