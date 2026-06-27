package com.davidnardie.pokemon.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun createSmokeTestHttpClient(
    config: HttpClientConfig<*>.() -> Unit = {},
): HttpClient
