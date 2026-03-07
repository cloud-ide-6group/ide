@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import front.composeapp.generated.resources.Res
import front.composeapp.generated.resources.visibility_off_24dp
import front.composeapp.generated.resources.visibility_on_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.component.CodeTogetherButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Экран авторизации
 *
 * @param modifier Modifier, который будет применён к данному экрану
 * @param onSignUpClick Коллбек, вызывающийся при клике на кнопку перехода на экран регистрации
 * @param viewModel Вьюмодель
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    Row(
        modifier = modifier
            .background(CodeTogetherTheme.colors.secondaryBackground)
            .padding(32.dp)
            .fillMaxSize()
    ) {
        LeftSide()
        RightSide(
            email = uiState.value.email,
            password = uiState.value.password,
            isPasswordVisible = uiState.value.isPasswordVisible,
            onEmailChange = { email ->
                viewModel.processCommand(LoginCommand.ChangeEmail(email))
            },
            onPasswordChange = { password ->
                viewModel.processCommand(LoginCommand.ChangePassword(password))
            },
            onLoginClick = {
                viewModel.processCommand(LoginCommand.ClickLogin)
            },
            onChangePasswordVisibilityClick = {
                viewModel.processCommand(LoginCommand.ChangePasswordVisibility)
            },
            onSignUpClick = onSignUpClick,
        )
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
 * @param email Текущая почта
 * @param password Текущий пароль
 * @param isPasswordVisible Текущее состояние видимости пароля
 * @param onEmailChange Коллбек, вызывающийся при изменении почты
 * @param onPasswordChange Коллбек, вызывающийся при изменении пароля
 * @param onLoginClick Коллбек, вызывающийся при клике на кнопку логин
 * @param onChangePasswordVisibilityClick Коллбек, вызывающийся при клике на смену видимости пароля
 * @param onSignUpClick Коллбек, вызывающийся при клике на кнопку перехода на экран регистрации
 */
@Composable
private fun RowScope.RightSide(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onChangePasswordVisibilityClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    SideColumn(modifier = modifier) {
        LoginCard(
            email = email,
            password = password,
            isPasswordVisible = isPasswordVisible,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
            onChangePasswordVisibilityClick = onChangePasswordVisibilityClick
        )
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
 * Карточка авторизации
 *
 * @param modifier Modifier, который будет применён к данной части
 * @param email Текущая почта
 * @param password Текущий пароль
 * @param isPasswordVisible Текущее состояние видимости пароля
 * @param onEmailChange Коллбек, вызывающийся при изменении почты
 * @param onPasswordChange Коллбек, вызывающийся при изменении пароля
 * @param onLoginClick Коллбек, вызывающийся при клике на кнопку логин
 * @param onChangePasswordVisibilityClick Коллбек, вызывающийся при клике на смену видимости пароля
 * @param onSignUpClick Коллбек, вызывающийся при клике на кнопку перехода на экран регистрации
 */
@Composable
private fun LoginCard(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onChangePasswordVisibilityClick: () -> Unit,
    onSignUpClick: () -> Unit,
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
                value = email,
                hint = "Your Email",
                onValueChange = onEmailChange
            )
            Section(
                sectionName = "Password",
                value = password,
                hint = "Your Password",
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val iconRes = when (isPasswordVisible) {
                        true -> Res.drawable.visibility_off_24dp
                        false -> Res.drawable.visibility_on_24dp
                    }
                    CodeTogetherButton(
                        onClick = onChangePasswordVisibilityClick,
                    ) {
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = "Change password visibility",
                            tint = CodeTogetherTheme.colors.primary
                        )
                    }
                },
                onValueChange = onPasswordChange
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
                onClick = onLoginClick
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
                    onClick = onSignUpClick
                )
            }
        }
    }
}