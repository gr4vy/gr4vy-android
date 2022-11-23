package com.gr4vy.android_sdk.web

import android.os.Parcel
import com.gr4vy.android_sdk.models.IConfig
import com.gr4vy.android_sdk.models.Parameters
import com.gr4vy.android_sdk.models.PaymentSource

private val testConfig = object: IConfig {
    override val id = "config-id"
    override val instance = "config-instance"
    override val debug = false

    override fun describeContents(): Int {
        TODO("Not needed")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not needed")
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
    gr4vyIntent = null,
    cartItems = null,
    paymentSource = PaymentSource.NOT_SET,
    metadata = null
)