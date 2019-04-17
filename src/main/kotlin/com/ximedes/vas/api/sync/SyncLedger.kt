package com.ximedes.vas.api.sync

import com.ximedes.vas.api.Constants
import com.ximedes.vas.domain.*
import com.ximedes.vas.dsl.*
import mu.KotlinLogging
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.ProjectionType
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import java.time.Instant

class SyncLedger {
    private val logger = KotlinLogging.logger {}

    private val client = DynamoDbClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
        .region(Region.EU_WEST_1)
        .build()

    fun init() {
        client.assertTable("ledger") {
            attribute("pk", ScalarAttributeType.S)
            attribute("sk", ScalarAttributeType.S)
            attribute("owner_id", ScalarAttributeType.S)

            partitionKey("pk")
            sortKey("sk")

            throughput(
                readCapacityUnits = Constants.readCapacityUnits,
                writeCapacityUnits = Constants.writeCapacityUnits
            )

            globalSecondaryIndex("accounts") {
                partitionKey("owner_id")
                sortKey("pk")
                projection(ProjectionType.ALL)
                throughput(
                    readCapacityUnits = Constants.readCapacityUnits,
                    writeCapacityUnits = Constants.writeCapacityUnits
                )
            }
        }
    }

    fun reset() {
        client.deleteTable("ledger")
        init()
    }

    fun createUser(user: User) {
        client.put("ledger") {
            item {
                "pk" from user.id
                "sk" from user.id
                "email" from user.email
            }
            condition("attribute_not_exists(pk)")
        }
    }

    fun queryUserAccounts(userID: UserID): List<Account> {
        val response = client.query("ledger") {
            useIndex("accounts")
            keyCondition("owner_id = :userId")
            attributes {
                ":userId" from userID
            }
        }
        return response.items().map {
            val ownerID = UserID(it.take("owner_id"))
            val accountID = AccountID(it.take("pk"))
            val overdraft = it.take<Long>("overdraft")
            val balance = it.take<Long>("headroom") - overdraft
            val description = it.take<String>("description")
            Account(ownerID, accountID, balance, overdraft, description)
        }

    }

    fun createAccount(account: Account) {
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

    fun transfer(transfer: Transfer) {
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