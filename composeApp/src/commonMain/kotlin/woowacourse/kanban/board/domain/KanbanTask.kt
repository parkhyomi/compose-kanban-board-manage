package woowacourse.kanban.board.domain

import java.util.UUID

data class KanbanTask(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val tags: List<Tag> = emptyList(),
    val status: TaskStatus,
    val crewName: String,
) {
    init {
        require(isTitleValid(title)) { "제목은 비어 있거나 공백만 있을 수 없습니다." }
        require(isTagCountValid(tags)) { "태그는 5개까지만 등록할 수 있습니다." }
    }

    companion object {
        fun isTitleValid(title: String): Boolean = title.isNotBlank()

        fun isTagCountValid(tags: List<Tag>): Boolean = tags.size <= 5
    }
}
