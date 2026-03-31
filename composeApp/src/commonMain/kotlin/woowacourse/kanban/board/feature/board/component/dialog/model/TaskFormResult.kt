package woowacourse.kanban.board.feature.board.component.dialog.model

import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TaskStatus

data class TaskFormResult(val title: String, val description: String?, val tags: List<Tag>, val status: TaskStatus, val assignee: String)
