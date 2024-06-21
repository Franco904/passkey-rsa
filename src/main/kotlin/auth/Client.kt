package auth

import scanner
import utils.CryptoManager
import utils.CryptoManager.encrypt
import utils.KeyStoreManager
import utils.faker
import java.security.cert.Certificate

object Client {
    fun storePasskeyAndCreateCertificate(): Certificate {
        val (privateKey, publicKey) = CryptoManager.createRSAKeyPair()

        val selfSignedCertificate = CryptoManager.createRSACertificate(
            publicKey = publicKey,
            signerPrivateKey = privateKey,
        )
        KeyStoreManager.storeKey(
            alias = "private",
            key = privateKey,
            certificate = selfSignedCertificate,
        )

        // Certificate is the public key signed with a private key
        // CA = Certification Authority
        val (caPrivateKey, _) = CryptoManager.createRSAKeyPair()
        val caCertificate = CryptoManager.createRSACertificate(
            publicKey = publicKey,
            signerPrivateKey = caPrivateKey,
        )

        return caCertificate
    }

    fun inputDisplayNameAndEmail(): Pair<String, String> {
//        println("Nome:")
//        val displayName = scanner.nextLine().trim()
        val displayName = faker.name.name()

//        println("E-mail:")
//        val email = scanner.nextLine().trim()
        val email = "${displayName.lowercase().split(' ').last()}@email.com"

        return Pair(displayName, email)
    }

    fun inputEmail(): String {
        println("E-mail:")
        val email = scanner.nextLine().trim()

        return email
    }

    fun signChallengeBuffer(challengeBuffer: String): String {
        val privateKey = KeyStoreManager.getKey("private") ?: throw Exception("[Erro] Client: Chave privada não encontrada.")

        return challengeBuffer.encrypt(privateKey)
    }
}
