package woowacourse.kanban.board.domain

object TaskStatusRules {
    const val UNASSIGNED: String = "없음"

    private val defaultAssignees = listOf("다이노", "페임스")

    fun availableAssignees(status: TaskStatus): List<String> = when (status) {
        TaskStatus.TODO -> listOf(UNASSIGNED) + defaultAssignees
        TaskStatus.IN_PROGRESS, TaskStatus.REVIEW, TaskStatus.DONE -> defaultAssignees
    }

    fun canMove(from: TaskStatus, to: TaskStatus): Boolean = when (from) {
        TaskStatus.TODO -> to == TaskStatus.IN_PROGRESS
        TaskStatus.IN_PROGRESS -> to == TaskStatus.TODO || to == TaskStatus.REVIEW
        TaskStatus.REVIEW -> to == TaskStatus.IN_PROGRESS || to == TaskStatus.DONE
        TaskStatus.DONE -> to == TaskStatus.TODO
    }

    fun isAssigneeAllowed(status: TaskStatus, assignee: String): Boolean {
        if (status == TaskStatus.TODO) return true
        return assignee != UNASSIGNED
    }
}
