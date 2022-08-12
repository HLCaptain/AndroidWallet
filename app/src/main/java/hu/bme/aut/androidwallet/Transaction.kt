package hu.bme.aut.androidwallet

data class Transaction(
    val name: String,
    val worth: Double,
    val currency: String
) {
    val isExpense: Boolean get() = worth < 0.0
}
