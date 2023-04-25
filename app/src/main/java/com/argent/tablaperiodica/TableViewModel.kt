package com.argent.tablaperiodica

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TableViewModel: ViewModel() {
    final val TOTAL=118;
    private val _estadoTabla= MutableStateFlow(TablaUIState())
    val estadoTabla: StateFlow<TablaUIState> = _estadoTabla.asStateFlow()

    fun setListaOriginal(lista:List<ElementoQuimico>){
        _estadoTabla.update { x -> x.copy(ListaOriginal = lista, ListaMostrar = lista) }
    }

    fun filtrar(familia:String,letra:String,estado:String){
        var lista=_estadoTabla.value.ListaOriginal
        lista.toMutableList()
        if(!familia.equals("ALL")) {
            lista = lista.filter { (it.familia.equals(familia)) }
        }

        if(!letra.equals("ALL")){
            lista=lista.filter { it.nombre.trim().startsWith(letra)}
        }
        if(!estado.equals("ALL")){
            lista=lista.filter { it.estado.equals(estado) }
        }
        lista.toList()
        _estadoTabla.update { x -> x.copy(ListaFiltrada = lista, ListaMostrar = lista) }
    }

    fun noFiltrar(){
        _estadoTabla.update { x -> x.copy(ListaFiltrada = x.ListaOriginal, ListaMostrar = x.ListaOriginal) }
    }
}

data class TablaUIState(
    val ListaOriginal: List<ElementoQuimico> =listOf(),
    val ListaFiltrada: List<ElementoQuimico> =listOf(),
    val ListaMostrar: List<ElementoQuimico> =listOf()
)