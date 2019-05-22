package com.ximedes.vas.dsl.builders

import com.ximedes.vas.dsl.DynamoDbDSL
import software.amazon.awssdk.services.dynamodb.model.Get

@DynamoDbDSL
class GetBuilder(tableName: String) {
    private val _builder = Get.builder().tableName(tableName)

    fun build(): Get = _builder.build()

    fun key(init: ItemBuilder.() -> Unit) {
        _builder.key(ItemBuilder().apply(init).build())
    }

    fun projection(expression: String) {
        _builder.projectionExpression(expression)
    }

    /*
            TODO can we improve this syntax?
            Maybe something like {
                project("#abc") where {
                    "#a" is "bla"
                }
            }
     */
    fun attributeNames(vararg names: Pair<String, String>) {
        _builder.expressionAttributeNames(mapOf(*names))
    }

}