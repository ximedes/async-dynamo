package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.DeleteTableRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse

fun DynamoDbClient.deleteTable(
    tableName: String,
    init: DeleteTableRequestBuilder.() -> Unit = {}
): DeleteTableResponse {
    val request = DeleteTableRequestBuilder(tableName).apply(init).build()
    return deleteTable(request)
}

suspend fun DynamoDbAsyncClient.deleteTable(
    tableName: String,
    init: DeleteTableRequestBuilder.() -> Unit = {}
): DeleteTableResponse {
    val request = DeleteTableRequestBuilder(tableName).apply(init).build()
    return deleteTable(request).await()
}



