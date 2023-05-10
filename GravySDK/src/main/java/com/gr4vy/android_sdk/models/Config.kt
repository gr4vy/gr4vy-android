package com.gr4vy.android_sdk.models

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import kotlinx.parcelize.Parcelize

@Parcelize
class Config(private val metaData: Bundle, private val id: String, private val environment: String = "production", private val debugMode: Boolean) : IConfig {

    override val instance: String get() = if (environment == "sandbox") "sandbox.$id" else id
    override val debug: Boolean get() = debugMode
    override val isProduction: Boolean
        get() = environment == "production"


    init {}

    companion object {
        fun fromContextWithParams(context: Context, id: String, environment: String, debugMode: Boolean): Config {
            val metaData = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData

            return Config(metaData, id, environment, debugMode)
        }
    }
}