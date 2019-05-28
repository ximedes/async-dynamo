package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.BatchGetItemRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse

fun DynamoDbClient.batchGetItem(init: BatchGetItemRequestBuilder.() -> Unit): BatchGetItemResponse {
    val request = BatchGetItemRequestBuilder().apply(init).build()
    return batchGetItem(request)
}

suspend fun DynamoDbAsyncClient.batchGetItem(init: BatchGetItemRequestBuilder.() -> Unit): BatchGetItemResponse {
    val request = BatchGetItemRequestBuilder().apply(init).build()
    return batchGetItem(request).await()
}