package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.Delete
import software.amazon.awssdk.services.dynamodb.model.ReturnValuesOnConditionCheckFailure

@DynamoDbDSL
class DeleteBuilder(tableName: String) {
    private val _builder = Delete.builder().tableName(tableName)

    fun build(): Delete = _builder.build()

    fun condition(expression: String) {
        _builder.conditionExpression(expression)
    }

    fun key(init: ItemBuilder.() -> Unit) {
        _builder.key(ItemBuilder().apply(init).build())
    }

    fun attributeValues(init: ItemBuilder.() -> Unit) {
        _builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

    fun attributeNames(vararg names: Pair<String, String>) {
        _builder.expressionAttributeNames(mapOf(*names))
    }

    fun returnValuesOnConditionCheckFailure(returnValuesOnConditionCheckFailure: ReturnValuesOnConditionCheckFailure) {
        _builder.returnValuesOnConditionCheckFailure(returnValuesOnConditionCheckFailure)
    }
}