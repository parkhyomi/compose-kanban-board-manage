package woowacourse.kanban.board.feature.board.component.dialog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AssigneeOptionCard(name: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TaskOptionCard(
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier,
        paddingValues = PaddingValues(vertical = 20.dp, horizontal = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "기본 이미지",
                modifier = Modifier.size(24.dp),
            )

            Text(
                text = name,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AssigneeOptionCardPreview() {
    var isSelected by remember { mutableStateOf(false) }

    AssigneeOptionCard(
        name = "다이노",
        isSelected = isSelected,
        onClick = { isSelected = !isSelected },
    )
}
