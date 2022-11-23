package com.gr4vy.android_sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.gr4vy.android_sdk.web.MyWebChromeClient
import com.gr4vy.gr4vy_android.R

class Secure3DActivity : AppCompatActivity() {
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
                }
            }
            this.loadUrl(intent.getStringExtra("URL").orEmpty())
        }
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_CODE = 1122
        const val RESULT_KEY = "3dSecureResult"

        fun startForResult(url: String, activity: Activity) {

            val intent = Intent(activity, Secure3DActivity::class.java).apply {
                putExtra("URL", url)
            }

            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}