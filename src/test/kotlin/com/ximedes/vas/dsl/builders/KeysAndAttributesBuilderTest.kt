package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes

internal class KeysAndAttributesBuilderTest {
    private fun dslKA(init: KeysAndAttributesBuilder.() -> Unit) = KeysAndAttributesBuilder().apply(init).build()

    @Test
    fun keys() {
        val sdkKA = KeysAndAttributes.builder().keys(
            listOf(
                mapOf(
                    "a" to AttributeValue.builder().s("b").build(),
                    "y" to AttributeValue.builder().s("z").build()
                )
            )
        ).build()
        val dslKA = dslKA {
            keys({
                "a" from "b"
                "y" from "z"
            })
        }
        assertEquals(sdkKA.keys(), dslKA.keys())
    }

    @Test
    fun attributeNames() {
        val sdkRequest = KeysAndAttributes.builder().expressionAttributeNames(mapOf("a" to "z")).build()
        val dslKA = dslKA {
            attributeNames("a" to "z")
        }
        assertEquals(sdkRequest.expressionAttributeNames(), dslKA.expressionAttributeNames())
    }

    @Test
    fun projection() {
        val sdkRequest = KeysAndAttributes.builder().projectionExpression("a, b, c").build()
        val dslKA = dslKA { projection("a, b, c") }
        assertEquals(sdkRequest.projectionExpression(), dslKA.projectionExpression())
    }


    @Test
    fun consistentRead() {
        val sdkRequest = KeysAndAttributes.builder().consistentRead(true).build()
        val dslKA = dslKA {
            consistentRead(true)
        }
        assertTrue(sdkRequest.consistentRead())
        assertEquals(sdkRequest.consistentRead(), dslKA.consistentRead())
    }

}