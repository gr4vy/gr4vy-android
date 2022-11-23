# Gr4vy Android SDK

![Build Status](https://github.com/gr4vy/gr4vy-android/actions/workflows/build.yaml/badge.svg?branch=main)

![Platforms](https://img.shields.io/badge/Platforms-Android-yellowgreen?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-1.3.0-yellowgreen?style=for-the-badge)

Quickly embed Gr4vy in your Android app to store card details, authorize payments, and capture a transaction.

## Installing

gr4vy_android doesn't contain any external dependencies.

### Gradle

```gradle
repositories {
  mavenCentral()
  maven { url "https://jitpack.io" }
}

dependencies {
  implementation 'com.github.gr4vy:gr4vy-android:v1.3.0'
}
```

## Get started

### Setup

Add the following meta information to your App's manifest

```
<meta-data android:name="gr4vy-id" android:value="[GRAVY_ID]" />
<meta-data android:name="gr4vy-environment" android:value="[ENVIRONMENT]" />
```

> **Note**:
> Replace `[GR4VY_ID]` with the ID of your instance and `[ENVIRONMENT]`
> with either the `sandbox` or `production` environment.

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
        token = [TOKEN],
        amount = 1299,
        currency = "USD",
        country = "US")
```

> **Note**:
> Replace `[TOKEN]` with your JWT access token (See any of our [server-side SDKs](https://github.com/gr4vy?q=sdk) for more details.).

### Options

These are the options available in this SDK:

| Field                | Optional / Required | Description                                                                                                                                                                                                                                                                                                                               |
| -------------------- | ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --- |
| `gr4vyId`            | **`Required`**      | Provided in the manifest. The Gr4vy ID automatically sets the `apiHost` to `api.<gr4vyId>.gr4vy.app` for production and `api.sandbox.<gr4vyId>.gr4vy.app` and to `embed.sandbox.<gr4vyId>.gr4vy.app` for the sandbox environment.                                                                                                         |
| `token`              | **`Required`**      | The server-side generated JWT token used to authenticate any of the API calls.                                                                                                                                                                                                                                                            |
| `amount`             | **`Required`**      | The amount to authorize or capture in the specified `currency` only.                                                                                                                                                                                                                                                                      |     |
| `currency`           | **`Required`**      | A valid, active, 3-character `ISO 4217` currency code to authorize or capture the `amount` for.                                                                                                                                                                                                                                           |
| `country`            | **`Required`**      | A valid `ISO 3166` country code.                                                                                                                                                                                                                                                                                                          |
| `buyerId`            | `Optional`          | Provided in the manifest. The buyer ID for this transaction. The transaction will automatically be associated to a buyer with that ID.                                                                                                                                                                                                    |
| `externalIdentifier` | `Optional`          | An optional external identifier that can be supplied. This will automatically be associated to any resource created by Gr4vy and can subsequently be used to find a resource by that ID.                                                                                                                                                  |
| `store`              | `Optional`          | `'ask'`, `true`, `false` - Explicitly store the payment method or ask the buyer, this is used when a buyerId is provided.                                                                                                                                                                                                                 |
| `display`            | `Optional`          | `all`, `addOnly`, `storedOnly`, `supportsTokenization` - Filters the payment methods to show stored methods only, new payment methods only or methods that support tokenization.                                                                                                                                                          |
| `intent`             | `Optional`          | `authorize`, `capture` - Defines the intent of this API call. This determines the desired initial state of the transaction.                                                                                                                                                                                                               |
| `metadata`           | `Optional`          | An optional dictionary of key/values for transaction metadata. All values should be a string.                                                                                                                                                                                                                                             |
| `paymentSource`      | `Optional`          | `installment`, `recurring` - Can be used to signal that Embed is used to capture the first transaction for a subscription or an installment. When used, `store` is implied to be `true` and `display` is implied to be `supportsTokenization`. This means that payment options that do not support tokenization are automatically hidden. |
| `cartItems`          | `Optional`          | An optional array of cart item objects, each object must define a `name`, `quantity`, and `unitAmount`.                                                                                                                                                                                                                                   |

### Step 3. Handle events from Gr4vy

When you implement `Gr4vyResultHandler` you can handle the events emitted by Gr4vy like so:

```kotlin
override fun onGr4vyResult(result: Gr4vyResult) {
        when(result) {
            is Gr4vyResult.GeneralError -> startActivity(Intent(this, FailureActivity::class.java))
            is Gr4vyResult.TransactionCreated -> startActivity(Intent(this, SuccessActivity::class.java))
            is Gr4vyResult.TransactionFailed -> startActivity(Intent(this, FailureActivity::class.java))
        }
    }
```

#### `transactionCreated`

Returns a data about the transaction object when the transaction was successfully created.

```json
{
  "transactionID": "8724fd24-5489-4a5d-90fd-0604df7d3b83",
  "status": "pending",
  "paymentMethodID": "17d57b9a-408d-49b8-9a97-9db382593003"
}
```

#### `transactionFailed`

Returned when the transaction encounters an error.

```json
{
  "transactionID": "8724fd24-5489-4a5d-90fd-0604df7d3b83",
  "status": "pending",
  "paymentMethodID": "17d57b9a-408d-49b8-9a97-9db382593003"
}
```

#### `generalError`

Returned when the SDK encounters an error.

```json
{
  "Gr4vy Error: Failed to load"
}
```

## License

This project is provided as-is under the [MIT license](LICENSE).
