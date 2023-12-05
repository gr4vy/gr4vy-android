package com.gr4vy.android_sdk.google_pay

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.webkit.WebView
import com.google.android.gms.common.api.Status
import com.google.android.gms.wallet.*
import com.gr4vy.android_sdk.models.GoogleSession
import com.gr4vy.gr4vy_android.R
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

class GooglePayClient(private val paymentsClient: PaymentsClient, private val gr4vyId: String, private val isProduction: Boolean) {

    companion object {

        const val REQUEST_CODE = 1123

        fun createClient(activity: Activity, gr4vyId: String, isProduction: Boolean): GooglePayClient {
            val walletOptions = Wallet.WalletOptions.Builder()
                .setEnvironment(if(isProduction) WalletConstants.ENVIRONMENT_PRODUCTION else WalletConstants.ENVIRONMENT_TEST)
                .build()

            val walletClient = Wallet.getPaymentsClient(activity, walletOptions)

            return GooglePayClient(walletClient, gr4vyId, isProduction)
        }

    }

    fun handleGooglePayResult(resultCode: Int, data: Intent?, onSuccess: (input: PaymentData) -> Unit,  onFailure: (status: Status) -> Unit, onCancelled: () -> Unit) {
        when (resultCode) {
            RESULT_OK ->
                data?.let { intent ->
                    PaymentData.getFromIntent(intent)?.let { onSuccess.invoke(it) }
                }

            RESULT_CANCELED -> {
                // The user cancelled the payment attempt
                onCancelled.invoke()
            }

            AutoResolveHelper.RESULT_ERROR -> {
                AutoResolveHelper.getStatusFromIntent(data)?.let { onFailure.invoke(it) }
            }
        }
    }

    fun pay(activity: Activity, googleSession: GoogleSession, price: Int) {

        val convertedAmount = if (price <= 0) "-0.01" else BigDecimal(price).movePointLeft(2).toString()
        val request = createPaymentRequest(googleSession, convertedAmount, googleSession.gatewayMerchantId)
        val paymentRequestData = PaymentDataRequest.fromJson(request.toString())
        AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(paymentRequestData), activity, REQUEST_CODE)
    }

    suspend fun isGooglePayEnabled(): Boolean {

        val request = IsReadyToPayRequest.fromJson(createDevicePaymentCheckRequest().toString())

        return try {
            paymentsClient.isReadyToPay(request).await()
        } catch (e: java.lang.Exception) {
            false
        }
    }

    private fun createPaymentRequest(googleSession: GoogleSession, price: String, gatewayMerchantId: String): JSONObject? {

        val merchantInfo = JSONObject().put("merchantName", googleSession.merchantInfo.merchantName)

        val allowedCardNetworks = JSONArray(googleSession.allowedCardNetworks)

        val allowedCardAuthMethods = JSONArray(googleSession.allowedAuthMethods)

        val cardPaymentMethod = JSONObject().apply {

                val parameters = JSONObject().apply {
                    put("allowedAuthMethods", allowedCardAuthMethods)
                    put("allowedCardNetworks", allowedCardNetworks)
                    put("billingAddressRequired", false)
                    put("billingAddressParameters", JSONObject())
                }

                val gatewayTokenizationSpecification = JSONObject().apply {
                    put("type", "PAYMENT_GATEWAY")
                    put(
                        "parameters", JSONObject(
                            mapOf(
                                "gateway" to googleSession.gateway,
                                "gatewayMerchantId" to gatewayMerchantId
                            )
                        )
                    )
                }

                put("type", "CARD")
                put("parameters", parameters)
                put("tokenizationSpecification", gatewayTokenizationSpecification)

            }


        val baseRequest = JSONObject().apply {
            put("apiVersion", 2)
            put("apiVersionMinor", 0)
        }

        val transactionInfo = JSONObject().apply {
                put("totalPrice", price)
                put("totalPriceStatus", "FINAL")
                put("countryCode", googleSession.transactionInfo.countryCode)
                put("currencyCode", googleSession.transactionInfo.currencyCode)
            }

            return try {
                baseRequest.apply {
                    put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
                    put("transactionInfo", transactionInfo)
                    put("merchantInfo", merchantInfo)
                    put("shippingAddressRequired", false)
                }
            } catch (e: JSONException) {
                null
            }
    }

    private fun createDevicePaymentCheckRequest(): JSONObject? {

        val allowedCardNetworks = JSONArray(listOf("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA"))

        val allowedCardAuthMethods = JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS"))

        val cardPaymentMethod = JSONObject().apply {

            val parameters = JSONObject().apply {
                put("allowedAuthMethods", allowedCardAuthMethods)
                put("allowedCardNetworks", allowedCardNetworks)
            }

            put("type", "CARD")
            put("parameters", parameters)
        }

        val baseRequest = JSONObject().apply {
            put("apiVersion", 2)
            put("apiVersionMinor", 0)
        }

        return try {
            baseRequest.apply {
                put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
            }
        } catch (e: JSONException) {
            null
        }
    }
}