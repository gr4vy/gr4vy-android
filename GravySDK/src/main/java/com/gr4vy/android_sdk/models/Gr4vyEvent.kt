package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface Gr4vyResultEventInterface

sealed class Gr4vyEvent : Parcelable, Gr4vyResultEventInterface {
    @Parcelize
    class TransactionFailed(
        val transactionId: String?,
        val status: String,
        val paymentMethodId: String?
    ) : Gr4vyEvent()
}