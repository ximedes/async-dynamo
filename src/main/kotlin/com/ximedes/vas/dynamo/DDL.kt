package com.ximedes.vas.dynamo

import com.ximedes.vas.dynamo.dsl.createTable
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

suspend fun initTables(client: DynamoDbAsyncClient) {

    val tables = client.listTables().await().tableNames()

    if ("account" !in tables) {
        client.createTable("account") {
            attribute("account_id", ScalarAttributeType.S, KeyType.HASH)
            throughput(5, 5)
        }
    }

    if ("transaction" !in tables) {
        client.createTable("transaction") {
            attribute("transaction_id", ScalarAttributeType.S, KeyType.HASH)
            attribute("timestamp", ScalarAttributeType.S, KeyType.RANGE)
            throughput(5, 5)
        }
    }

}