package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity
import software.amazon.awssdk.services.dynamodb.model.ReturnItemCollectionMetrics
import software.amazon.awssdk.services.dynamodb.model.TransactWriteItem
import software.amazon.awssdk.services.dynamodb.model.TransactWriteItemsRequest

@DynamoDbDSL
class TransactWriteItemsRequestBuilder() {
    private val _builder: TransactWriteItemsRequest.Builder =
        TransactWriteItemsRequest.builder()
    private val writeItems = mutableListOf<TransactWriteItem>()

    fun build(): TransactWriteItemsRequest = _builder.transactItems(writeItems).build()

    fun update(tableName: String, block: UpdateBuilder.() -> Unit) {
        val update = UpdateBuilder(tableName).apply(block).build()
        writeItems.add(TransactWriteItem.builder().update(update).build())
    }

    fun put(tableName: String, block: PutBuilder.() -> Unit) {
        val put = PutBuilder(tableName).apply(block).build()
        writeItems.add(TransactWriteItem.builder().put(put).build())
    }

    fun delete(tableName: String, block: DeleteBuilder.() -> Unit) {
        val delete = DeleteBuilder(tableName).apply(block).build()
        writeItems.add(TransactWriteItem.builder().delete(delete).build())
    }

    fun clientRequestToken(clientRequestToken: String) {
        _builder.clientRequestToken(clientRequestToken)
    }

    fun returnConsumedCapacity(returnConsumedCapacity: ReturnConsumedCapacity) {
        _builder.returnConsumedCapacity(returnConsumedCapacity)
    }

    fun returnItemCollectionMetrics(returnItemCollectionMetrics: ReturnItemCollectionMetrics) {
        _builder.returnItemCollectionMetrics(returnItemCollectionMetrics)
    }

}