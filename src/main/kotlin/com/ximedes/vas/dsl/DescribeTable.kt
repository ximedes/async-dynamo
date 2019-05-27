package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.DescribeTableRequestBuilder
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse

fun DynamoDbClient.describeTable(
    tableName: String,
    init: DescribeTableRequestBuilder.() -> Unit = {}
): DescribeTableResponse {
    val request = DescribeTableRequestBuilder(tableName).apply(init).build()
    return describeTable(request)
}

suspend fun DynamoDbAsyncClient.describeTable(
    tableName: String,
    init: DescribeTableRequestBuilder.() -> Unit = {}
): DescribeTableResponse {
    val request = DescribeTableRequestBuilder(tableName).apply(init).build()
    return describeTable(request).await()
}



