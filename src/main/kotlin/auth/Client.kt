package auth

//import scanner
import utils.Certificate
import utils.CryptoManager
import utils.KeyStoreManager

object Client {
    fun inputDisplayNameAndEmail(): Pair<String, String> {
        println("Nome:")
//        val displayName = scanner.nextLine().trim()
        val displayName = "Franco"

        println("E-mail:")
//        val email = scanner.nextLine().trim()
        val email = "franco@email.com"

        return Pair(displayName, email)
    }

    fun generatePasskeyAndCertificate(): Certificate {
        // Passkey = private key
        val storedPrivateKey = KeyStoreManager.getKey(alias = "private")

        val (privateKey, publicKey) = CryptoManager.createRSAKeyPair()

        val selfSignedCertificate = CryptoManager.createRSACertificate(
            publicKey = publicKey,
            signerPrivateKey = privateKey,
        )
        if (storedPrivateKey == null) KeyStoreManager.storeKey(
            alias = "private",
            key = privateKey,
            certificate = selfSignedCertificate,
        )

        // Certificate is the public key signed with a private key
        // CA = Certification Authority
        val (caPrivateKey, _) = CryptoManager.createRSAKeyPair()
        val certificate = CryptoManager.createRSACertificate(
            publicKey = publicKey,
            signerPrivateKey = caPrivateKey,
        )

        return certificate
    }
}
