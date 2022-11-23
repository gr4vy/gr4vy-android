package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartItem(
    val name: String,
    val quantity: Int,
    val unitAmount: Int) : Parcelable
