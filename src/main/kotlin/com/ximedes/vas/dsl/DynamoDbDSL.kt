package com.ximedes.vas.dsl

import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias Item = Map<String, AttributeValue>
typealias MutableItem = MutableMap<String, AttributeValue>

@DslMarker
annotation class DynamoDbDSL

inline fun <reified T> Item.take(key: String) = when (T::class) {
    String::class -> get(key)?.s() as T
    Int::class -> get(key)?.n()?.toInt() as T
    Long::class -> get(key)?.n()?.toLong() as T
    else -> throw IllegalArgumentException("Unsupported type ${T::class}")
}

inline fun <reified T> Map<String, AttributeValue>.prop(key: String): ReadOnlyProperty<Any, T> {
    return object : ReadOnlyProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return take(key)
        }
    }
}