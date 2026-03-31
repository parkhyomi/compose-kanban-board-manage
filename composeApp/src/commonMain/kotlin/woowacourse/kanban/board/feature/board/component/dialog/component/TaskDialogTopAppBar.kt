package woowacourse.kanban.board.feature.board.component.dialog.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskDialogTopAppBar(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "닫기 버튼",
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskDialogTopAppBarPreview() {
    TaskDialogTopAppBar(
        title = "새 태스크 생성",
        onClick = { },
        modifier = Modifier.width(672.dp),
    )
}
