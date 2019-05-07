package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

internal class AttributeDefinitionsBuilderTest {

    @Test
    fun mix() {
        val sdkAttributes = listOf(
            AttributeDefinition.builder().attributeName("s1").attributeType(ScalarAttributeType.S).build(),
            AttributeDefinition.builder().attributeName("s2").attributeType(ScalarAttributeType.S).build(),
            AttributeDefinition.builder().attributeName("b").attributeType(ScalarAttributeType.B).build(),
            AttributeDefinition.builder().attributeName("n").attributeType(ScalarAttributeType.N).build()
        )
        val dslAttributes = AttributeDefinitionsBuilder().apply {
            string("s1", "s2")
            binary("b")
            number("n")
        }.build()

        assertEquals(sdkAttributes, dslAttributes)
    }

    @Test
    fun empty() {
        val dslAttributes = AttributeDefinitionsBuilder().build()
        assertEquals(emptyList<AttributeDefinition>(), dslAttributes)
    }
}