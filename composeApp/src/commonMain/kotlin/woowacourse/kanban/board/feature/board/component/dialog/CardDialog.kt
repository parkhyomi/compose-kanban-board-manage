package woowacourse.kanban.board.feature.board.component.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import woowacourse.kanban.board.core.designsystem.theme.KanbanRed
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.component.dialog.component.TaskDialogButton
import woowacourse.kanban.board.feature.board.component.dialog.model.TaskFormResult
import java.util.UUID

@Composable
fun CardDialog(
    onDismissClick: () -> Unit,
    onDeletedClick: (task: KanbanTask) -> Unit,
    onUpdatedClick: (taskId: UUID, result: TaskFormResult) -> Unit,
    initialTask: KanbanTask? = null,
    modifier: Modifier = Modifier,
) {
    val task = requireNotNull(initialTask) { "CardDialog requires a non-null initialTask." }
    val formState = rememberTaskFormState(task)
    val initialStatusIndex = TaskStatus.entries.indexOf(task.status).takeIf { it >= 0 } ?: 0

    var selectedStatusIndex by remember(task.id) { mutableIntStateOf(initialStatusIndex) }
    val assignees = formState.assigneeResult(TaskStatus.entries[selectedStatusIndex])
    var selectedAssigneeIndex by remember(task.id) {
        mutableIntStateOf(
            task.crewName.let { assignees.indexOf(it) }.takeIf { it >= 0 } ?: 0,
        )
    }

    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        TaskDialogContent(
            modifier = modifier,
            topAppBarTitle = "기존 테스크 수정",
            titleValue = formState.title,
            isTitleError = formState.isTitleError,
            onTitleChanged = {
                formState.onTitleChanged(it)
            },
            descriptionValue = formState.description,
            onDescriptionChanged = { formState.onDescriptionChanged(it) },
            tagValue = formState.tagValue,
            isTagCountError = formState.isTagCountError,
            isTagFormatError = formState.isTagFormatError,
            onTagChanged = { formState.onTagChanged(it) },
            statuses = TaskStatus.entries,
            selectedStatusIndex = selectedStatusIndex,
            onStatusChanged = {
                selectedStatusIndex = it
                selectedAssigneeIndex = 0
            },
            assignees = assignees,
            selectedAssigneeIndex = selectedAssigneeIndex,
            onAssigneeChanged = { selectedAssigneeIndex = it },
            onDismissClick = onDismissClick,
        ){
            TaskDialogButton(
                text = "삭제",
                onClick = {
                    onDeletedClick(task)
                },
                contentColor = Color.White,
                containerColor = Color.KanbanRed,
            )
            Spacer(Modifier.width(12.dp))
            TaskDialogButton(
                text = "수정",
                onClick = {
                    onUpdatedClick(
                        task.id,
                        TaskFormResult(
                            title = formState.title,
                            description = formState.description.takeIf { it.isNotBlank() },
                            tags = formState.tags,
                            status = TaskStatus.entries[selectedStatusIndex],
                            assignee = assignees[selectedAssigneeIndex],
                        ),
                    )
                },
                contentColor = Color.White,
                containerColor = Color.Blue,
            )
        }
    }
}
