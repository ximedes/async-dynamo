package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.TransactGetItemsRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.TransactGetItemsResponse

fun DynamoDbClient.readTransaction(block: TransactGetItemsRequestBuilder.() -> Unit): TransactGetItemsResponse {
    val request = TransactGetItemsRequestBuilder().apply(block).build()
    return transactGetItems(request)
}

suspend fun DynamoDbAsyncClient.readTransaction(block: TransactGetItemsRequestBuilder.() -> Unit): TransactGetItemsResponse {
    val request = TransactGetItemsRequestBuilder().apply(block).build()
    return transactGetItems(request).await()
}
