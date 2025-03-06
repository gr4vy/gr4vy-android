package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartItem(
    val name: String,
    val quantity: Int,
    val unitAmount: Int,
    val discountAmount:Int? = 0,
    val taxAmount:Int? = 0,
    val externalIdentifier:String? = null,
    val sku: String? = null,
    val productUrl: String? = null,
    val imageUrl: String? = null,
    val categories: List<String>? = null,
    val productType: String? = null,
    val sellerCountry: String? = null) : Parcelable
