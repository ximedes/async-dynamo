package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.Update

@DynamoDbDSL
class UpdateBuilder(tableName: String) {
    private val builder = Update.builder().tableName(tableName)

    fun build(): Update = builder.build()

    fun key(init: ItemBuilder.() -> Unit) {
        builder.key(ItemBuilder().apply(init).build())
    }

    fun update(expression: String) {
        builder.updateExpression(expression)
    }

    fun condition(expression: String) {
        builder.conditionExpression(expression)
    }

    fun attributes(init: ItemBuilder.() -> Unit) {
        builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

}