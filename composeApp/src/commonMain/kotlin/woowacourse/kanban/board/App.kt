package woowacourse.kanban.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import woowacourse.kanban.board.core.designsystem.component.KanbanSnackbar
import woowacourse.kanban.board.feature.board.KanbanBoardScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Box(modifier = Modifier.fillMaxSize()) {
            KanbanBoardScreen(
                onShowSnackbar = { message ->
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                },
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            ) { data ->
                KanbanSnackbar(
                    message = data.visuals.message,
                    onClick = data::dismiss,
                )
            }
        }
    }
}
