package com.gr4vy.android_sdk.web

import android.webkit.WebChromeClient
import android.webkit.WebView

class MyWebChromeClient : WebChromeClient() {

    var navigationUpdateListener: ((title: String, canGoBack: Boolean?) -> Unit)? = null

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        navigationUpdateListener?.invoke(title.orEmpty(), null)
    }
}