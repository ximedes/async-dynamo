package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest

internal class DeleteTableRequestBuilderTestm {

    @Test
    fun nameMatches() {
        val sdkRequest = DeleteTableRequest.builder().tableName("mytable").build()
        val dslRequest = DeleteTableRequestBuilder("mytable").build()
        assertEquals(sdkRequest, dslRequest)
    }
}