package com.gr4vy.android_sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val status: String,
    @SerialName("id") val transactionId: String,
    @SerialName("paymentMethodID") val paymentMethodId: String?
)

@Serializable
sealed class Message

@Serializable
object UnknownMessage : Message()

@Serializable
data class FrameReadyMessage(
    val type: String,
    val channel: String,
) : Message()

@Serializable
data class ApprovalMessage(
    val type: String,
    val channel: String,
    val data: String,
) : Message()

@Serializable
data class TransactionMessage(
    val type: String,
    val channel: String,
    val data: Transaction,
) : Message()

@Serializable
data class GoogleStartSessionMessage(
    val type: String,
    val channel: String,
    val data: GoogleSession,
) : Message()

@Serializable
data class GooglePaySessionAuthorizedMessage(
    val type: String,
    val data: String,
)

@Serializable
data class UpdateMessage(
    val type: String,
    val data: Update,

    ) {
    companion object {

        fun fromParameters(parameters: Parameters): UpdateMessage {

            return UpdateMessage(
                type = "updateOptions",
                data = Update(
                    apiHost = "api.${parameters.config.instance}.gr4vy.app",
                    apiUrl = "https://api.${parameters.config.instance}.gr4vy.app",
                    buyerId = parameters.buyerId,
                    token = parameters.token,
                    amount = parameters.amount,
                    country = parameters.country,
                    currency = parameters.currency,
                    externalIdentifier = parameters.externalIdentifier,
                    store = parameters.store,
                    display = parameters.display,
                    intent = parameters.intent,
                    cartItems = parameters.cartItems?.map {UpdateCartItem.fromCartItem(it)},
                    paymentSource = parameters.paymentSource.toSerialisedString(),
                    metadata = parameters.metadata,
                    supportedGooglePayVersion = 1,
                )
            )
        }
    }
}

@Serializable
data class Update(
    val apiHost: String,
    val apiUrl: String,
    val token: String,
    val amount: Int,
    val country: String,
    val currency: String,
    val buyerId: String?,
    val externalIdentifier: String? = null,
    val store: String? = null,
    val display: String? = null,
    val intent: String? = null,
    val cartItems: List<UpdateCartItem>?,
    val paymentSource: String?,
    val metadata: Gr4vyMetaData?,
    var supportedGooglePayVersion: Int,
)

@Serializable
data class UpdateCartItem(
    val name: String,
    val quantity: Int,
    val unitAmount: Int) {

    companion object {

        fun fromCartItem(item: CartItem): UpdateCartItem {
            return UpdateCartItem(
                name = item.name,
                quantity = item.quantity,
                unitAmount = item.unitAmount)
        }
    }
}