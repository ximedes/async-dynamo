package com.ximedes.vas.api.sync

import com.ximedes.vas.domain.*
import com.ximedes.vas.dsl.*
import mu.KotlinLogging
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.ProjectionType
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import java.time.Instant

class SyncLedger {
    private val logger = KotlinLogging.logger {}

    private val client = DynamoDbClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
        .region(Region.EU_WEST_1)
        .build()

    fun init(capacity: Pair<Long, Long>?) {
        client.assertTable("ledger") {
            attributes {
                string("pk", "sk", "owner_id")
            }
            partitionKey("pk")
            sortKey("sk")

            capacity?.let {
                provisionedThroughput(
                    readCapacityUnits = capacity.first,
                    writeCapacityUnits = capacity.second
                )
            }

            globalSecondaryIndex("accounts") {
                partitionKey("owner_id")
                sortKey("pk")
                projection(ProjectionType.ALL)
            }
        }
    }

    fun reset(capacity: Pair<Long, Long>?) {
        try {
            client.deleteTable("ledger")
        } catch (e: ResourceNotFoundException) {
            // No problem
        }
        init(capacity)
    }

    fun createUser(user: User) {
        client.putItem("ledger") {
            item {
                "pk" from user.id.id
                "sk" from user.id.id
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
                ":userId" from userID.id
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
        client.putItem("ledger") {
            item {
                "pk" from account.accountID.id
                "sk" from account.accountID.id
                "owner_id" from account.owner.id
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
                    "pk" from transfer.from.id
                    "sk" from transfer.from.id

                }
                update("SET headroom = headroom - :a")

                condition("headroom >= :a")
                attributes {
                    ":a" from transfer.amount
                }

            }
            update("ledger") {
                key {
                    "pk" from transfer.to.id
                    "sk" from transfer.to.id

                }
                update("SET headroom = headroom + :a")
                attributes {
                    ":a" from transfer.amount
                }

            }
            put("ledger") {
                item {
                    "pk" from transfer.from.id
                    "sk" from "trc:$timeStamp-${transfer.id}"
                    "accountID" from transfer.id.id
                    "type" from "DEBIT"
                    "amount" from transfer.amount
                    "description" from transfer.description
                }

            }
            put("ledger") {
                item {
                    "pk" from transfer.to.id
                    "sk" from "trc:$timeStamp-${transfer.id}"
                    "accountID" from transfer.id.id
                    "type" from "CREDIT"
                    "amount" from transfer.amount
                    "description" from transfer.description
                }
            }
        }

    }

}