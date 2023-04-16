package com.gr4vy.android_sdk.models

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import kotlinx.parcelize.Parcelize

@Parcelize
class Config(private val metaData: Bundle, private val id: String) : IConfig {

    override val instance: String get() = if (environment == "sandbox") "sandbox.$id" else id
    override val debug: Boolean get() = metaData.getBoolean("gr4vy-debug", false)
    private val environment: String
        get() = metaData.getString(
            "gr4vy-environment",
            "production"
        ) //production or sandbox
    override val isProduction: Boolean
        get() = environment == "production"


    init {}

    companion object {
        fun fromContextWithID(context: Context, id: String): Config {
            val metaData = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData

            return Config(metaData, id)
        }
    }
}