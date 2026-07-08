package com.gr4vy.android_sdk.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Explicitly store the payment method, do not store it, or ask the buyer.
 *
 * Embed requires `store` to be a JSON boolean (`true`/`false`) or the string `"ask"`/`"preselect"`,
 * so [Gr4vyStoreSerializer] encodes [TRUE] and [FALSE] as booleans rather than strings.
 */
@Serializable(with = Gr4vyStoreSerializer::class)
enum class Gr4vyStore {
    TRUE,
    FALSE,
    ASK,
    PRESELECT,
}

object Gr4vyStoreSerializer : KSerializer<Gr4vyStore> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Gr4vyStore", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Gr4vyStore) {
        val jsonEncoder = encoder as JsonEncoder
        val element = when (value) {
            Gr4vyStore.TRUE -> JsonPrimitive(true)
            Gr4vyStore.FALSE -> JsonPrimitive(false)
            Gr4vyStore.ASK -> JsonPrimitive("ask")
            Gr4vyStore.PRESELECT -> JsonPrimitive("preselect")
        }
        jsonEncoder.encodeJsonElement(element)
    }

    override fun deserialize(decoder: Decoder): Gr4vyStore {
        val jsonDecoder = decoder as JsonDecoder
        val primitive = jsonDecoder.decodeJsonElement().jsonPrimitive
        return when {
            !primitive.isString && primitive.booleanOrNull == true -> Gr4vyStore.TRUE
            !primitive.isString && primitive.booleanOrNull == false -> Gr4vyStore.FALSE
            primitive.isString && primitive.content == "ask" -> Gr4vyStore.ASK
            primitive.isString && primitive.content == "preselect" -> Gr4vyStore.PRESELECT
            else -> throw SerializationException("Unknown Gr4vyStore value: ${primitive.content}")
        }
    }
}
