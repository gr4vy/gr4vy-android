package com.gr4vy.gr4vy_android_sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gr4vy.gr4vy_android_sample.ui.theme.GravyAndroidSDKSampleTheme
import com.gr4vy.android_sdk.*
import com.gr4vy.android_sdk.models.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.add
import kotlinx.serialization.json.JsonArray


class MainActivity : ComponentActivity(), Gr4vyResultHandler {

    private lateinit var gr4vySDK: Gr4vySDK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gr4vySDK = Gr4vySDK(activityResultRegistry, this, this)
        lifecycle.addObserver(gr4vySDK)

        setContent {
            GravyAndroidSDKSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainContent(::launchGr4vy)
                }
            }
        }
    }

    private fun launchGr4vy() {

        // TODO: Set your own token and gr4vyID here
        val token = "<TOKEN HERE>"
        val gr4vyId = "<GR4VY ID HERE>"

        val connectionOptions: Map<String, JsonElement> = mapOf(
            "forter-anti-fraud" to buildJsonObject {
                put("is_guest_buyer", JsonPrimitive(false))
            }
        )

        val items = ArrayList<CartItem>()
        val categories = ArrayList<String>()
        categories.add("test")
        categories.add("cat2")
        val item1 = CartItem("itemName", 1, 10973, discountAmount = 100, taxAmount = 0, categories = categories)
        items.add(item1)
        
        gr4vySDK.launch(
            context = this,
            gr4vyId = gr4vyId,
            environment = "sandbox",
            token = token,
            amount = 10873,
            currency = "USD",
            country = "US",
            debugMode = true,
            cartItems = items,
            theme = Gr4vyTheme(Gr4vyFonts("google:Lato, Tahoma, Arial"), Gr4vyColours("#ffffff", "#a1b0bd",
                "#fff",
                "#fff",
                "#1d334b",
                "#1d334b",
                "#2c4765",
                "#304c6a",
                "#f2f2f2",
                "#2a4159",
                "#fff",
                "#ff556a",
                "#2c4765",
                "#fff",
                "#3ea2ff",
                "#e7f2fb",
                "#0367c4",
                "#4844ff",
                "#ffffff",
                "#2c4765", "#ffffff", "#2c4765"), Gr4vyBorderWidths("thin", "thin"), Gr4vyRadii("subtle", "subtle"), Gr4vyShadows
            ("0 0 0 2px #ffffff, 0 0 0 4px #4844ff"),
            ),
            connectionOptions = connectionOptions,
        )
    }

    override fun onGr4vyResult(result: Gr4vyResult) {
        when(result) {
            is Gr4vyResult.GeneralError -> startActivity(Intent(this, FailureActivity::class.java))
            is Gr4vyResult.Cancelled -> print("User Cancelled")
            is Gr4vyResult.TransactionCreated -> startActivity(Intent(this, SuccessActivity::class.java))
        }
    }

    override fun onGr4vyEvent(event: Gr4vyEvent) {
        when(event) {
            is Gr4vyEvent.TransactionFailed -> {
                print("Transaction Failed")
            }
            is Gr4vyEvent.CardDetailsChanged -> {
                print("Card Details Changed: bin=${event.bin}, cardType=${event.cardType}, scheme=${event.scheme}")
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContent(onClick: (() -> Unit)) {

    Scaffold(topBar = { AppBar() }) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp), content = {
            OverviewText()
            Spacer(modifier = Modifier.height(8.dp))
            CheckoutItems()
            Spacer(modifier = Modifier.height(16.dp))
            CheckoutSummary()
            Spacer(modifier = Modifier.height(16.dp))
            CheckoutButton(onClick = onClick)
        })
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        title = { Text(text = "Checkout") },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "contentDescription",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun OverviewText() {
    Text(text = "OVERVIEW", color = Color.Gray, fontWeight = FontWeight.ExtraBold)
}

@Composable
fun CheckoutItems() {

    val items = listOf<CheckoutItemModel>(
        CheckoutItemModel("T-shirt", "69.99", R.drawable.tshirt),
        CheckoutItemModel("Backpack", "34.99", R.drawable.backpack)
    )

    Column {
        Divider(color = Color.Gray, thickness = 1.dp)
        items.forEach { message ->
            CheckoutItem(message)
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun CheckoutItem(item: CheckoutItemModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier
                .size(64.dp)
                .padding(vertical = 2.dp),
            painter = painterResource(item.icon),
            contentDescription = "Contact profile picture",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = item.name, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Filled.Delete,
                "Remove item",
                tint = Color.Gray
            )
        }
        Text(text = item.price)
    }
}

@Composable
fun CheckoutSummary() {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Subtotal")
            Text(text = "104.98")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Shipping costs")
            Text(text = "3.75")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Total", fontWeight = FontWeight.Bold)
            Text(text = "108.73", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CheckoutButton(onClick: (() -> Unit)) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Checkout", modifier = Modifier.padding(all = 4.dp), fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GravyAndroidSDKSampleTheme {
        MainContent(onClick = {})
    }
}

data class CheckoutItemModel(
    val name: String,
    val price: String,
    val icon: Int,
)