package woowacourse.kanban.board.feature.board.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.component.dialog.component.AssigneeOptionCard
import woowacourse.kanban.board.feature.board.component.dialog.component.StatusOptionCard
import woowacourse.kanban.board.feature.board.component.dialog.component.TaskDialogButton
import woowacourse.kanban.board.feature.board.component.dialog.component.TaskDialogTextField
import woowacourse.kanban.board.feature.board.component.dialog.component.TaskDialogTopAppBar
import woowacourse.kanban.board.feature.board.component.dialog.component.TaskFieldLabel
import woowacourse.kanban.board.feature.board.mapper.toDisplayText

@Composable
fun TaskDialogContent(
    topAppBarTitle: String,
    titleValue: String,
    isTitleError: Boolean,
    onTitleChanged: (String) -> Unit,
    descriptionValue: String,
    onDescriptionChanged: (String) -> Unit,
    tagValue: String,
    isTagCountError: Boolean,
    isTagFormatError: Boolean,
    onTagChanged: (String) -> Unit,
    statuses: List<TaskStatus>,
    selectedStatusIndex: Int,
    onStatusChanged: (Int) -> Unit,
    assignees: List<String>,
    selectedAssigneeIndex: Int,
    onAssigneeChanged: (Int) -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope. () -> Unit,
) {
    val isTagError = isTagCountError || isTagFormatError
    val tagErrorMessage = when {
        isTagCountError -> "태그는 5자 이내로 5개까지만 등록할 수 있습니다."
        isTagFormatError -> "태그 형식이 올바르지 않습니다."
        else -> "5자 이내의 태그를 최대 5개까지 등록할 수 있습니다."
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(vertical = 28.dp, horizontal = 24.dp)
            .width(672.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        TaskDialogTopAppBar(
            title = topAppBarTitle,
            onClick = onDismissClick,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        HorizontalDivider(
            color = Color.Black,
            thickness = Dp.Hairline,
        )

        TaskLabelLayout(
            label = "제목",
            isRequired = true,
        ) {
            TaskDialogTextField(
                value = titleValue,
                onValueChanged = onTitleChanged,
                isError = isTitleError,
                placeholder = "태스크 제목을 입력하세요.",
                maxLines = 1,
            )

            if (isTitleError) {
                Text(
                    text = "제목을 입력해 주세요.",
                    fontSize = 12.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 16.dp),
                    maxLines = 1,
                )
            }
        }

        TaskLabelLayout(label = "설명") {
            TaskDialogTextField(
                value = descriptionValue,
                onValueChanged = onDescriptionChanged,
                placeholder = "태스크에 대한 자세한 설명을 입력하세요.",
                modifier = Modifier.height(116.dp),
            )
        }

        TaskLabelLayout(label = "태그") {
            TaskDialogTextField(
                value = tagValue,
                onValueChanged = onTagChanged,
                placeholder = "태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)",
                isError = isTagError,
                modifier = Modifier.padding(bottom = 4.dp),
                maxLines = 1,
            )
            Text(
                text = tagErrorMessage,
                fontSize = 12.sp,
                color = if (isTagError) Color.Red else Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = 1,
            )
        }

        TaskLabelLayout(
            label = "상태",
            isRequired = true,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                statuses.forEachIndexed { index, status ->
                    StatusOptionCard(
                        text = status.toDisplayText,
                        isSelected = selectedStatusIndex == index,
                        onClick = { onStatusChanged(index) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        TaskLabelLayout(
            label = "담당자",
            isRequired = true,
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                assignees.forEachIndexed { index, string ->
                    AssigneeOptionCard(
                        name = string,
                        isSelected = selectedAssigneeIndex == index,
                        onClick = { onAssigneeChanged(index) },
                    )
                }
            }
        }

        HorizontalDivider(
            color = Color.Black,
            thickness = Dp.Hairline,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TaskDialogButton(
                text = "취소",
                onClick = onDismissClick,
            )
            Spacer(Modifier.width(12.dp))

            content()

        }
    }
}

@Composable
private fun TaskLabelLayout(
    label: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        TaskFieldLabel(
            label = label,
            isRequired = isRequired,
        )

        Spacer(Modifier.height(8.dp))

        content()
    }
}

@Preview(heightDp = 920)
@Composable
private fun TaskDialogContentPreview() {
    TaskDialogContent(
        topAppBarTitle = "기존 태스크 수정",
        titleValue = "",
        isTitleError = false,
        onTitleChanged = {},
        descriptionValue = "",
        onDescriptionChanged = {},
        tagValue = "버그, 긴급",
        isTagCountError = false,
        isTagFormatError = false,
        onTagChanged = {},
        statuses = TaskStatus.entries,
        selectedStatusIndex = 0,
        onStatusChanged = {},
        assignees = listOf("다이노", "페임스"),
        selectedAssigneeIndex = 0,
        onAssigneeChanged = {},
        onDismissClick = {},
        content = {},
    )
}
