package dev.ysengoku.swiftycompanion.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.navigation.compose.rememberNavController

@Composable
fun SearchScreen(onSubmit: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter 42 Login to search") }
        )
        Button(onClick = { onSubmit(text) }) {
            Text("Search")
            // TODO: Handle empty input
        }
    }
}
