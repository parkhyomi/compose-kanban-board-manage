package woowacourse.kanban.board.feature.board.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.board.domain.KanbanBoard
import woowacourse.kanban.board.domain.TaskStatus

@Composable
fun KanbanBoardHeader(board: KanbanBoard, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val completionRate = board.completionRate
    val completeCount = board.getCountByStatus(TaskStatus.DONE)
    val totalCount = board.tasks.size

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(
                width = Dp.Hairline,
                color = Color.LightGray,
            )
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Compose1",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "완료율: ${(completionRate * 100).toInt()}% ($completeCount/$totalCount)",
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }

            Button(
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 11.dp),
                onClick = onClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "태스크 생성 아이콘",
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = "새 태스크 생성",
                    fontSize = 16.sp,
                )
            }
        }

        LinearProgressIndicator(
            gapSize = 0.dp,
            strokeCap = StrokeCap.Square,
            progress = { completionRate },
            modifier = Modifier.clip(CircleShape).fillMaxWidth(),
            color = Color.Blue,
            trackColor = Color.Gray,
            drawStopIndicator = {},
        )
    }
}

@Preview(widthDp = 1000)
@Composable
private fun KanbanBoardHeaderPreview() {
    KanbanBoardHeader(
        board = KanbanBoard(),
        onClick = {},
    )
}
