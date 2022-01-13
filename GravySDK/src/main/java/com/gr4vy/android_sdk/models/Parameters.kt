package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Parameters(
    val config: IConfig,
    val token: String,
    val amount: Int,
    val currency: String,
    val country: String,
    val externalIdentifier: String?,
    val store: String?,
    val display: String?,
    val gr4vyIntent: String?
) : Parcelable {
    init {
        require(token.isNotBlank()) { "Gr4vy token was blank" }
        require(amount > 0) { "Gr4vy amount was not greater than 0" }
        require(currency.isNotBlank()) { "Currency was not provided" }
        require(country.isNotBlank()) { "Currency was not provided" }
    }
}
