package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.Put
import software.amazon.awssdk.services.dynamodb.model.ReturnValuesOnConditionCheckFailure

@DynamoDbDSL
class PutBuilder(tableName: String) {
    private val _builder = Put.builder().tableName(tableName)

    fun build(): Put = _builder.build()

    fun returnValuesOnConditionCheckFailure(returnValuesOnConditionCheckFailure: ReturnValuesOnConditionCheckFailure) {
        _builder.returnValuesOnConditionCheckFailure(returnValuesOnConditionCheckFailure)
    }

    fun item(init: ItemBuilder.() -> Unit) {
        _builder.item(ItemBuilder().apply(init).build())
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