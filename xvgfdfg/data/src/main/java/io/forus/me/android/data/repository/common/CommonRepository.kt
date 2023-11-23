package io.forus.me.android.data.repository.common

import io.forus.me.android.data.repository.common.datasource.CommonDataSource
import io.forus.me.android.domain.repository.common.CommonRepository
import io.reactivex.Observable


class CommonRepository(private val commonDataSource: CommonDataSource) : CommonRepository {


    override fun status(): Observable<Boolean> {
        return commonDataSource.status()
                .map {
                    it
                }
    }
}