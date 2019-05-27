package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest

@DynamoDbDSL
class DeleteTableRequestBuilder(tableName: String) {
    private var _builder = DeleteTableRequest.builder().tableName(tableName)

    fun build(): DeleteTableRequest {
        return _builder.build()
    }

}