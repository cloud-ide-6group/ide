package ru.vsu.front.projects.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.projects.ProjectViewModel

/**
 * Модуль проекта.
 * * Отвечает за предоставление ViewModel для экранов.
 * * Что внутри:
 * - [ProjectViewModel] - вьюмодель экрана проекта.
 */
val projectModule = module {
    viewModel { (projectId: Int) ->
        ProjectViewModel(
            projectId = projectId,
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}