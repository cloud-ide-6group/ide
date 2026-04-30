package ru.vsu.front.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.component.VisibilityButton
import ru.vsu.front.domain.validation.EmailMatcher

/**
 * Секция профиля.
 *
 * @param name Имя пользователя.
 * @param email Почта пользователя.
 * @param currentPassword Текущий пароль пользователя.
 * @param newPassword Новый пароль пользователя.
 * @param hasChangesName Имя отличается.
 * @param hasChangesEmail Почта отличается.
 * @param hasChangesPassword Пароль отличается.
 * @param isCurrentPasswordVisible Видимость текущего пароля.
 * @param isNewPasswordVisible Видимость нового пароля.
 * @param modifier Modifier для настройки.
 * @param onNameChange Коллбек, вызывающийся при изменении имени.
 * @param onEmailChange Коллбек, вызывающийся при изменении почты.
 * @param onCurrentPasswordChange Коллбек, вызывающийся при изменении текущего пароля.
 * @param onNewPasswordChange Коллбек, вызывающийся при изменении нового пароля.
 * @param onCurrentPasswordVisibilityChange Коллбек, вызывающийся при изменении видимости текущего пароля.
 * @param onNewPasswordVisibilityChange Коллбек, вызывающийся при изменении видимости нового пароля.
 * @param onUpdateDataClick Коллбек, вызывающийся при клике на обновление имени или почты.
 * @param onUpdatePasswordClick Коллбек, вызывающийся при клике на обновление пароля.
 */
@Composable
fun ProfileSections(
    name: String,
    email: String,
    currentPassword: String,
    newPassword: String,
    hasChangesName: Boolean,
    hasChangesEmail: Boolean,
    hasChangesPassword: Boolean,
    isCurrentPasswordVisible: Boolean,
    isNewPasswordVisible: Boolean,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onCurrentPasswordVisibilityChange: () -> Unit,
    onNewPasswordVisibilityChange: () -> Unit,
    onUpdateDataClick: () -> Unit,
    onUpdatePasswordClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Section(
            sectionName = "Name",
            value = name,
            hint = "Name",
            onValueChange = onNameChange,
            trailingIcon = {
                if (hasChangesName) {
                    ConfirmIcon(
                        onClick = onUpdateDataClick,
                    )
                }
            }
        )

        Section(
            sectionName = "Email",
            value = email,
            hint = "Email",
            onValueChange = onEmailChange,
            isError = !EmailMatcher.isValid(email),
            trailingIcon = {
                if (hasChangesEmail) {
                    ConfirmIcon(
                        onClick = onUpdateDataClick,
                    )
                }
            }
        )

        Section(
            sectionName = "Current Password",
            value = currentPassword,
            hint = "Your Current Password",
            onValueChange = onCurrentPasswordChange,
            visualTransformation = if (isCurrentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                VisibilityButton(
                    isVisible = isCurrentPasswordVisible,
                    onClick = onCurrentPasswordVisibilityChange
                )

            }
        )

        Section(
            sectionName = "New Password",
            value = newPassword,
            hint = "Your New Password",
            onValueChange = onNewPasswordChange,
            visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (hasChangesPassword) {
                        ConfirmIcon(
                            onClick = onUpdatePasswordClick,
                        )
                    }
                    VisibilityButton(
                        isVisible = isNewPasswordVisible,
                        onClick = onNewPasswordVisibilityChange
                    )
                }
            }
        )
    }
}