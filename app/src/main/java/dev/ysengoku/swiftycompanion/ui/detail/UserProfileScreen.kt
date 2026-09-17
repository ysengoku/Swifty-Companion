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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            user.image,
            user.displayname ?: user.login,
            user.title ?: user.login,
            user.campus,
            user.groups
        )
        CursusInfo(
            cursus = user.cursus,
            selectedCursus = selectedCursus,
            onCursusSelected = { selectedCursus = it }
        )
        ProjectList(
                projects = user.projects,
                selectedCursusId = selectedCursus.id
        )
        SkillList(skills = selectedCursus.skills)
    }
}

@Suppress("FunctionName")
fun LazyListScope.ProfileHeader(
    imagePath: String?,
    displayname: String,
    title: String,
    campus: CampusUi,
    groups: List<String>
) {
    item {
        Row {
            ProfilePicture(imagePath)
            Spacer(Modifier.size(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    displayname,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                Campus(campus)
                Spacer(Modifier.size(8.dp))
                Groups(groups)
            }
        }
    }
}

@Composable
private fun ProfilePicture(imagePath: String?) {
    AsyncImage(
        model = imagePath,
        contentDescription = "Profile picture",
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.default_picture)
    )
}

@Composable
private fun Campus(campus: CampusUi) {
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
            style = MaterialTheme.typography.labelMedium,
            color = OceanBlue
        )
    }
}

@Composable
private fun Groups(groups: List<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        groups.forEach { g ->
            GroupBadge(g)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun GroupBadge(label: String) {
    Surface(
        color = LightGreen.copy(alpha = 0.2f),
        shape = RoundedCornerShape(20)
    ) {
        Text(
            label,
            color = OceanBlue,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
fun LazyListScope.CursusInfo(
    cursus: List<CursusUi>,
    selectedCursus: CursusUi,
    onCursusSelected: (CursusUi) -> Unit
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            CursusSelector(cursus, selectedCursus.name, onCursusSelected)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Grade:  ",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(selectedCursus.grade ?: "N/A", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.size(16.dp))
            CursusLevel(selectedCursus.level, selectedCursus.percentage)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CursusSelector(
    cursus: List<CursusUi>,
    cursusName: String,
    onCursusSelected: (CursusUi) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "Cursus:  ",
            style = MaterialTheme.typography.labelSmall
        )
        if (cursus.size == 1) {
            Text(
                cursusName,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 18.sp
            )
        } else {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                Row(
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = cursusName,
                        onValueChange = {},
                        readOnly = true,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f, fill = false)
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
                            text = {
                                Text(
                                    c.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )},
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
}

@Composable
private fun CursusLevel(
    level: Int,
    percentage: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "Level ${level}",
            color = OceanBlue,
            style = MaterialTheme.typography.titleMedium
        )
        Text("  -  ${percentage} %", fontSize = 14.sp)
    }
    LinearProgressIndicator(
        progress = { percentage / 100f },
        color = OceanBlue,
        trackColor = LightGreen.copy(alpha = 0.1f),
        strokeCap = StrokeCap.Round,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(8.dp)
    )
}

@Suppress("FunctionName")
fun LazyListScope.ProjectList(
    projects: List<ProjectUi>,
    selectedCursusId: Int?
) {
    val filtered = projects.filter { it.cursusId == selectedCursusId }

    item {
        SectionCard(
            title = "Projects",
            showExpandIcon = filtered.isNotEmpty(),
        ) {
            if (filtered.isNotEmpty()) {
                filtered.forEachIndexed { index, project ->
                    ProjectRow(project)
                }
            } else {
                Text(
                    "No projects completed yet.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun ProjectRow(project: ProjectUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            project.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 4.dp)
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
                color = if (project.validated) Green else ErrorRed,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }
    }
}

@Suppress("FunctionName")
fun LazyListScope.SkillList(
    skills: List<SkillUi>
) {
    item {
        SectionCard(
            title = "Skills",
            showExpandIcon = skills.isNotEmpty(),
        ) {
            if (skills.isNotEmpty()) {
                skills.forEachIndexed { index, skill ->
                    SkillRow(skill)
                }
            } else {
                Text(
                    "No skills yet",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun SkillRow(skill: SkillUi) {
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
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
            Text(
                skill.level.toString(),
                modifier = Modifier
                    .padding(end = 16.dp)
            )
        }
        LinearProgressIndicator(
            progress = { skill.level / 20f },
            color = Green,
            trackColor = LightGreen.copy(alpha = 0.1f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                .height(4.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
    }
}
