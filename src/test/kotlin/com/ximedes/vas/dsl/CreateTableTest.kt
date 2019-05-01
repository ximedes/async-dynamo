package com.ximedes.vas.dsl

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateTableTest {

    private val client = mockk<DynamoDbClient>()
    private val slot = slot<CreateTableRequest>()

    @BeforeEach
    fun reset() {
        clearAllMocks()
    }

    @Test
    fun `minimal CreateTable test`() {
        every { client.createTable(capture(slot)) } answers { CreateTableResponse.builder().build() }

        val sdkRequest = CreateTableRequest.builder()
            .tableName("foo")
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("myString1").attributeType(S).build()
            )
            .keySchema(
                KeySchemaElement.builder().attributeName("myString1").keyType(KeyType.HASH).build()
            )
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build()

        client.createTable("foo") {
            attributes {
                S("myString1")
            }
            partitionKey("myString1")
        }

        assertEquals(sdkRequest, slot.captured)

    }

    @Test
    fun `full CreateTable test`() {
        every { client.createTable(capture(slot)) } answers { CreateTableResponse.builder().build() }

        val sdkRequest = CreateTableRequest.builder()
            .tableName("foo")
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("myString1").attributeType(S).build(),
                AttributeDefinition.builder().attributeName("myString2").attributeType(S).build(),
                AttributeDefinition.builder().attributeName("myBool").attributeType(B).build(),
                AttributeDefinition.builder().attributeName("myNumber").attributeType(N).build()
            )
            .keySchema(
                KeySchemaElement.builder().attributeName("myString1").keyType(KeyType.HASH).build(),
                KeySchemaElement.builder().attributeName("myNumber").keyType(KeyType.RANGE).build()
            )
            .globalSecondaryIndexes(
                GlobalSecondaryIndex.builder()
                    .indexName("globalIndex1")
                    .keySchema(KeySchemaElement.builder().attributeName("myString2").keyType(KeyType.HASH).build())
                    .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                    .build()
            )
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build()



        client.createTable("foo") {
            attributes {
                S("myString1", "myString2")
                B("myBool")
                N("myNumber")
            }
            partitionKey("myString1")
            sortKey("myNumber")
            globalSecondaryIndex("globalIndex1") {
                partitionKey("myString2")
                projection(ProjectionType.ALL)
            }
        }

        assertEquals(sdkRequest, slot.captured)

    }

}