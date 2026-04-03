package woowacourse.kanban.board.feature.board.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.component.dialog.component.TaskDialogButton
import woowacourse.kanban.board.feature.board.component.dialog.model.TaskFormResult

@Composable
fun TaskDialog(onCreateClick: (result: TaskFormResult) -> Unit, onDismissClick: () -> Unit, modifier: Modifier = Modifier) {
    val formState = rememberTaskFormState()

    var selectedStatusIndex by remember { mutableIntStateOf(0) }
    var selectedAssigneeIndex by remember { mutableIntStateOf(0) }

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
            topAppBarTitle = "새 태스크 생성",
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
            assignees = formState.assigneeResult(TaskStatus.entries[selectedStatusIndex]),
            selectedAssigneeIndex = selectedAssigneeIndex,
            onAssigneeChanged = { selectedAssigneeIndex = it },
            onDismissClick = onDismissClick,
        ) {
            TaskDialogButton(
                text = "생성",
                onClick = {
                    onCreateClick(
                        TaskFormResult(
                            title = formState.title,
                            description = formState.description.takeIf { it.isNotBlank() },
                            tags = formState.tags,
                            status = TaskStatus.entries[selectedStatusIndex],
                            assignee = formState.assigneeResult(TaskStatus.entries[selectedStatusIndex])[selectedAssigneeIndex],
                        ),
                    )
                },
                enabled = formState.isCreateButtonEnabled,
                contentColor = Color.White,
                containerColor = Color.Blue,
            )
        }
    }
}
