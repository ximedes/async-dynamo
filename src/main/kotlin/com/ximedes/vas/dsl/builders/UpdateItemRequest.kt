package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest

@DynamoDbDSL
class UpdateItemRequestBuilder(tableName: String) {
    private val builder = UpdateItemRequest.builder().tableName(tableName)

    fun build(): UpdateItemRequest = builder.build()

    fun update(expression: String) {
        builder.updateExpression(expression)
    }

    fun condition(expression: String) {
        builder.conditionExpression(expression)
    }

    fun attributes(init: ItemBuilder.() -> Unit) {
        builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

    fun key(init: ItemBuilder.() -> Unit) {
        builder.key(ItemBuilder().apply(init).build())
    }

}