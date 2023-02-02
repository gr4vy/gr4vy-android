package com.gr4vy.android_sdk.models

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import kotlinx.parcelize.Parcelize

@Parcelize
class Config(private val metaData: Bundle) : IConfig {

    override val id: String get() = metaData.getString("gr4vy-id", "")
    override val instance: String get() = if (environment == "sandbox") "sandbox.$id" else id
    override val debug: Boolean get() = metaData.getBoolean("gr4vy-debug", false)
    private val environment: String
        get() = metaData.getString(
            "gr4vy-environment",
            "production"
        ) //production or sandbox
    override val isProduction: Boolean
        get() = environment == "production"


    init {
        require(id.isNotBlank()) { "Gr4vy ID must be set - add <meta-data android:name=\"gr4vy-id\" android:value=\"ID\" /> to the manifest" }
    }

    companion object {
        fun fromContext(context: Context): Config {
            val metaData = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData

            return Config(metaData)
        }
    }
}