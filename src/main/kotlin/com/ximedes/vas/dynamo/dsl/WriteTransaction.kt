package com.ximedes.vas.dynamo.dsl

import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.*


suspend fun DynamoDbAsyncClient.writeTransaction(block: TransactWriteItemsRequestBuilder.() -> Unit): TransactWriteItemsResponse {
    val request = TransactWriteItemsRequestBuilder().apply(block).build()
    return transactWriteItems(request).await()
}

@DynamoDbDSL
class TransactWriteItemsRequestBuilder() {
    private val builder: TransactWriteItemsRequest.Builder = TransactWriteItemsRequest.builder()
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

@DynamoDbDSL
class PutBuilder(tableName: String) {
    private val builder = Put.builder().tableName(tableName)

    fun build(): Put = builder.build()

    fun item(init: ItemBuilder.() -> Unit) {
        builder.item(ItemBuilder().apply(init).build())
    }

    fun condition(expression: String) {
        builder.conditionExpression(expression)
    }

    fun attributes(init: ItemBuilder.() -> Unit) {
        builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

}

@DynamoDbDSL
class UpdateBuilder(tableName: String) {
    private val builder = Update.builder().tableName(tableName)

    fun build(): Update = builder.build()

    fun key(init: ItemBuilder.() -> Unit) {
        builder.key(ItemBuilder().apply(init).build())
    }

    fun update(expression: String) {
        builder.updateExpression(expression)
    }

    fun condition(expression: String) {
        builder.conditionExpression(expression)
    }

    fun attributes(init: ItemBuilder.() -> Unit) {
        builder.expressionAttributeValues(ItemBuilder().apply(init).build())
    }

}