package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import com.ximedes.vas.dsl.Item
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes

@DynamoDbDSL
class KeysAndAttributesBuilder {
    private val _builder = KeysAndAttributes.builder()
    private val keys = mutableListOf<Item>()

    fun build(): KeysAndAttributes = _builder.keys(keys).build()

    fun consistentRead(consistentRead: Boolean) {
        _builder.consistentRead(consistentRead)
    }

    fun attributeNames(vararg names: Pair<String, String>) {
        _builder.expressionAttributeNames(mapOf(*names))
    }

    fun projection(expression: String) {
        _builder.projectionExpression(expression)
    }

    fun keys(vararg inits: ItemBuilder.() -> Unit) {
        keys.addAll(inits.map { ItemBuilder().apply(it).build() })
    }


}