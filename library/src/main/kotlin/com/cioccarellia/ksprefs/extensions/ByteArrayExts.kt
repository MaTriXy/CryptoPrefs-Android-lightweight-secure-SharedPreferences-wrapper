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
@file:Suppress("NOTHING_TO_INLINE")

package com.cioccarellia.ksprefs.extensions

import androidx.annotation.CheckResult
import androidx.annotation.RestrictTo
import com.cioccarellia.ksprefs.KsPrefs
import com.cioccarellia.ksprefs.defaults.Defaults
import com.cioccarellia.ksprefs.engines.SymmetricKey

@CheckResult
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun ByteArray.toSymmetricKey() = SymmetricKey(this)

@CheckResult
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun ByteArray.string() = this.toString(
    charset = try {
        KsPrefs.config.charset
    } catch (configNotInitialized: KotlinNullPointerException) {
        Defaults.CHARSET
    }
)

@CheckResult
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun ByteArray.bitCount() = size * 8

@PublishedApi
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal inline fun emptyByteArray(
    byteCount: Int = 0
) = ByteArray(byteCount)