package dev.ysengoku.swiftycompanion.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size  
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ysengoku.swiftycompanion.ui.theme.Navy
import dev.ysengoku.swiftycompanion.ui.theme.OceanBlue
import dev.ysengoku.swiftycompanion.ui.theme.LightGreen
import dev.ysengoku.swiftycompanion.ui.theme.BlueWhite
import dev.ysengoku.swiftycompanion.ui.theme.ErrorRed
import dev.ysengoku.swiftycompanion.R

@Composable
fun DetailScreen (
  login: String,
  viewModel: DetailViewModel = viewModel(factory = DetailViewModel.Factory),
  onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val loadState = state.loadState

    Scaffold (
        topBar = {
            if (loadState !is LoadState.Loading) {
                DetailScreenTopBar(
                    login = (loadState as? LoadState.Success)?.detail?.login,
                    onBack
                )
            }
        }
    ) { innerPadding ->
        Column (
            Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            LightGreen.copy(alpha = 0.1f),
                            OceanBlue.copy(alpha = 0.1f),
                            Navy.copy(alpha = 0.1f)
                        )
                    )
                )
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {            
            when (loadState) {
                is LoadState.Loading -> CircularProgressIndicator()
                is LoadState.Success -> Text(loadState.detail.login)
                is LoadState.Error -> ErrorScreen(loadState.message, onBack)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailScreenTopBar (
    login: String?,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = login ?: "",
                color = BlueWhite,
                overflow = TextOverflow.Ellipsis
            )
        },
        Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(OceanBlue, LightGreen)
                )
            ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    tint = BlueWhite,
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun ErrorScreen (
    message: String,
    onBack: () -> Unit,
) {
    Icon(
        painter = painterResource(id = R.drawable.sentiment_very_dissatisfied),
        contentDescription = "Error",
        modifier = Modifier
            .padding(bottom = 16.dp)
            .size(176.dp),
        tint = OceanBlue,
    )   

    Text(
        text = message,
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        color = ErrorRed,
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    )

    Button(
        onClick = onBack,
        Modifier
            .padding(top = 16.dp),
    ) {
        Text("Back to Search Form")
    }
}
