package com.gr4vy.android_sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.google.android.gms.common.api.Status
import com.google.android.gms.wallet.PaymentData
import com.gr4vy.android_sdk.google_pay.GooglePayClient
import com.gr4vy.android_sdk.models.GoogleSession
import com.gr4vy.android_sdk.models.Gr4vyResult
import com.gr4vy.android_sdk.models.Parameters
import com.gr4vy.android_sdk.web.MessageHandler
import com.gr4vy.android_sdk.web.MyWebChromeClient
import com.gr4vy.android_sdk.web.UrlFactory
import com.gr4vy.android_sdk.web.WebAppInterface
import com.gr4vy.gr4vy_android.BuildConfig
import com.gr4vy.gr4vy_android.R

class Gr4vyActivity : AppCompatActivity() {

    private val parameters: Parameters by lazy { intent.getParcelableExtra<Parameters>(PARAMETERS_EXTRA_KEY) as Parameters }
    private lateinit var googlePayClient: GooglePayClient

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gr4vy)
        setSupportActionBar(findViewById(R.id.gr4vy_toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val chromeClient = MyWebChromeClient().apply {
            this.titleUpdateListener = {
                supportActionBar?.title = it
            }
        }

        val javascriptInterface = WebAppInterface(MessageHandler(parameters)).apply {
            this.open3dsListener = { url -> open3ds(url) }
            this.startGooglePayListener = { data: GoogleSession -> googlePayClient.pay(this@Gr4vyActivity, data, parameters.amount) }
            this.callback = { message -> handleCallback(message) }
        }

        googlePayClient = GooglePayClient.createClient(this, parameters.config.id, parameters.config.isProduction)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            Secure3DActivity.REQUEST_CODE -> handle3dSecureResult(data)
            GooglePayClient.REQUEST_CODE -> googlePayClient.handleGooglePayResult(resultCode, data, ::handleGooglePaymentSuccess, ::handleGooglePaymentError, ::handleGooglePaymentCancelled)
        }

    }

    private fun handleGooglePaymentSuccess(paymentData: PaymentData) {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message = "window.postMessage({\"channel\":123,\"type\":\"googlePaySessionAuthorized\",\"data\":${paymentData.toJson()}})"
        webView.evaluateJavascript(message, null)
    }

    private fun handleGooglePaymentError(status: Status) {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message = "window.postMessage({\"channel\":123,\"type\":\"googlePaySessionErrored\",\"data\":{\"status\":${status.statusCode}, \"statusMessage\": \"${status.statusMessage.toString()}\"}})"
        webView.evaluateJavascript(message, null)
    }

    private fun handleGooglePaymentCancelled() {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message = "window.postMessage({\"channel\":123,\"type\":\"googlePaySessionCancelled\"})"
        webView.evaluateJavascript(message, null)
    }

    private fun handle3dSecureResult(data: Intent?) {

        val result = data?.getStringExtra(Secure3DActivity.RESULT_KEY).orEmpty()

        val webView = findViewById<WebView>(R.id.gr4vy_webview);
        webView.evaluateJavascript("window.postMessage($result)", null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleCallback(result: Gr4vyResult) {

        val resultIntent = Intent().apply {
            this.putExtra(RESULT_KEY, result)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun open3ds(url: String) = Secure3DActivity.startForResult(url, this)

    private fun initialUrl(): String = UrlFactory.fromParameters(parameters)

    companion object {

        private const val PARAMETERS_EXTRA_KEY = "PARAMETERS"
        const val RESULT_KEY = "GR4VY_RESULT"

        fun createIntentWithParameters(context: Context, parameters: Parameters): Intent {
            return Intent(context, Gr4vyActivity::class.java).apply {
                putExtra(PARAMETERS_EXTRA_KEY, parameters)
            }
        }
    }
}

