package woowacourse.kanban.board.feature.board.component.dialog.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun StatusOptionCard(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TaskOptionCard(
        isSelected = isSelected,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Blue else Black,
            fontSize = 16.sp,
            modifier = modifier.align(Alignment.Center),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatusOptionCardPreview() {
    var isSelected by remember { mutableStateOf(false) }

    StatusOptionCard(
        text = "To Do",
        isSelected = isSelected,
        onClick = { isSelected = !isSelected },
    )
}
