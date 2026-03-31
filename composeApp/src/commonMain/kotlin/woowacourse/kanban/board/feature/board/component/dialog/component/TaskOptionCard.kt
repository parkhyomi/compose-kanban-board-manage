package woowacourse.kanban.board.feature.board.component.dialog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskOptionCard(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(vertical = 14.dp, horizontal = 16.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val backgroundColor = if (isSelected) Color.Blue.copy(alpha = 0.1f) else Color.White
    val borderColor = if (isSelected) Color.Blue else Color.LightGray
    val cardShape = RoundedCornerShape(10.dp)

    Box(
        modifier = modifier
            .clip(cardShape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = cardShape,
            )
            .width(200.dp)
            .padding(paddingValues = paddingValues),
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskOptionCardPreview() {
    var isSelected by remember { mutableStateOf(false) }

    TaskOptionCard(
        isSelected = isSelected,
        onClick = { isSelected = !isSelected },
    ) {
        Text(
            text = "To Do",
            textAlign = TextAlign.Center,
            color = if (isSelected) Color.Blue else Black,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
