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
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.component.VisibilityButton
import ru.vsu.front.domain.validation.EmailMatcher

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
    onChangeCurrentPasswordVisibility: () -> Unit,
    onChangeNewPasswordVisibility: () -> Unit,
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
                    onClick = onChangeCurrentPasswordVisibility
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
                        onClick = onChangeNewPasswordVisibility
                    )
                }
            }
        )
    }
}