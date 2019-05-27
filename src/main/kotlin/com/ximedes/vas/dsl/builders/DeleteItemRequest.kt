package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest

@DynamoDbDSL
class DeleteItemRequestBuilder(tableName: String) {
    private val _builder = DeleteItemRequest.builder().tableName(tableName)

    fun build(): DeleteItemRequest = _builder.build()


    fun key(init: ItemBuilder.() -> Unit) {
        _builder.key(ItemBuilder().apply(init).build())
    }

    fun condition(expression: String) {
        _builder.conditionExpression(expression)
    }

    fun attributes(init: ItemBuilder.() -> Unit) {
        _builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

}