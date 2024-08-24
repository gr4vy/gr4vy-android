package com.gr4vy.android_sdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Gr4vyBuyer(
  val displayName: String? = null,
  val externalIdentifier: String? = null,
  val billingDetails: Gr4vyBillingShippingDetails? = null,
  val shippingDetails: Gr4vyBillingShippingDetails? = null
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyBillingShippingDetails(
  val firstName: String? = null,
  val lastName: String? = null,
  val emailAddress: String? = null,
  val phoneNumber: String? = null,
  val address: Gr4vyAddress? = null,
  val taxId: Gr4vyTaxId? = null
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyAddress(
  val houseNumberOrName: String? = null,
  val line1: String? = null,
  val line2: String? = null,
  val organization: String? = null,
  val city: String? = null,
  val postalCode: String? = null,
  val country: String? = null,
  val state: String? = null,
  val stateCode: String? = null,
) : Parcelable

@Serializable
@Parcelize
data class Gr4vyTaxId(
  val value: String? = null,
  val kind: String? = null,
) : Parcelable