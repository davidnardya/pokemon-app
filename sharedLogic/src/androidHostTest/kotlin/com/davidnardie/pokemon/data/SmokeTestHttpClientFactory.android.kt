package com.davidnardie.pokemon.data

import com.davidnardie.pokemon.di.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

actual fun createSmokeTestHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    createHttpClient(config)
