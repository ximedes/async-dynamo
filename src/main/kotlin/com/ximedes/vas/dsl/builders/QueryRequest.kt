package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.QueryRequest

@DynamoDbDSL
class QueryRequestBuilder(tableName: String) {
    private val builder = QueryRequest.builder().tableName(tableName)

    fun build(): QueryRequest = builder.build()

    fun useIndex(indexName: String) {
        builder.indexName(indexName)
    }

    fun keyCondition(expression: String) {
        builder.keyConditionExpression(expression)
    }

    fun attributeValues(init: ItemBuilder.() -> Unit) {
        builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }
}