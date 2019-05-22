package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

internal class PutItemRequestBuilderTest {

    private fun dslRequest(tableName: String = "foo", init: PutItemRequestBuilder.() -> Unit): PutItemRequest {
        return PutItemRequestBuilder(tableName).apply(init).build()
    }

    @Test
    fun tableName() {
        val sdkRequest = PutItemRequest.builder().tableName("table").build()
        val dslRequest = dslRequest("table") {}
        assertEquals(sdkRequest.tableName(), dslRequest.tableName())
    }

    @Test
    fun item() {
        val item = ItemBuilder().apply {
            "string" from "a"
            "int" from 2
        }.build()
        val sdkRequest = PutItemRequest.builder().item(item).build()
        val dslRequest = dslRequest {
            item {
                "string" from "a"
                "int" from 2
            }
        }
        assertEquals(sdkRequest.item(), dslRequest.item())
    }

    @Test
    fun attributeValues() {
        val sdkRequest =
            PutItemRequest.builder().expressionAttributeValues(mapOf("a" to AttributeValue.builder().s("z").build()))
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
        val sdkRequest = PutItemRequest.builder().expressionAttributeNames(mapOf("a" to "z")).build()
        val dslRequest = dslRequest {
            attributeNames("a" to "z")
        }
        assertEquals(sdkRequest.expressionAttributeNames(), dslRequest.expressionAttributeNames())
    }


}