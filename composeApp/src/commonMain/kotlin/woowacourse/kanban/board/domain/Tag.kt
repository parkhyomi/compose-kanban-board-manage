package woowacourse.kanban.board.domain

data class Tag(val value: String) {
    init {
        require(isValid(value)) { "태그는 1~5자 이내로 입력해야 합니다." }
    }

    companion object {
        private val TAG_REGEX = "^.{1,5}$".toRegex()

        fun isValid(value: String): Boolean = value.matches(TAG_REGEX)
    }
}
