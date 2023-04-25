package com.argent.tablaperiodica

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.tablaperiodica.ui.theme.TablaPeriodicaTheme

class ElementoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var datos=intent.getStringExtra("elemento")
        if (datos==null){
            datos="1, H , Hydrogen, 1.00794(4),FFFFFF,1s1 ,2.2,37,,120,1312,-73,-1 |  1,gas,diatomic,14,20,0.0000899,nonmetal,1766,,"
        }
        setContent {
            TablaPeriodicaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.white)
                ) {
                    ElementoScreen(datos)
                }
            }
        }
    }
}

@Composable
fun ElementoScreen(raw: String) {
    val ctxt = LocalContext.current;
    val datos = raw.split(",").toTypedArray()
    val lista= listOf("Número atómico","Símbolo","Nombre","Masa Atómica","Color Cpk","Configuración Electrónica","Electronegatividad","Radio Atómico","Radio de iones","Radio de vanDelWaals","Energía de Ionización","Afinidad de Electrones","Estados de Oxidación", "Estado Estándar","Tipo de Enlace","Punto de Fusión","Punto de Ebullición","Densidad","Familia/Bloque","Año Descubierto")
    var src: Int
    var col: Int
    when (datos[13]) {
        "solid" -> {
            src = R.drawable.solid;col = R.color.RedCSTM
        }
        "liquid" -> {
            src = R.drawable.liquid;col = R.color.BlueCSTM
        }
        "gas" -> {
            src = R.drawable.gas;col = R.color.GreenCSTM
        }
        else -> {
            src = R.drawable.syntetic;col = R.color.YellowCSTM
        }
    }
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(colorResource(R.color.CyanCSTM))
        ) {
            Button(
                onClick = { val cas = ctxt as Activity;cas?.finish() },
                modifier = Modifier
                    .padding(start = 10.dp, top = 20.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.BlueCSTM))
            ) {
                Text(
                    text = "<",
                    style = MaterialTheme.typography.body2,
                    color = colorResource(R.color.WhiteCSTM),
                    fontSize = 15.sp
                )
            }
            Text(
                text = "ELEMENTO",
                style = MaterialTheme.typography.body2,
                color = colorResource(R.color.WhiteCSTM),
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .background(colorResource(R.color.IndigoCSTM), shape = RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(src), contentDescription = null, modifier = Modifier
                    .size(120.dp)
                    .padding(all = 10.dp)
            )
            Text(
                text = datos[2],
                style = MaterialTheme.typography.body2,
                color = colorResource(col),
                fontSize = 35.sp, modifier = Modifier.padding(top=30.dp)
            )
        }
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .background(color = colorResource(R.color.BlueCSTM), shape = RoundedCornerShape(20.dp))){
        itemsIndexed(lista) {index,item ->
                Text(text = "🔹 ${lista[index]}:", style = MaterialTheme.typography.body2 , fontSize = 20.sp, color= colorResource(id = R.color.CyanCSTM))
            Text(text = "\t${datos[index]}", style = MaterialTheme.typography.body2 , fontSize = 20.sp, color= colorResource(id = R.color.WhiteCSTM))

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    TablaPeriodicaTheme {
        ElementoScreen("1, H , Hydrogen, 1.00794(4),FFFFFF,1s1 ,2.2,37,,120,1312,-73,-1 |  1,gas,diatomic,14,20,0.0000899,nonmetal,1766,,")
    }
}