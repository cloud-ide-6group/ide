package ru.vsu.front.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.component.VisibilityButton

@Composable
fun ProfileSections(
    name: String,
    email: String,
    currentPassword: String,
    newPassword: String,
    isCurrentPasswordVisible: Boolean,
    isNewPasswordVisible: Boolean,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onChangeCurrentPasswordVisibility: () -> Unit,
    onChangeNewPasswordVisibility: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Section(
            sectionName = "Name",
            value = name,
            hint = "Name",
            onValueChange = onNameChange
        )

        Section(
            sectionName = "Email",
            value = email,
            hint = "Email",
            onValueChange = onEmailChange
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
                VisibilityButton(
                    isVisible = isNewPasswordVisible,
                    onClick = onChangeNewPasswordVisibility
                )
            }
        )
    }
}