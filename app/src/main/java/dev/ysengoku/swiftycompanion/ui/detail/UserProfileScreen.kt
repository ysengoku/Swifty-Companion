package dev.ysengoku.swiftycompanion.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import dev.ysengoku.swiftycompanion.R
import dev.ysengoku.swiftycompanion.ui.theme.ErrorRed
import dev.ysengoku.swiftycompanion.ui.theme.Green
import dev.ysengoku.swiftycompanion.ui.theme.LightGreen
import dev.ysengoku.swiftycompanion.ui.theme.OceanBlue

@Composable
@Suppress("FunctionName")
fun UserProfileScreen (
    user: DetailUiModel,
    selectedCursusId: Int?
) {
    var selectedCursus by remember(user, selectedCursusId) {
        mutableStateOf(user.cursus.find { it.id == selectedCursusId } ?: user.cursus.first())
    }

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
        CursusInfo(
            cursus = user.cursus,
            selectedCursus = selectedCursus,
            onCursusSelected = { selectedCursus = it }
        )
        Spacer(Modifier.size(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            ProjectList(
                projects = user.projects,
                selectedCursusId = selectedCursus.id
            )
            SkillList(skills = selectedCursus.skills)
        }
    }
}

@Composable
@Suppress("FunctionName")
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
                fontWeight = FontWeight.Medium,
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
@Suppress("FunctionName")
fun CursusInfo(
    cursus: List<CursusUi>,
    selectedCursus: CursusUi,
    onCursusSelected: (CursusUi) -> Unit
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
                Text(selectedCursus.name, /*fontSize = 14.sp*/)
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
                            value = selectedCursus.name,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(/*fontSize = 14.sp*/)
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
                                    onCursusSelected(c)
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
            Text(selectedCursus.grade ?: "N/A", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row {
            Text(
                "Level ${selectedCursus.level}",
                color = OceanBlue,
                fontWeight = FontWeight.Medium
            )
            Text("  -  ${selectedCursus.percentage} %", fontSize = 14.sp)
        }
        LinearProgressIndicator(
            progress = { selectedCursus.percentage / 100f },
            color = OceanBlue,
            trackColor = LightGreen.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(8.dp)
        )
    }
}

@Suppress("FunctionName")
fun LazyListScope.ProjectList(
    projects: List<ProjectUi>,
    selectedCursusId: Int?
) {
    val filtered = projects.filter { it.cursusId == selectedCursusId }

    item {
        Text(
            "Projects:",
            color = OceanBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
    }

    items(filtered, key = { it.id }) { project ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(
                project.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (project.validated) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = if (project.validated) Green else ErrorRed
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    project.finalMark.toString(),
                    color = if (project.validated) Green else ErrorRed
                )
            }
        }
    }
}

@Suppress("FunctionName")
fun LazyListScope.SkillList(
    skills: List<SkillUi>
) {
    item {
        Text(
            "Skills:",
            color = OceanBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
    }

    items(skills, key = { it.id }) { skill ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    skill.name,
                    /*fontSize = 14.sp,*/
                    modifier = Modifier.weight(1f)
                )
                Text(
                    skill.level.toString(),
                    /*fontSize = 14.sp,*/
                )
            }
            LinearProgressIndicator(
                progress = { skill.level / 20f },
                color = Green,
                trackColor = LightGreen.copy(alpha = 0.1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(4.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}
