package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.domain.Account
import com.ximedes.vas.domain.UserID
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.QueryRequest


suspend fun Ledger.queryUserAccounts(userID: UserID): List<Account> {
    val request = QueryRequest.builder()
            .tableName("ledger")
            .keyConditionExpression("pk = :userId")
            .expressionAttributeValues(mapOf(":userId" to AttributeValue.builder().s(userID.toPK()).build()))
            .build()
    val result = client.query(request).await()
    return result.items().map { it.accountFromDynamo() }

}
