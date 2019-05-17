package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest

internal class DescribeTableRequestBuilderTest {

    @Test
    fun nameMatches() {
        val sdkRequest = DescribeTableRequest.builder().tableName("mytable").build()
        val dslRequest = DescribeTableRequestBuilder("mytable").build()
        assertEquals(sdkRequest, dslRequest)
    }
}