package io.forus.me.android.data.repository.datasource

abstract class RemoteDataSource<T> protected constructor(private val f: () -> T) {

    protected var service: T = f()

    open fun updateService(){
        service = f()
    }
}
