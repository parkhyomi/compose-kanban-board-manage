package woowacourse.kanban.board.component.dialog

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import woowacourse.kanban.board.feature.board.component.dialog.TaskDialog

@OptIn(ExperimentalTestApi::class)
class TaskDialogTest {
    @Test
    fun `초기 렌더링 시 제목이 비어있으므로 생성 버튼이 비활성화된다`() = runComposeUiTest {
        // Given
        setContent {
            TaskDialog(
                onCreateClick = { _ -> },
                onDismissClick = {},
            )
        }

        // When (초기 상태 그대로)

        // Then
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `제목을 입력하면 생성 버튼이 활성화된다`() = runComposeUiTest {
        // Given
        setContent {
            TaskDialog(
                onCreateClick = { _ -> },
                onDismissClick = {},
            )
        }

        // When
        onNodeWithText("태스크 제목을 입력하세요.").performTextInput("안드로이드 과제하기")

        // Then
        onNodeWithText("생성").assertIsEnabled()
    }

    @Test
    fun `제목을 입력하고 태그 개수가 1개이면서 1~5자 이내일 경우 생성 버튼이 활성화된다`() = runComposeUiTest {
        // Given
        setContent {
            TaskDialog(
                onCreateClick = { _ -> },
                onDismissClick = {},
            )
        }

        // When
        onNodeWithText("태스크 제목을 입력하세요.").performTextInput("안드로이드 과제하기")
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("정상태그")

        // Then
        onNodeWithText("5자 이내의 태그를 최대 5개까지 등록할 수 있습니다.").assertIsDisplayed()
        onNodeWithText("생성").assertIsEnabled()
    }

    @Test
    fun `제목을 입력하고 6자 이상의 태그를 입력하면 생성 버튼이 비활성화되고 에러 메시지가 노출된다`() = runComposeUiTest {
        // Given
        setContent {
            TaskDialog(
                onCreateClick = { _ -> },
                onDismissClick = {},
            )
        }

        // When
        onNodeWithText("태스크 제목을 입력하세요.").performTextInput("안드로이드 과제하기")
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("여섯글자태그")

        // Then
        onNodeWithText("태그 형식이 올바르지 않습니다.").assertIsDisplayed()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `제목을 입력하고 태그를 6개 입력하면 생성 버튼이 비활성화되고 에러 메시지가 노출된다`() = runComposeUiTest {
        // Given
        setContent {
            TaskDialog(
                onCreateClick = { _ -> },
                onDismissClick = {},
            )
        }

        // When
        onNodeWithText("태스크 제목을 입력하세요.").performTextInput("안드로이드 과제하기")
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("1, 2, 3, 4, 5, 6")

        // Then
        onNodeWithText("태그는 5자 이내로 5개까지만 등록할 수 있습니다.").assertIsDisplayed()
        onNodeWithText("생성").assertIsNotEnabled()
    }
}
