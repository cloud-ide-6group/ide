@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.designsystem.common

/**
 * Необходимые кнопки на всех экранах
 *
 * @see Minimize Кнопка "Свернуть"
 * @see Maximize Кнопка "Свернуть в окно"
 * @see Close Кнопка "Закрыть"
 */
enum class NecessaryAppButtons {
    Minimize, Maximize, Close;

    /**
     * @see NECESSARY_BUTTON_SIZE_IN_DP Минимальный размер (size) кнопки в dp
     */
    companion object {
        const val NECESSARY_BUTTON_SIZE_IN_DP = 40
    }
}