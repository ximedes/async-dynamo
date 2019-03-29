package com.ximedes.vas.dsl

import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.Get
import software.amazon.awssdk.services.dynamodb.model.TransactGetItem
import software.amazon.awssdk.services.dynamodb.model.TransactGetItemsRequest
import software.amazon.awssdk.services.dynamodb.model.TransactGetItemsResponse

suspend fun DynamoDbAsyncClient.readTransaction(block: TransactGetItemsRequestBuilder.() -> Unit): TransactGetItemsResponse {
    val request = TransactGetItemsRequestBuilder().apply(block).build()
    return transactGetItems(request).await()
}

@DynamoDbDSL
class TransactGetItemsRequestBuilder {
    private val _builder = TransactGetItemsRequest.builder()
    private val getItems = mutableListOf<TransactGetItem>()

    fun build(): TransactGetItemsRequest {
        return _builder.transactItems(getItems).build()
    }

    fun get(tableName: String, init: GetBuilder.() -> Unit) {
        val get = GetBuilder(tableName).apply(init).build()
        getItems.add(TransactGetItem.builder().get(get).build())
    }

}

@DynamoDbDSL
class GetBuilder(tableName: String) {
    private val _builder = Get.builder().tableName(tableName)

    fun build(): Get = _builder.build()

    fun key(init: ItemBuilder.() -> Unit) {
        _builder.key(ItemBuilder().apply(init).build())
    }

}