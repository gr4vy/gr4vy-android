package com.gr4vy.android_sdk.web

import com.gr4vy.android_sdk.models.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessageHandler(private val parameters: Parameters, private val isGooglePayEnabled: Boolean = false) {

    private val json = Json {
        classDiscriminator = "theType"
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    fun handleMessage(realMessage: String): MessageHandlerResult {
        
        val decodedMessage: Message =
            json.decodeFromString(MessagePolymorphicSerializer(), realMessage)

        return when (decodedMessage) {
            is UnknownMessage -> Unknown()
            is OpenLinkMessage -> OpenLink(decodedMessage.data.url)
            is ApprovalMessage -> Open3ds(decodedMessage.data)
            is GoogleStartSessionMessage -> StartGooglePay(decodedMessage.data)
            is FrameReadyMessage -> {
                val jsonToPost = json.encodeToString(UpdateMessage.fromParameters(parameters, isGooglePayEnabled))
                val js = "window.postMessage($jsonToPost)"
                return FrameReady(js)
            }
            is NavigationMessage -> {
                return UpdateNavigation(decodedMessage.data)
            }
            is TransactionMessage -> {
                when (decodedMessage.data.status) {
                    "capture_succeeded", "capture_pending", "authorization_succeeded", "authorization_pending", "processing" -> {
                        return Gr4vyMessageResult(
                            Gr4vyResult.TransactionCreated(
                                status = decodedMessage.data.status,
                                paymentMethodId = decodedMessage.data.paymentMethodId,
                                transactionId = decodedMessage.data.transactionId,
                            )
                        )
                    }
                    "capture_declined", "authorization_failed", "authorization_declined" -> {
                        return Gr4vyMessageResult(
                            Gr4vyEvent.TransactionFailed(
                                status = decodedMessage.data.status,
                                paymentMethodId = decodedMessage.data.paymentMethodId,
                                transactionId = decodedMessage.data.transactionId,
                            )
                        )
                    }
                    else -> {
                        return Gr4vyMessageResult(
                            Gr4vyEvent.TransactionFailed(
                                status = decodedMessage.data.status,
                                paymentMethodId = decodedMessage.data.paymentMethodId,
                                transactionId = decodedMessage.data.transactionId,
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class MessageHandlerResult
class UpdateNavigation(val navigationData: Navigation) : MessageHandlerResult()
class Open3ds(val url: String) : MessageHandlerResult()
class OpenLink(val url: String) : MessageHandlerResult()
class Gr4vyMessageResult(val result: Gr4vyResultEventInterface) : MessageHandlerResult()
class FrameReady(val js: String) : MessageHandlerResult()
class StartGooglePay(val googleSessionData: GoogleSession) : MessageHandlerResult()
class Unknown : MessageHandlerResult()