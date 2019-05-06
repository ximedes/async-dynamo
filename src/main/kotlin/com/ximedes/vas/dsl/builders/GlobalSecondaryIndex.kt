package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.*

@DynamoDbDSL
class GlobalSecondaryIndexBuilder(name: String) {
    private val _builder = GlobalSecondaryIndex.builder().indexName(name)
    private val keySchemaElements = mutableListOf<KeySchemaElement>()
    private var provisionedThroughput: ProvisionedThroughput? = null


    fun partitionKey(name: String) {
        keySchemaElements.add(KeySchemaElement.builder().attributeName(name).keyType(KeyType.HASH).build())
    }

    fun sortKey(name: String) {
        keySchemaElements.add(KeySchemaElement.builder().attributeName(name).keyType(KeyType.RANGE).build())
    }

    fun provisionedThroughput(readCapacityUnits: Long, writeCapacityUnits: Long) {
        provisionedThroughput = ProvisionedThroughput.builder()
            .readCapacityUnits(readCapacityUnits)
            .writeCapacityUnits(writeCapacityUnits)
            .build()
    }

    fun projection(type: ProjectionType, block: (ProjectionBuilder.() -> Unit) = {}) {
        val projection = ProjectionBuilder(type).apply(block).build()
        _builder.projection(projection)
    }


    fun build(parentThroughput: ProvisionedThroughput?): GlobalSecondaryIndex {
        _builder.keySchema(*keySchemaElements.toTypedArray())

        (provisionedThroughput ?: parentThroughput)?.let {
            _builder.provisionedThroughput(it)
        }

        return _builder.build()
    }

}