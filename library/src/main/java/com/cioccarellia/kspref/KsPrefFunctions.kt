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
package com.cioccarellia.kspref

import com.cioccarellia.kspref.extensions.emptyByteArray
import java.security.SecureRandom
import kotlin.random.asKotlinRandom

fun KsPrefs.Companion.randomIV(): ByteArray = SecureRandom().asKotlinRandom().nextBytes(
    emptyByteArray(
        byteCount = config.encryption.keySize.byteCount()
    )
)