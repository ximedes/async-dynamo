package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.GetItemRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse

fun DynamoDbClient.getItem(tableName: String, init: GetItemRequestBuilder.() -> Unit): GetItemResponse {
    val request = GetItemRequestBuilder(tableName).apply(init).build()
    return getItem(request)
}

suspend fun DynamoDbAsyncClient.getItem(tableName: String, init: GetItemRequestBuilder.() -> Unit): GetItemResponse {
    val request = GetItemRequestBuilder(tableName).apply(init).build()
    return getItem(request).await()
}