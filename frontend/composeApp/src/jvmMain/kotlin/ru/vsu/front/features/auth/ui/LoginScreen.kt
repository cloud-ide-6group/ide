package ru.vsu.front.features.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import ru.vsu.front.features.auth.domain.validation.EmailMatcher
import ru.vsu.front.features.auth.ui.component.AuthCard
import ru.vsu.front.features.auth.ui.component.AuthScaffoldWrapper
import ru.vsu.front.features.auth.ui.component.LeftSide
import ru.vsu.front.features.auth.ui.component.SideColumn

/**
 * Экран авторизации (входа в аккаунт).
 *
 * @param modifier Модификатор для настройки.
 * @param onSignUpClick Коллбек для перехода на экран регистрации.
 * @param viewModel Вьюмодель.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiStateLogin.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isValidEmail = uiState.value.email.isEmpty() || EmailMatcher.isValid(uiState.value.email)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEffect.ShowError -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    AuthScaffoldWrapper(
        snackbarHostState = snackbarHostState
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
        ) {
            LeftSide()
            RightSide(
                email = uiState.value.email,
                password = uiState.value.password,
                isNotValidEmail = !isValidEmail,
                isPasswordVisible = uiState.value.isPasswordVisible,
                buttonEnabled = uiState.value.buttonEnabled,
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
}

/**
 * Правая часть экрана.
 *
 * @param email Введенная почта.
 * @param password Введенный пароль.
 * @param isNotValidEmail Введенная почта не прошла или прошла валидатор.
 * @param isPasswordVisible Состояние видимости пароля.
 * @param onEmailChange Коллбек изменения текста почты.
 * @param onPasswordChange Коллбек изменения текста пароля.
 * @param onLoginClick Коллбек попытки входа.
 * @param onChangePasswordVisibilityClick Коллбек переключения видимости пароля.
 * @param onSignUpClick Коллбек перехода к регистрации.
 * @param buttonEnabled Доступность клика на кнопку.
 * @param modifier Модификатор для настройки.
 */
@Composable
private fun RowScope.RightSide(
    email: String,
    password: String,
    isNotValidEmail: Boolean,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onChangePasswordVisibilityClick: () -> Unit,
    onSignUpClick: () -> Unit,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    SideColumn(modifier = modifier) {
        LoginCard(
            email = email,
            password = password,
            isNotValidEmail = isNotValidEmail,
            isPasswordVisible = isPasswordVisible,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = onLoginClick,
            onChangePasswordVisibilityClick = onChangePasswordVisibilityClick,
            onSignUpClick = onSignUpClick,
            buttonEnabled = buttonEnabled,
        )
    }
}

/**
 * Карточка с полями ввода и кнопками для авторизации.
 *
 * @param email Введенная почта.
 * @param password Введенный пароль.
 * @param isNotValidEmail Введенная почта не прошла или прошла валидатор.
 * @param isPasswordVisible Состояние видимости пароля.
 * @param onEmailChange Коллбек изменения текста почты.
 * @param onPasswordChange Коллбек изменения текста пароля.
 * @param onLoginClick Коллбек попытки входа.
 * @param onChangePasswordVisibilityClick Коллбек переключения видимости пароля.
 * @param onSignUpClick Коллбек перехода к регистрации.
 * @param buttonEnabled Доступность клика на кнопку.
 * @param modifier Модификатор для настройки.
 */
@Composable
private fun LoginCard(
    email: String,
    password: String,
    isNotValidEmail: Boolean,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onChangePasswordVisibilityClick: () -> Unit,
    onSignUpClick: () -> Unit,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    AuthCard {
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
                onValueChange = onEmailChange,
                isError = isNotValidEmail
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
                enabled = buttonEnabled,
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