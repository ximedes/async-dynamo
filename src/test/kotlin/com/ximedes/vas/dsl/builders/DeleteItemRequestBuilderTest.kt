package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity
import software.amazon.awssdk.services.dynamodb.model.ReturnItemCollectionMetrics

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

    @Test
    fun returnConsumedCapacity() {
        val sdkRequest = DeleteItemRequest.builder().returnConsumedCapacity(ReturnConsumedCapacity.INDEXES).build()
        val dslRequest = dslRequest {
            returnConsumedCapacity(ReturnConsumedCapacity.INDEXES)
        }
        assertEquals(sdkRequest.returnConsumedCapacity(), dslRequest.returnConsumedCapacity())
        assertEquals(ReturnConsumedCapacity.INDEXES, dslRequest.returnConsumedCapacity())
    }


    @Test
    fun returnItemCollectionMetrics() {
        val sdkRequest = DeleteItemRequest.builder()
            .returnItemCollectionMetrics(ReturnItemCollectionMetrics.SIZE)
            .build()
        val dslRequest = dslRequest {
            returnItemCollectionMetrics(ReturnItemCollectionMetrics.SIZE)
        }
        assertEquals(sdkRequest.returnItemCollectionMetrics(), dslRequest.returnItemCollectionMetrics())
        assertEquals(ReturnItemCollectionMetrics.SIZE, dslRequest.returnItemCollectionMetrics())
    }

    @Test
    fun attributeValues() {
        val sdkRequest =
            DeleteItemRequest.builder().expressionAttributeValues(mapOf("a" to AttributeValue.builder().s("z").build()))
                .build()
        val dslRequest = dslRequest {
            attributeValues {
                "a" from "z"
            }
        }
        assertEquals(sdkRequest.expressionAttributeValues(), dslRequest.expressionAttributeValues())
    }

    @Test
    fun attributeNames() {
        val sdkRequest = DeleteItemRequest.builder().expressionAttributeNames(mapOf("a" to "z")).build()
        val dslRequest = dslRequest {
            attributeNames("a" to "z")
        }
        assertEquals(sdkRequest.expressionAttributeNames(), dslRequest.expressionAttributeNames())
    }


}