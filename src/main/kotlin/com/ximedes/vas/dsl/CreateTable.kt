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
    private val globalSecondaryIndices = mutableListOf<GlobalSecondaryIndex>()

    fun attribute(name: String, type: ScalarAttributeType) {
        attributeDefinitions.add(
            AttributeDefinition.builder()
                .attributeName(name)
                .attributeType(type)
                .build()
        )
    }

    fun partitionKey(name: String) {
        keySchemaElements.add(KeySchemaElement.builder().attributeName(name).keyType(KeyType.HASH).build())
    }

    fun sortKey(name: String) {
        keySchemaElements.add(KeySchemaElement.builder().attributeName(name).keyType(KeyType.RANGE).build())
    }

    fun throughput(readCapacityUnits: Long, writeCapacityUnits: Long) {
        _builder.provisionedThroughput(
            ProvisionedThroughput.builder()
                .writeCapacityUnits(readCapacityUnits)
                .readCapacityUnits(writeCapacityUnits)
                .build()
        )
    }

    fun globalSecondaryIndex(name: String, init: GlobalSecondaryIndexBuilder.() -> Unit) {
        val index = GlobalSecondaryIndexBuilder(name).apply(init).build()
        globalSecondaryIndices.add(index)
    }


    fun build(): CreateTableRequest {
        _builder.attributeDefinitions(*attributeDefinitions.toTypedArray())
        _builder.keySchema(*keySchemaElements.toTypedArray())
        _builder.globalSecondaryIndexes(*globalSecondaryIndices.toTypedArray())
        return _builder.build()
    }

}

enum class ProjectionType {
    ALL, KEYS_ONLY
}