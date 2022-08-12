package hu.bme.aut.androidwallet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var currency by mutableStateOf("HUF")
        private set
    var transactions = mutableStateListOf(
        Transaction(
            name = "DefaultItem",
            worth = 0.0,
            currency = currency
        )
    )
}