package woowacourse.kanban.board.feature.board.mapper

import woowacourse.kanban.board.feature.board.model.SnackbarMessageType

internal fun SnackbarMessageType.toSnackbarMessage(): String = when (this) {
    SnackbarMessageType.TaskMoved -> "태스크가 이동되었습니다."
    SnackbarMessageType.TaskMoveFailed -> "태스크를 이동할 수 없습니다."
    SnackbarMessageType.TaskAdded -> "태스크가 추가되었습니다."
    SnackbarMessageType.TaskAddFailed -> "태스크 추가에 실패했습니다."
    SnackbarMessageType.TaskDeleted -> "태스크가 삭제되었습니다."
    SnackbarMessageType.TaskDeleteFailed -> "태스크 삭제에 실패했습니다."
    SnackbarMessageType.TaskUpdated -> "태스크가 수정되었습니다."
    SnackbarMessageType.TaskUpdateFailed -> "태스크 수정에 실패했습니다."
}
