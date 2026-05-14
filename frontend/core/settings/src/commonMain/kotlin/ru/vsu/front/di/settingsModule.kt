package ru.vsu.front.di

import org.koin.dsl.module
import ru.vsu.front.ThemeSettings

val settingsModule = module {
    single {
        ThemeSettings()
    }
}