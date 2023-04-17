package com.gr4vy.android_sdk.web

import com.gr4vy.android_sdk.models.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class MessagePolymorphicSerializer : JsonContentPolymorphicSerializer<Message>(Message::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Message> {

        val jsonObject = element as? JsonObject ?: throw IllegalArgumentException()

        val messageType = jsonObject["type"]?.jsonPrimitive?.content.orEmpty()

        return when (messageType) {
            "frameReady" -> FrameReadyMessage.serializer()
            "approvalUrl" -> ApprovalMessage.serializer()
            "transactionCreated" -> TransactionMessage.serializer()
            "googlePaySessionStart" -> GoogleStartSessionMessage.serializer()
            "navigation" -> NavigationMessage.serializer()
            else -> UnknownMessage.serializer()
        }
    }
}