package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.*

internal class TransactGetItemsRequestBuilderTest {

    private fun dslRequest(init: TransactGetItemsRequestBuilder.() -> Unit) =
        TransactGetItemsRequestBuilder().apply(init).build()

    @Test
    fun returnConsumedCapacity() {
        val sdkRequest =
            TransactGetItemsRequest.builder().returnConsumedCapacity(ReturnConsumedCapacity.INDEXES).build()
        val dslRequest = dslRequest {
            returnConsumedCapacity(ReturnConsumedCapacity.INDEXES)
        }
        assertEquals(ReturnConsumedCapacity.INDEXES, sdkRequest.returnConsumedCapacity())
        assertEquals(sdkRequest.returnConsumedCapacity(), dslRequest.returnConsumedCapacity())
    }

    @Test
    fun singleGet() {
        val sdkRequest = TransactGetItemsRequest.builder().transactItems(
            TransactGetItem.builder().get(
                Get.builder().tableName("foo").key(
                    mapOf("a" to AttributeValue.builder().s("z").build())
                ).build()
            ).build()
        ).build()
        val dslRequest = dslRequest {
            get("foo") {
                key {
                    "a" from "z"
                }
            }
        }
        assertEquals(sdkRequest.transactItems(), dslRequest.transactItems())
    }


}