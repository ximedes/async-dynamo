package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity

internal class BatchGetItemRequestBuilderTest {
    private fun dslRequest(init: BatchGetItemRequestBuilder.() -> Unit) =
        BatchGetItemRequestBuilder().apply(init).build()

    @Test
    fun returnConsumedCapacity() {
        val sdkRequest = BatchGetItemRequest.builder().returnConsumedCapacity(ReturnConsumedCapacity.INDEXES).build()
        val dslRequest = dslRequest {
            returnConsumedCapacity(ReturnConsumedCapacity.INDEXES)
        }
        assertEquals(ReturnConsumedCapacity.INDEXES, sdkRequest.returnConsumedCapacity())
        assertEquals(sdkRequest.returnConsumedCapacity(), dslRequest.returnConsumedCapacity())
    }

    @Test
    fun items() {
        val sdkRequest = BatchGetItemRequest.builder().requestItems(
            mapOf(
                "table1" to KeysAndAttributes.builder().keys(
                    mapOf("a" to AttributeValue.builder().s("b").build()),
                    mapOf("c" to AttributeValue.builder().s("d").build())
                ).build(),
                "table2" to KeysAndAttributes.builder().keys(
                    mapOf("y" to AttributeValue.builder().s("z").build())
                ).consistentRead(true).build()
            )
        ).build()
        val dslRequest = dslRequest {
            items("table1") {
                // TODO this API sucks
                keys({ "a" from "b" },
                    { "c" from "d" })
            }
            items("table2") {
                consistentRead(true)
                keys({
                    "y" from "z"
                })
            }
        }
        assertEquals(sdkRequest.requestItems(), dslRequest.requestItems())
    }
}