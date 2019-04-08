package com.ximedes.vas

import com.ximedes.vas.domain.*
import com.ximedes.vas.dsl.createTable
import com.ximedes.vas.dsl.query
import com.ximedes.vas.dsl.take
import com.ximedes.vas.dsl.writeTransaction
import kotlinx.coroutines.future.await
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import java.time.Instant
import java.util.*

class Ledger {
    private val client = DynamoDbAsyncClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
        .region(Region.EU_WEST_1)
        .build()

    suspend fun init() {
        val tableNames = client.listTables().await().tableNames()

        if ("ledger" !in tableNames) {
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

    suspend fun createAccount(account: Account) {
        client.writeTransaction {
            put("ledger") {
                item {
                    "pk" from account.owner
                    "sk" from account.id
                    "overdraft" from account.overdraft
                    "headroom" from account.overdraft
                    "description" from account.description
                }
                condition("attribute_not_exists(sk)")
            }
        }
    }

    suspend fun queryUserAccounts(userID: UserID): List<Account> {
        val result = client.query("ledger") {
            keyCondition("pk = :userId")
            attributes {
                ":userId" from userID
            }
        }

        return result.items().map { it.asAccount() }

    }

    suspend fun transfer(transfer: Transfer) {
        val timeStamp = Instant.now().toEpochMilli()
        val id = UUID.randomUUID().toString()

        val fromUser = findUserIDByAccountID(transfer.from)
        val toUser = findUserIDByAccountID(transfer.to)

        client.writeTransaction {
            update("ledger") {
                key {
                    "pk" from fromUser
                    "sk" from transfer.from

                }
                update("SET headroom = headroom - :a")

                condition("headroom >= :a")
                attributes {
                    ":a" from transfer.amount
                }

            }
            update("ledger") {
                key {
                    "pk" from toUser
                    "sk" from transfer.to

                }
                update("SET headroom = headroom + :a")
                attributes {
                    ":a" from transfer.amount
                }

            }
            put("ledger") {
                item {
                    "pk" from transfer.from
                    "sk" from "TX-$timeStamp-$id"
                    "id" from id
                    "type" from "DEBIT"
                    "amount" from transfer.amount
                    "description" from transfer.description
                }

            }
            put("ledger") {
                item {
                    "pk" from transfer.to
                    "sk" from "TX-$timeStamp-$id"
                    "id" from id
                    "type" from "CREDIT"
                    "amount" from transfer.amount
                    "description" from transfer.description
                }
            }
        }

    }

    private suspend fun findUserIDByAccountID(accountID: AccountID): UserID {
        val response = client.query("ledger") {
            useIndex("accounts")
            keyCondition("sk = :accountID and begins_with(pk, :user)")
            attributes {
                ":accountID" from accountID
                ":user" from "USER-"
            }
        }
        return response.items().map { UserID(it.take("pk")) }.single()
    }

}

