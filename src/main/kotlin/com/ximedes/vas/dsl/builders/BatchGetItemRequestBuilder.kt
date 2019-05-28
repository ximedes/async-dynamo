package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity

@DynamoDbDSL
class BatchGetItemRequestBuilder {
    private val _builder = BatchGetItemRequest.builder()
    private val requestItems = mutableMapOf<String, KeysAndAttributes>()

    fun build(): BatchGetItemRequest = _builder.requestItems(requestItems).build()

    fun returnConsumedCapacity(returnConsumedCapacity: ReturnConsumedCapacity) {
        _builder.returnConsumedCapacity(returnConsumedCapacity)
    }

    fun items(tableName: String, init: KeysAndAttributesBuilder.() -> Unit) {
        requestItems[tableName] = KeysAndAttributesBuilder().apply(init).build()
    }


}