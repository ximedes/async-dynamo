package com.ximedes.vas.dsl

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

typealias Item = Map<String, AttributeValue>
typealias MutableItem = MutableMap<String, AttributeValue>

@DslMarker
annotation class DynamoDbDSL

inline fun <reified T> Map<String, AttributeValue>.take(key: String) = when (T::class) {
    String::class -> get(key)?.s() as T
    Int::class -> get(key)?.n() as T
    Long::class -> get(key)?.n()?.toLong() as T
    else -> TODO()
}