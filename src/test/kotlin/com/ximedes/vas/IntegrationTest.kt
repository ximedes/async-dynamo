package com.ximedes.vas

import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Test

class IntegrationTest {

    @Test
    fun yo() = withTestApplication({
        module()
    }) {
        with(handleRequest(HttpMethod.Post, "/account") {
        }) {

        }
    }
}