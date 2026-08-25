package dev.ysengoku.swiftycompanion.ui.detail

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(
  login: String,
  viewModel: DetailViewModel = viewModel(factory = DetailViewModel.Factory),
  onBack: () -> Unit) {
}
