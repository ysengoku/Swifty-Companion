package dev.ysengoku.swiftycompanion.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import coil3.compose.AsyncImage
import dev.ysengoku.swiftycompanion.R
import dev.ysengoku.swiftycompanion.ui.theme.OceanBlue

@Composable
fun UserProfileScreen (
    user: DetailUiModel,
    selectedCursusId: Int?
) {
    var selectedCursus = user.cursus.find {  it.id == selectedCursusId } ?: user.cursus.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            user.image,
            user.displayname ?: user.login,
            user.title ?: user.login,
            user.campus
        )
        CursusInfo(user.cursus, selectedCursus)
    }
}

@Composable
fun ProfileHeader(
    imagePath: String?,
    displayname: String,
    title: String,
    campus: CampusUi,
) {
    Row {
        AsyncImage(
            model = imagePath,
            contentDescription = "Profile picture",
            modifier = Modifier
              .size(100.dp)
              .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.default_picture),
        )

        Spacer(Modifier.size(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                displayname,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Text(
                title,
                fontSize = 14.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = OceanBlue
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "${campus.name}, ${campus.country}",
                    fontSize = 14.sp,
                    color = OceanBlue
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursusInfo(
    cursus: List<CursusUi>,
    selectedCursus: CursusUi
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Cursus:  ",
                fontSize = 12.sp
            )
            if (cursus.size == 1) {
                Text(selectedCursus.name, fontSize = 14.sp)
            } else {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    Row(
                        modifier = Modifier.menuAnchor(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = selectedCursus.name, // TODO
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 14.sp)
                        )
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded,
                            modifier = Modifier.alpha(0.6f)
                        )
                    }
                    ExposedDropdownMenu(
                          expanded = expanded,
                          onDismissRequest = { expanded = false }
                    ) {
                        cursus.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(c.name) },
                                onClick = {
                                    // TODO: Handle selection
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        Row {
            Text("Grade:  ", fontSize = 12.sp)
            Text("${selectedCursus.grade ?: "N/A"}", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text("Level ${selectedCursus.level}")
        LinearProgressIndicator(
            progress = { selectedCursus.percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}