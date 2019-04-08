package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.TransactWriteItemsRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.TransactWriteItemsResponse

fun DynamoDbClient.writeTransaction(block: TransactWriteItemsRequestBuilder.() -> Unit): TransactWriteItemsResponse {
    val request = TransactWriteItemsRequestBuilder().apply(block).build()
    return transactWriteItems(request)
}

suspend fun DynamoDbAsyncClient.writeTransaction(block: TransactWriteItemsRequestBuilder.() -> Unit): TransactWriteItemsResponse {
    val request = TransactWriteItemsRequestBuilder().apply(block).build()
    return transactWriteItems(request).await()
}

