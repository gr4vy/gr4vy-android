package com.gr4vy.android_sdk.web

import junit.framework.TestCase
import org.junit.Test

class UrlFactoryTest : TestCase() {

    @Test
    fun testUrlFactoryCreatesExpectedUrlWhenProvidedParameters() {

        val result = UrlFactory.fromParameters(testParameters)

        assertEquals("https://embed.config-instance.gr4vy.app/mobile?channel=123", result)
    }
}