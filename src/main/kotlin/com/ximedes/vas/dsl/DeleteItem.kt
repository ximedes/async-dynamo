package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.DeleteItemRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse

fun DynamoDbClient.deleteItem(tableName: String, block: DeleteItemRequestBuilder.() -> Unit): DeleteItemResponse {
    val request = DeleteItemRequestBuilder(tableName).apply(block).build()
    return deleteItem(request)
}

suspend fun DynamoDbAsyncClient.deleteItem(
    tableName: String,
    block: DeleteItemRequestBuilder.() -> Unit
): DeleteItemResponse {
    val request = DeleteItemRequestBuilder(tableName).apply(block).build()
    return deleteItem(request).await()
}
