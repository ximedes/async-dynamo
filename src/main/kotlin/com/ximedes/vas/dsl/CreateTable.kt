package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.CreateTableRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse

suspend fun DynamoDbAsyncClient.createTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
): CreateTableResponse {
    val builder = CreateTableRequestBuilder(tableName)
    builder.init()
    val request = builder.build()
    return createTable(request).await()
}

