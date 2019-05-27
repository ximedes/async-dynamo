package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.Delete
import software.amazon.awssdk.services.dynamodb.model.Put
import software.amazon.awssdk.services.dynamodb.model.ReturnValuesOnConditionCheckFailure

internal class DeleteBuilderTest {

    private fun dslDelete(tableName: String = "foo", init: DeleteBuilder.() -> Unit) =
        DeleteBuilder(tableName).apply(init).build()

    @Test
    fun tableName() {
        val sdkDelete = Delete.builder().tableName("table").build()
        val dslDelete = dslDelete("table") {}
        assertEquals(sdkDelete.tableName(), dslDelete.tableName())
    }

    @Test
    fun condition() {
        val sdkDelete = Delete.builder().conditionExpression("condition").build()
        val dslDelete = dslDelete { condition("condition") }
        assertEquals(sdkDelete.conditionExpression(), dslDelete.conditionExpression())
    }

    @Test
    fun key() {
        val sdkDelete = Delete.builder().key(mapOf("a" to AttributeValue.builder().s("b").build())).build()
        val dslDelete = dslDelete { key { "a" from "b" } }
        assertEquals(sdkDelete.key(), dslDelete.key())
    }


    @Test
    fun attributeValues() {
        val sdkDelete =
            Delete.builder().expressionAttributeValues(mapOf("a" to AttributeValue.builder().s("z").build()))
                .build()
        val dslDelete = dslDelete {
            attributeValues {
                "a" from "z"
            }
        }
        assertEquals(sdkDelete.expressionAttributeValues(), dslDelete.expressionAttributeValues())
    }

    @Test
    fun attributeNames() {
        val sdkDelete = Delete.builder().expressionAttributeNames(mapOf("a" to "z")).build()
        val dslDelete = dslDelete {
            attributeNames("a" to "z")
        }
        assertEquals(sdkDelete.expressionAttributeNames(), dslDelete.expressionAttributeNames())
    }

    @Test
    fun returnValuesOnConditionCheckFailure() {
        val sdkDelete =
            Put.builder().returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD).build()
        val dslDelete = dslDelete {
            returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
        }
        assertEquals(sdkDelete.returnValuesOnConditionCheckFailure(), dslDelete.returnValuesOnConditionCheckFailure())
    }


}