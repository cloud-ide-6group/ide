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
 * Экран регистрации.
 *
 * @param modifier Модификатор для настройки.
 * @param onLoginClick Коллбек для возврата на экран авторизации.
 * @param viewModel Вьюмодель.
 */
@Composable
fun SignScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    viewModel: SignViewModel
) {
    val uiState = viewModel.uiStateSign.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isValidEmail = uiState.value.email.isEmpty() || EmailMatcher.isValid(uiState.value.email)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignEffect.ShowError -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    AuthScaffoldWrapper(
        snackbarHostState = snackbarHostState,
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
        ) {
            LeftSide()
            RightSide(
                name = uiState.value.name,
                email = uiState.value.email,
                isNotValidEmail = !isValidEmail,
                password = uiState.value.password,
                confirmedPassword = uiState.value.confirmedPassword,
                isPasswordVisible = uiState.value.isPasswordVisible,
                isConfirmedPasswordVisible = uiState.value.isConfirmedPasswordVisible,
                onNameChange = { name ->
                    viewModel.processCommand(SignCommand.ChangeName(name))
                },
                onEmailChange = { email ->
                    viewModel.processCommand(SignCommand.ChangeEmail(email))
                },
                onPasswordChange = { password ->
                    viewModel.processCommand(SignCommand.ChangePassword(password))
                },
                onConfirmedPasswordChange = { confirmedPassword ->
                    viewModel.processCommand(SignCommand.ChangeConfirmedPassword(confirmedPassword))
                },
                onSignUpClick = {
                    viewModel.processCommand(SignCommand.ClickSignUp)
                },
                onChangePasswordVisibilityClick = {
                    viewModel.processCommand(SignCommand.ChangePasswordVisibility)
                },
                onChangeConfirmedPasswordVisibilityClick = {
                    viewModel.processCommand(SignCommand.ChangeConfirmedPasswordVisibility)
                },
                onLoginClick = onLoginClick,
            )
        }
    }
}

/**
 * Правая часть экрана.
 *
 * @param modifier Модификатор для настройки.
 * @param name Введенное имя.
 * @param email Введенная почта.
 * @param isNotValidEmail Введенная почта прошла или не прошла проверку.
 * @param password Введенный пароль.
 * @param confirmedPassword Введенный пароль для подтверждения.
 * @param isPasswordVisible Состояние видимости основного пароля.
 * @param isConfirmedPasswordVisible Состояние видимости подтвержденного пароля.
 * @param onNameChange Коллбек изменения имени.
 * @param onEmailChange Коллбек изменения почты.
 * @param onPasswordChange Коллбек изменения пароля.
 * @param onConfirmedPasswordChange Коллбек изменения подтверждения пароля.
 * @param onSignUpClick Коллбек попытки регистрации.
 * @param onChangePasswordVisibilityClick Коллбек видимости основного пароля.
 * @param onChangeConfirmedPasswordVisibilityClick Коллбек видимости подтвержденного пароля.
 * @param onLoginClick Коллбек перехода к авторизации.
 */
@Composable
private fun RowScope.RightSide(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    isNotValidEmail: Boolean,
    password: String,
    confirmedPassword: String,
    isPasswordVisible: Boolean,
    isConfirmedPasswordVisible: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmedPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onChangePasswordVisibilityClick: () -> Unit,
    onChangeConfirmedPasswordVisibilityClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    SideColumn(modifier = modifier) {
        SignCard(
            name = name,
            email = email,
            isNotValidEmail = isNotValidEmail,
            password = password,
            confirmedPassword = confirmedPassword,
            isPasswordVisible = isPasswordVisible,
            isConfirmedPasswordVisible = isConfirmedPasswordVisible,
            onNameChange = onNameChange,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onConfirmedPasswordChange = onConfirmedPasswordChange,
            onSignUpClick = onSignUpClick,
            onChangePasswordVisibilityClick = onChangePasswordVisibilityClick,
            onChangeConfirmedPasswordVisibilityClick = onChangeConfirmedPasswordVisibilityClick,
            onLoginClick = onLoginClick,
        )
    }
}

/**
 * Карточка с полями ввода и кнопками для регистрации.
 *
 * @param modifier Модификатор настройки.
 * @param name Введенное имя.
 * @param email Введенная почта.
 * @param isNotValidEmail Введенная почта прошла или не прошла проверку.
 * @param password Введенный пароль.
 * @param confirmedPassword Введенный пароль для подтверждения.
 * @param isPasswordVisible Состояние видимости основного пароля.
 * @param isConfirmedPasswordVisible Состояние видимости подтвержденного пароля.
 * @param onNameChange Коллбек ввода имени.
 * @param onEmailChange Коллбек ввода почты.
 * @param onPasswordChange Коллбек ввода пароля.
 * @param onConfirmedPasswordChange Коллбек ввода подтверждения пароля.
 * @param onSignUpClick Коллбек нажатия на кнопку регистрации.
 * @param onChangePasswordVisibilityClick Коллбек нажатия на иконку видимости основного пароля.
 * @param onChangeConfirmedPasswordVisibilityClick Коллбек нажатия на иконку видимости подтверждения пароля.
 * @param onLoginClick Коллбек нажатия на кнопку возврата к авторизации.
 */
@Composable
private fun SignCard(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    isNotValidEmail: Boolean,
    password: String,
    confirmedPassword: String,
    isPasswordVisible: Boolean,
    isConfirmedPasswordVisible: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmedPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onChangePasswordVisibilityClick: () -> Unit,
    onChangeConfirmedPasswordVisibilityClick: () -> Unit,
    onLoginClick: () -> Unit,
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
                text = "Sign Up",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace
                ),
            )
            Section(
                sectionName = "Name",
                value = name,
                hint = "Your Name",
                onValueChange = onNameChange
            )
            Section(
                sectionName = "Email",
                value = email,
                hint = "Your Email",
                isError = isNotValidEmail,
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
            Section(
                sectionName = "Confirm Password",
                value = confirmedPassword,
                hint = "Confirm Password",
                visualTransformation = if (isConfirmedPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val iconRes = when (isConfirmedPasswordVisible) {
                        true -> Res.drawable.visibility_off_24dp
                        false -> Res.drawable.visibility_on_24dp
                    }
                    CodeTogetherButton(
                        onClick = onChangeConfirmedPasswordVisibilityClick,
                    ) {
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = "Change password visibility",
                            tint = CodeTogetherTheme.colors.primary
                        )
                    }
                },
                onValueChange = onConfirmedPasswordChange
            )
            CodeTogetherTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Sign Up",
                colors = ButtonDefaults.textButtonColors(
                    containerColor = CodeTogetherTheme.colors.primary.copy(0.1f),
                    contentColor = CodeTogetherTheme.colors.black.copy(0.005f)
                ),
                style = CodeTogetherTheme.typography.style.copy(
                    fontWeight = FontWeight.Bold
                ),
                onClick = onSignUpClick
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CodeTogetherText(text = "Already have an account?")
                CodeTogetherTextButton(
                    text = "Log In",
                    textColor = CodeTogetherTheme.colors.primary,
                    colors = ButtonDefaults.textButtonColors(contentColor = CodeTogetherTheme.colors.primary.copy(0.1f)),
                    style = CodeTogetherTheme.typography.style.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    onClick = onLoginClick
                )
            }
        }
    }
}