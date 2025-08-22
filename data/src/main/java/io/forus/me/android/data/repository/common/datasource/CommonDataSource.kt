package io.forus.me.android.data.repository.common.datasource

import io.reactivex.Observable

interface CommonDataSource {
    fun status(): Observable<Boolean>
}