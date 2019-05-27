package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.*

@DynamoDbDSL
class CreateTableRequestBuilder(tableName: String) {
    private var _builder = CreateTableRequest.builder().tableName(tableName).billingMode(BillingMode.PAY_PER_REQUEST)
    private val keySchemaElements = mutableListOf<KeySchemaElement>()
    private val globalSecondaryIndexBuilders = mutableListOf<GlobalSecondaryIndexBuilder>()
    private var provisionedThroughput: ProvisionedThroughput? = null

    fun attributes(init: AttributeDefinitionsBuilder.() -> Unit) {
        _builder.attributeDefinitions(AttributeDefinitionsBuilder().apply(init).build())
    }

    fun partitionKey(name: String) {
        keySchemaElements.add(
            KeySchemaElement.builder().attributeName(name).keyType(
                KeyType.HASH
            ).build()
        )
    }

    fun sortKey(name: String) {
        keySchemaElements.add(
            KeySchemaElement.builder().attributeName(name).keyType(
                KeyType.RANGE
            ).build()
        )
    }

    fun provisionedThroughput(readCapacityUnits: Long, writeCapacityUnits: Long) {
        provisionedThroughput = ProvisionedThroughput.builder()
            .readCapacityUnits(readCapacityUnits)
            .writeCapacityUnits(writeCapacityUnits)
            .build()
    }

    fun globalSecondaryIndex(name: String, init: GlobalSecondaryIndexBuilder.() -> Unit) {
        val index = GlobalSecondaryIndexBuilder(name).apply(init)
        globalSecondaryIndexBuilders.add(index)
    }


    fun build(): CreateTableRequest {
        _builder.keySchema(*keySchemaElements.toTypedArray())

        provisionedThroughput?.let {
            _builder.billingMode(BillingMode.PROVISIONED).provisionedThroughput(it)
        }

        if (globalSecondaryIndexBuilders.isNotEmpty()) {
            _builder.globalSecondaryIndexes(*globalSecondaryIndexBuilders.map { it.build(provisionedThroughput) }.toTypedArray())
        }

        return _builder.build()
    }

}