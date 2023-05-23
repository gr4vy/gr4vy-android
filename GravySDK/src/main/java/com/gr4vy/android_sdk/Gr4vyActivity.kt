package com.gr4vy.android_sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.lifecycleScope
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.google.android.gms.common.api.Status
import com.google.android.gms.wallet.PaymentData
import com.gr4vy.android_sdk.google_pay.GooglePayClient
import com.gr4vy.android_sdk.models.*
import com.gr4vy.android_sdk.web.MessageHandler
import com.gr4vy.android_sdk.web.MyWebChromeClient
import com.gr4vy.android_sdk.web.UrlFactory
import com.gr4vy.android_sdk.web.WebAppInterface
import com.gr4vy.gr4vy_android.BuildConfig
import com.gr4vy.gr4vy_android.R
import kotlinx.coroutines.launch


class Gr4vyActivity : AppCompatActivity() {

    private val parameters: Parameters by lazy {
        intent.getParcelableExtra<Parameters>(
            PARAMETERS_EXTRA_KEY
        ) as Parameters
    }
    private lateinit var googlePayClient: GooglePayClient

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gr4vy)
        setSupportActionBar(findViewById(R.id.gr4vy_toolbar))

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (parameters.theme?.colors?.headerBackground != null && parameters.theme?.colors?.headerBackground!!.isNotEmpty()) {
            try {
                val color = Color.parseColor(parameters.theme?.colors?.headerBackground)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
            } catch (exception: IllegalArgumentException) {
            }
        }
        val chromeClient = MyWebChromeClient().apply {

            this.navigationUpdateListener = { title: String, canGoBack: Boolean? ->
                supportActionBar?.title = title
                if (canGoBack != null) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(canGoBack)
                }

                if (parameters.theme?.colors?.headerText != null && parameters.theme?.colors?.headerText!!.isNotEmpty()) {
                    try {
                        val color = Color.parseColor(parameters.theme?.colors?.headerText)

                        val text: Spannable = SpannableString(title)
                        text.setSpan(
                            ForegroundColorSpan(color),
                            0,
                            text.length,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        supportActionBar?.title = text

                        val upArrow = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.abc_ic_ab_back_material
                        )
                        upArrow?.colorFilter =
                            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                                color,
                                BlendModeCompat.SRC_ATOP
                            )
                        supportActionBar?.setHomeAsUpIndicator(upArrow)

                    } catch (exception: IllegalArgumentException) {
                    }
                }
            }
        }

        googlePayClient =
            GooglePayClient.createClient(this, parameters.gr4vyId, parameters.config.isProduction)

        lifecycleScope.launch {
            val javascriptInterface = WebAppInterface(
                MessageHandler(
                    parameters,
                    googlePayClient.isGooglePayEnabled()
                )
            ).apply {
                this.open3dsListener = { url -> open3ds(url) }
                this.openLinkListener = { url -> openLink(url) }
                this.startGooglePayListener = { data: GoogleSession ->
                    googlePayClient.pay(
                        this@Gr4vyActivity,
                        data,
                        parameters.amount
                    )
                }
                this.navigationListener = { data: Navigation ->
                    chromeClient.navigationUpdateListener?.invoke(
                        data.title,
                        data.canGoBack
                    )
                }
                this.callback = { message -> handleCallback(message) }
            }

            findViewById<WebView>(R.id.gr4vy_webview).apply {
                WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
                this.webViewClient = WebViewClient()
                this.webChromeClient = chromeClient
                this.settings.javaScriptEnabled = true
                if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
                    WebViewCompat.addWebMessageListener(
                        this,
                        "nativeapp",
                        setOf("https://*.gr4vy.app"),
                        javascriptInterface
                    )
                }
                this.loadUrl(initialUrl())
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Secure3DActivity.REQUEST_CODE -> handle3dSecureResult(resultCode, data)
            GooglePayClient.REQUEST_CODE -> googlePayClient.handleGooglePayResult(
                resultCode,
                data,
                ::handleGooglePaymentSuccess,
                ::handleGooglePaymentError,
                ::handleGooglePaymentCancelled
            )
        }

    }

    private fun handleGooglePaymentSuccess(paymentData: PaymentData) {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message =
            "window.postMessage({\"channel\":123,\"type\":\"googlePaySessionAuthorized\",\"data\":${paymentData.toJson()}})"
        webView.evaluateJavascript(message, null)
    }

    private fun handleGooglePaymentError(status: Status) {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message =
            "window.postMessage({\"channel\":123,\"type\":\"googlePaySessionErrored\",\"data\":{\"status\":${status.statusCode}, \"statusMessage\": \"${status.statusMessage.toString()}\"}})"
        webView.evaluateJavascript(message, null)
    }

    private fun handleGooglePaymentCancelled() {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message = "window.postMessage({\"channel\":123,\"type\":\"googlePaySessionCancelled\"})"
        webView.evaluateJavascript(message, null)
    }

    private fun handleNavigationBack() {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message = "window.postMessage({\"channel\":123,\"type\":\"navigationBack\"})"
        webView.evaluateJavascript(message, null)
    }

    private fun handleApprovalCancelled() {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message = "window.postMessage({\"channel\":123,\"type\":\"approvalCancelled\"})"
        webView.evaluateJavascript(message, null)
    }

    private fun handle3dSecureResult(resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_CANCELED) {
            handleApprovalCancelled()
        } else {
            val result = data?.getStringExtra(Secure3DActivity.RESULT_KEY).orEmpty()
            val webView = findViewById<WebView>(R.id.gr4vy_webview);
            webView.evaluateJavascript("window.postMessage($result)", null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                handleNavigationBack()
                val webView = findViewById<WebView>(R.id.gr4vy_webview);
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    handleApprovalCancelled()
                    finish()
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleCallback(result: Gr4vyResultEventInterface) {
        if (result is Gr4vyEvent) {
            sendBroadcast(Intent(Gr4vySDK.BROADCAST_KEY).putExtra(EVENT_KEY, result as Gr4vyEvent))
        } else {
            val resultIntent = Intent().apply {
                this.putExtra(RESULT_KEY, result as Gr4vyResult)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun open3ds(url: String) = Secure3DActivity.startForResult(url, parameters, this)

    private fun openLink(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun initialUrl(): String = UrlFactory.fromParameters(parameters)

    companion object {

        private const val PARAMETERS_EXTRA_KEY = "PARAMETERS"
        const val RESULT_KEY = "GR4VY_RESULT"
        const val EVENT_KEY = "GR4VY_EVENT"

        fun createIntentWithParameters(context: Context, parameters: Parameters): Intent {
            return Intent(context, Gr4vyActivity::class.java).apply {
                putExtra(PARAMETERS_EXTRA_KEY, parameters)
            }
        }
    }
}

