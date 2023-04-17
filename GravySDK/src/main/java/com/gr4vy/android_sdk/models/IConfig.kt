package com.gr4vy.android_sdk.models

import android.os.Parcelable

interface IConfig : Parcelable {
    val instance: String
    val debug: Boolean
    val isProduction: Boolean
}