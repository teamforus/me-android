package io.forus.me.android.data.repository.common.datasource

import io.forus.me.android.data.net.common.CommonService
import io.forus.me.android.data.repository.datasource.RemoteDataSource
import io.reactivex.Observable

class CommonRemoteDataSource(f: () -> CommonService) : CommonDataSource, RemoteDataSource<CommonService>(f) {
    override fun status(): Observable<Boolean> {
        return service.status()
                .map {
                    val result = it.string();
                    result == "1"||result.isEmpty()

                }
    }
}

