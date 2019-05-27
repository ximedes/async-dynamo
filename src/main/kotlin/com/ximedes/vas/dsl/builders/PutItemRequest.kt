package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity
import software.amazon.awssdk.services.dynamodb.model.ReturnItemCollectionMetrics

@DynamoDbDSL
class PutItemRequestBuilder(tableName: String) {
    private val _builder = PutItemRequest.builder().tableName(tableName)

    fun build(): PutItemRequest = _builder.build()

    fun returnConsumedCapacity(returnConsumedCapacity: ReturnConsumedCapacity) {
        _builder.returnConsumedCapacity(returnConsumedCapacity)
    }

    fun returnItemCollectionMetrics(returnItemCollectionMetrics: ReturnItemCollectionMetrics) {
        _builder.returnItemCollectionMetrics(returnItemCollectionMetrics)
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