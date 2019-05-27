package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.ReturnValuesOnConditionCheckFailure
import software.amazon.awssdk.services.dynamodb.model.Update


@DynamoDbDSL
class UpdateBuilder(tableName: String) {
    private val _builder = Update.builder().tableName(tableName)

    fun build(): Update = _builder.build()

    fun returnValuesOnConditionCheckFailure(returnValuesOnConditionCheckFailure: ReturnValuesOnConditionCheckFailure) {
        _builder.returnValuesOnConditionCheckFailure(returnValuesOnConditionCheckFailure)
    }

    fun key(init: ItemBuilder.() -> Unit) {
        _builder.key(ItemBuilder().apply(init).build())
    }

    fun update(expression: String) {
        _builder.updateExpression(expression)
    }

    fun condition(expression: String) {
        _builder.conditionExpression(expression)
    }

    fun attributeValues(init: ItemBuilder.() -> Unit) {
        _builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

    fun attributeNames(vararg names: Pair<String, String>) {
        _builder.expressionAttributeNames(mapOf(*names))
    }

}