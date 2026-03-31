package woowacourse.kanban.board.feature.board.component.dialog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun TaskFieldLabel(label: String, modifier: Modifier = Modifier, isRequired: Boolean = false) {
    val text = if (isRequired) "$label *" else label

    Text(
        text = text,
        fontSize = 14.sp,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun TaskFieldLabelPreview() {
    Column {
        TaskFieldLabel(
            label = "제목",
            isRequired = true,
        )

        TaskFieldLabel(label = "설명")
    }
}
