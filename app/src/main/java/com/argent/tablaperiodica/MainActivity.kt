package com.argent.tablaperiodica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.tablaperiodica.ui.theme.TablaPeriodicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TablaPeriodicaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.white)
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val ctxt= LocalContext.current;
    val hptc= LocalHapticFeedback.current;
    Column(Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally){

        Image(painter = painterResource(id = R.drawable.elemento), contentDescription = null, modifier = Modifier.padding(top=60.dp).size(230.dp))
        Text(text = "TABLA PERIODICA", style = MaterialTheme.typography.body2,color= colorResource(R.color.IndigoCSTM), fontSize = 40.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.weight(0.75f))
        Button(onClick = {
            val intent=Intent(ctxt,TablaActivity::class.java);
            ctxt.startActivity(intent)
                         },
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.BlueCSTM)),
            modifier= Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(150.dp)
                .clip(CircleShape)) {
            Text(text = "INICIO", style = MaterialTheme.typography.body2,color= colorResource(R.color.WhiteCSTM), fontSize = 40.sp)
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreePreview() {
    TablaPeriodicaTheme {
        MainScreen()
    }
}