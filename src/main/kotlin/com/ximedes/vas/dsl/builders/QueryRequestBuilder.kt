package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity
import software.amazon.awssdk.services.dynamodb.model.Select

@DynamoDbDSL
class QueryRequestBuilder(tableName: String) {
    private val _builder = QueryRequest.builder().tableName(tableName)

    fun build(): QueryRequest = _builder.build()

    fun indexName(indexName: String) {
        _builder.indexName(indexName)
    }

    fun consistentRead(consistentRead: Boolean) {
        _builder.consistentRead(consistentRead)
    }

    fun returnConsumedCapacity(returnConsumedCapacity: ReturnConsumedCapacity) {
        _builder.returnConsumedCapacity(returnConsumedCapacity)
    }

    fun keyCondition(expression: String) {
        _builder.keyConditionExpression(expression)
    }

    fun filter(expression: String) {
        _builder.filterExpression(expression)
    }

    fun exclusiveStartKey(init: ItemBuilder.() -> Unit) {
        _builder.exclusiveStartKey(ItemBuilder().apply(init).build())
    }

    fun attributeValues(init: ItemBuilder.() -> Unit) {
        _builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

    fun attributeNames(vararg names: Pair<String, String>) {
        _builder.expressionAttributeNames(mapOf(*names))
    }

    fun projection(expression: String) {
        _builder.projectionExpression(expression)
    }

    fun limit(limit: Int) {
        _builder.limit(limit)
    }

    fun scanIndexForward(b: Boolean) {
        _builder.scanIndexForward(b)
    }

    fun select(select: Select) {
        _builder.select(select)
    }
}