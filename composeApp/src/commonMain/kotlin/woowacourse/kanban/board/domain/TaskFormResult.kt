package woowacourse.kanban.board.domain

data class TaskFormResult(val title: String, val description: String?, val tags: List<Tag>, val status: TaskStatus, val assignee: String)
