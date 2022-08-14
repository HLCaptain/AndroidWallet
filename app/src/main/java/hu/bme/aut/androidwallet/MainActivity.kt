package hu.bme.aut.androidwallet

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    var isExpended by remember { mutableStateOf(false) }
                    IconButton(onClick = { isExpended = !isExpended }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.more_options)
                        )
                    }
                    DropdownMenu(
                        expanded = isExpended,
                        onDismissRequest = { isExpended = !isExpended }) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.delete_all))
                            },
                            onClick = {
                                viewModel.transactions.clear()
                                isExpended = !isExpended
                            })
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding(),
                    start = 8.dp,
                    end = 8.dp
                )
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
    context: Context = LocalContext.current,
    viewModel: MainViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        var name by remember { mutableStateOf("") }
        var worth by remember { mutableStateOf("") }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                label = { Text(stringResource(R.string.transaction_name)) },
                modifier = Modifier.weight(2f),
                value = name,
                onValueChange = { name = it }
            )
            TextField(
                label = { Text(stringResource(R.string.cost)) },
                modifier = Modifier.weight(1f),
                value = worth,
                onValueChange = { worth = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                text = viewModel.currency,
                style = MaterialTheme.typography.labelLarge
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.End
            ),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.summary) + ": " +
                        viewModel.transactions.sumOf { it.worth } + " " +
                        viewModel.currency,
                style = MaterialTheme.typography.labelLarge
            )
            val coroutineScope = rememberCoroutineScope()
            Button(
                onClick = {
                    if (name.isEmpty() || worth.toDoubleOrNull() == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.getString(
                                    R.string.please_enter_data_on_both_fields
                                ),
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
                Text(text = stringResource(R.string.save))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionList(
    viewModel: MainViewModel = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = true
    ) {
        val items = viewModel.transactions
        items(items) {
            AnimatedVisibility(
                visible = items.contains(it),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(modifier = Modifier.animateItemPlacement()) {
                    TransactionCard(item = it)
                }
            }
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
            val (painter, color) = if (item.isExpense) {
                painterResource(R.drawable.ic_round_money_off_24) to
                        MaterialTheme.colorScheme.error
            } else {
                painterResource(R.drawable.ic_round_attach_money_24) to
                        MaterialTheme.colorScheme.primary
            }
            Image(
                painter = painter,
                contentDescription = if (item.isExpense) {
                    stringResource(R.string.expense)
                } else {
                    stringResource(R.string.income)
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp),
                colorFilter = ColorFilter.tint(color)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "${item.worth} ${item.currency}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = color
                )
            }
        }
    }
}
