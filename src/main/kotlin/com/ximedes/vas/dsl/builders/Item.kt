package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import com.ximedes.vas.dsl.MutableItem
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@DynamoDbDSL
class ItemBuilder {

    private val item: MutableItem = mutableMapOf()

    infix fun String.from(block: ItemBuilder.() -> Unit) {
        item[this] = AttributeValue.builder().m(ItemBuilder().apply(block).build()).build()
    }

    infix fun String.from(value: String) {
        item[this] = AttributeValue.builder().s(value).build()
    }

    infix fun String.from(value: Int) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: Long) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: Boolean) {
        item[this] = AttributeValue.builder().bool(value).build()
    }

    fun build(): Map<String, AttributeValue> = item

}