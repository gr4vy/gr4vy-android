package com.gr4vy.android_sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.gr4vy.android_sdk.models.Parameters
import com.gr4vy.android_sdk.web.MessageHandler
import com.gr4vy.android_sdk.web.MyWebChromeClient
import com.gr4vy.gr4vy_android.R

class Secure3DActivity : AppCompatActivity() {

    private val parameters: Parameters by lazy {
        intent.getParcelableExtra<Parameters>(
            Secure3DActivity.PARAMETERS_EXTRA_KEY
        ) as Parameters
    }

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

        findViewById<WebView>(R.id.gr4vy_webview).apply {
            this.webViewClient = WebViewClient()
            this.webChromeClient = chromeClient
            this.settings.javaScriptEnabled = true
            if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
                WebViewCompat.addWebMessageListener(
                    this, "nativeapp", setOf("*")
                ) { _, message, _, _, _ ->

                    val realMessage = message.data.orEmpty()

                    if (realMessage.contains("transactionUpdated") || realMessage.contains("approvalErrored")) {

                        setResult(RESULT_OK, Intent().apply {
                            putExtra(RESULT_KEY, realMessage)
                        })
                        finish()
                    }
                    if (realMessage.contains("navigation")) {
                        MessageHandler(parameters)
                    }
                }
            }
            this.loadUrl(intent.getStringExtra("URL").orEmpty())
        }
    }

    private fun handleNavigationBack() {
        val webView = findViewById<WebView>(R.id.gr4vy_webview)
        val message = "window.postMessage({\"channel\":123,\"type\":\"navigationBack\"})"
        webView.evaluateJavascript(message, null)
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                handleNavigationBack()
                val webView = findViewById<WebView>(R.id.gr4vy_webview);
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    setResult(RESULT_CANCELED, Intent().apply {
                        putExtra(RESULT_KEY, "Cancelled")
                    })
                    finish()
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val PARAMETERS_EXTRA_KEY = "PARAMETERS"
        private const val URL_EXTRA_KEY = "URL"
        const val REQUEST_CODE = 1122
        const val RESULT_KEY = "3dSecureResult"

        fun startForResult(url: String, parameters: Parameters, activity: Activity) {

            val intent = Intent(activity, Secure3DActivity::class.java).apply {
                putExtra(URL_EXTRA_KEY, url)
                putExtra(PARAMETERS_EXTRA_KEY, parameters)
            }

            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}