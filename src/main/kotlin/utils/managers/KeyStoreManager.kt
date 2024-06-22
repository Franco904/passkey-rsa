package utils.managers

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.Key
import java.security.KeyStore
import java.security.cert.Certificate

class KeyStoreManager(fileName: String) {
    private val filePath = "$FILE_PATH$fileName.bcfks"

    private val keyStore = KeyStore.getInstance(TYPE, PROVIDER).apply {
        val file = File(filePath)
        if (file.exists()) {
            load(FileInputStream(filePath), PASSWORD.toCharArray())
        } else {
            load(null, PASSWORD.toCharArray())
            store(FileOutputStream(filePath), PASSWORD.toCharArray())
        }
    }

    fun storeKey(
        alias: String,
        key: Key,
        certificate: Certificate?,
    ) {
        keyStore.setKeyEntry(
            alias,
            key,
            PASSWORD.toCharArray(),
            if (certificate == null) null else arrayOf(certificate),
        )

        saveKeyStore()
    }

    fun getKey(alias: String): Key? {
        return keyStore.getKey(
            alias,
            PASSWORD.toCharArray(),
        )
    }

    fun storeCertificate(
        alias: String,
        certificate: Certificate,
    ) {
        keyStore.setCertificateEntry(
            alias,
            certificate,
        )

        saveKeyStore()
    }

    fun getCertificate(alias: String): Certificate? {
        return keyStore.getCertificate(alias)
    }

    private fun saveKeyStore() {
        keyStore.store(FileOutputStream(filePath), PASSWORD.toCharArray())
    }

    companion object {
        private const val TYPE = "BCFKS"
        private const val PROVIDER = "BCFIPS"

        private const val FILE_PATH = "src/main/resources/keystore/"
        private const val PASSWORD = "password"
    }
}
