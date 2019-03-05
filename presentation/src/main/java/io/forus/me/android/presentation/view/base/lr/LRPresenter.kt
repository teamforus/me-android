package io.forus.me.android.presentation.view.base.lr

import android.support.annotation.CallSuper
import android.util.Log
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

abstract class LRPresenter<I, M, V : LRView<M>> : MviBasePresenter<V, LRViewState<M>>() {

    protected abstract fun initialModelSingle(): Single<I>
    protected open fun refreshModelSingle(): Single<I> = initialModelSingle()

    protected open val reloadIntent: Observable<Any> = Observable.never()

    protected val retryIntent: Observable<Any> = intent { it.retry() }
    protected val refreshIntent: Observable<Any> = intent { it.refresh() }
    protected val updateIntent: Observable<Any> = intent { it.updateData() }

    protected open fun loadRefreshPartialChanges(): Observable<LRPartialChange> = Observable.merge(
            Observable
                    .merge(
                            Observable.just(Any()),
                            reloadIntent,
                            retryIntent
                    )
                    .switchMap {
                        initialModelSingle()
                                .toObservable()
                                .subscribeOn(Schedulers.io())
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { throwable ->
                                    throwable.printStackTrace()
                                    LRPartialChange.LoadingError(throwable)
                                }
                                .startWith(LRPartialChange.LoadingStarted)
                    },
            refreshIntent
                    .switchMap {
                        refreshModelSingle()
                                .toObservable()
                                .subscribeOn(Schedulers.io())
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { LRPartialChange.RefreshError(it) }
                                .startWith(LRPartialChange.RefreshStarted)
                    },
            updateIntent
                    .switchMap {
                        initialModelSingle()
                                .toObservable()
                                .subscribeOn(Schedulers.io())
                                .map<LRPartialChange> { LRPartialChange.InitialModelLoaded(it) }
                                .onErrorReturn { LRPartialChange.RefreshError(it) }
                                .startWith(LRPartialChange.RefreshStarted)
                    }
    )

    protected abstract fun M.changeInitialModel(i: I): M

    @CallSuper
    protected open fun stateReducer(vs: LRViewState<M>, change: PartialChange): LRViewState<M> {
        Log.d("AASSDD", "LRPresenter stateReducer $change")
        val viewState = vs.copy(closeScreen = false)
        if (change !is LRPartialChange) throw Exception()
        return when (change) {
            LRPartialChange.LoadingStarted -> viewState.copy(loading = true, loadingError = null, canRefresh = false)
            is LRPartialChange.LoadingError -> viewState.copy(loading = false, loadingError = change.t)
            LRPartialChange.RefreshStarted -> viewState.copy(refreshing = true, refreshingError = null)
            is LRPartialChange.RefreshError -> viewState.copy(refreshing = false, refreshingError = change.t)
            is LRPartialChange.InitialModelLoaded<*> -> {
                @Suppress("UNCHECKED_CAST")
                viewState.copy(
                        loading = false,
                        loadingError = null,
                        model = viewState.model.changeInitialModel(change.i as I),
                        canRefresh = true,
                        refreshing = false
                )
            }
        }
    }
}