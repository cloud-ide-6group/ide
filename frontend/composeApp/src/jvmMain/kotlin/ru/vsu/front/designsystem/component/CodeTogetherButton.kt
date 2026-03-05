@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import front.composeapp.generated.resources.Res
import front.composeapp.generated.resources.close_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Code Together кнопка
 *
 * @param onClick Коллбек, вызывающийся при клике на кнопку
 * @param modifier Modifier который будет применён к кнопке
 * @param shape Фигура, которая будет применена к кнопке
 * @param content Слот под контент
 */
@Composable
fun CodeTogetherButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = IconButtonDefaults.standardShape,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
    ) {
        content()
    }
}

@Preview
@Composable
private fun CodeTogetherButtonPreview() {
    CodeTogetherTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeTogetherTheme.colors.primaryBackground),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {

                },
                modifier = Modifier,
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.close_24dp),
                    contentDescription = "",
                    tint = CodeTogetherTheme.colors.error
                )
            }

            IconButton(
                onClick = {

                },
                modifier = Modifier,
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.close_24dp),
                    contentDescription = "",
                    tint = CodeTogetherTheme.colors.primary
                )
            }
        }
    }
}