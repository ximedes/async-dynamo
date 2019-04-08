package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.Put

@DynamoDbDSL
class PutBuilder(tableName: String) {
    private val builder = Put.builder().tableName(tableName)

    fun build(): Put = builder.build()

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