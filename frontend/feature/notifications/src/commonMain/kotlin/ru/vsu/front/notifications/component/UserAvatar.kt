package ru.vsu.front.notifications.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kotlin.io.encoding.Base64

/**
 * Аватар пользователя с возможностью его изменения.
 *
 * @param photoBase64 Строка изображения в формате Base64.
 * @param modifier Модификатор для настройки.
 * @param shape Форма изображения.
 */

@Composable
internal fun UserAvatar(
    photoBase64: String,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    val decodedImage = remember(photoBase64) {
        Base64.decode(photoBase64)
    }

    Box(
        modifier = modifier
            .clip(shape),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(decodedImage)
                .build(),
            contentDescription = "User Avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}