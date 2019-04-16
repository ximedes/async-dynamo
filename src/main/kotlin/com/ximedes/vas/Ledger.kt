package com.ximedes.vas

import com.ximedes.vas.domain.*
import com.ximedes.vas.dsl.assertTable
import com.ximedes.vas.dsl.put
import com.ximedes.vas.dsl.query
import com.ximedes.vas.dsl.writeTransaction
import mu.KotlinLogging
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.ProjectionType
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import java.time.Instant

class Ledger {

    private val logger = KotlinLogging.logger {}

    private val client = DynamoDbAsyncClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
        .region(Region.EU_WEST_1)
        .build()

    suspend fun init() {
        client.assertTable("ledger") {
            attribute("pk", ScalarAttributeType.S)
            attribute("sk", ScalarAttributeType.S)
            attribute("owner_id", ScalarAttributeType.S)

            partitionKey("pk")
            sortKey("sk")

            throughput(readCapacityUnits = 10, writeCapacityUnits = 10)

            globalSecondaryIndex("accounts") {
                partitionKey("owner_id")
                sortKey("pk")
                projection(ProjectionType.ALL)
                throughput(readCapacityUnits = 10, writeCapacityUnits = 10)
            }
        }
    }

    suspend fun createUser(user: User) {
        client.put("ledger") {
            item {
                "pk" from user.id
                "sk" from user.id
                "email" from user.email
            }
            condition("attribute_not_exists(pk)")
        }
    }

    suspend fun createAccount(account: Account) {
        client.put("ledger") {
            item {
                "pk" from account.id
                "sk" from account.id
                "owner_id" from account.owner
                "overdraft" from account.overdraft
                "headroom" from account.overdraft - account.balance
                "description" from account.description
            }
            condition("attribute_not_exists(pk)")
        }
    }

    suspend fun queryUserAccounts(userID: UserID): List<Account> {
        val response = client.query("ledger") {
            useIndex("accounts")
            keyCondition("owner_id = :userId")
            attributes {
                ":userId" from userID
            }
        }
        return response.items().map {
            with(AccountItem(it)) {
                Account(UserID(ownerID), AccountID(accountID), headroom - overdraft, overdraft, description)
            }
        }

    }

    suspend fun transfer(transfer: Transfer) {
        val timeStamp = Instant.now().toEpochMilli()

        client.writeTransaction {
            update("ledger") {
                key {
                    "pk" from transfer.from
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
                    "pk" from transfer.to
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
                    "sk" from "trc:$timeStamp-${transfer.id}"
                    "id" from transfer.id
                    "type" from "DEBIT"
                    "amount" from transfer.amount
                    "description" from transfer.description
                }

            }
            put("ledger") {
                item {
                    "pk" from transfer.to
                    "sk" from "trc:$timeStamp-${transfer.id}"
                    "id" from transfer.id
                    "type" from "CREDIT"
                    "amount" from transfer.amount
                    "description" from transfer.description
                }
            }
        }

    }

}




