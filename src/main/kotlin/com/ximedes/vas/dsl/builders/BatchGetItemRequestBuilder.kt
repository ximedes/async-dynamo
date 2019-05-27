package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest

@DynamoDbDSL
class BatchGetItemRequestBuilder {
    private val _builder = BatchGetItemRequest.builder()

    fun build(): BatchGetItemRequest = _builder.build()
}