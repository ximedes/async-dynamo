package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.Get

internal class GetBuilderTest {

    private fun dslGet(tableName: String = "foo", init: GetBuilder.() -> Unit): Get {
        return GetBuilder(tableName).apply(init).build()
    }

    @Test
    fun tableName() {
        val sdkGet = Get.builder().tableName("ledger").build()
        val dslGet = dslGet("ledger") {}
        assertEquals(sdkGet.tableName(), dslGet.tableName())
    }

    @Test
    fun key() {
        val sdkGet = Get.builder().key(
            mapOf(
                "a" to AttributeValue.builder().n("1").build(),
                "b" to AttributeValue.builder().s("a").build()
            )
        ).build()
        val dslGet = dslGet {
            key {
                "a" from 1
                "b" from "a"
            }
        }
        assertEquals(sdkGet.key(), dslGet.key())
    }

    @Test
    fun projection() {
        val sdkGet = Get.builder().projectionExpression("a, b, c").build()
        val dslGet = dslGet { projection("a, b, c") }
        assertEquals(sdkGet.projectionExpression(), dslGet.projectionExpression())
    }

    @Test
    fun attributeNames() {
        val sdkGet = Get.builder().expressionAttributeNames(mapOf("a" to "1", "b" to "2")).build()
        val dslGet = dslGet { attributeNames("a" to "1", "b" to "2") }
        assertEquals(sdkGet.expressionAttributeNames(), dslGet.expressionAttributeNames())
    }
}