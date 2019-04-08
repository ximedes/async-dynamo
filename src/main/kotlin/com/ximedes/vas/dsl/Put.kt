package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.PutItemRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse

fun DynamoDbClient.put(tableName: String, block: PutItemRequestBuilder.() -> Unit): PutItemResponse {
    val request = PutItemRequestBuilder(tableName).apply(block).build()
    return putItem(request)
}

suspend fun DynamoDbAsyncClient.put(tableName: String, block: PutItemRequestBuilder.() -> Unit): PutItemResponse {
    val request = PutItemRequestBuilder(tableName).apply(block).build()
    return putItem(request).await()
}

