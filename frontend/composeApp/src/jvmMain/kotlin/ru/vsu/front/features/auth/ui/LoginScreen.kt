@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Экран авторизации
 *
 * @param modifier Modifier, который будет применён к данному экрану
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(CodeTogetherTheme.colors.secondaryBackground)
            .padding(32.dp)
            .fillMaxSize()
    ) {
        LeftSide()
        RightSide()
    }
}

/**
 * Левая часть экрана
 *
 * @param modifier Modifier, который будет применён к данной части
 */
@Composable
private fun RowScope.LeftSide(
    modifier: Modifier = Modifier,
) {
    SideColumn(modifier = modifier) {
        CodeTogetherText(
            text = "Let's Code Together!",
            style = CodeTogetherTheme.typography.style.copy(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = modifier.height(8.dp))
        CodeTogetherText(
            text = "Enjoy programming!",
            color = CodeTogetherTheme.colors.primary,
            style = CodeTogetherTheme.typography.style.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )
    }
}

/**
 * Права часть экрана
 *
 * @param modifier Modifier, который будет применён к данной части
 */
@Composable
private fun RowScope.RightSide(
    modifier: Modifier = Modifier,
) {
    SideColumn(modifier = modifier) {
        SignCard()
    }
}

/**
 * Устранение дублирования, функция для использования корневого столбца в левой и правой части
 *
 * @param modifier Modifier, который будет применён к данной части
 * @param content Слот под контент
 */
@Composable
private fun RowScope.SideColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}


/**
 * Карточка регистрации
 *
 * @param modifier Modifier, который будет применён к карточке
 */
@Composable
private fun SignCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(400.dp),
        colors = CardDefaults.cardColors(
            containerColor = CodeTogetherTheme.colors.primaryBackground,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CodeTogetherText(
                text = "Welcome Back!",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace
                ),
            )
            Section(
                sectionName = "Email",
                value = "",
                hint = "Your Email",
                onValueChange = {

                }
            )
            Section(
                sectionName = "Password",
                value = "qwerty123",
                hint = "Your Password",
                onValueChange = {

                }
            )
            CodeTogetherTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Log In",
                colors = ButtonDefaults.textButtonColors(
                    containerColor = CodeTogetherTheme.colors.primary.copy(0.1f),
                    contentColor = CodeTogetherTheme.colors.black.copy(0.005f)
                ),
                style = CodeTogetherTheme.typography.style.copy(
                    fontWeight = FontWeight.Bold
                ),
                onClick = {

                }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CodeTogetherText(text = "Don't have an account?")
                CodeTogetherTextButton(
                    text = "Sign Up",
                    textColor = CodeTogetherTheme.colors.primary,
                    colors = ButtonDefaults.textButtonColors(contentColor = CodeTogetherTheme.colors.primary.copy(0.1f)),
                    style = CodeTogetherTheme.typography.style.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    onClick = {

                    }
                )
            }
        }
    }
}