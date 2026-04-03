package woowacourse.kanban.board.feature.board.model

sealed interface SnackbarMessageType {
    data object TaskMoved : SnackbarMessageType
    data object TaskMoveFailed : SnackbarMessageType
    data object TaskAdded : SnackbarMessageType
    data object TaskAddFailed : SnackbarMessageType
    data object TaskDeleted : SnackbarMessageType
    data object TaskDeleteFailed : SnackbarMessageType
    data object TaskUpdated : SnackbarMessageType
    data object TaskUpdateFailed : SnackbarMessageType
}

data class SnackbarEvent(val id: Long, val type: SnackbarMessageType)
