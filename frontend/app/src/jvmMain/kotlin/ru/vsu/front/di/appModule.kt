package ru.vsu.front.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.vsu.front.config.BuildKonfig

val appModule = module {
    single(named("baseUrl")) {
        BuildKonfig.BASE_URL
    }
}