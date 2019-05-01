package com.ximedes.dynamo4k.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

@DynamoDbDSL
class AttributeDefinitionsBuilder {

    private val definitions = mutableListOf<AttributeDefinition>()

    operator fun ScalarAttributeType.invoke(vararg names: String) {
        definitions.addAll(names.map { AttributeDefinition.builder().attributeName(it).attributeType(this).build() })
    }

    fun build(): List<AttributeDefinition> = definitions

}