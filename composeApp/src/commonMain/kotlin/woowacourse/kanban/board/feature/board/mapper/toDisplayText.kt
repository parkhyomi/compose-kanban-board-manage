package woowacourse.kanban.board.feature.board.mapper

import woowacourse.kanban.board.domain.TaskStatus

internal val TaskStatus.toDisplayText: String
    get() =  when (this) {
        TaskStatus.TODO -> "To Do"
        TaskStatus.IN_PROGRESS -> "In Progress"
        TaskStatus.REVIEW -> "Review"
        TaskStatus.DONE -> "Done"
    }
