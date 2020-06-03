package io.forus.me.android.domain.repository.common

import io.reactivex.Observable

interface CommonRepository {

    fun status(): Observable<Boolean>
}