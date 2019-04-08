package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.*

@DynamoDbDSL
class GlobalSecondaryIndexBuilder(name: String) {
    private val _builder = GlobalSecondaryIndex.builder().indexName(name)
    private val keySchemaElements = mutableListOf<KeySchemaElement>()


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


    fun build(): GlobalSecondaryIndex {
        val projection = Projection.builder().projectionType(ProjectionType.KEYS_ONLY).build()
        _builder.keySchema(*keySchemaElements.toTypedArray()).projection(projection)
        return _builder.build()
    }
}