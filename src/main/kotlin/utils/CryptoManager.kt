package utils

import constants.FIVE_SECONDS_IN_MILLIS
import constants.THIRTY_DAYS_IN_MILLIS
import org.apache.commons.codec.binary.Hex
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.security.Key
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.cert.Certificate
import java.util.*
import javax.crypto.Cipher

object CryptoManager {
    private const val ALGORITHM = "RSA"
    private const val BLOCK_MODE = "None"
    private const val PADDING_SCHEME = "OAEPWithSHA256AndMGF1Padding"
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING_SCHEME"
    private const val PROVIDER = "BCFIPS"

    private const val X500_NAME = "CN=Issuer CA"
    private const val SIGNATURE_ALGORITHM = "SHA256withRSA"

    fun createRSAKeyPair(): Pair<Key, Key> {
        val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER)
        val keyPair = keyPairGenerator.genKeyPair()

        return Pair(keyPair.private, keyPair.public)
    }

    fun createRSACertificate(
        publicKey: Key,
        signerPrivateKey: Key,
    ): Certificate {
        val timestamp = System.currentTimeMillis()

        val certificateBuilder = JcaX509v1CertificateBuilder(
            X500Name(X500_NAME),
            BigInteger.valueOf(timestamp),
            Date(timestamp - FIVE_SECONDS_IN_MILLIS),
            Date(timestamp + THIRTY_DAYS_IN_MILLIS),
            X500Name(X500_NAME),
            publicKey as PublicKey,
        )

        val signerBuilder = JcaContentSignerBuilder(SIGNATURE_ALGORITHM).setProvider(PROVIDER)
        val certificateConverter = JcaX509CertificateConverter().setProvider(PROVIDER)

        return certificateConverter.getCertificate(
            certificateBuilder.build(signerBuilder.build(signerPrivateKey as PrivateKey))
        )
    }

    private fun getEncrypter(key: Key): Cipher {
        return Cipher.getInstance(TRANSFORMATION, PROVIDER).apply {
            init(Cipher.ENCRYPT_MODE, key)
        }
    }

    private fun getDecrypter(certificate: Certificate): Cipher {
        return Cipher.getInstance(TRANSFORMATION, PROVIDER).apply {
            init(Cipher.DECRYPT_MODE, certificate)
        }
    }

    fun String.encrypt(key: Key): String {
        val encrypter = getEncrypter(key)
        val encryptedTextBytes = encrypter.doFinal(this.toByteArray())

        return Hex.encodeHexString(encryptedTextBytes)
    }

    fun String.decrypt(certificate: Certificate): String {
        val decrypter = getDecrypter(certificate)
        val decryptedTextBytes = decrypter.doFinal(Hex.decodeHex(this))

        return String(decryptedTextBytes, Charsets.UTF_8)
    }
}
