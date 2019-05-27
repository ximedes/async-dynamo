package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest

internal class DeleteItemRequestBuilderTest {
    private fun dslRequest(tableName: String = "foo", init: DeleteItemRequestBuilder.() -> Unit) =
        DeleteItemRequestBuilder(tableName).apply(init).build()

    @Test
    fun key() {
        val sdkRequest = DeleteItemRequest.builder().key(mapOf("a" to AttributeValue.builder().s("z").build())).build()
        val dslRequest = dslRequest {
            key {
                "a" from "z"
            }
        }
        assertEquals(sdkRequest.key(), dslRequest.key())
    }

    @Test
    fun tableName() {
        val sdkRequest = DeleteItemRequest.builder().tableName("table").build()
        val dslRequest = dslRequest("table") { }
        assertEquals(sdkRequest.tableName(), dslRequest.tableName())
    }

    @Test
    fun condition() {
        val sdkRequest = DeleteItemRequest.builder().conditionExpression("my condition").build()
        val dslRequest = dslRequest { condition("my condition") }
        assertEquals(sdkRequest.conditionExpression(), dslRequest.conditionExpression())
    }


}