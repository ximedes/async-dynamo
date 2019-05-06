package com.ximedes.vas.dsl.builders

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType

internal class ProjectionBuilderTest {

    private fun dslProjection(
        type: ProjectionType = ProjectionType.ALL,
        init: ProjectionBuilder.() -> Unit
    ): Projection {
        return ProjectionBuilder(type).apply(init).build()
    }

    @Test
    fun type() {
        val sdkProjection = Projection.builder().projectionType(ProjectionType.KEYS_ONLY).build()
        val dslProjection = dslProjection(ProjectionType.KEYS_ONLY) {}

        assertEquals(sdkProjection.projectionType(), dslProjection.projectionType())
    }

    @Test
    fun attributes() {
        val sdkProjection = Projection.builder().nonKeyAttributes("a", "b", "c").build()
        val dslProjection = dslProjection {
            nonKeyAttributes("a", "b", "c")
        }
        assertEquals(sdkProjection.nonKeyAttributes(), dslProjection.nonKeyAttributes())
    }


}