package ru.vsu.front.profile.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.common.localIconRes
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Список кнопок выбора языка программирования.
 *
 * @param languages Список языков программирования.
 * @param state Состояние прокрутки списка.
 * @param modifier Модификатор для настройки.
 * @param onLanguageClick Коллбек, срабатывающий при нажатии на язык.
 */
@Composable
internal fun LanguageButtonItems(
    languages: List<ProgramingLanguage>,
    state: ScrollState = rememberScrollState(),
    modifier: Modifier = Modifier,
    onLanguageClick: (ProgramingLanguage) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(state),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        languages.forEach { language ->
            LanguageButtonItem(
                programingLanguage = language,
                icon = language.localIconRes,
                onClick = onLanguageClick
            )
        }
    }
}