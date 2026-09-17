package dev.ysengoku.swiftycompanion.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ysengoku.swiftycompanion.ui.theme.LightGreen
import dev.ysengoku.swiftycompanion.ui.theme.OceanBlue

object SectionCardDefaults {
    @Composable
    fun colors() = CardDefaults.cardColors(
        containerColor = LightGreen.copy(alpha = 0.05f),
    )
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    title: String,
    showExpandIcon: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = SectionCardDefaults.colors(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        var expanded by remember { mutableStateOf(true) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(start = 16.dp, end = 12.dp, top = 8.dp, bottom = 12.dp)
        ) {
            Text(
                "${title}: ",
                color = OceanBlue,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
            )
            if (showExpandIcon) {
                ExpandIcon(expanded)
            }
        }

        if (expanded) {
            Spacer(modifier = Modifier.size(4.dp))
            content()
        }
    }
}

@Composable
private fun ExpandIcon(expanded: Boolean) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 250),
        label = "expandIconRotation"
    )
    Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        tint = OceanBlue.copy(alpha = 0.5f),
        contentDescription = if (expanded) "¨Collapse" else "Expand",
        modifier = Modifier
            .rotate(rotation)
    )
}
