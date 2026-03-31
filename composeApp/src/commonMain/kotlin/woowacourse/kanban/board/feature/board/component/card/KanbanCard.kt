package woowacourse.kanban.board.feature.board.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TaskStatus

@Composable
fun KanbanCard(
    title: String,
    crewName: String,
    modifier: Modifier = Modifier,
    tags: List<Tag> = emptyList(),
    description: String? = null,
    crewImage: DrawableResource? = null,
    onDragStart: () -> Unit = {},
    onDragChange: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
) {
    var cardWindowPosition by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = modifier
            .width(286.dp)
            .onGloballyPositioned { cardWindowPosition = it.positionInWindow() }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDrag = { change, _ ->
                        change.consume()
                        onDragChange(cardWindowPosition + change.position)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragCancel() },
                )
            }
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(Dp.Hairline, Color.Gray, RoundedCornerShape(10.dp))
            .padding(17.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (!description.isNullOrBlank()) {
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (tags.isNotEmpty()) {
            KanbanCardTags(tags = tags)
        }

        HorizontalDivider(thickness = Dp.Hairline, color = Color.LightGray)

        if (crewImage != null) {
            KanbanCardProfile(
                crewName = crewName,
                crewImage = crewImage,
            )
        } else {
            KanbanCardProfile(
                crewName = crewName,
            )
        }
    }
}

@Composable
private fun KanbanCardTags(tags: List<Tag>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        tags.forEach { tag -> TagChip(name = tag.value) }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun KanbanCardPreview_Optional() {
    val commonTitle = "LazyColumn 컴포넌트 구현"
    val commonDescription = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다."
    val tags = listOf(Tag("컴포넌트"), Tag("성능"))
    val commonCrewName = "아키"

    Row(
        modifier = Modifier
            .padding(12.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val fullTask = KanbanTask(
            title = commonTitle,
            description = commonDescription,
            tags = tags,
            crewName = commonCrewName,
            status = TaskStatus.TODO,
        )
        KanbanCard(
            title = fullTask.title,
            crewName = fullTask.crewName,
            tags = fullTask.tags,
            description = fullTask.description,
        )

        val noDescriptionTask = KanbanTask(
            title = commonTitle,
            tags = tags,
            crewName = commonCrewName,
            status = TaskStatus.TODO,
        )
        KanbanCard(
            title = noDescriptionTask.title,
            crewName = noDescriptionTask.crewName,
            tags = noDescriptionTask.tags,
        )

        val noTagsTask = KanbanTask(
            title = commonTitle,
            description = commonDescription,
            crewName = commonCrewName,
            status = TaskStatus.TODO,
        )
        KanbanCard(
            title = noTagsTask.title,
            crewName = noTagsTask.crewName,
            description = noTagsTask.description,
        )

        val minimalTask = KanbanTask(
            title = commonTitle,
            crewName = commonCrewName,
            status = TaskStatus.TODO,
        )
        KanbanCard(
            title = minimalTask.title,
            crewName = minimalTask.crewName,
        )
    }
}

@Preview
@Composable
private fun KanbanCardPreview_Max() {
    Box(modifier = Modifier.padding(12.dp)) {
        val maxTask = KanbanTask(
            title = "너무너무 긴 제목은 한 줄까지만 노출합니다".repeat(3),
            description = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.".repeat(3),
            tags = listOf(Tag("컴포넌트"), Tag("성능"), Tag("긴 태그"), Tag("최대로"), Tag("5자까지")),
            crewName = "아키".repeat(10),
            status = TaskStatus.TODO,
        )
        KanbanCard(
            title = maxTask.title,
            crewName = maxTask.crewName,
            tags = maxTask.tags,
            description = maxTask.description,
        )
    }
}
