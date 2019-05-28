package com.ximedes.vas.dsl

import com.ximedes.vas.dsl.builders.BatchGetItemRequestBuilder
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.paginators.BatchGetItemIterable
import software.amazon.awssdk.services.dynamodb.paginators.BatchGetItemPublisher

fun DynamoDbClient.batchGetItemPaginator(init: BatchGetItemRequestBuilder.() -> Unit): BatchGetItemIterable {
    val request = BatchGetItemRequestBuilder().apply(init).build()
    return batchGetItemPaginator(request)
}

// TODO make a flow adapter for publisher
fun DynamoDbAsyncClient.batchGetItemPaginator(init: BatchGetItemRequestBuilder.() -> Unit): BatchGetItemPublisher {
    val request = BatchGetItemRequestBuilder().apply(init).build()
    return batchGetItemPaginator(request)
}