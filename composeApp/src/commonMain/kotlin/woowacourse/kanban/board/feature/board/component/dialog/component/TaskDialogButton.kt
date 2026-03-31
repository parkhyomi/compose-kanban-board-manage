package woowacourse.kanban.board.feature.board.component.dialog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskDialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Black,
    disabledContainerColor: Color = Color.LightGray,
    disabledContentColor: Color = Color.White,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        ),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun TaskDialogButtonPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TaskDialogButton(
            text = "취소",
            onClick = {},
        )

        TaskDialogButton(
            text = "생성",
            enabled = false,
            containerColor = Color.Blue,
            contentColor = Color.White,
            onClick = {},
        )

        TaskDialogButton(
            text = "생성",
            enabled = true,
            containerColor = Color.Blue,
            contentColor = Color.White,
            onClick = {},
        )
    }
}
