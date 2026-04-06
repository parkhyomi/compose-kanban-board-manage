package woowacourse.kanban.board.feature.board.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.KanbanTask
import woowacourse.kanban.board.domain.Tag

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
