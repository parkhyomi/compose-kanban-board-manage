package woowacourse.kanban.board.component.board

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.TaskStatus
import woowacourse.kanban.board.feature.board.KanbanBoardScreen
import woowacourse.kanban.board.feature.board.KanbanBoardState
import woowacourse.kanban.board.feature.board.component.dialog.model.TaskFormResult

@OptIn(ExperimentalTestApi::class)
class KanbanBoardScreenTest {

    @Test
    fun `사이드바와 보드 영역이 함께 렌더링된다`() = runComposeUiTest {
        // Given & When
        setContent {
            KanbanBoardScreen(onShowSnackbar = {})
        }

        // Then
        onNodeWithText("프로젝트").assertIsDisplayed()
        onNodeWithText("완료율: 0% (0/0)").assertIsDisplayed()
    }

    @Test
    fun `주입된 보드 상태에 따라 태스크 카드와 헤더 통계가 올바르게 화면에 렌더링된다`() = runComposeUiTest {
        // Given
        val tasks = listOf(
            KanbanTask(title = "완료된 작업", status = TaskStatus.DONE, crewName = "다이노"),
            KanbanTask(title = "진행중 작업", status = TaskStatus.IN_PROGRESS, crewName = "페임스"),
        )
        val state = KanbanBoardState(initialBoard = KanbanBoard(tasks))

        // When
        setContent {
            KanbanBoardScreen(
                boardState = state,
                onShowSnackbar = {},
            )
        }

        // Then
        onNodeWithText("완료율: 50% (1/2)").assertIsDisplayed()
        onNodeWithText("완료된 작업").assertIsDisplayed()
        onNodeWithText("진행중 작업").assertIsDisplayed()
    }

    @Test
    fun `새 태스크 생성 버튼을 누르면 상태가 변경되어 다이얼로그가 노출된다`() = runComposeUiTest {
        // Given
        setContent {
            val state = KanbanBoardState()
            KanbanBoardScreen(
                boardState = state,
                onShowSnackbar = {},
            )
        }

        // When
        onNodeWithText("새 태스크 생성").performClick()

        // Then
        onNodeWithText("태스크 제목을 입력하세요.").assertIsDisplayed()
    }

    @Test
    fun `상태 객체의 addTask를 호출하면 화면이 리컴포지션되어 새로운 태스크와 완료율이 갱신된다`() = runComposeUiTest {
        // Given
        val state = KanbanBoardState()
        setContent {
            KanbanBoardScreen(
                boardState = state,
                onShowSnackbar = {},
            )
        }

        onNodeWithText("완료율: 0% (0/0)").assertIsDisplayed()

        // When
        val newTaskResult = TaskFormResult(
            title = "새로운 완료 작업",
            description = "테스트용입니다",
            tags = emptyList(),
            status = TaskStatus.DONE,
            assignee = "페임스",
        )
        state.addTask(newTaskResult)

        // Then
        onNodeWithText("새로운 완료 작업").assertIsDisplayed()
        onNodeWithText("완료율: 100% (1/1)").assertIsDisplayed()
    }
}
