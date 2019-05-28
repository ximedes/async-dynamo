package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.*

@DynamoDbDSL
class BatchWriteItemRequestBuilder {
    private val _builder = BatchWriteItemRequest.builder()
    private val writeRequests = mutableMapOf<String, MutableList<WriteRequest>>()

    fun build(): BatchWriteItemRequest = _builder.requestItems(writeRequests).build()

    fun delete(tableName: String, vararg inits: ItemBuilder.() -> Unit) {
        writeRequests.putIfAbsent(tableName, mutableListOf())
        writeRequests[tableName]?.addAll(inits.map {
            WriteRequest.builder().deleteRequest(DeleteRequest.builder().key(ItemBuilder().apply(it).build()).build())
                .build()
        })
    }

    fun put(tableName: String, vararg inits: ItemBuilder.() -> Unit) {
        writeRequests.putIfAbsent(tableName, mutableListOf())
        writeRequests[tableName]?.addAll(inits.map {
            WriteRequest.builder().putRequest(PutRequest.builder().item(ItemBuilder().apply(it).build()).build())
                .build()
        })
    }

    fun returnConsumedCapacity(returnConsumedCapacity: ReturnConsumedCapacity) {
        _builder.returnConsumedCapacity(returnConsumedCapacity)
    }

    fun returnItemCollectionMetrics(returnItemCollectionMetrics: ReturnItemCollectionMetrics) {
        _builder.returnItemCollectionMetrics(returnItemCollectionMetrics)
    }
}