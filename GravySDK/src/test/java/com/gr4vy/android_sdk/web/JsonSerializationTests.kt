package com.gr4vy.android_sdk.web

import com.gr4vy.android_sdk.gr4vyConvertJSONStringToMap
import com.gr4vy.android_sdk.gr4vyMapConnectionOptions
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

        assertNull(gr4vyMapConnectionOptions(input, null))
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

        assertEquals(gr4vyMapConnectionOptions(adyenCardInput, null), adyenCardInputString)
        assertEquals(gr4vyMapConnectionOptions(null, adyenCardInputString), adyenCardInputString)
        assertEquals(gr4vyMapConnectionOptions(adyenCardInput, "{\"key1\":{\"subKey1\":\"value\"}}"), adyenCardInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

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

        assertEquals(gr4vyMapConnectionOptions(cybersourceAntiFraudInput, null), cybersourceAntiFraudInputString)
        assertEquals(gr4vyMapConnectionOptions(null, cybersourceAntiFraudInputString), cybersourceAntiFraudInputString)
        assertEquals(gr4vyMapConnectionOptions(cybersourceAntiFraudInput, "{\"key1\":{\"subKey1\":\"value\"}}"), cybersourceAntiFraudInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

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

        assertEquals(gr4vyMapConnectionOptions(forterAntiFraudEmailInput, null), forterAntiFraudEmailInputString)
        assertEquals(gr4vyMapConnectionOptions(null, forterAntiFraudEmailInputString), forterAntiFraudEmailInputString)
        assertEquals(gr4vyMapConnectionOptions(forterAntiFraudEmailInput, "{\"key1\":{\"subKey1\":\"value\"}}"), forterAntiFraudEmailInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

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

        assertEquals(gr4vyMapConnectionOptions(forterAntiFraudPhysicalInput, null), forterAntiFraudPhysicalInputString)
        assertEquals(gr4vyMapConnectionOptions(null, forterAntiFraudPhysicalInputString), forterAntiFraudPhysicalInputString)
        assertEquals(gr4vyMapConnectionOptions(forterAntiFraudPhysicalInput, "{\"key1\":{\"subKey1\":\"value\"}}"), forterAntiFraudPhysicalInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

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

        assertEquals(gr4vyMapConnectionOptions(paypalPaypalInput, null), paypalPaypalInputString)
        assertEquals(gr4vyMapConnectionOptions(null, paypalPaypalInputString), paypalPaypalInputString)
        assertEquals(gr4vyMapConnectionOptions(paypalPaypalInput, "{\"key1\":{\"subKey1\":\"value\"}}"), paypalPaypalInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

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

        assertEquals(gr4vyMapConnectionOptions(paypalPaypalPayLaterInput, null), paypalPaypalPayLaterInputString)
        assertEquals(gr4vyMapConnectionOptions(null, paypalPaypalPayLaterInputString), paypalPaypalPayLaterInputString)
        assertEquals(gr4vyMapConnectionOptions(paypalPaypalPayLaterInput, "{\"key1\":{\"subKey1\":\"value\"}}"), paypalPaypalPayLaterInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))


        var intInput: Map<String, JsonElement>? = mapOf(
            "int" to buildJsonObject {
                put("additional_data", JsonPrimitive(1))
            }
        )

        var intInputString = "{\"int\":{\"additional_data\":1}}"

        assertEquals(gr4vyMapToJsonString(intInput), intInputString)
        assertEquals(gr4vyConvertJSONStringToMap(intInputString), intInput)

        assertEquals(gr4vyMapConnectionOptions(intInput, null), intInputString)
        assertEquals(gr4vyMapConnectionOptions(null, intInputString), intInputString)
        assertEquals(gr4vyMapConnectionOptions(intInput, "{\"key1\":{\"subKey1\":\"value\"}}"), intInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

        var boolInput: Map<String, JsonElement>? = mapOf(
            "bool" to buildJsonObject {
                put("additional_data", JsonPrimitive(true))
            }
        )
        var boolInputString = "{\"bool\":{\"additional_data\":true}}"

        assertEquals(gr4vyMapToJsonString(boolInput), boolInputString)
        assertEquals(gr4vyConvertJSONStringToMap(boolInputString), boolInput)

        assertEquals(gr4vyMapConnectionOptions(boolInput, null), boolInputString)
        assertEquals(gr4vyMapConnectionOptions(null, boolInputString), boolInputString)
        assertEquals(gr4vyMapConnectionOptions(boolInput, "{\"key1\":{\"subKey1\":\"value\"}}"), boolInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

        var stringInput: Map<String, JsonElement>? = mapOf(
            "string" to buildJsonObject {
                put("additional_data", JsonPrimitive("gr4vy"))
            }
        )
        var stringInputString = "{\"string\":{\"additional_data\":\"gr4vy\"}}"

        assertEquals(gr4vyMapToJsonString(stringInput), stringInputString)
        assertEquals(gr4vyConvertJSONStringToMap(stringInputString), stringInput)

        assertEquals(gr4vyMapConnectionOptions(stringInput, null), stringInputString)
        assertEquals(gr4vyMapConnectionOptions(null, stringInputString), stringInputString)
        assertEquals(gr4vyMapConnectionOptions(stringInput, "{\"key1\":{\"subKey1\":\"value\"}}"), stringInputString)
        assertNull(gr4vyMapConnectionOptions(null, null))

        var emptyInput: Map<String, JsonElement>? = mapOf()

        assertEquals(gr4vyMapToJsonString(emptyInput), null)
        assertEquals(gr4vyConvertJSONStringToMap(null), null)
    }
}