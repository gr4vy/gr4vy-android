package com.gr4vy.android_sdk.web

import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView

class MyWebChromeClient : WebChromeClient() {

    var navigationUpdateListener: ((title: String, canGoBack: Boolean?) -> Unit)? = null

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        if (URLUtil.isValidUrl(title)) {
            // We don't want the title to be a URL
            return
        }
        navigationUpdateListener?.invoke(title.orEmpty(), null)
    }
}