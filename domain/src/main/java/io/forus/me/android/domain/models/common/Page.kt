package io.forus.me.android.domain.models.common

class Page<T> {
    var items: List<T> = emptyList()

    constructor(items: List<T>) {
        this.items = items
    }
}
