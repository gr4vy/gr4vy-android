package com.gr4vy.android_sdk.web

import com.gr4vy.android_sdk.models.*
import junit.framework.TestCase
import org.junit.Test

class MessageHandlerTest : TestCase() {

    @Test
    fun testHandleMessageReturnsErrorWhenMessageIsUnknown() {

        val message =
            "{" +
                "\"type\": \"unknown\"," +
                " \"channel\": \"123\"" +
            "}"

        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        assert(messageHandlerResult is Unknown)
    }

    @Test
    fun testHandleMessageReturnsFrameReadyWhenGivenMessage() {

        val expectedJsonToPost = "{\"type\":\"updateOptions\",\"data\":{\"apiHost\":\"api.config-instance.gr4vy.app\",\"apiUrl\":\"https://api.config-instance.gr4vy.app\",\"token\":\"token\",\"amount\":10873,\"country\":\"GB\",\"currency\":\"GBP\",\"buyerId\":\"buyerId\"}}"
        val expectedJs = "window.postMessage($expectedJsonToPost)"

        val message = "{\"type\": \"frameReady\", \"channel\": \"123\"}"

        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        assert(messageHandlerResult is FrameReady)
        assertEquals(expectedJs, (messageHandlerResult as FrameReady).js)
    }

    @Test
    fun testHandleMessageEncodesOptionalFieldsWhenNotNull() {

        val expectedExternalIdentifier = "expected-indentifier"
        val expectedMetaDataKey = "meta-data-key"
        val expectedMetaDataValue = "meta-data-value"
        val expectedCartItemName = "cartItemName"
        val expectedCartItemQuantity = 2
        val expectedCartItemUnitAmount = 1
        val expectedJsonToPost = "{" +
                "\"type\":\"updateOptions\"," +
                "\"data\":{" +
                        "\"apiHost\":\"api.config-instance.gr4vy.app\"," +
                        "\"apiUrl\":\"https://api.config-instance.gr4vy.app\"," +
                        "\"token\":\"token\"," +
                        "\"amount\":10873," +
                        "\"country\":\"GB\"," +
                        "\"currency\":\"GBP\"," +
                        "\"buyerId\":\"buyerId\"," +
                        "\"externalIdentifier\":\"$expectedExternalIdentifier\"," +
                        "\"cartItems\":[" +
                        "{\"name\":\"$expectedCartItemName\",\"quantity\":$expectedCartItemQuantity,\"unitAmount\":$expectedCartItemUnitAmount,\"discountAmount\":0,\"taxAmount\":0}" +
                        "]," +
                        "\"paymentSource\":\"installment\"," +
                        "\"metadata\":{" +
                            "\"$expectedMetaDataKey\":\"$expectedMetaDataValue\"" +
                        "}" +
                    "}" +
                "}"
        val expectedJs = "window.postMessage($expectedJsonToPost)"

        val message = "{\"type\": \"frameReady\", \"channel\": \"123\"}"

        val messageHandler = MessageHandler(testParameters.copy(
            externalIdentifier = expectedExternalIdentifier,
            paymentSource = PaymentSource.INSTALLMENT,
            metadata = hashMapOf(expectedMetaDataKey to expectedMetaDataValue),
            cartItems = listOf(CartItem(name = expectedCartItemName, quantity = expectedCartItemQuantity, unitAmount = expectedCartItemUnitAmount))
        ))

        val messageHandlerResult = messageHandler.handleMessage(message)

        assert(messageHandlerResult is FrameReady)
        assertEquals(expectedJs, (messageHandlerResult as FrameReady).js)
    }

    @Test
    fun testHandleMessageEncodesPaymentSourceAsNullWhenNotSet() {

        val expectedJsonToPost = "{" +
                "\"type\":\"updateOptions\"," +
                "\"data\":{" +
                    "\"apiHost\":\"api.config-instance.gr4vy.app\"," +
                    "\"apiUrl\":\"https://api.config-instance.gr4vy.app\"," +
                    "\"token\":\"token\"," +
                    "\"amount\":10873," +
                    "\"country\":\"GB\"," +
                    "\"currency\":\"GBP\"," +
                    "\"buyerId\":\"buyerId\"" +
                    "}" +
                "}"
        val expectedJs = "window.postMessage($expectedJsonToPost)"

        val message = "{\"type\": \"frameReady\", \"channel\": \"123\"}"

        val messageHandler = MessageHandler(testParameters.copy(
            paymentSource = PaymentSource.NOT_SET,
        ))

        val messageHandlerResult = messageHandler.handleMessage(message)

        assert(messageHandlerResult is FrameReady)
        assertEquals(expectedJs, (messageHandlerResult as FrameReady).js)
    }

    @Test
    fun testHandleMessageEncodesBuyerWhenNotNull() {

        val expectedJsonToPost = "{" +
                "\"type\":\"updateOptions\"," +
                "\"data\":{" +
                        "\"apiHost\":\"api.config-instance.gr4vy.app\"," +
                        "\"apiUrl\":\"https://api.config-instance.gr4vy.app\"," +
                        "\"token\":\"token\"," +
                        "\"amount\":10873," +
                        "\"country\":\"GB\"," +
                        "\"currency\":\"GBP\"," +
                        "\"buyer\":{" +
                            "\"displayName\":\"Test buyer\"," +
                            "\"billingDetails\":{" +
                                "\"firstName\":\"Ivy\"," +
                                "\"address\":{" +
                                    "\"city\":\"London\"" +
                                "}" +
                            "}" +
                        "}" +
                    "}" +
                "}"
        val expectedJs = "window.postMessage($expectedJsonToPost)"

        val message = "{\"type\": \"frameReady\", \"channel\": \"123\"}"

        val messageHandler = MessageHandler(testParameters.copy(
            buyerId = null,
            buyer = Gr4vyBuyer(
                displayName = "Test buyer",
                billingDetails = Gr4vyBillingDetails(
                    firstName = "Ivy",
                    address = Gr4vyAddress(city = "London")
                )
            )
        ))

        val messageHandlerResult = messageHandler.handleMessage(message)

        assert(messageHandlerResult is FrameReady)
        assertEquals(expectedJs, (messageHandlerResult as FrameReady).js)
    }

    @Test
    fun testHandleMessageReturnsOpen3dsWhenGivenMessage() {

        val expectedUrl = "https://test.com"
        val expectedChannel = "123"

        val message = "{\"type\": \"approvalUrl\", \"channel\": \"$expectedChannel\", \"data\": \"$expectedUrl\"}"

        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        assert(messageHandlerResult is Open3ds)
        assertEquals(expectedUrl, (messageHandlerResult as Open3ds).url)
    }

    private fun testReturnsTransactionCreatedWhenMessageIs(expectedStatus: String) {

        val expectedChannel = "123"

        val message =
            "{" +
                "\"type\": \"transactionCreated\", " +
                "\"channel\": \"$expectedChannel\", " +
                "\"data\": {" +
                        "\"status\": \"$expectedStatus\"," +
                        "\"id\": \"$expectedStatus\"," +
                        "\"paymentMethodID\": \"$expectedStatus\"" +
                    "}" +
            "}"

        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        val gr4vyResult = (messageHandlerResult as Gr4vyMessageResult).result

        assertEquals(expectedStatus, (gr4vyResult as Gr4vyResult.TransactionCreated).status)
    }

    @Test
    fun testHandleMessageReturnsTransactionCreatedWhenMessageHasStatusOfCaptureSucceeded() {
        testReturnsTransactionCreatedWhenMessageIs("capture_succeeded")
    }

    @Test
    fun testHandleMessageReturnsTransactionCreatedWhenMessageHasStatusOfCapturePending() {
        testReturnsTransactionCreatedWhenMessageIs("capture_pending")
    }

    @Test
    fun testHandleMessageReturnsTransactionCreatedWhenMessageHasStatusOfAuthSucceeded() {
        testReturnsTransactionCreatedWhenMessageIs("authorization_succeeded")
    }

    @Test
    fun testHandleMessageReturnsTransactionCreatedWhenMessageHasStatusOfAuthPending() {
        testReturnsTransactionCreatedWhenMessageIs("authorization_pending")
    }

    @Test
    fun testHandleMessageReturnsTransactionFailedWhenMessageHasStatusOfCaptureDeclined() {
        testReturnsTransactionFailedWhenMessageIs("capture_declined")
    }

    @Test
    fun testHandleMessageReturnsTransactionFailedWhenMessageHasStatusOfAuthFailed() {
        testReturnsTransactionFailedWhenMessageIs("authorization_failed")
    }

    @Test
    fun testHandleMessageReturnsTransactionCreatedWhenMessageHasApprovalUrl() {

        val expectedChannel = "123"
        val expectedStatus = "authorization_pending"
        val expectedApprovalUrl = "example.com"

        val message =
            "{" +
                    "\"type\": \"transactionCreated\", " +
                    "\"channel\": \"$expectedChannel\", " +
                    "\"data\": {" +
                    "\"paymentMethod\": {\"approvalUrl\": \"example.com\"}," +
                    "\"status\": \"$expectedStatus\"," +
                    "\"id\": \"$expectedStatus\"," +
                    "\"paymentMethodID\": \"$expectedStatus\"" +
                    "}" +
                    "}"

        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        val gr4vyResult = (messageHandlerResult as Gr4vyMessageResult).result

        assertEquals(expectedStatus, (gr4vyResult as Gr4vyResult.TransactionCreated).status)
        assertEquals(expectedApprovalUrl, (gr4vyResult as Gr4vyResult.TransactionCreated).approvalUrl)
    }


    private fun testReturnsTransactionFailedWhenMessageIs(expectedStatus: String) {

        val expectedChannel = "123"

        val message =
            "{" +
                "\"type\": \"transactionCreated\"," +
                " \"channel\": \"$expectedChannel\"," +
                " \"data\": {" +
                        "\"status\": \"$expectedStatus\"," +
                        "\"id\": \"\"," +
                        "\"paymentMethodID\": \"\"" +
                    "}" +
            "}"

        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        val gr4vyResult = (messageHandlerResult as Gr4vyMessageResult).result

        assertEquals(expectedStatus, (gr4vyResult as Gr4vyEvent.TransactionFailed).status)
    }

    @Test
    fun testReturnsTransactionFailedWhenJsonMessageIsLenient() {

        val message =
            "{\"channel\":\"123\",\"type\":\"transactionFailed\",\"data\":{\"status\":400,\"type\":\"error\",\"code\":\"bad_request\",\"message\":\"Request failed validation\",\"details\":[{\"location\":\"body\",\"message\":\"ensure this value has at least 5 characters\",\"pointer\":\"/statement_descriptor/name\",\"type\":\"value_error.any_str.min_length\"}]}}"


        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        val gr4vyResult = (messageHandlerResult as Gr4vyMessageResult).result

        assertEquals("400", (gr4vyResult as Gr4vyEvent.TransactionFailed).status)
    }

    @Test
    fun testHandleMessageReturnsCardDetailsChanged() {
        val expectedBin = "41111111"
        val expectedCardType = "credit"
        val expectedScheme = "visa"

        val message =
            "{" +
                "\"type\": \"cardDetailsChanged\"," +
                " \"channel\": \"123\"," +
                " \"data\": {" +
                        "\"bin\": \"$expectedBin\"," +
                        "\"cardType\": \"$expectedCardType\"," +
                        "\"scheme\": \"$expectedScheme\"" +
                    "}" +
            "}"

        val messageHandler = MessageHandler(testParameters)

        val messageHandlerResult = messageHandler.handleMessage(message)

        val gr4vyResult = (messageHandlerResult as Gr4vyMessageResult).result

        assertEquals(expectedBin, (gr4vyResult as Gr4vyEvent.CardDetailsChanged).bin)
        assertEquals(expectedCardType, (gr4vyResult as Gr4vyEvent.CardDetailsChanged).cardType)
        assertEquals(expectedScheme, (gr4vyResult as Gr4vyEvent.CardDetailsChanged).scheme)
    }
}