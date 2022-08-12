package hu.bme.aut.androidwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.androidwallet.ui.theme.AndroidWalletTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidWalletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    var isExpended by remember { mutableStateOf(false) }
                    IconButton(onClick = { isExpended = !isExpended }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = isExpended,
                        onDismissRequest = { isExpended = !isExpended }) {
                        DropdownMenuItem(
                            text = { Text(text = "Delete all") },
                            onClick = { viewModel.transactions.clear() })
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TransactionAdder(snackbarHostState)
            TransactionList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAdder(
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        var name by remember { mutableStateOf("") }
        var worth by remember { mutableStateOf("") }
        Row {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    label = { Text("Transaction name") },
                    modifier = Modifier
                        .weight(2f),
                    value = name,
                    onValueChange = { name = it }
                )
                TextField(
                    label = { Text("Cost") },
                    modifier = Modifier
                        .weight(1f),
                    value = worth,
                    onValueChange = { worth = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            Text(
                text = viewModel.currency
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.End
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            val coroutineScope = rememberCoroutineScope()
            Button(
                onClick = {
                    if (name.isEmpty() || worth.toDoubleOrNull() == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please enter data on both fields!",
                                duration = SnackbarDuration.Short,
                            )
                        }
                    } else {
                        viewModel.transactions.add(
                            Transaction(
                                name = name,
                                worth = worth.toDoubleOrNull() ?: 0.0,
                                currency = viewModel.currency
                            )
                        )
                    }
                }
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun TransactionList(
    viewModel: MainViewModel = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val items = viewModel.transactions
        items(items) {
            TransactionCard(item = it)
        }
    }
}

@Composable
fun TransactionCard(
    item: Transaction
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = if (item.isExpense) "Expense" else "Income",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(96.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "${item.worth} ${item.currency}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}