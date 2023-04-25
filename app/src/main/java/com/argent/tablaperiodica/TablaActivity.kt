@file:OptIn(ExperimentalMaterialApi::class)

package com.argent.tablaperiodica

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.tablaperiodica.ui.theme.TablaPeriodicaTheme

class TablaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tvm by viewModels<TableViewModel>()
        tvm.setListaOriginal(LeerArchivo())
        setContent {
            TablaPeriodicaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.white)
                ) {
                    TablaScreen(tvm)
                }
            }
        }
    }

    fun LeerArchivo():List<ElementoQuimico>{
        val lista= mutableListOf<ElementoQuimico>()
        val input=resources.openRawResource(R.raw.tabla)
        val bf=input.bufferedReader()
        var linea=bf.readLine()
        while (linea!=null){
            val datos=linea.split(",").toTypedArray()
            lista.add(ElementoQuimico(datos[0],datos[1],datos[2],datos[3],datos[13],datos[12],datos[18],linea))
            linea=bf.readLine()
        }
        lista.removeAt(0)
        //Toast.makeText(this,"ArchivoLeido",Toast.LENGTH_SHORT).show()
        return lista
    }
}

@Composable
fun TablaScreen(tvm: TableViewModel) {
    val ctxt= LocalContext.current;
    val hptc= LocalHapticFeedback.current;
    val ts by tvm.estadoTabla.collectAsState()
    val listaEstados=arrayOf("ALL","solid", "liquid", "gas", "synthetic")
    var filtroEstado="ALL"
    val listaFamilia=arrayOf("ALL","nonmetal","noble gas","alkali metal","alkaline earth metal","metalloid","halogen","metal","transition metal","lanthanoid","actinoid")
    var filtroFamilia="ALL"
    val listaLetras= arrayOf("ALL","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
    var filtroLetra="ALL"
    Column(Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally){
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(colorResource(R.color.CyanCSTM))) {
            Button(onClick = {val cas=ctxt as Activity;cas?.finish()},
                    modifier= Modifier
                    .padding(start=10.dp,top=20.dp)
                    .clip(CircleShape),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.BlueCSTM))){
                Text(text = "<", style = MaterialTheme.typography.body2,color= colorResource(R.color.WhiteCSTM), fontSize = 15.sp)
            }
            Text(text = "TABLA PERIODICA", style = MaterialTheme.typography.body2,color= colorResource(R.color.WhiteCSTM), fontSize = 25.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top=20.dp))
        }
        val scrollState = rememberScrollState()
        Row(Modifier
                .fillMaxWidth().horizontalScroll(scrollState)
                .height(80.dp)
                .background(colorResource(R.color.BlueCSTM))){
            ComboBox(listaFamilia, onItem = {filtroFamilia=it;tvm.filtrar(filtroFamilia, filtroLetra, filtroEstado)},160)
            ComboBox(listaLetras, onItem = {filtroLetra=it;tvm.filtrar(filtroFamilia, filtroLetra, filtroEstado)},100)
            ComboBox(listaEstados, onItem = {filtroEstado=it;tvm.filtrar(filtroFamilia, filtroLetra, filtroEstado)},160)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top=10.dp)){
            itemsIndexed(ts.ListaMostrar) { index, item ->
                Elemento(item, ctxt, hptc, index);
            }
        }
        
    }
}
@Composable
fun ComboBox(items:Array<String>,onItem:(String) -> Unit,tam:Int) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(items[0]) }

    Box(
        modifier = Modifier
            .padding(5.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(color = colorResource(R.color.WhiteCSTM)),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.width(tam.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        content= { Text(text = item, style = MaterialTheme.typography.body2, color = colorResource(id = R.color.WhiteCSTM))},
                        onClick = {
                            selectedText = item
                            onItem(item)
                            expanded = false
//                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Elemento(item: ElementoQuimico, ctxt: Context, hptc: HapticFeedback,x:Int){

    val colorEstado=when(item.estado){
        "gas"->R.color.GreenCSTM
        "solid"-> R.color.RedCSTM
        "liquid"-> R.color.BlueCSTM
        else -> R.color.YellowCSTM
    }
    Row(modifier= Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
        .background(colorResource(R.color.IndigoCSTM), shape = RoundedCornerShape(20.dp))
        .clickable {
//            Toast
//                .makeText(ctxt, item.raw, Toast.LENGTH_SHORT)
//                .show()
            var intent=Intent(ctxt,ElementoActivity::class.java)
            intent.putExtra("elemento",item.raw)
            ctxt.startActivity(intent)
        }) {
        Box() {
            Image(painter = painterResource(id = R.drawable.molecule), contentDescription =null, modifier = Modifier.size(90.dp), colorFilter = ColorFilter.tint(
                colorResource(colorEstado)))
            Text(text = item.simbolo, style = MaterialTheme.typography.body2,color= colorResource(R.color.WhiteCSTM), fontSize = 40.sp)
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(){
            Row() {
                Text(text = item.nombre, style = MaterialTheme.typography.body2,color= colorResource(R.color.WhiteCSTM), fontSize = 25.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = item.numero, style = MaterialTheme.typography.body2,color= colorResource(R.color.BlueCSTM), fontSize = 25.sp, modifier = Modifier.padding(end = 10.dp))
            }
            Text(text = "⚖️ ${item.peso}", style = MaterialTheme.typography.body2,color= colorResource(R.color.WhiteCSTM), fontSize = 15.sp)
            Text(text = "🔗 ${item.oxidacion}", style = MaterialTheme.typography.body2,color= colorResource(R.color.WhiteCSTM), fontSize = 15.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TablaScreenPreview() {
    TablaPeriodicaTheme {
        TablaScreen(TableViewModel())
    }
}

data class ElementoQuimico(var numero:String,var simbolo:String,var nombre:String,var peso:String,var estado:String,var oxidacion:String,var familia:String,var raw:String)