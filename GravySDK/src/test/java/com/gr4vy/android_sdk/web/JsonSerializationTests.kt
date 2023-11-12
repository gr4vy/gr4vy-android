package com.gr4vy.android_sdk.web

import com.gr4vy.android_sdk.gr4vyConvertJSONStringToMap
import com.gr4vy.android_sdk.gr4vyMapToJsonString
import com.gr4vy.android_sdk.gr4vyMapToJsonString
import junit.framework.TestCase
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.Test

class JsonSerializationTests : TestCase() {

    @Test
    fun testEmptyConnectionOptions() {
        var input: Map<String, JsonElement>? = null

        assertNull(gr4vyMapToJsonString(input))
    }

    @Test
    fun testConnectionOptions() {
        var adyenCardInput: Map<String, JsonElement>? = mapOf(
            "adyen-card" to buildJsonObject {
                put("additionalData", JsonPrimitive("value"))
            }
        )
        var adyenCardInputString = "{\"adyen-card\":{\"additionalData\":\"value\"}}"

        assertEquals(
            gr4vyMapToJsonString(adyenCardInput),
            adyenCardInputString
        )

        assertEquals(gr4vyConvertJSONStringToMap(adyenCardInputString), adyenCardInput)

        var cybersourceAntiFraudInput: Map<String, JsonElement>? = mapOf(
            "cybersource-anti-fraud" to buildJsonObject {
                put("merchant_defined_data", JsonPrimitive("value"))
            }
        )
        var cybersourceAntiFraudInputString =
            "{\"cybersource-anti-fraud\":{\"merchant_defined_data\":\"value\"}}"

        assertEquals(
            gr4vyMapToJsonString(cybersourceAntiFraudInput),
            cybersourceAntiFraudInputString
        )
        assertEquals(
            gr4vyConvertJSONStringToMap(cybersourceAntiFraudInputString),
            cybersourceAntiFraudInput
        )

        var forterAntiFraudEmailInput: Map<String, JsonElement>? = mapOf(
            "forter-anti-fraud" to buildJsonObject {
                put("delivery_method", JsonPrimitive("email"))
            }
        )

        var forterAntiFraudEmailInputString =
            "{\"forter-anti-fraud\":{\"delivery_method\":\"email\"}}"

        assertEquals(
            gr4vyMapToJsonString(forterAntiFraudEmailInput),
            forterAntiFraudEmailInputString
        )
        assertEquals(
            gr4vyConvertJSONStringToMap(forterAntiFraudEmailInputString),
            forterAntiFraudEmailInput
        )

        var forterAntiFraudPhysicalInput: Map<String, JsonElement>? = mapOf(
            "forter-anti-fraud" to buildJsonObject {
                put("delivery_type", JsonPrimitive("PHYSICAL"))
            }
        )

        var forterAntiFraudPhysicalInputString =
            "{\"forter-anti-fraud\":{\"delivery_type\":\"PHYSICAL\"}}"

        assertEquals(
            gr4vyMapToJsonString(forterAntiFraudPhysicalInput),
            forterAntiFraudPhysicalInputString
        )
        assertEquals(
            gr4vyConvertJSONStringToMap(forterAntiFraudPhysicalInputString),
            forterAntiFraudPhysicalInput
        )


        var paypalPaypalInput: Map<String, JsonElement>? = mapOf(
            "paypal-paypal" to buildJsonObject {
                put("additional_data", JsonPrimitive("value"))
            }
        )

        var paypalPaypalInputString = "{\"paypal-paypal\":{\"additional_data\":\"value\"}}"

        assertEquals(
            gr4vyMapToJsonString(paypalPaypalInput),
            paypalPaypalInputString
        )
        assertEquals(gr4vyConvertJSONStringToMap(paypalPaypalInputString), paypalPaypalInput)


        var paypalPaypalPayLaterInput: Map<String, JsonElement>? = mapOf(
            "paypal-paypalpaylater" to buildJsonObject {
                put("additional_data", JsonPrimitive("value"))
            }
        )

        var paypalPaypalPayLaterInputString =
            "{\"paypal-paypalpaylater\":{\"additional_data\":\"value\"}}"

        assertEquals(
            gr4vyMapToJsonString(paypalPaypalPayLaterInput),
            paypalPaypalPayLaterInputString
        )
        assertEquals(
            gr4vyConvertJSONStringToMap(paypalPaypalPayLaterInputString),
            paypalPaypalPayLaterInput
        )


        var intInput: Map<String, JsonElement>? = mapOf(
            "int" to buildJsonObject {
                put("additional_data", JsonPrimitive(1))
            }
        )

        var intInputString = "{\"int\":{\"additional_data\":1}}"

        assertEquals(gr4vyMapToJsonString(intInput), intInputString)
        assertEquals(gr4vyConvertJSONStringToMap(intInputString), intInput)

        var boolInput: Map<String, JsonElement>? = mapOf(
            "bool" to buildJsonObject {
                put("additional_data", JsonPrimitive(true))
            }
        )
        var boolInputString = "{\"bool\":{\"additional_data\":true}}"

        assertEquals(gr4vyMapToJsonString(boolInput), boolInputString)
        assertEquals(gr4vyConvertJSONStringToMap(boolInputString), boolInput)


        var stringInput: Map<String, JsonElement>? = mapOf(
            "string" to buildJsonObject {
                put("additional_data", JsonPrimitive("gr4vy"))
            }
        )
        var stringInputString = "{\"string\":{\"additional_data\":\"gr4vy\"}}"


        assertEquals(gr4vyMapToJsonString(stringInput), stringInputString)
        assertEquals(gr4vyConvertJSONStringToMap(stringInputString), stringInput)

        var emptyInput: Map<String, JsonElement>? = mapOf()

        assertEquals(gr4vyMapToJsonString(emptyInput), null)
        assertEquals(gr4vyConvertJSONStringToMap(null), null)
    }
}