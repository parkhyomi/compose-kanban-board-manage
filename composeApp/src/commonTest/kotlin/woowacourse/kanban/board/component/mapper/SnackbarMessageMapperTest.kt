package woowacourse.kanban.board.component.mapper

import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.board.feature.board.mapper.toSnackbarMessage
import kotlin.test.Test
import woowacourse.kanban.board.feature.board.model.SnackbarMessageType

class SnackbarMessageMapperTest {

    @Test
    fun `TaskMoved는 성공 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskMoved.toSnackbarMessage())
            .isEqualTo("태스크가 이동되었습니다.")
    }

    @Test
    fun `TaskMoveFailed는 실패 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskMoveFailed.toSnackbarMessage())
            .isEqualTo("태스크를 이동할 수 없습니다.")
    }

    @Test
    fun `TaskAdded는 성공 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskAdded.toSnackbarMessage())
            .isEqualTo("태스크가 추가되었습니다.")
    }

    @Test
    fun `TaskAddFailed는 실패 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskAddFailed.toSnackbarMessage())
            .isEqualTo("태스크 추가에 실패했습니다.")
    }

    @Test
    fun `TaskDeleted는 성공 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskDeleted.toSnackbarMessage())
            .isEqualTo("태스크가 삭제되었습니다.")
    }

    @Test
    fun `TaskDeleteFailed는 실패 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskDeleteFailed.toSnackbarMessage())
            .isEqualTo("태스크 삭제에 실패했습니다.")
    }

    @Test
    fun `TaskUpdated는 성공 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskUpdated.toSnackbarMessage())
            .isEqualTo("태스크가 수정되었습니다.")
    }

    @Test
    fun `TaskUpdateFailed는 실패 메시지로 매핑된다`() {
        assertThat(SnackbarMessageType.TaskUpdateFailed.toSnackbarMessage())
            .isEqualTo("태스크 수정에 실패했습니다.")
    }
}
