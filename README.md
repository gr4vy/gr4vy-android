# Gr4vy Android SDK

![Build Status](https://github.com/gr4vy/gr4vy-android/actions/workflows/build.yaml/badge.svg?branch=main)

![Platforms](https://img.shields.io/badge/Platforms-Android-yellowgreen?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-1.4.0-yellowgreen?style=for-the-badge)

Quickly embed Gr4vy in your Android app to store card details, authorize payments, and capture a transaction.

## Installation

gr4vy_android doesn't contain any external dependencies.

### Minimum API Level / Target API Level

> **Note**:
> The minimum API level required for this SDK is `21`
> The target API level required for this SDK is `33`

### Gradle

```gradle
repositories {
  mavenCentral()
  maven { url "https://jitpack.io" }
}

dependencies {
  implementation 'com.github.gr4vy:gr4vy-android:v1.6.3'
}
```

## Get started

### Setup

Ensure you have the internet permission added to your manifest:

`<uses-permission android:name="android.permission.INTERNET" />`

## Running

### Step 1. Add Gr4vy to your activity

Add the following to the top of your activity:

`private lateinit var gr4vySDK: Gr4vySDK`

then initialise it within the onCreate() method like so:

`gr4vySDK = Gr4vySDK(activityResultRegistry, this, this)`

then register the observer like so:

`lifecycle.addObserver(gr4vySDK)`

finally implement the Gr4vyResultHandler interface on the Activity and implement the methods:

`class MainActivity : ComponentActivity(), Gr4vyResultHandler`

### Step 2. Run Gr4vy

To use Gr4vy Embed call the `.launch()` method:

```kotlin
    gr4vySDK.launch(
        context = this,
        gr4vyId = "[GR4VY_ID]"
        token = "[TOKEN]",
        amount = 1299,
        currency = "USD",
        country = "US")
```

> **Note**:
> Replace `[TOKEN]` with your JWT access token (See any of our [server-side SDKs](https://github.com/gr4vy?q=sdk) for more details.).

### Options

These are the options available in this SDK:

| Field                     | Optional / Required | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| ------------------------- | ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | 
| `gr4vyId`                 | **`Required`**      | The Gr4vy ID automatically sets the `apiHost` to `api.<gr4vyId>.gr4vy.app` for production and `api.sandbox.<gr4vyId>.gr4vy.app` and to `embed.sandbox.<gr4vyId>.gr4vy.app` for the sandbox environment.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| `token`                   | **`Required`**      | The server-side generated JWT token used to authenticate any of the API calls.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| `environment`             | `Optional`          | `"sandbox"`, `"production"`. Defaults to `"production"`. When `"sandbox"` is provided the URL will contain `sandbox.GR4VY_ID`.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| `amount`                  | **`Required`**      | The amount to authorize or capture in the specified `currency` only.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |   
| `currency`                | **`Required`**      | A valid, active, 3-character `ISO 4217` currency code to authorize or capture the `amount` for.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| `country`                 | **`Required`**      | A valid `ISO 3166` country code.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `buyerId`                 | `Optional`          | The buyer ID for this transaction. The transaction will automatically be associated to a buyer with that ID.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| `externalIdentifier`      | `Optional`          | An optional external identifier that can be supplied. This will automatically be associated to any resource created by Gr4vy and can subsequently be used to find a resource by that ID.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| `store`                   | `Optional`          | `'ask'`, `true`, `false` - Explicitly store the payment method or ask the buyer, this is used when a buyerId is provided.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| `display`                 | `Optional`          | `all`, `addOnly`, `storedOnly`, `supportsTokenization` - Filters the payment methods to show stored methods only, new payment methods only or methods that support tokenization.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `intent`                  | `Optional`          | `authorize`, `capture` - Defines the intent of this API call. This determines the desired initial state of the transaction.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| `metadata`                | `Optional`          | An optional dictionary of key/values for transaction metadata. All values should be a string.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `paymentSource`           | `Optional`          | `installment`, `recurring` - Can be used to signal that Embed is used to capture the first transaction for a subscription or an installment. When used, `store` is implied to be `true` and `display` is implied to be `supportsTokenization`. This means that payment options that do not support tokenization are automatically hidden.                                                                                                                                                                                                                                                                                                                                                                                                                            |
| `cartItems`               | `Optional`          | An optional array of cart item objects, each object must define a `name`, `quantity`, and `unitAmount`.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| `theme`                   | `Optional`          | Theme customisation options. See [Theming](https://github.com/gr4vy/gr4vy-embed/tree/main/packages/embed#theming). The iOS SDK also contains an additional two properties within the `colors` object; `headerBackground` and `headerText`. These are used for the navigation background and forground colors.                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `buyerExternalIdentifier` | `Optional`          | An optional external ID for a Gr4vy buyer. The transaction will automatically be associated to a buyer with that external ID. If no buyer with this external ID exists then it will be ignored. This option is ignored if the `buyerId` is provided.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| `locale`                  | `Optional`          | An optional locale, this consists of a `ISO 639 Language Code` followed by an optional `ISO 3166 Country Code`, e.g. `en`, `en-gb` or `pt-br`.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| `statementDescriptor`     | `Optional`          | An optional object with information about the purchase to construct the statement information the buyer will see in their bank statement. Please note support for these fields varies across payment service providers and underlying banks, so Gr4vy can only ensure a best effort approach for each supported platform. As an example, most platforms will only support a concatenation of `name` and `description` fields, this is truncated to a length of 22 characters within embed. The object can contain `name`, `description`, `phoneNumber`, `city` and `url` properties, with string values. `phoneNumber` should be in E164 format. Gr4vy recommends avoiding characters outside the alphanumeric range and the dot (`.`) to ensure wide compatibility. |
| `requireSecurityCode`     | `Optional`          | An optional boolean which forces security code to be prompted for stored card payments.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| `shippingDetailsId`       | `Optional`          | An optional unique identifier of a set of shipping details stored for the buyer.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `merchantAccountId`       | `Optional`          | An optional merchant account ID.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `debugMode`               | `Optional`          | `true`, `false`. Defaults to `false`, this prints to the console.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |

### Step 3. Handle events from Gr4vy

When you implement `Gr4vyResultHandler` you can handle the events emitted by Gr4vy like so:

```kotlin
override fun onGr4vyResult(result: Gr4vyResult) {
        when(result) {
            is Gr4vyResult.GeneralError -> startActivity(Intent(this, FailureActivity::class.java))
            is Gr4vyResult.TransactionCreated -> startActivity(Intent(this, SuccessActivity::class.java))
            is Gr4vyResult.Cancelled -> print("User Cancelled")
        }
    }

override fun onGr4vyEvent(event: Gr4vyEvent) {
        when(event) {
            is Gr4vyResult.TransactionFailed -> print("Transaction Failed")
        }
    }
```

#### `Gr4vyResult.TransactionCreated`

Returns a data about the transaction object when the transaction was successfully created.

```json
{
  "transactionID": "8724fd24-5489-4a5d-90fd-0604df7d3b83",
  "status": "pending",
  "paymentMethodID": "17d57b9a-408d-49b8-9a97-9db382593003"
}
```

#### `Gr4vyResult.GeneralError`

Returned when the SDK encounters an error.

```json
{
  "Gr4vy Error: Failed to load"
}
```

#### `Gr4vyResult.Cancelled`

Returned when the user cancels/quits the SDK.


#### `Gr4vyEvent.TransactionFailed`

Returned when the transaction encounters an error.

```json
{
  "transactionID": "8724fd24-5489-4a5d-90fd-0604df7d3b83",
  "status": "authorization_failed",
  "paymentMethodID": "17d57b9a-408d-49b8-9a97-9db382593003"
}
```

## License

This project is provided as-is under the [MIT license](LICENSE).
