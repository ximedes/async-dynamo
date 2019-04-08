package com.ximedes.vas.dsl.builders

import com.ximedes.vas.domain.AccountID
import com.ximedes.vas.domain.UserID
import com.ximedes.vas.dsl.DynamoDbDSL
import com.ximedes.vas.dsl.Item
import com.ximedes.vas.dsl.MutableItem
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.util.*

@DynamoDbDSL
class ItemBuilder {

    private val item: MutableItem = mutableMapOf()

    infix fun String.from(value: String) {
        item[this] = AttributeValue.builder().s(value).build()
    }

    infix fun String.from(value: Int) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: Long) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: UUID) {
        item[this] = AttributeValue.builder().n(value.toString()).build()
    }

    infix fun String.from(value: Item) {
        item[this] = AttributeValue.builder().m(value).build()
    }

    infix fun String.from(userID: UserID) = from(userID.id)

    infix fun String.from(accountID: AccountID) = from(accountID.id)

    fun build(): Map<String, AttributeValue> = item

}