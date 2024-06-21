package utils

import org.bouncycastle.util.encoders.Hex
import java.security.SecureRandom

fun createSecureRandomString(size: Int): String {
    val secureRandom = SecureRandom.getInstance("SHA1PRNG")

    val bytes = ByteArray(size).apply { secureRandom.nextBytes(this) }

    return Hex.toHexString(bytes)
}
