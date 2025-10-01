package ca.gbc.comp3074.lab4week5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.lab4week5.ui.theme.Lab4Week5Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab4Week5Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        AddComponent()
                        ItemsList()
                    }
                }
            }
        }
    }
}

class ItemViewModel(private val repository: ItemsDatabase) : ViewModel() {
    val allItems: StateFlow<List<Item>> = repository.itemDao().getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

@Composable
fun MyListItem(item: Item) {
    Card(modifier = Modifier.padding(10.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = item.name)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = item.quantity.toString())
        }
    }
}

@Composable
fun ItemsList(
    viewModel: ItemViewModel = ItemViewModel(
        ItemsDatabase.getDatabase(LocalContext.current)
    )
) {
    val data by viewModel.allItems.collectAsStateWithLifecycle()
    LazyColumn {
        items(data) { item ->
            MyListItem(item)
        }
    }
}

@Composable
fun AddComponent() {
    var name by remember { mutableStateOf("") }
    var quantityNum by remember { mutableStateOf(0) }
    var quantity by remember { mutableStateOf("") }

    val db = ItemsDatabase.getDatabase(LocalContext.current)

    Column(modifier = Modifier.padding(10.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = quantity,
            onValueChange = { newVal ->
                quantity = newVal.filter { it.isDigit() }
                quantityNum = quantity.toIntOrNull() ?: 0
            },
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    scope.launch {
                        db.itemDao().insert(Item(name = name, quantity = quantityNum))
                        name = ""
                        quantity = ""
                        quantityNum = 0
                    }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add")
        }
    }
}
