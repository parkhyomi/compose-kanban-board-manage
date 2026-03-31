package woowacourse.kanban.board.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.assertj.core.api.Assertions.assertThat

class KanbanTaskTest {

    @Test
    fun `KanbanTask 정상 생성 - 모든 정보가 올바른 경우`() {
        // Given
        val title = "새로운 기능 구현"
        val description = "이 기능은 매우 중요합니다."
        val tags = listOf(Tag("긴급"), Tag("백엔드"))
        val crewName = "아키"

        // When
        val task = KanbanTask(
            title = title,
            description = description,
            tags = tags,
            crewName = crewName,
            status = TaskStatus.TODO,
        )

        // Then
        assertThat(task.id).isNotNull()
        assertThat(task.title).isEqualTo(title)
        assertThat(task.description).isEqualTo(description)
        assertThat(task.tags).containsExactlyElementsOf(tags)
        assertThat(task.crewName).isEqualTo(crewName)
    }

    @Test
    fun `서로 다른 두 태스크를 생성하면 서로 다른 고유 ID를 가져야 한다`() {
        // Given & When
        val task1 = KanbanTask(title = "제목1", crewName = "크루1", status = TaskStatus.TODO)
        val task2 = KanbanTask(title = "제목2", crewName = "크루2", status = TaskStatus.TODO)

        // Then
        assertThat(task1.id).isNotEqualTo(task2.id)
    }

    @Test
    fun `KanbanTask 생성 실패 - 제목이 비어 있는 경우`() {
        // Given
        val emptyTitle = ""

        // When & Then
        val exception = assertFailsWith<IllegalArgumentException> {
            KanbanTask(title = emptyTitle, crewName = "아키", status = TaskStatus.TODO)
        }
        assertThat(exception.message).isEqualTo("제목은 비어 있거나 공백만 있을 수 없습니다.")
    }

    @Test
    fun `KanbanTask 생성 실패 - 제목이 공백만 있는 경우`() {
        // Given
        val blankTitle = "   "

        // When & Then
        val exception = assertFailsWith<IllegalArgumentException> {
            KanbanTask(title = blankTitle, crewName = "아키", status = TaskStatus.TODO)
        }
        assertThat(exception.message).isEqualTo("제목은 비어 있거나 공백만 있을 수 없습니다.")
    }

    @Test
    fun `KanbanTask 생성 실패 - 태그가 6개 이상인 경우`() {
        // Given
        val tags = listOf(Tag("1"), Tag("2"), Tag("3"), Tag("4"), Tag("5"), Tag("6"))

        // When & Then
        val exception = assertFailsWith<IllegalArgumentException> {
            KanbanTask(title = "제목", tags = tags, crewName = "아키", status = TaskStatus.TODO)
        }
        assertThat(exception.message).isEqualTo("태그는 5개까지만 등록할 수 있습니다.")
    }

    @Test
    fun `isTitleValid 검증 - 정상적인 제목이면 true를 반환한다`() {
        assertThat(KanbanTask.isTitleValid("제목")).isTrue()
    }

    @Test
    fun `isTitleValid 검증 - 공백이거나 비어있으면 false를 반환한다`() {
        assertThat(KanbanTask.isTitleValid("")).isFalse()
        assertThat(KanbanTask.isTitleValid("   ")).isFalse()
    }

    @Test
    fun `isTagCountValid 검증 - 태그가 5개 이하이면 true를 반환한다`() {
        assertThat(KanbanTask.isTagCountValid(emptyList())).isTrue()
        assertThat(KanbanTask.isTagCountValid(listOf(Tag("1"), Tag("2"), Tag("3"), Tag("4"), Tag("5")))).isTrue()
    }

    @Test
    fun `isTagCountValid 검증 - 태그가 6개 이상이면 false를 반환한다`() {
        assertThat(KanbanTask.isTagCountValid(listOf(Tag("1"), Tag("2"), Tag("3"), Tag("4"), Tag("5"), Tag("6")))).isFalse()
    }
}
