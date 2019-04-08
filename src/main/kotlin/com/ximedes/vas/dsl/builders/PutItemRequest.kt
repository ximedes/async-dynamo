package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

@DynamoDbDSL
class PutItemRequestBuilder(tableName: String) {
    private val builder = PutItemRequest.builder().tableName(tableName)

    fun build(): PutItemRequest = builder.build()

    fun item(init: ItemBuilder.() -> Unit) {
        builder.item(ItemBuilder().apply(init).build())
    }

    fun condition(expression: String) {
        builder.conditionExpression(expression)
    }

    fun attributes(init: ItemBuilder.() -> Unit) {
        builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }


}