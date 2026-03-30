package woowacourse.kanban.board.feature.board.mapper

import woowacourse.kanban.board.feature.board.model.SnackbarMessageType

internal fun SnackbarMessageType.toSnackbarMessage(): String = when (this) {
    SnackbarMessageType.TaskMoved -> "태스크가 이동되었습니다."
    SnackbarMessageType.TaskAdded -> "태스크가 추가되었습니다."
    SnackbarMessageType.TaskAddFailed -> "태스크 추가에 실패했습니다."
}
