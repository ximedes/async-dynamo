package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.dsl.createTable
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType


suspend fun Ledger.init() {

    val tables = client.listTables().await().tableNames()

    if ("ledger" !in tables) {
        client.createTable("ledger") {
            attribute("pk", ScalarAttributeType.S)
            attribute("sk", ScalarAttributeType.S)

            partitionKey("pk")
            sortKey("sk")

            throughput(readCapacityUnits = 10, writeCapacityUnits = 10)

            globalSecondaryIndex("accounts") {
                partitionKey("sk")
                sortKey("pk")
                throughput(readCapacityUnits = 10, writeCapacityUnits = 10)
            }
        }
    }
}