package com.gr4vy.android_sdk

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.webkit.WebViewFeature
import com.gr4vy.android_sdk.models.*
import kotlinx.parcelize.RawValue

class Gr4vySDK(
    private val registry: ActivityResultRegistry,
    private val handler: Gr4vyResultHandler,
    context: Context
) : DefaultLifecycleObserver {

    private lateinit var launchGr4vy: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        launchGr4vy = registry.register(
            "gr4vy",
            owner,
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            val result = activityResult.data?.getParcelableExtra<Gr4vyResult>(Gr4vyActivity.RESULT_KEY)

            if (result != null) {
                handler.onGr4vyResult(result)
            } else {
                handler.onGr4vyResult(Gr4vyResult.GeneralError())
            }
        }
    }

    /**
     * Launches Gr4vy!
     *
     * @param context Android context to be used to launch
     * @param gr4vyId A Gr4vy ID
     * @param environment The environment to be used. Defaults to production.
     * @param token A Gr4vy SDK Authentication token
     * @param amount the amount to charge for the transaction
     * @param currency the currency to use. For example "GBP" for British Pounds
     * @param country a short country code. For example "GB"
     * @param buyerId BuyerId is an optional Identifier that can be set to distinguish a customer
     */
    fun launch(
        context: Context,
        gr4vyId: String,
        environment: String = "production",
        token: String,
        amount: Int,
        currency: String,
        country: String,
        buyerId: String? = null,
        externalIdentifier: String? = null,
        store: String? = null,
        display: String? = null,
        intent: String? = null,
        cartItems: List<CartItem>? = null,
        paymentSource: PaymentSource = PaymentSource.NOT_SET,
        metadata: Gr4vyMetaData? = null,
        theme: @RawValue Gr4vyTheme? = null,
        buyerExternalIdentifier: String? = null,
        locale: String? = null,
        statementDescriptor: @RawValue Gr4vyStatementDescriptor? = null,
        requireSecurityCode: Boolean? = null,
        shippingDetailsId: String? = null
    ) {

        if(!isSupported()) {
            Log.e("Gr4vy", "Gr4vy is not supported on this device")
            return
        }

        val config: Config = Config.fromContextWithParams(context, gr4vyId, environment)

        val parameters = Parameters(
            config = config,
            gr4vyId = gr4vyId,
            buyerId = buyerId,
            token = token,
            amount = amount,
            currency = currency,
            country = country,
            externalIdentifier = externalIdentifier,
            store = store,
            display = display,
            intent = intent,
            cartItems = cartItems,
            paymentSource = paymentSource,
            metadata = metadata,
            theme = theme,
            buyerExternalIdentifier = buyerExternalIdentifier,
            locale = locale,
            statementDescriptor = statementDescriptor,
            requireSecurityCode = requireSecurityCode,
            shippingDetailsId = shippingDetailsId
        )

        val intent = Gr4vyActivity.createIntentWithParameters(context, parameters)

        launchGr4vy.launch(intent)
    }

    /**
     * This SDK uses Web Message Listener to communicate with Gr4vy. Most devices should support
     * this. This method allows a check to be made to confirm compatibility.
     *
     * @return Boolean if the SDK is supported on this device
     */
    fun isSupported(): Boolean {
        return WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)
    }
}

interface Gr4vyResultHandler {
    fun onGr4vyResult(result: Gr4vyResult)
}