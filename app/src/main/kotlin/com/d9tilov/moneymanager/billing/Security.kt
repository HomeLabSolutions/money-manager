package com.d9tilov.moneymanager.billing

import android.text.TextUtils
import android.util.Base64
import timber.log.Timber
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.BuildConfig
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.Signature
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import kotlin.Throws

/*
 * This class is an sample of how you can check to make sure your purchases on the device came from
 * Google Play. Putting code like this on your server will provide additional protection.
 * <p>
 * One thing that you may also wish to consider doing is caching purchase IDs to make replay attacks
 * harder. The reason this code isn't just part of the library is to allow you to customize it (and
 * rename it!) to make generic patching exploits more difficult.
 */ /**
 * Security-related methods. For a secure implementation, all of this code should be implemented on
 * a server that communicates with the application on the device.
 */
object Security {
    private const val KEY_FACTORY_ALGORITHM = "RSA"
    private const val SIGNATURE_ALGORITHM = "SHA1withRSA"

    /**
     * BASE_64_ENCODED_PUBLIC_KEY should be YOUR APPLICATION PUBLIC KEY. You currently get this
     * from the Google Play developer console under the "Monetization Setup" category in the
     * Licensing area. This build has been setup so that if you define base64EncodedPublicKey in
     * your local.properties, it will be echoed into BuildConfig.
     */
    private const val BASE_64_ENCODED_PUBLIC_KEY = BuildConfig.BASE64_ENCODED_PUBLIC_KEY

    /**
     * Verifies that the data was signed with the given signature
     *
     * @param signedData the signed JSON string (signed, not encrypted)
     * @param signature  the signature for the data, signed with the private key
     */
    fun verifyPurchase(signedData: String, signature: String?): Boolean {
        if (TextUtils.isEmpty(signedData) || TextUtils.isEmpty(BASE_64_ENCODED_PUBLIC_KEY) ||
            TextUtils.isEmpty(signature)
        ) {
            Timber.tag(App.TAG).w("Purchase verification failed: missing data.")
            return false
        }
        return try {
            val key = generatePublicKey(BASE_64_ENCODED_PUBLIC_KEY)
            verify(key, signedData, signature)
        } catch (e: IOException) {
            Timber.tag(App.TAG).d("Error generating PublicKey from encoded key: %s", e.message)
            false
        }
    }

    /**
     * Generates a PublicKey instance from a string containing the Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
     * @throws IOException if encoding algorithm is not supported or key specification
     * is invalid
     */
    @Throws(IOException::class)
    private fun generatePublicKey(encodedPublicKey: String): PublicKey {
        return try {
            val decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
            keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (e: NoSuchAlgorithmException) {
            // "RSA" is guaranteed to be available.
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            val msg = "Invalid key specification: $e"
            Timber.tag(App.TAG).d(msg)
            throw IOException(msg)
        }
    }

    /**
     * Verifies that the signature from the server matches the computed signature on the data.
     * Returns true if the data is correctly signed.
     *
     * @param publicKey  public key associated with the developer account
     * @param signedData signed data from server
     * @param signature  server signature
     * @return true if the data and signature match
     */
    private fun verify(publicKey: PublicKey, signedData: String, signature: String?): Boolean {
        val signatureBytes: ByteArray = try {
            Base64.decode(signature, Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            Timber.tag(App.TAG).d("Base64 decoding failed.")
            return false
        }
        try {
            val signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITHM)
            signatureAlgorithm.initVerify(publicKey)
            signatureAlgorithm.update(signedData.toByteArray())
            if (!signatureAlgorithm.verify(signatureBytes)) {
                Timber.tag(App.TAG).d("Signature verification failed...")
                return false
            }
            return true
        } catch (e: NoSuchAlgorithmException) {
            // "RSA" is guaranteed to be available.
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            Timber.tag(App.TAG).d("Invalid key specification.")
        } catch (e: SignatureException) {
            Timber.tag(App.TAG).d("Signature exception.")
        }
        return false
    }
}
