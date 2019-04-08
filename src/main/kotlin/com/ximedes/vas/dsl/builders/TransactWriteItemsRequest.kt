package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.TransactWriteItem
import software.amazon.awssdk.services.dynamodb.model.TransactWriteItemsRequest

@DynamoDbDSL
class TransactWriteItemsRequestBuilder() {
    private val builder: TransactWriteItemsRequest.Builder =
        TransactWriteItemsRequest.builder()
    private val writeItems = mutableListOf<TransactWriteItem>()

    fun build(): TransactWriteItemsRequest = builder.transactItems(writeItems).build()

    fun update(tableName: String, block: UpdateBuilder.() -> Unit) {
        val update = UpdateBuilder(tableName).apply(block).build()
        writeItems.add(TransactWriteItem.builder().update(update).build())
    }

    fun put(tableName: String, block: PutBuilder.() -> Unit) {
        val put = PutBuilder(tableName).apply(block).build()
        writeItems.add(TransactWriteItem.builder().put(put).build())
    }

}