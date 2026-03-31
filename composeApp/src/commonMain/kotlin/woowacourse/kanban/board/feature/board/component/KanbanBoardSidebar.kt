package woowacourse.kanban.board.feature.board.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.board.core.designsystem.theme.KanbanLightBlue

@Composable
fun KanbanBoardSidebar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White)
            .width(255.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "프로젝트",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = "4주차 미션 보드",
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }

        HorizontalDivider(thickness = Dp.Hairline, color = Color.LightGray)

        Column(
            modifier = Modifier.padding(16.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SideOptionSelectButton(
                text = "Compose1",
                isSelected = true,
                onClick = {},
            )
            SideOptionSelectButton(
                text = "Compose2",
                isSelected = false,
                onClick = {},
            )
            SideOptionSelectButton(
                text = "Compose3너무너무너무너무길어요",
                isSelected = false,
                onClick = {},
            )
        }
    }
}

@Composable
private fun SideOptionSelectButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val backgroundColor = if (isSelected) Color.KanbanLightBlue else Color.White
    val textColor = if (isSelected) Color.Blue else Color.Black

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .width(220.dp)
            .padding(vertical = 12.dp, horizontal = 15.dp),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun KanbanBoardSidebarPreview() {
    KanbanBoardSidebar()
}
