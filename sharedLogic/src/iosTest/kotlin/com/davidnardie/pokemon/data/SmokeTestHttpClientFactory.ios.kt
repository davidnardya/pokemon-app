@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.davidnardie.pokemon.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin
import platform.Foundation.NSURLAuthenticationMethodServerTrust
import platform.Foundation.NSURLCredential
import platform.Foundation.NSURLSessionAuthChallengePerformDefaultHandling
import platform.Foundation.NSURLSessionAuthChallengeUseCredential
import platform.Foundation.credentialForTrust
import platform.Foundation.serverTrust

actual fun createSmokeTestHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(Darwin) {
        engine {
            // Standalone iOS simulator test binaries lack the system trust store, which
            // rejects valid PokeAPI certificates with NSURLErrorDomain -1202.
            handleChallenge { _, _, challenge, completionHandler ->
                if (challenge.protectionSpace.authenticationMethod != NSURLAuthenticationMethodServerTrust) {
                    completionHandler(NSURLSessionAuthChallengePerformDefaultHandling.toInt(), null)
                    return@handleChallenge
                }

                val trust = challenge.protectionSpace.serverTrust
                if (trust != null) {
                    completionHandler(
                        NSURLSessionAuthChallengeUseCredential.toInt(),
                        NSURLCredential.credentialForTrust(trust),
                    )
                } else {
                    completionHandler(NSURLSessionAuthChallengePerformDefaultHandling.toInt(), null)
                }
            }
        }
        config()
    }
