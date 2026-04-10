package woowacourse.kanban.board.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object TaskUuid{
    fun uuid(): String = Uuid.random().toString()
}