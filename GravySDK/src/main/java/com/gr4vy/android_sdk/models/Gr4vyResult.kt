package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Gr4vyResult : Parcelable, Gr4vyResultEventInterface {
    @Parcelize
    class TransactionCreated(
        val transactionId: String?,
        val status: String,
        val paymentMethodId: String?,
        val approvalUrl: String?
    ) : Gr4vyResult()

    @Parcelize
    class GeneralError(val reason: String? = "") : Gr4vyResult()

    @Parcelize
    class Cancelled() : Gr4vyResult()
}

