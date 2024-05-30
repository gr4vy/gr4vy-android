package com.gr4vy.android_sdk.models

import android.os.Parcelable
import com.gr4vy.android_sdk.gr4vyConvertJSONStringToMap
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Transaction(
    val status: String,
    @SerialName("id") val transactionId: String?,
    @SerialName("paymentMethodID") val paymentMethodId: String?,
    @SerialName("paymentMethod") val paymentMethod: PaymentMethod?
)

@Serializable
data class PaymentMethod(
    val approvalUrl: String?,
)

@Serializable
data class Navigation(
    val title: String,
    val canGoBack: Boolean,
)

@Serializable
data class OpenLink(
    val url: String
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
data class NavigationMessage(
    val type: String,
    val channel: String,
    val data: Navigation,
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
data class OpenLinkMessage(
    val data: OpenLink,
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

        fun fromParameters(parameters: Parameters, isGooglePayEnabled: Boolean): UpdateMessage {

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
                    supportedGooglePayVersion =  if(isGooglePayEnabled) 1 else null,
                    theme =  parameters.theme,
                    buyerExternalIdentifier =  parameters.buyerExternalIdentifier,
                    locale =  parameters.locale,
                    statementDescriptor =  parameters.statementDescriptor,
                    requireSecurityCode =  parameters.requireSecurityCode,
                    shippingDetailsId =  parameters.shippingDetailsId,
                    merchantAccountId = parameters.merchantAccountId,
                    connectionOptions = gr4vyConvertJSONStringToMap(parameters.connectionOptions)
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
    var supportedGooglePayVersion: Int? = null,
    val theme: Gr4vyTheme? = null,
    val buyerExternalIdentifier: String? = null,
    val locale: String? = null,
    val statementDescriptor: Gr4vyStatementDescriptor? = null,
    val requireSecurityCode: Boolean? = null,
    val shippingDetailsId: String? = null,
    val merchantAccountId: String? = null,
    val connectionOptions: Map<String, JsonElement>? = null
)

@Serializable
data class UpdateCartItem(
    val name: String,
    val quantity: Int,
    val unitAmount: Int,
    val discountAmount:Int?,
    val taxAmount:Int?,
    val externalIdentifier:String?,
    val sku: String?,
    val productUrl: String?,
    val imageUrl: String?,
    val categories: List<String>?,
    val productType: String?) {

    companion object {

        fun fromCartItem(item: CartItem): UpdateCartItem {
            return UpdateCartItem(
                name = item.name,
                quantity = item.quantity,
                unitAmount = item.unitAmount,
                discountAmount = item.discountAmount,
                taxAmount = item.taxAmount,
                externalIdentifier = item.externalIdentifier,
                sku = item.sku,
                productUrl = item.productUrl,
                imageUrl = item.imageUrl,
                categories = item.categories,
                productType = item.productType)
        }
    }
}

@Serializable
@Parcelize
data class Gr4vyTheme(
    val fonts: Gr4vyFonts? = null,
    val colors: Gr4vyColours? = null,
    val borderWidths: Gr4vyBorderWidths? = null,
    val radii: Gr4vyRadii? = null,
    val shadows: Gr4vyShadows? = null
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyFonts(
    var fonts: String?
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyColours(
    val text: String? = null,
    val subtvalext: String? = null,
    val labelText: String? = null,
    val primary: String? = null,
    val pageBackground: String? = null,
    val containerBackgroundUnchecked: String? = null,
    val containerBackground: String? = null,
    val containerBorder: String? = null,
    val inputBorder: String? = null,
    val inputBackground: String? = null,
    val inputText: String? = null,
    val inputRadioBorder: String? = null,
    val inputRadioBorderChecked: String? = null,
    val danger: String? = null,
    val dangerBackground: String? = null,
    val dangerText: String? = null,
    val info: String? = null,
    val infoBackground: String? = null,
    val infoText: String? = null,
    val focus: String? = null,
    val headerText: String? = null,
    val headerBackground: String? = null
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyBorderWidths(
    val container: String? = null,
    val input: String? = null
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyRadii(
    val container: String? = null,
    val input: String? = null
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyShadows(
    val focusRing: String? = null
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyStatementDescriptor(
    val name: String? = null,
    val description: String? = null,
    val phoneNumber: String? = null,
    val city: String?= null,
    val url: String? = null
) : Parcelable