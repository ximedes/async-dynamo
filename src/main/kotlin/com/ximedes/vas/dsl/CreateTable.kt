package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.CreateTableRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse

fun DynamoDbClient.createTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
): CreateTableResponse {
    val request = CreateTableRequestBuilder(tableName).apply(init).build()
    return createTable(request)
}

suspend fun DynamoDbAsyncClient.createTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
): CreateTableResponse {
    val request = CreateTableRequestBuilder(tableName).apply(init).build()
    return createTable(request).await()
}



