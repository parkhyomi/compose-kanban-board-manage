package woowacourse.kanban.board.domain

import java.util.UUID

data class KanbanBoard(val tasks: List<KanbanTask> = emptyList()) {
    fun getTasksByStatus(status: TaskStatus): List<KanbanTask> = tasks.filter { it.status == status }

    fun getCountByStatus(status: TaskStatus): Int = tasks.count { it.status == status }

    fun moveTask(taskId: UUID, targetStatus: TaskStatus): KanbanBoard {
        val updatedTasks = tasks.map { task ->
            if (task.id == taskId) task.copy(status = targetStatus) else task
        }
        return copy(tasks = updatedTasks)
    }

    val completionRate: Float
        get() {
            if (tasks.isEmpty()) return 0.0f
            val completeCount = getCountByStatus(TaskStatus.DONE)
            return completeCount.toFloat() / tasks.size
        }
}
