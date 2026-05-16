package ru.vsu.front.notifications.di

import org.koin.dsl.module
import ru.vsu.front.notifications.ProjectViewModel

val projectModule = module {
    single {
        ProjectViewModel()
    }
}