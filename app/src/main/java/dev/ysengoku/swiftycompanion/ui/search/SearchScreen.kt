package dev.ysengoku.swiftycompanion.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import dev.ysengoku.swiftycompanion.ui.theme.Navy
import dev.ysengoku.swiftycompanion.ui.theme.OceanBlue
import dev.ysengoku.swiftycompanion.ui.theme.LightGreen
import dev.ysengoku.swiftycompanion.ui.theme.Asimovian

@Composable
fun SearchScreen(
    onSubmit: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column(
        Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightGreen, OceanBlue, Navy)
                )
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.padding(16.dp),
        ) {
            Text(
                "Swifty\n Companion",
                Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = 80.dp),
                fontFamily = Asimovian,
                fontSize = 56.sp,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White.copy(alpha = 0.6f)
                )
            )

            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter 42 Login to search") },
            )
            
            Button(
                onClick = { onSubmit(text) },
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text("Search")
                // TODO: Handle empty input
            }
        }
    }
}
