package ru.vsu.front.network

/**
 * Константы с эндпоинтами API для сетевых запросов к бэкенду.
 */
object SocketRoutes {
    /**
     * Подписка на деревья файлов проекта.
     */
    const val FILES_TREES_LIST = "files_trees_list"
    /**
     * Подключение к комнате проекта.
     */
    const val JOIN_PROJECT_ROOM = "join_project_room"
    /**
     * Отправка ввода (строки) в выполняемую программу.
     */
    const val SEND_FILE_CONTENT = "send_file_content"
    /**
     * Подписка на содержимое файла (строки).
     */
    const val GET_FILE_CONTENT = "get_file_content"
    /**
     * Обновление содержимого файла (строки).
     */
    const val UPDATE_FILE_CONTENT = "update_file_content"
    /**
     * Запуск программы.
     */
    const val RUN_CODE = "run_code"
    /**
     * Остановить выполняемую программу.
     */
    const val STOP_CODE = "stop_code"
    /**
     * Подписка на вывод выполняемой программы.
     */
    const val CONSOLE_OUTPUT = "console_output"
    /**
     * Отправка ввода (строки) в выполняемую программу.
     */
    const val SEND_INPUT = "send_input"
    /**
     * Подключение к комнате чата.
     */
    const val JOIN_CHAT_ROOM = "join_chat_room"
    /**
     * Выход из комнаты чата.
     */
    const val LEAVE_CHAT_ROOM = "leave_chat_room"
    /**
     * Выход из комнаты проекта.
     */
    const val LEAVE_PROJECT_ROOM = "leave_project_room"
    /**
     * Подписка на событие исключения текущего пользователя из проекта.
     */
    const val REMOVED_FROM_PROJECT = "removed_from_project"
    /**
     * Подписка на сообщения.
     */
    const val GET_MESSAGES = "get_messages"
    /**
     * Подписка на уведомления.
     */
    const val NOTIFICATIONS_LIST = "notifications_list"
    /**
     * Подписка на конец подписки.
     */
    const val SUBSCRIPTION_EXPIRED  = "subscription_expired"
}