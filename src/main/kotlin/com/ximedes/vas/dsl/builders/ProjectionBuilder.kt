package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType

@DynamoDbDSL
class ProjectionBuilder(type: ProjectionType) {
    private val _builder = Projection.builder().projectionType(type)

    fun build(): Projection = _builder.build()

    fun nonKeyAttributes(vararg names: String) {
        _builder.nonKeyAttributes(*names)
    }

}