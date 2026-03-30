package woowacourse.kanban.board.feature.board.model

sealed interface SnackbarMessageType {
    data object TaskMoved : SnackbarMessageType
    data object TaskAdded : SnackbarMessageType
    data object TaskAddFailed : SnackbarMessageType
}

data class SnackbarEvent(val id: Long, val type: SnackbarMessageType)
