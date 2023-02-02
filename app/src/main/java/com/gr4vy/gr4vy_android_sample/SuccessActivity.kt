package com.gr4vy.gr4vy_android_sample

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gr4vy.gr4vy_android_sample.ui.theme.GravyAndroidSDKSampleTheme

class SuccessActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GravyAndroidSDKSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SuccessContent()
                }
            }
        }
    }
}

@Composable
fun SuccessContent() {

    Scaffold { padding ->
        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp), content = {
            Icon(Icons.Filled.Done, "Done")
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Thank you for shopping with us",
                color = Color.Gray,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your payment was processed successfully",
                color = Color.Gray,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "You should now receive an email with your receipt an order details",
                color = Color.Gray,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(24.dp))
            SuccessContinueButton()
        })
    }
}


@Composable
fun SuccessContinueButton() {

    val activity = (LocalContext.current as? Activity)

    Button(onClick = { activity?.finish() }, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Checkout", modifier = Modifier.padding(all = 4.dp), fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultSuccessPreview() {
    GravyAndroidSDKSampleTheme {
        SuccessContent()
    }
}