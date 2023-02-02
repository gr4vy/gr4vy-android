package com.gr4vy.android_sdk.web

import com.gr4vy.android_sdk.models.ApprovalMessage
import com.gr4vy.android_sdk.models.FrameReadyMessage
import com.gr4vy.android_sdk.models.TransactionMessage
import com.gr4vy.android_sdk.models.UnknownMessage
import junit.framework.TestCase
import kotlinx.serialization.json.Json
import org.junit.Assert.assertThrows
import org.junit.Test
import java.lang.IllegalArgumentException

class MessagePolymorphicSerializerTest : TestCase() {

    private val format = Json {
        classDiscriminator = "theType"
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun testSelectDeserializerReturnsUnknownMessageWhenMessageTypeIsUnknown() {

        val result = format.decodeFromString(MessagePolymorphicSerializer(), "{\"type\": \"bad\"}")

        assertTrue(
            "when given an unknown type the message was not of type UnknownMessage",
            result is UnknownMessage
        )
    }

    @Test
    fun testSelectDeserializerReturnsFrameReadyMessageWhenMessageTypeIsFrameReady() {

        val expectedChannel = "123"

        val result = format.decodeFromString(
            MessagePolymorphicSerializer(),
            "{\"type\": \"frameReady\",  \"channel\": \"$expectedChannel\"}"
        )

        assertTrue(
            "when given a type of FrameReady the message was not of type FrameReady",
            result is FrameReadyMessage
        )

        assertEquals(expectedChannel, (result as FrameReadyMessage).channel)
    }

    @Test
    fun testSelectDeserializerReturnsApprovalMessageWhenMessageTypeIsApproval() {

        val expectedChannel = "123"
        val expectedData = "expected-data"

        val result = format.decodeFromString(
            MessagePolymorphicSerializer(),
            "{\"type\": \"approvalUrl\", \"channel\": \"$expectedChannel\", \"data\": \"$expectedData\"}"
        )

        assertTrue(
            "when given a type of approvalUrl the message was not of type ApprovalMessage",
            result is ApprovalMessage
        )

        assertEquals(expectedChannel, (result as ApprovalMessage).channel)
        assertEquals(expectedData, result.data)
    }

    @Test
    fun testSelectDeserializerReturnsTransactionMessageWhenMessageTypeIsTransactionCreated() {

        val expectedChannel = "123"
        val expectedStatus = "expected-status"
        val expectedTransactionId = "expected-transaction-id"
        val expectedPaymentMethodId = "expected-payment-method-id"

        val result = format.decodeFromString(
            MessagePolymorphicSerializer(),
            "{" +
                        "\"type\": \"transactionCreated\"," +
                        " \"channel\": \"$expectedChannel\"," +
                        " \"data\": {" +
                                "\"status\": \"$expectedStatus\"," +
                                "\"id\": \"$expectedTransactionId\"," +
                                "\"paymentMethodID\": \"$expectedPaymentMethodId\"" +
                        "}" +
                    "}"
        )

        assertTrue(
            "when given a type of transactionCreated the message was not of type TransactionMessage",
            result is TransactionMessage
        )

        assertEquals(expectedChannel, (result as TransactionMessage).channel)
        assertEquals(expectedStatus, result.data.status)
    }

    @Test
    fun testSelectDeserializerThrowsWhenMessageIsMisconstructed() {

        assertThrows(IllegalArgumentException::class.java) {
            format.decodeFromString(MessagePolymorphicSerializer(), "BAD-MESSAGE")
        }
    }
}