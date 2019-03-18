package com.ximedes.vas.dynamo.dsl

import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.Update
import java.util.*

fun putItemRequest(tableName: String, init: PutItemRequestBuilder.() -> Unit): PutItemRequest {
    val builder = PutItemRequestBuilder(PutItemRequest.builder().tableName(tableName))
    builder.init()
    return builder.build()
}

fun update(tableName: String, init: UpdateBuilder.() -> Unit): Update {
    val builder = UpdateBuilder(tableName)
    builder.init()
    return builder.build()
}


class PutItemRequestBuilder(private val builder: PutItemRequest.Builder) {

    fun condition(condition: () -> String) {
        builder.conditionExpression(condition())
    }

    fun item(block: ItemBuilder.() -> Unit): Item {
        val itemBuilder = ItemBuilder()
        itemBuilder.block()
        val item = itemBuilder.build()
        builder.item(item)
        return item
    }

    fun build(): PutItemRequest = builder.build()
}

class ItemBuilder {

    private val item: MutableItem = mutableMapOf()

    infix fun String.from(value: String) {
        item[this] = AttributeValue.builder().s(value).build()
    }

    infix fun String.from(value: Int) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: Long) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: UUID) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: Item) {
        item[this] = AttributeValue.builder().m(value).build()
    }

    fun build(): Map<String, AttributeValue> = item

}