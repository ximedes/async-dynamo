package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest

@DynamoDbDSL
class DescribeTableRequestBuilder(tableName: String) {
    private var _builder = DescribeTableRequest.builder().tableName(tableName)

    fun build(): DescribeTableRequest {
        return _builder.build()
    }

}