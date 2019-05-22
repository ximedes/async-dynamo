package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.Put
import software.amazon.awssdk.services.dynamodb.model.ReturnValuesOnConditionCheckFailure

internal class PutBuilderTest {
    private fun dslPut(
        tableName: String = "foo",
        rvoccf: ReturnValuesOnConditionCheckFailure? = null,
        init: PutBuilder.() -> Unit
    ) =
        if (rvoccf != null) {
            PutBuilder(tableName, rvoccf)
        } else {
            PutBuilder(tableName)
        }.apply(init).build()

    @Test
    fun item() {
        val sdkPut = Put.builder().item(mapOf("a" to AttributeValue.builder().s("z").build())).build()
        val dslPut = dslPut {
            item {
                "a" from "z"
            }
        }
        assertEquals(sdkPut.item(), dslPut.item())
    }

    @Test
    fun condition() {
        val sdkPut = Put.builder().conditionExpression("condition").build()
        val dslPut = dslPut {
            condition("condition")
        }
        assertEquals(sdkPut.conditionExpression(), dslPut.conditionExpression())
    }

    @Test
    fun attributeValues() {
        val sdkPut =
            Put.builder().expressionAttributeValues(mapOf("a" to AttributeValue.builder().s("z").build())).build()
        val dslPut = dslPut {
            attributeValues {
                "a" from "z"
            }
        }
        assertEquals(sdkPut.expressionAttributeValues(), dslPut.expressionAttributeValues())
    }

    @Test
    fun attributeNames() {
        val sdkPut = Put.builder().expressionAttributeNames(mapOf("a" to "z")).build()
        val dslPut = dslPut {
            attributeNames("a" to "z")
        }
        assertEquals(sdkPut.expressionAttributeNames(), dslPut.expressionAttributeNames())
    }

    @Nested
    inner class ReturnValuesOnConditionCheckFailureTests {
        @Test
        fun defaultIsNull() {
            val sdkPut = Put.builder().build()
            val dslPut = dslPut { }
            assertEquals(sdkPut.returnValuesOnConditionCheckFailure(), dslPut.returnValuesOnConditionCheckFailure())
            assertNull(sdkPut.returnValuesOnConditionCheckFailure())
        }

        @Test
        fun returnValuesOnConditionCheckFailure() {
            val sdkPut =
                Put.builder().returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD).build()
            val dslPut = dslPut("foo", ReturnValuesOnConditionCheckFailure.ALL_OLD) { }
            assertEquals(sdkPut.returnValuesOnConditionCheckFailure(), dslPut.returnValuesOnConditionCheckFailure())
        }
    }
}