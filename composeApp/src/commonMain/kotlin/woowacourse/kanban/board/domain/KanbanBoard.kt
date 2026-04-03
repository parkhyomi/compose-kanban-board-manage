package woowacourse.kanban.board.domain

import java.util.UUID

data class KanbanBoard(val tasks: List<KanbanTask> = emptyList()) {
    fun getTasksByStatus(status: TaskStatus): List<KanbanTask> = tasks.filter { it.status == status }

    fun getCountByStatus(status: TaskStatus): Int = tasks.count { it.status == status }

    fun addTask(task: KanbanTask): KanbanBoard {
        val updatedBoard = copy(tasks = tasks + task)
        return updatedBoard
    }

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

    private fun canMove(form: TaskStatus, move: TaskStatus): Boolean = when (form) {
        TaskStatus.TODO -> move == TaskStatus.IN_PROGRESS
        TaskStatus.IN_PROGRESS -> move == TaskStatus.TODO || move == TaskStatus.REVIEW
        TaskStatus.REVIEW -> move == TaskStatus.IN_PROGRESS || move == TaskStatus.DONE
        TaskStatus.DONE -> move == TaskStatus.TODO
    }

    fun canDelete(task: KanbanTask): CanDeleteResult {
        val deleteTask = tasks.filterNot { it.id == task.id }

        if (task.status == TaskStatus.TODO || task.status == TaskStatus.IN_PROGRESS) {
            val updatedBoard = copy(tasks = deleteTask)
            return CanDeleteResult.DeleteSuccess(updatedBoard)
        } else {
            return CanDeleteResult.DeleteFailed
        }
    }

    val completionRate: Float
        get() {
            if (tasks.isEmpty()) return 0.0f
            val completeCount = getCountByStatus(TaskStatus.DONE)
            return completeCount.toFloat() / tasks.size
        }
}

sealed class MoveResult {
    data class MoveSuccess(val updatedBoard: KanbanBoard) : MoveResult()
    data object MoveFailed : MoveResult()
}

sealed class CanDeleteResult {
    data class DeleteSuccess(val updatedBoard: KanbanBoard) : CanDeleteResult()
    data object DeleteFailed : CanDeleteResult()
}
