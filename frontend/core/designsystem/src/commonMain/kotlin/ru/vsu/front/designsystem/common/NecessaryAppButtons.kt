package ru.vsu.front.designsystem.common

/**
 * Базовые системные кнопки для управления окном десктопного приложения.
 */
enum class NecessaryAppButtons {

    /**
     * Кнопка "Свернуть".
     */
    Minimize,

    /**
     * Кнопка "Свернуть в окно".
     */
    Maximize,

    /**
     * Кнопка "Закрыть".
     */
    Close;

    companion object {
        /**
         * Фиксированный размер (ширина и высота) кнопки в dp.
         */
        const val NECESSARY_BUTTON_SIZE_IN_DP = 40
    }
}