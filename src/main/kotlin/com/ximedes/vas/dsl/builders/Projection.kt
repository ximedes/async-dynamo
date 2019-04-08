package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType

@DynamoDbDSL
class ProjectionBuilder(type: ProjectionType) {
    private val _builder = Projection.builder().projectionType(type)
    private val nonKeyAttributes = mutableListOf<String>()

    fun build(): Projection {
        if (nonKeyAttributes.size > 0) {
            _builder.nonKeyAttributes(nonKeyAttributes)
        }
        return _builder.build()
    }

    fun nonKeyAttribute(name: String) = nonKeyAttributes.add(name)

}