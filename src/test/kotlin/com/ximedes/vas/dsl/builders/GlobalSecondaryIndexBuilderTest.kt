package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex
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
}