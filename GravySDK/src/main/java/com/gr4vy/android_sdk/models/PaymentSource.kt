package com.gr4vy.android_sdk.models

enum class PaymentSource {
    INSTALLMENT,
    RECURRING,
    NOT_SET;

    fun toSerialisedString() = when(this) {
        INSTALLMENT -> "installment"
        RECURRING -> "recurring"
        NOT_SET -> null
    }
}