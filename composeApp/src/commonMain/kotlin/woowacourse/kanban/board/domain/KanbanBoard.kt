package woowacourse.kanban.board.domain

import java.util.UUID

data class KanbanBoard(
    private val tasks: List<KanbanTask> = emptyList()
) {
    val getTaskCountByTotal: Int
        get() = tasks.size

    fun getTasksByStatus(status: TaskStatus): List<KanbanTask> = tasks.filter { it.status == status }

    fun getCountByStatus(status: TaskStatus): Int = tasks.count { it.status == status }

    fun addTask(task: TaskFormResult): AddResult {
        val task = runCatching {
            KanbanTask(
                title = task.title,
                description = task.description,
                tags = task.tags,
                status = task.status,
                crewName = task.assignee,
            )
        }.getOrElse {
            return AddResult.AddFailed
        }

        return AddResult.AddSuccess(copy(tasks = tasks + task))
    }

    fun moveTask(taskId: UUID, targetStatus: TaskStatus): MoveResult {
        val currentTask = tasks.find { it.id == taskId } ?: return MoveResult.MoveFailed
        if (!TaskStatusRules.canMove(currentTask.status, targetStatus)) {
            return MoveResult.MoveFailed
        }

        if (!TaskStatusRules.isAssigneeAllowed(targetStatus, currentTask.crewName)) {
            return MoveResult.MoveFailed
        }

        val updatedBoard = copy(
            tasks = tasks.map { task ->
                if (task.id == taskId) task.copy(status = targetStatus) else task
            },
        )
        return MoveResult.MoveSuccess(updatedBoard)
    }

    fun updateTask(taskId: UUID, task: TaskFormResult): UpdateResult {
        val currentTask = tasks.find { it.id == taskId } ?: return UpdateResult.UpdateFailed

        val updatedTask = runCatching {
            currentTask.copy(
                title = task.title,
                description = task.description,
                tags = task.tags,
                status = task.status,
                crewName = task.assignee,
            )
        }.getOrElse {
            return UpdateResult.UpdateFailed
        }

        return UpdateResult.UpdateSuccess(
            copy(
                tasks = tasks.map { task ->
                    if (task.id == taskId) updatedTask else task
                },
            ),
        )
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

sealed class AddResult {
    data class AddSuccess(val updatedBoard: KanbanBoard) : AddResult()
    data object AddFailed : AddResult()
}

sealed class UpdateResult {
    data class UpdateSuccess(val updatedBoard: KanbanBoard) : UpdateResult()
    data object UpdateFailed : UpdateResult()
}

sealed class CanDeleteResult {
    data class DeleteSuccess(val updatedBoard: KanbanBoard) : CanDeleteResult()
    data object DeleteFailed : CanDeleteResult()
}
