package com.gr4vy.android_sdk.web

import android.os.Parcel
import com.gr4vy.android_sdk.models.IConfig
import com.gr4vy.android_sdk.models.Parameters
import com.gr4vy.android_sdk.models.PaymentSource

private val testConfig = object: IConfig {
    override val id = "config-id"
    override val instance = "config-instance"
    override val debug = false
    override val isProduction: Boolean = false

    override fun describeContents(): Int {
        TODO("Not needed")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }
}

val testParameters = Parameters(
    config =  testConfig,
    buyerId = "buyerId",
    token = "token",
    amount = 10873,
    currency = "GBP",
    country = "GB",
    externalIdentifier = null,
    store = null,
    display = null,
    intent = null,
    cartItems = null,
    paymentSource = PaymentSource.NOT_SET,
    metadata = null
)