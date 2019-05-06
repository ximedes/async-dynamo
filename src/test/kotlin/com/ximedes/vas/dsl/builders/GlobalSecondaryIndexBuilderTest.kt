package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput

internal class GlobalSecondaryIndexBuilderTest {

    private fun dslGSI(
        name: String = "gsi",
        throughput: ProvisionedThroughput? = null,
        init: GlobalSecondaryIndexBuilder.() -> Unit
    ): GlobalSecondaryIndex {
        return GlobalSecondaryIndexBuilder(name).apply(init).build(throughput)
    }

    @Test
    fun `index name`() {
        val sdkGSI = GlobalSecondaryIndex.builder()
            .indexName("foobar")
            .build()
        val dslGSI = dslGSI("foobar") {}
        assertEquals(sdkGSI.indexName(), dslGSI.indexName())
    }

    @Test
    fun throughput() {
        val sdkGSI = GlobalSecondaryIndex.builder()
            .provisionedThroughput(
                ProvisionedThroughput.builder().readCapacityUnits(1L).writeCapacityUnits(2L).build()
            ).build()
        val dslGSI = dslGSI {
            provisionedThroughput(1L, 2L)
        }
        assertEquals(sdkGSI.provisionedThroughput(), dslGSI.provisionedThroughput())
    }

    @Test
    fun projection() {
        val sdkGSI = GlobalSecondaryIndex.builder()
            .projection(
                Projection.builder().projectionType(ProjectionType.INCLUDE).nonKeyAttributes("nk1", "nk2").build()
            ).build()
        val dslGSI = dslGSI {
            projection(ProjectionType.INCLUDE) {
                nonKeyAttributes("nk1", "nk2")
            }
        }
        assertEquals(sdkGSI.projection(), dslGSI.projection())
    }
}