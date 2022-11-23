package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val status: String,
    @SerialName("transactionID") val transactionId: String,
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
                    gr4vyIntent = parameters.gr4vyIntent,
                    cartItems = parameters.cartItems?.map {UpdateCartItem.fromCartItem(it)},
                    paymentSource = parameters.paymentSource.toSerialisedString(),
                    metadata = parameters.metadata
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
    val gr4vyIntent: String? = null,
    val cartItems: List<UpdateCartItem>?,
    val paymentSource: String?,
    val metadata: Gr4vyMetaData?,
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