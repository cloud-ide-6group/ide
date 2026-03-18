@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import ru.vsu.front.features.auth.domain.validation.EmailMatcher
import ru.vsu.front.features.auth.ui.component.AuthCard
import ru.vsu.front.features.auth.ui.component.LeftSide
import ru.vsu.front.features.auth.ui.component.SideColumn

/**
 * Экран регистрации
 *
 * @param modifier Modifier, который будет применён к данному экрану
 * @param onLoginClick Коллбек, вызывающийся при клике на кнопку перехода на экран авторизации
 * @param viewModel Вьюмодель
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

    Scaffold(
        modifier = modifier
            .background(CodeTogetherTheme.colors.secondaryBackground)
            .padding(32.dp)
            .fillMaxSize(),
        containerColor = CodeTogetherTheme.colors.secondaryBackground,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                CodeTogetherText(
                    modifier = Modifier
                        .background(CodeTogetherTheme.colors.black.copy(alpha = 0.1f))
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = it.visuals.message,
                    style = CodeTogetherTheme.typography.style.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    color = CodeTogetherTheme.colors.error
                )
            }
        }
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
 * Права часть экрана
 *
 * @param modifier Modifier, который будет применён к данной части
 * @param name Имя пользователя
 * @param email Текущая почта
 * @param password Текущий пароль
 * @param confirmedPassword Текущий подтвержденный пароль
 * @param isPasswordVisible Текущее состояние видимости пароля
 * @param isConfirmedPasswordVisible Текущее состояние видимости подтвержденного пароля
 * @param onNameChange Коллбек, вызывающийся при изменении имени
 * @param onEmailChange Коллбек, вызывающийся при изменении почты
 * @param onPasswordChange Коллбек, вызывающийся при изменении пароля
 * @param onConfirmedPasswordChange Коллбек, вызывающийся при изменении подтвержденного пароля
 * @param onSignUpClick Коллбек, вызывающийся при клике на кнопку регистрации
 * @param onChangePasswordVisibilityClick Коллбек, вызывающийся при клике на смену видимости пароля
 * @param onChangeConfirmedPasswordVisibilityClick Коллбек, вызывающийся при клике на смену видимости подтвержденного пароля
 * @param onLoginClick Коллбек, вызывающийся при клике на кнопку перехода на экран авторизации
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
 * Карточка регистрации
 *
 * @param modifier Modifier, который будет применён к данной части
 * @param name Имя пользователя
 * @param email Текущая почта
 * @param password Текущий пароль
 * @param confirmedPassword Текущий подтвержденный пароль
 * @param isPasswordVisible Текущее состояние видимости пароля
 * @param isConfirmedPasswordVisible Текущее состояние видимости подтвержденного пароля
 * @param onNameChange Коллбек, вызывающийся при изменении имени
 * @param onEmailChange Коллбек, вызывающийся при изменении почты
 * @param onPasswordChange Коллбек, вызывающийся при изменении пароля
 * @param onConfirmedPasswordChange Коллбек, вызывающийся при изменении подтвержденного пароля
 * @param onSignUpClick Коллбек, вызывающийся при клике на кнопку регистрации
 * @param onChangePasswordVisibilityClick Коллбек, вызывающийся при клике на смену видимости пароля
 * @param onChangeConfirmedPasswordVisibilityClick Коллбек, вызывающийся при клике на смену видимости подтвержденного пароля
 * @param onLoginClick Коллбек, вызывающийся при клике на кнопку перехода на экран авторизации
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