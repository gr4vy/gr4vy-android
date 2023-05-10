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

        // TODO: Set your own token, buyerID and gr4vyID here
        val token = "<TOKEN HERE>"
        val buyerId = "<BUYER ID HERE>"
        val gr4vyId = "<GR4VY ID HERE>"

        gr4vySDK.launch(
            context = this,
            gr4vyId = gr4vyId,
            token = token,
            environment = "sandbox",
            amount = 10873,
            currency = "GBP",
            country = "GB",
            buyerId = buyerId,
            debugMode = true
        )
    }

    override fun onGr4vyResult(result: Gr4vyResult) {
        when(result) {
            is Gr4vyResult.GeneralError -> startActivity(Intent(this, FailureActivity::class.java))
            is Gr4vyResult.Cancelled -> startActivity(Intent(this, CancelledActivity::class.java))
            is Gr4vyResult.TransactionCreated -> startActivity(Intent(this, SuccessActivity::class.java))
            is Gr4vyResult.TransactionFailed -> startActivity(Intent(this, FailureActivity::class.java))
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