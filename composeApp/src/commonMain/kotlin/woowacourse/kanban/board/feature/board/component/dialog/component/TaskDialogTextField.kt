package woowacourse.kanban.board.feature.board.component.dialog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskDialogTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        maxLines = maxLines,
        isError = isError,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            cursorColor = Color.Black,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.DarkGray,
            errorContainerColor = Color.White,
            errorIndicatorColor = Color.Red,
            errorTextColor = Color.Red,
        ),
        placeholder = {
            if (!placeholder.isNullOrBlank()) {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                )
            }
        },
        trailingIcon = {
            if (isError) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "입력값 에러",
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TaskDialogTextFieldPreview() {
    var value by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TaskDialogTextField(
            value = value,
            onValueChanged = {
                value = it
                isError = value.length > 10
            },
            isError = isError,
            placeholder = "기본 텍스트 필드",
        )

        TaskDialogTextField(
            value = value,
            onValueChanged = {
                value = it
                isError = value.length > 10
            },
            isError = isError,
            placeholder = "높이가 있는 텍스트 필드",
            modifier = Modifier.height(110.dp),
        )
    }
}
