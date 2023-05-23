package com.gr4vy.android_sdk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.webkit.WebViewFeature
import com.gr4vy.android_sdk.models.*
import kotlinx.parcelize.RawValue


class Gr4vySDK(
    private val registry: ActivityResultRegistry,
    private val handler: Gr4vyResultHandler,
    private val context: Context
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
                handler.onGr4vyResult(Gr4vyResult.Cancelled())
            }
        }
        val gr4vyBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                val event = intent.getParcelableExtra<Parameters>(
                    Gr4vyActivity.EVENT_KEY
                ) as Gr4vyEvent

                handler.onGr4vyEvent(event)
            }
        }

        val filter = IntentFilter(Gr4vySDK.BROADCAST_KEY)
        context.registerReceiver(gr4vyBroadcastReceiver, filter)
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
     * @param externalIdentifier An optional external identifier that can be supplied. This will automatically be associated to any resource created by Gr4vy and can subsequently be used to find a resource by that ID.
     * @param store Explicitly store the payment method or ask the buyer, this is used when a buyerId is provided.
     * @param display Filters the payment methods to show stored methods only, new payment methods only or methods that support tokenization.
     * @param intent Defines the intent of this API call. This determines the desired initial state of the transaction.
     * @param cartItems An optional array of cart item objects
     * @param paymentSource Can be used to signal that Embed is used to capture the first transaction for a subscription or an installment.
     * @param metadata An optional dictionary of key/values for transaction metadata. All values should be a string.
     * @param theme Theme customisation options.
     * @param buyerExternalIdentifier An optional external ID for a Gr4vy buyer.
     * @param locale An optional locale, this consists of a ISO 639 Language Code followed by an optional ISO 3166 Country Code
     * @param statementDescriptor An optional object with information about the purchase to construct the statement information the buyer will see in their bank statement.
     * @param requireSecurityCode An optional boolean which forces security code to be prompted for stored card payments.
     * @param shippingDetailsId An optional unique identifier of a set of shipping details stored for the buyer.
     * @param merchantAccountId An optional merchant account ID.
     * @param debugMode Enables debug mode.
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
        shippingDetailsId: String? = null,
        merchantAccountId: String? = null,
        debugMode: Boolean = false,
    ) {

        if(!isSupported()) {
            Log.e("Gr4vy", "Gr4vy is not supported on this device")
            return
        }

        val config: Config = Config.fromContextWithParams(context, gr4vyId, environment, debugMode)

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
            shippingDetailsId = shippingDetailsId,
            merchantAccountId = merchantAccountId
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

    companion object {
        const val BROADCAST_KEY = "GR4VY"
    }
}

interface Gr4vyResultHandler {
    fun onGr4vyResult(result: Gr4vyResult)
    fun onGr4vyEvent(event: Gr4vyEvent)
}