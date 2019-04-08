package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.TransactGetItem
import software.amazon.awssdk.services.dynamodb.model.TransactGetItemsRequest

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