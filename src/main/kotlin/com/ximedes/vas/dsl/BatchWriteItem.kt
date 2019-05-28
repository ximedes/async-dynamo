package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.BatchWriteItemRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse

fun DynamoDbClient.batchWriteItem(init: BatchWriteItemRequestBuilder.() -> Unit): BatchWriteItemResponse {
    val request = BatchWriteItemRequestBuilder().apply(init).build()
    return batchWriteItem(request)
}

suspend fun DynamoDbAsyncClient.batchWriteItem(init: BatchWriteItemRequestBuilder.() -> Unit): BatchWriteItemResponse {
    val request = BatchWriteItemRequestBuilder().apply(init).build()
    return batchWriteItem(request).await()
}