package woowacourse.kanban.board.feature.board

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import woowacourse.kanban.board.feature.board.component.KanbanBoardContent
import woowacourse.kanban.board.feature.board.component.KanbanBoardSidebar
import woowacourse.kanban.board.feature.board.component.dialog.TaskDialog

@Composable
fun KanbanBoardScreen(
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    boardState: KanbanBoardState = rememberKanbanBoardState(),
) {
    LaunchedEffect(boardState.snackbarEvent?.id) {

        val event = boardState.snackbarEvent ?: return@LaunchedEffect

        onShowSnackbar(event.message)
        boardState.clearSnackbar(event.id)

    }

    Row(modifier = modifier.fillMaxSize()) {
        KanbanBoardSidebar()
        KanbanBoardContent(
            modifier = Modifier.weight(1f),
            kanbanBoard = boardState.kanbanBoard,
            onTaskCreateClick = boardState::showTaskDialog,
            onMoveTask = boardState::moveTask,
        )
    }

    if (boardState.isTaskDialogVisible) {
        TaskDialog(
            onCreateClick = boardState::addTask,
            onDismissClick = boardState::hideTaskDialog,
        )
    }
}
