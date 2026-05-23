package ru.vsu.front.projects.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.projects.ProjectViewModel

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
            get()
        )
    }
}