package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity
import software.amazon.awssdk.services.dynamodb.model.ReturnItemCollectionMetrics
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest

internal class UpdateItemRequestBuilderTest {

    private fun dslRequest(tableName: String = "foo", init: UpdateItemRequestBuilder.() -> Unit) =
        UpdateItemRequestBuilder(tableName).apply(init).build()

    @Test
    fun update() {
        val sdkRequest = UpdateItemRequest.builder().updateExpression("update").build()
        val dslRequest = dslRequest { update("update") }
        assertEquals(sdkRequest.updateExpression(), dslRequest.updateExpression())
    }

    @Test
    fun condition() {
        val sdkRequest = UpdateItemRequest.builder().conditionExpression("condition").build()
        val dslRequest = dslRequest { condition("condition") }
        assertEquals(sdkRequest.conditionExpression(), dslRequest.conditionExpression())
    }

    @Test
    fun key() {
        val sdkRequest = UpdateItemRequest.builder().key(mapOf("a" to AttributeValue.builder().s("b").build())).build()
        val dslRequest = dslRequest { key { "a" from "b" } }
        assertEquals(sdkRequest.key(), dslRequest.key())
    }


    @Test
    fun attributeValues() {
        val sdkRequest =
            UpdateItemRequest.builder().expressionAttributeValues(mapOf("a" to AttributeValue.builder().s("z").build()))
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
        val sdkRequest = UpdateItemRequest.builder().expressionAttributeNames(mapOf("a" to "z")).build()
        val dslRequest = dslRequest {
            attributeNames("a" to "z")
        }
        assertEquals(sdkRequest.expressionAttributeNames(), dslRequest.expressionAttributeNames())
    }

    @Test
    fun returnConsumedCapacity() {
        val sdkRequest = UpdateItemRequest.builder().returnConsumedCapacity(ReturnConsumedCapacity.INDEXES).build()
        val dslRequest = dslRequest {
            returnConsumedCapacity(ReturnConsumedCapacity.INDEXES)
        }
        assertEquals(sdkRequest.returnConsumedCapacity(), dslRequest.returnConsumedCapacity())
        assertEquals(ReturnConsumedCapacity.INDEXES, dslRequest.returnConsumedCapacity())
    }


    @Test
    fun returnItemCollectionMetrics() {
        val sdkRequest = UpdateItemRequest.builder()
            .returnItemCollectionMetrics(ReturnItemCollectionMetrics.SIZE)
            .build()
        val dslRequest = dslRequest {
            returnItemCollectionMetrics(ReturnItemCollectionMetrics.SIZE)
        }
        assertEquals(sdkRequest.returnItemCollectionMetrics(), dslRequest.returnItemCollectionMetrics())
        assertEquals(ReturnItemCollectionMetrics.SIZE, dslRequest.returnItemCollectionMetrics())
    }


}