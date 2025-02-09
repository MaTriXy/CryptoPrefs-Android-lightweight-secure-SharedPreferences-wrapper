/**
 * Designed and developed by Andrea Cioccarelli (@cioccarellia)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cioccarellia.ksprefs.engines.model.keystore

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.cioccarellia.ksprefs.engines.Transmission
import com.cioccarellia.ksprefs.engines.base.CryptoEngine
import com.cioccarellia.ksprefs.engines.base.Engine
import com.cioccarellia.ksprefs.engines.base.KeystoreEngine
import com.cioccarellia.ksprefs.engines.model.keystore.fetcher.KeyStoreFetcher
import com.cioccarellia.ksprefs.extensions.initDecryptAesGcmKeystore
import com.cioccarellia.ksprefs.extensions.initEncryptAesGcmKeystore
import com.cioccarellia.ksprefs.internal.SafeRun
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey

@RequiresApi(Build.VERSION_CODES.M)
internal class AesKeyStoreEngine(
    alias: String,
    private val keyTagSizeInBits: Int,
    private val base64Flags: Int
) : Engine(), CryptoEngine, KeystoreEngine, SafeRun {

    override val keystoreInstance = "AndroidKeyStore"

    override val algorithm = "AES"
    override val blockCipherMode = "GCM"
    override val paddingScheme = "NoPadding"

    override fun derive(incoming: Transmission) = Transmission(
        encrypt(incoming.payload)
    )

    override fun integrate(outgoing: Transmission) = Transmission(
        decrypt(outgoing.payload)
    )

    private val keyStore: KeyStore = KeyStore
        .getInstance(keystoreInstance)
        .also {
            it.load(null)
        }

    private val secretKey: SecretKey = KeyStoreFetcher.keystore(keyStore, alias)

    private val encryptionCipher: Cipher
        get() = Cipher.getInstance(cipherTransformation).apply {
            initEncryptAesGcmKeystore(secretKey, keyTagSizeInBits)
        }

    private val decryptionCipher: Cipher = Cipher.getInstance(cipherTransformation).apply {
        initDecryptAesGcmKeystore(secretKey, keyTagSizeInBits)
    }

    override fun encrypt(input: ByteArray): ByteArray = runSafely {
        Base64.encode(
            encryptionCipher.doFinal(input), base64Flags
        )
    }

    override fun decrypt(cipherText: ByteArray): ByteArray = runSafely {
        decryptionCipher.doFinal(
            Base64.decode(
                cipherText, base64Flags
            )
        )
    }
}