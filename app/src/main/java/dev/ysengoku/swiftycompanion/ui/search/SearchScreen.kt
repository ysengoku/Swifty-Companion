package dev.ysengoku.swiftycompanion.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ysengoku.swiftycompanion.ui.theme.BlueWhite
import dev.ysengoku.swiftycompanion.ui.theme.BrandGradient
import dev.ysengoku.swiftycompanion.ui.theme.LightColorScheme
import dev.ysengoku.swiftycompanion.ui.theme.ZenLoop

@Composable
fun SearchScreen(
    onSubmit: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column(
        Modifier
            .background(
                brush = BrandGradient
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
                    .padding(bottom = 56.dp),
                fontFamily = ZenLoop,
                fontSize = 72.sp,
                lineHeight = 56.sp,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = BlueWhite.copy(alpha = 0.7f)
                )
            )

            MaterialTheme(colorScheme = LightColorScheme) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            "Enter 42 Login to search",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BlueWhite.copy(alpha = 0.8f),
                        unfocusedContainerColor = BlueWhite.copy(alpha = 0.6f)
                    ),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                )
            
                Button(
                    onClick = { if (text.isNotBlank()) onSubmit(text) },
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                ) {
                    Text("Search")
                }
            }
        }
    }
}
