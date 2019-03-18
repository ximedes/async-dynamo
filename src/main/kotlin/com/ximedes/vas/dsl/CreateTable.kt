package com.ximedes.vas.dsl

import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.*

suspend fun DynamoDbAsyncClient.createTable(
    tableName: String,
    init: CreateTableRequestBuilder.() -> Unit
): CreateTableResponse {
    val builder = CreateTableRequestBuilder(tableName)
    builder.init()
    val request = builder.build()
    return createTable(request).await()
}

@DynamoDbDSL
class CreateTableRequestBuilder(tableName: String) {
    private var _builder = CreateTableRequest.builder().tableName(tableName)
    private val attributeDefinitions = mutableListOf<AttributeDefinition>()
    private val keySchemaElements = mutableListOf<KeySchemaElement>()

    fun attribute(name: String, type: ScalarAttributeType, keyType: KeyType? = null) {
        attributeDefinitions.add(
            AttributeDefinition.builder()
                .attributeName(name)
                .attributeType(type)
                .build()
        )
        keyType?.let {
            keySchemaElements.add(KeySchemaElement.builder().attributeName(name).keyType(keyType).build())
        }
    }

    fun throughput(readCapacityUnits: Long, writeCapacityUnits: Long) {
        _builder.provisionedThroughput(
            ProvisionedThroughput.builder()
                .writeCapacityUnits(readCapacityUnits)
                .readCapacityUnits(writeCapacityUnits)
                .build()
        )
    }

    fun build(): CreateTableRequest {
        _builder.attributeDefinitions(*attributeDefinitions.toTypedArray())
        _builder.keySchema(*keySchemaElements.toTypedArray())
        return _builder.build()
    }

}