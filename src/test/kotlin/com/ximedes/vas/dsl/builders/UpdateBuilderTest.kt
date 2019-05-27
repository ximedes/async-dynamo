package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.Get
import software.amazon.awssdk.services.dynamodb.model.ReturnValuesOnConditionCheckFailure
import software.amazon.awssdk.services.dynamodb.model.Update

internal class UpdateBuilderTest {

    private fun dslUpdate(tableName: String = "foo", init: UpdateBuilder.() -> Unit) =
        UpdateBuilder(tableName).apply(init).build()

    @Test
    fun condition() {
        val sdkUpdate = Update.builder().conditionExpression("condition").build()
        val dslUpdate = dslUpdate {
            condition("condition")
        }
        assertEquals(sdkUpdate.conditionExpression(), dslUpdate.conditionExpression())
    }

    @Test
    fun key() {
        val sdkUpdate = Get.builder().key(
            mapOf(
                "a" to AttributeValue.builder().n("1").build(),
                "b" to AttributeValue.builder().s("a").build()
            )
        ).build()
        val dslUpdate = dslUpdate {
            key {
                "a" from 1
                "b" from "a"
            }
        }
        assertEquals(sdkUpdate.key(), dslUpdate.key())
    }

    @Test
    fun attributeValues() {
        val sdkUpdate =
            Update.builder().expressionAttributeValues(mapOf("a" to AttributeValue.builder().s("z").build())).build()
        val dslUpdate = dslUpdate {
            attributeValues {
                "a" from "z"
            }
        }
        assertEquals(sdkUpdate.expressionAttributeValues(), dslUpdate.expressionAttributeValues())
    }

    @Test
    fun attributeNames() {
        val sdkUpdate = Update.builder().expressionAttributeNames(mapOf("a" to "z")).build()
        val dslUpdate = dslUpdate {
            attributeNames("a" to "z")
        }
        assertEquals(sdkUpdate.expressionAttributeNames(), dslUpdate.expressionAttributeNames())
    }


    @Test
    fun returnValuesOnConditionCheckFailure() {
        val sdkUpdate =
            Update.builder().returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD).build()
        val dslUpdate = dslUpdate {
            returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
        }
        assertEquals(sdkUpdate.returnValuesOnConditionCheckFailure(), dslUpdate.returnValuesOnConditionCheckFailure())
    }


}