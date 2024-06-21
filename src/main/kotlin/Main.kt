import auth.Client
import auth.Server
import auth.models.ClientCredentialsDto
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
            "2" -> runAuthMode()
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

    val clientCredentialsDto = ClientCredentialsDto(
        displayName = displayName,
        email = email,
    )

    try {
        Server.signUpClient(clientCredentialsDto = clientCredentialsDto)
    } catch (e: Exception) {
        println(e.message)
    }
}

private fun runAuthMode() {
    println("[ Autenticação ]\n")
}
