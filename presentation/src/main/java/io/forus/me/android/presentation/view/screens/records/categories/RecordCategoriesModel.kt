package io.forus.me.android.presentation.view.screens.records.categories

import io.forus.me.android.domain.models.records.RecordCategory

data class RecordCategoriesModel(
        val items: List<RecordCategory> = emptyList()
        )