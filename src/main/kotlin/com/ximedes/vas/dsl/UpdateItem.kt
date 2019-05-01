package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.UpdateItemRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse

fun DynamoDbClient.updateItem(tableName: String, block: UpdateItemRequestBuilder.() -> Unit): UpdateItemResponse {
    val request = UpdateItemRequestBuilder(tableName).apply(block).build()
    return updateItem(request)
}

suspend fun DynamoDbAsyncClient.updateItem(
    tableName: String,
    block: UpdateItemRequestBuilder.() -> Unit
): UpdateItemResponse {
    val request = UpdateItemRequestBuilder(tableName).apply(block).build()
    return updateItem(request).await()
}

