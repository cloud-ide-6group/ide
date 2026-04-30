package ru.vsu.front.authorization.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
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
import ru.vsu.front.authorization.AuthCommand
import ru.vsu.front.authorization.AuthViewModel
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.component.VisibilityButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.domain.validation.EmailMatcher

/**
 * Компонент формы авторизации и регистрации.
 *
 * @param authViewModel Вьюмодель экрана аутентификации.
 * @param modifier Модификатор для настройки.
 */
@Composable
internal fun AuthForm(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    var isSignUpMode by remember { mutableStateOf(false) }
    val state by authViewModel.uiState.collectAsStateWithLifecycle()
    val isNotValidEmail = state.email.isNotEmpty() && !EmailMatcher.isValid(state.email.trim())

    SideColumn(modifier = modifier) {
        AuthCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CodeTogetherText(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = if (isSignUpMode) "Sign Up" else "Welcome Back!",
                    style = TextStyle(fontSize = 24.sp, fontFamily = FontFamily.Monospace)
                )

                AnimatedVisibility(
                    visible = isSignUpMode,
                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                ) {
                    Section(
                        modifier = Modifier.padding(bottom = 16.dp),
                        sectionName = "Name",
                        value = state.name,
                        hint = "Your Name",
                        onValueChange = { authViewModel.processCommand(AuthCommand.ChangeName(it)) }
                    )
                }

                Section(
                    modifier = Modifier.padding(bottom = 16.dp),
                    sectionName = "Email",
                    value = state.email,
                    hint = "Your Email",
                    isError = isNotValidEmail,
                    onValueChange = { authViewModel.processCommand(AuthCommand.ChangeEmail(it)) }
                )

                Section(
                    modifier = Modifier.padding(bottom = 16.dp),
                    sectionName = "Password",
                    value = state.password,
                    hint = "Your Password",
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        VisibilityButton(
                            isVisible = state.isPasswordVisible,
                            onClick = { authViewModel.processCommand(AuthCommand.ChangePasswordVisibility) }
                        )
                    },
                    onValueChange = { authViewModel.processCommand(AuthCommand.ChangePassword(it)) }
                )

                AnimatedVisibility(
                    visible = isSignUpMode,
                    enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Section(
                        modifier = Modifier.padding(bottom = 16.dp),
                        sectionName = "Confirm Password",
                        value = state.confirmedPassword,
                        hint = "Confirm Password",
                        visualTransformation = if (state.isConfirmedPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            VisibilityButton(
                                isVisible = state.isConfirmedPasswordVisible,
                                onClick = { authViewModel.processCommand(AuthCommand.ChangeConfirmedPasswordVisibility) }
                            )
                        },
                        onValueChange = { authViewModel.processCommand(AuthCommand.ChangeConfirmedPassword(it)) }
                    )
                }

                CodeTogetherTextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    text = if (isSignUpMode) "Sign Up" else "Log In",
                    enabled = state.buttonEnabled,
                    unHoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                    style = CodeTogetherTheme.typography.style.copy(fontWeight = FontWeight.Bold),
                    onClick = {
                        if (isSignUpMode) {
                            authViewModel.processCommand(AuthCommand.ClickSignUp)
                        } else {
                            authViewModel.processCommand(AuthCommand.ClickLogin)
                        }
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CodeTogetherText(text = if (isSignUpMode) "Already have an account?" else "Don't have an account?")
                    CodeTogetherTextButton(
                        text = if (isSignUpMode) "Log In" else "Sign Up",
                        textColor = CodeTogetherTheme.colors.primary,
                        style = CodeTogetherTheme.typography.style.copy(fontWeight = FontWeight.Bold),
                        onClick = { isSignUpMode = !isSignUpMode }
                    )
                }
            }
        }
    }
}