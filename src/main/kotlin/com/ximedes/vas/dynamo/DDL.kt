package com.ximedes.vas.dynamo

import com.ximedes.vas.dynamo.dsl.createTable
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType


suspend fun initTables(client: DynamoDbAsyncClient) {

    val tables = client.listTables().await().tableNames()

    if ("ledger" !in tables) {
        client.createTable("ledger") {
            attribute("pk", ScalarAttributeType.S, KeyType.HASH)
            attribute("sk", ScalarAttributeType.S, KeyType.RANGE)
            throughput(readCapacityUnits = 10, writeCapacityUnits = 10)
        }
    }
}