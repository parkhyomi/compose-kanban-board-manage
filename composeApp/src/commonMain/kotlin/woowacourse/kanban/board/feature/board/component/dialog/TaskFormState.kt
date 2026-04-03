package woowacourse.kanban.board.feature.board.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TaskStatus

class TaskFormState(initialTitle: String = "", initialDescription: String = "", initialTagValue: String = "") {
    var title by mutableStateOf(initialTitle)
        private set

    var isTitleDirty by mutableStateOf(false)
        private set

    var description by mutableStateOf(initialDescription)
        private set

    var tagValue by mutableStateOf(initialTagValue)
        private set

    val isTitleError: Boolean
        get() = isTitleDirty && !KanbanTask.isTitleValid(title)

    val tags: List<Tag>
        get() {
            if (tagValue.isBlank()) return emptyList()
            return tagValue.split(",")
                .map { it.trim() }
                .filter { Tag.isValid(it) }
                .map { Tag(it) }
        }

    val rawTags: List<String>
        get() {
            if (tagValue.isBlank()) return emptyList()
            return tagValue.split(",").map { it.trim() }
        }

    val isTagCountError: Boolean
        get() = rawTags.size > 5

    val isTagFormatError: Boolean
        get() = tagValue.isNotBlank() && !rawTags.all { Tag.isValid(it) }

    val isCreateButtonEnabled: Boolean
        get() = KanbanTask.isTitleValid(title) && !isTagCountError && !isTagFormatError

    fun onTitleChanged(value: String) {
        title = value
        isTitleDirty = true
    }

    fun onDescriptionChanged(value: String) {
        description = value
    }

    fun onTagChanged(value: String) {
        tagValue = value
    }

    fun assigneeResult(value: TaskStatus): List<String> {
        val assignee = when (value) {
            TaskStatus.TODO -> listOf("없음", "다이노", "페임스")
            TaskStatus.IN_PROGRESS -> listOf("다이노", "페임스")
            TaskStatus.REVIEW -> listOf("다이노", "페임스")
            TaskStatus.DONE -> listOf("다이노", "페임스")
        }
        return assignee
    }
}

@Composable
fun rememberTaskFormState(initialTask: KanbanTask? = null): TaskFormState {
    return remember(initialTask?.id) {
        TaskFormState(
            initialTitle = initialTask?.title.orEmpty(),
            initialDescription = initialTask?.description.orEmpty(),
            initialTagValue = initialTask?.tags?.joinToString(", ") { it.value }.orEmpty(),
        )
    }
}
