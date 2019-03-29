package com.ximedes.vas.dsl

import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse

suspend fun DynamoDbAsyncClient.query(tableName: String, block: QueryRequestBuilder.() -> Unit): QueryResponse {
    val queryRequest = QueryRequestBuilder(tableName).apply(block).build()
    return query(queryRequest).await()
}

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

    fun attributes(init: ItemBuilder.() -> Unit) {
        builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }
}