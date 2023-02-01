package com.gr4vy.android_sdk.models

import kotlinx.serialization.Serializable

@Serializable
data class GoogleSession(
    val transactionInfo: TransactionInfo,
    val gateway: String,
    val allowedAuthMethods: List<String>,
    val allowedCardNetworks: List<String>,
    val merchantInfo: MerchantInfo,
)

@Serializable
data class TransactionInfo(
    val currencyCode: String,
    val countryCode: String,
)

@Serializable
data class MerchantInfo(
    val merchantName: String,
)