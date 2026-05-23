package ru.vsu.front.projectinfo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProjectInfoViewModel(
    private val projectId: Int
): ViewModel() {
    private val _uiState = MutableStateFlow(projectId)
    val uiState = _uiState.asStateFlow()
}