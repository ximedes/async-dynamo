package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

@DynamoDbDSL
class AttributeDefinitionsBuilder {

    private val definitions = mutableListOf<AttributeDefinition>()

    private fun attributes(type: ScalarAttributeType, vararg names: String) {
        definitions.addAll(names.map { AttributeDefinition.builder().attributeName(it).attributeType(type).build() })
    }

    fun string(vararg names: String) = attributes(ScalarAttributeType.S, *names)

    fun number(vararg names: String) = attributes(ScalarAttributeType.N, *names)

    fun binary(vararg names: String) = attributes(ScalarAttributeType.B, *names)

    fun build(): List<AttributeDefinition> = definitions

}