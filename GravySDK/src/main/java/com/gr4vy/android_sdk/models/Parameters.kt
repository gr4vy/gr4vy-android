package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.json.JsonElement

@Parcelize
data class Parameters(
    val config: IConfig,
    val gr4vyId: String,
    val token: String,
    val amount: Int,
    val currency: String,
    val country: String,
    val buyerId: String?,
    val externalIdentifier: String?,
    val store: String?,
    val display: String?,
    val intent: String?,
    val cartItems: List<CartItem>?,
    val paymentSource: PaymentSource,
    val metadata: Gr4vyMetaData?,
    val theme: Gr4vyTheme? = null,
    val buyerExternalIdentifier: String? = null,
    val locale: String? = null,
    val statementDescriptor: Gr4vyStatementDescriptor? = null,
    val requireSecurityCode: Boolean? = null,
    val shippingDetailsId: String? = null,
    val merchantAccountId: String? = null,
    val connectionOptions: String? = null,
    val buyer: Gr4vyBuyer? = null,
    val installmentCount: Int? = null,
) : Parcelable {
    init {
        require(token.isNotBlank()) { "Gr4vy token was blank" }
        require(amount > 0) { "Gr4vy amount was not greater than 0" }
        require(currency.isNotBlank()) { "Currency was not provided" }
        require(country.isNotBlank()) { "Currency was not provided" }
    }
}
