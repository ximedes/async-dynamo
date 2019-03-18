package com.ximedes.vas.dsl

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@DslMarker
annotation class DynamoDbDSL

typealias Item = Map<String, AttributeValue>
typealias MutableItem = MutableMap<String, AttributeValue>