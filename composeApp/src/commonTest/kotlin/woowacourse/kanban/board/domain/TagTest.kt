package woowacourse.kanban.board.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.assertj.core.api.Assertions.assertThat

class TagTest {
    @Test
    fun `태그가 1~5자 이내이면 정상적으로 생성된다`() {
        // Given & When & Then
        assertThat(Tag("1").value).isEqualTo("1")
        assertThat(Tag("12345").value).isEqualTo("12345")
        assertThat(Tag("태그임다").value).isEqualTo("태그임다")
    }

    @Test
    fun `태그가 5자를 초과하면 예외가 발생한다`() {
        // Given
        val invalidTag = "여섯글자태그"

        // When & Then
        val exception = assertFailsWith<IllegalArgumentException> {
            Tag(invalidTag)
        }
        assertThat(exception.message).isEqualTo("태그는 1~5자 이내로 입력해야 합니다.")
    }

    @Test
    fun `태그가 비어있으면 예외가 발생한다`() {
        // Given
        val emptyTag = ""

        // When & Then
        val exception = assertFailsWith<IllegalArgumentException> {
            Tag(emptyTag)
        }
        assertThat(exception.message).isEqualTo("태그는 1~5자 이내로 입력해야 합니다.")
    }

    @Test
    fun `isValid 검증 - 올바른 형식이면 true를 반환한다`() {
        // Given & When & Then
        assertThat(Tag.isValid("1")).isTrue()
    }

    @Test
    fun `isValid 검증 - 잘못된 형식이면 false를 반환한다`() {
        // Given & When & Then
        assertThat(Tag.isValid("")).isFalse()
        assertThat(Tag.isValid("\n")).isFalse()
        assertThat(Tag.isValid("여섯글자태그")).isFalse()
    }
}
