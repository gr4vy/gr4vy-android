package com.gr4vy.android_sdk.web

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.webkit.JavaScriptReplyProxy
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebViewCompat
import com.gr4vy.android_sdk.models.GoogleSession
import com.gr4vy.android_sdk.models.Gr4vyResult

class WebAppInterface(private val messageHandler: MessageHandler) :
    WebViewCompat.WebMessageListener {

    var open3dsListener: ((url: String) -> Unit)? = null
    var startGooglePayListener: ((data: GoogleSession) -> Unit)? = null
    var callback: ((result: Gr4vyResult) -> Unit)? = null

    override fun onPostMessage(
        view: WebView,
        message: WebMessageCompat,
        sourceOrigin: Uri,
        isMainFrame: Boolean,
        replyProxy: JavaScriptReplyProxy
    ) {

        kotlin.runCatching {
            val result = messageHandler.handleMessage(message.data.orEmpty())

            when (result) {
                is FrameReady -> view.evaluateJavascript(result.js, null)
                is Gr4vyMessageResult -> callback?.invoke(result.result)
                is Open3ds -> open3dsListener?.invoke(result.url)
                is StartGooglePay -> startGooglePayListener?.invoke(result.googleSessionData)
                else -> Log.d("Gr4vy", "Unknown message received")
            }
        }
            .onFailure { exception ->
                Log.e(
                    "Gr4vy",
                    "Error handling message: ${exception.message}"
                )
                callback?.invoke(Gr4vyResult.GeneralError())
            }
    }
}