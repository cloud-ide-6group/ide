package ru.vsu.front.projectinfo.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.projectinfo.ProjectInfoViewModel

/**
 * Модуль проекта.
 * * Отвечает за предоставление ViewModel для экранов.
 * * Что внутри:
 * - [ProjectInfoViewModel] - вьюмодель экрана информации об проекте.
 */
val projectInfoModule = module {
    viewModel { (projectId: Int) ->
        ProjectInfoViewModel(
            projectId = projectId
        )
    }
}