package woowacourse.kanban.board.component.dialog

import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.board.feature.board.component.dialog.TaskFormState

class TaskFormStateTest {

    @Test
    fun `초기 상태에서는 에러가 발생하지 않는다`() {
        // Given & When
        val state = TaskFormState()

        // Then
        assertThat(state.isTitleError).isFalse()
        assertThat(state.isTagCountError).isFalse()
        assertThat(state.isTagFormatError).isFalse()
        assertThat(state.isCreateButtonEnabled).isFalse()
    }

    @Test
    fun `제목 입력을 시도했고, 값이 비어있으면 isTitleError가 true가 된다`() {
        // Given
        val state = TaskFormState()

        // When
        state.onTitleChanged("")

        // Then
        assertThat(state.isTitleError).isTrue()
        assertThat(state.isCreateButtonEnabled).isFalse()
    }

    @Test
    fun `올바른 제목과 태그를 입력하면 생성 버튼이 활성화된다`() {
        // Given
        val state = TaskFormState()

        // When
        state.onTitleChanged("정상 제목")
        state.onTagChanged("버그, 긴급")

        // Then
        assertThat(state.isTitleError).isFalse()
        assertThat(state.isTagCountError).isFalse()
        assertThat(state.isTagFormatError).isFalse()
        assertThat(state.isCreateButtonEnabled).isTrue()
    }

    @Test
    fun `태그 개수가 6개 이상이면 isTagCountError가 true가 된다`() {
        // Given
        val state = TaskFormState()

        // When
        state.onTitleChanged("제목")
        state.onTagChanged("1, 2, 3, 4, 5, 6")

        // Then
        assertThat(state.isTagCountError).isTrue()
        assertThat(state.isTagFormatError).isFalse()
        assertThat(state.isCreateButtonEnabled).isFalse()
    }

    @Test
    fun `태그 중 5자를 초과하는 것이 있으면 isTagFormatError가 true가 된다`() {
        // Given
        val state = TaskFormState()

        // When
        state.onTitleChanged("제목")
        state.onTagChanged("정상, 여섯글자태그")

        // Then
        assertThat(state.isTagCountError).isFalse()
        assertThat(state.isTagFormatError).isTrue()
        assertThat(state.isCreateButtonEnabled).isFalse()
    }
}
