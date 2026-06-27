package com.davidnardie.pokemon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform