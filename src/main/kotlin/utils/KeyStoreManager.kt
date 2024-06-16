package utils

import java.security.Key
import java.security.KeyStore

object KeyStoreManager {
    private const val TYPE = "BCFKS"
    private const val PROVIDER = "BCFIPS"

    private const val KEYSTORE_MASTER_PASSWORD = "password"

    private val keyStore = KeyStore.getInstance(TYPE, PROVIDER).apply {
        load(null, null)
    }

    fun storeKey(
        alias: String,
        key: Key,
        certificate: Certificate?,
    ) {
        keyStore.setKeyEntry(
            alias,
            key,
            KEYSTORE_MASTER_PASSWORD.toCharArray(),
            if (certificate == null) null else arrayOf(certificate),
        )
    }

    fun getKey(alias: String): Key? {
        return keyStore.getKey(
            alias,
            KEYSTORE_MASTER_PASSWORD.toCharArray(),
        )
    }
}