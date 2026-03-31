package woowacourse.kanban.board.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KanbanSnackbar(message: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(bottom = 54.dp)
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(4.dp),
            )
            .width(344.dp)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            maxLines = 1,
        )

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "스낵바 닫기",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onClick),
        )
    }
}

@Preview
@Composable
private fun KanbanSnackbarPreview() {
    KanbanSnackbar(
        message = "새로운 태스크가 추가되었습니다.",
        onClick = {},
    )
}
