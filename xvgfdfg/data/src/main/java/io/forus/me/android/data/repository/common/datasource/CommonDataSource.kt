package io.forus.me.android.data.repository.common.datasource

import com.gigawatt.android.data.net.sign.models.request.SignUp
import io.reactivex.Observable

interface CommonDataSource {

    fun status(): Observable<Boolean>
}