package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.*

internal class BatchWriteItemRequestBuilderTest {
    private fun dslRequest(init: BatchWriteItemRequestBuilder.() -> Unit) =
        BatchWriteItemRequestBuilder().apply(init).build()

    @Test
    fun deletes() {
        val sdkRequest = BatchWriteItemRequest.builder().requestItems(
            mapOf(
                "table" to listOf(
                    WriteRequest.builder().deleteRequest(
                        DeleteRequest.builder().key(
                            mapOf("a" to AttributeValue.builder().s("b").build())
                        ).build()
                    ).build(),
                    WriteRequest.builder().deleteRequest(
                        DeleteRequest.builder().key(
                            mapOf("x" to AttributeValue.builder().s("y").build())
                        ).build()
                    ).build()
                )
            )
        ).build()
        val dslRequest = dslRequest {
            delete("table", {
                "a" from "b"
            }, {
                "x" from "y"
            })
        }
        assertEquals(sdkRequest.requestItems(), dslRequest.requestItems())
    }

    @Test
    fun puts() {
        val sdkRequest = BatchWriteItemRequest.builder().requestItems(
            mapOf(
                "table" to listOf(
                    WriteRequest.builder().putRequest(
                        PutRequest.builder().item(
                            mapOf("a" to AttributeValue.builder().s("b").build())
                        ).build()
                    ).build(),
                    WriteRequest.builder().putRequest(
                        PutRequest.builder().item(
                            mapOf("x" to AttributeValue.builder().s("y").build())
                        ).build()
                    ).build()
                )
            )
        ).build()
        val dslRequest = dslRequest {
            put("table", {
                "a" from "b"
            }, {
                "x" from "y"
            })
        }
        assertEquals(sdkRequest.requestItems(), dslRequest.requestItems())
    }

    @Test
    fun returnConsumedCapacity() {
        val sdkRequest = BatchWriteItemRequest.builder().returnConsumedCapacity(ReturnConsumedCapacity.INDEXES).build()
        val dslRequest = dslRequest {
            returnConsumedCapacity(ReturnConsumedCapacity.INDEXES)
        }
        assertEquals(sdkRequest.returnConsumedCapacity(), dslRequest.returnConsumedCapacity())
        assertEquals(ReturnConsumedCapacity.INDEXES, dslRequest.returnConsumedCapacity())
    }


    @Test
    fun returnItemCollectionMetrics() {
        val sdkRequest = BatchWriteItemRequest.builder()
            .returnItemCollectionMetrics(ReturnItemCollectionMetrics.SIZE)
            .build()
        val dslRequest = dslRequest {
            returnItemCollectionMetrics(ReturnItemCollectionMetrics.SIZE)
        }
        assertEquals(sdkRequest.returnItemCollectionMetrics(), dslRequest.returnItemCollectionMetrics())
        assertEquals(ReturnItemCollectionMetrics.SIZE, dslRequest.returnItemCollectionMetrics())
    }
}