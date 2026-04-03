package woowacourse.kanban.board.domain

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class KanbanBoardTest {

    @Test
    fun `completionRate는_전체_태스크_2개중_DONE이_1개면_0_5를_반환한다`() {
        // given
        val tasks = listOf(
            createTask(status = TaskStatus.TODO),
            createTask(status = TaskStatus.DONE),
        )
        val board = KanbanBoard(tasks)

        // when
        val actual = board.completionRate

        // then
        assertThat(actual).isEqualTo(0.5f)
    }

    @Test
    fun `태스크가_없을_때_완료율은_0퍼센트이다`() {
        // given
        val tasks = emptyList<KanbanTask>()
        val board = KanbanBoard(tasks)

        // when
        val actual = board.completionRate

        // then
        assertThat(actual).isEqualTo(0.0f)
    }

    @Test
    fun `상태에_따라_분류된_태스크_목록을_정확히_반환한다`() {
        // given
        val todoTask = createTask(status = TaskStatus.TODO)
        val doneTask = createTask(status = TaskStatus.DONE)
        val board = KanbanBoard(listOf(todoTask, doneTask))

        // when
        val actual = board.getTasksByStatus(TaskStatus.TODO)

        // then
        assertThat(actual).containsExactly(todoTask)
        assertThat(actual).doesNotContain(doneTask)
    }

    @Test
    fun `태스크를 다른 컬럼으로 이동하면 해당 태스크의 상태만 변경된 새 보드를 반환한다`() {
        // given
        val task = createTask(status = TaskStatus.TODO)
        val board = KanbanBoard(listOf(task))

        // when
        val result = board.moveTask(task.id, TaskStatus.IN_PROGRESS)

        // then
        assertThat(result).isInstanceOf(MoveResult.MoveSuccess::class.java)
        val updatedBoard = (result as MoveResult.MoveSuccess).updatedBoard
        assertThat(updatedBoard.tasks.first().status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(updatedBoard.tasks.first().id).isEqualTo(task.id)
    }

    @Test
    fun `태스크를 이동해도 원래 보드 객체는 변경되지 않는다`() {
        // given
        val task = createTask(status = TaskStatus.TODO)
        val board = KanbanBoard(listOf(task))

        // when
        val result = board.moveTask(task.id, TaskStatus.IN_PROGRESS)

        // then
        assertThat(result).isInstanceOf(MoveResult.MoveSuccess::class.java)
        val updatedBoard = (result as MoveResult.MoveSuccess).updatedBoard
        assertThat(updatedBoard).isNotSameAs(board)
        assertThat(board.tasks.first().status).isEqualTo(TaskStatus.TODO)
    }

    @Test
    fun `존재하지 않는 ID로 이동을 시도하면 보드가 변경되지 않는다`() {
        // given
        val task = createTask(status = TaskStatus.TODO)
        val board = KanbanBoard(listOf(task))
        val nonExistentTask = createTask()

        // when
        val result = board.moveTask(nonExistentTask.id, TaskStatus.DONE)

        // then
        assertThat(result).isEqualTo(MoveResult.MoveFailed)
    }

    @Test
    fun `상태별_태스크_개수를_정확히_반환한다`() {
        // given
        val todoTask1 = createTask(status = TaskStatus.TODO)
        val todoTask2 = createTask(status = TaskStatus.TODO)
        val inProgressTask = createTask(status = TaskStatus.IN_PROGRESS)
        val board = KanbanBoard(listOf(todoTask1, todoTask2, inProgressTask))

        // when & then
        assertThat(board.getCountByStatus(TaskStatus.TODO)).isEqualTo(2)
        assertThat(board.getCountByStatus(TaskStatus.IN_PROGRESS)).isEqualTo(1)
        assertThat(board.getCountByStatus(TaskStatus.DONE)).isEqualTo(0)
    }

    private fun createTask(title: String = "테스트 제목", status: TaskStatus = TaskStatus.TODO, crewName: String = "테스트 크루"): KanbanTask {
        return KanbanTask(
            title = title,
            status = status,
            crewName = crewName,
        )
    }
}
