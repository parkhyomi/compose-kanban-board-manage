package woowacourse.kanban.board.domain

import java.util.UUID

data class KanbanBoard(val tasks: List<KanbanTask> = emptyList()) {
    fun getTasksByStatus(status: TaskStatus): List<KanbanTask> = tasks.filter { it.status == status }

    fun getCountByStatus(status: TaskStatus): Int = tasks.count { it.status == status }

    fun moveTask(taskId: UUID, targetStatus: TaskStatus): MoveResult {
        val currentTask = tasks.find { it.id == taskId } ?: return MoveResult.MoveFailed
        if (!canMove(currentTask.status, targetStatus)) {
            return MoveResult.MoveFailed
        }
        val updatedBoard = copy(
            tasks = tasks.map { task ->
                if (task.id == taskId) task.copy(status = targetStatus) else task
            },
        )
        return MoveResult.MoveSuccess(updatedBoard)

    }

    private fun canMove(form: TaskStatus, move: TaskStatus): Boolean = when(form) {
        TaskStatus.TODO -> move == TaskStatus.IN_PROGRESS
        TaskStatus.IN_PROGRESS -> move == TaskStatus.REVIEW || move == TaskStatus.DONE
        TaskStatus.REVIEW -> move == TaskStatus.IN_PROGRESS || move == TaskStatus.DONE
        TaskStatus.DONE -> move == TaskStatus.TODO
    }

    val completionRate: Float
        get() {
            if (tasks.isEmpty()) return 0.0f
            val completeCount = getCountByStatus(TaskStatus.DONE)
            return completeCount.toFloat() / tasks.size
        }
}

sealed class MoveResult{
    data class MoveSuccess(val updatedBoard: KanbanBoard) : MoveResult()
    data object MoveFailed : MoveResult()
}