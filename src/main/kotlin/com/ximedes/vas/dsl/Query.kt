package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.QueryRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse


fun DynamoDbClient.query(tableName: String, block: QueryRequestBuilder.() -> Unit): QueryResponse {
    val queryRequest = QueryRequestBuilder(tableName).apply(block).build()
    return query(queryRequest)
}

suspend fun DynamoDbAsyncClient.query(tableName: String, block: QueryRequestBuilder.() -> Unit): QueryResponse {
    val queryRequest = QueryRequestBuilder(tableName).apply(block).build()
    return query(queryRequest).await()
}

private fun doQuery(
    tableName: String,
    block: QueryRequestBuilder.() -> Unit,
    execute: (QueryRequest) -> QueryResponse
): QueryResponse {
    val queryRequest = QueryRequestBuilder(tableName).apply(block).build()
    return execute(queryRequest)
}


