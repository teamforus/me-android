package io.forus.me.android.presentation.view.screens.records.newrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_new_record.*

/**
 * Fragment New Record Screen.
 */
class NewRecordFragment : LRFragment<NewRecordModel, NewRecordView, NewRecordPresenter>(), NewRecordView  {


    private val viewIsValid: Boolean
        get() {
            return true
        }


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }


    private val createRecordAction = PublishSubject.create<NewRecordRequest>()



    override fun createRecord() = createRecordAction

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_new_record, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register.setOnClickListener {
            if (viewIsValid) {
                createRecordAction.onNext(NewRecordRequest(



                ))
            }
        }
    }



    override fun createPresenter() = NewRecordPresenter(
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<NewRecordModel>) {
        super.render(vs)

        if (vs.closeScreen) {
            closeScreen()
        }



    }

    fun closeScreen() {
        navigator.navigateToDashboard(activity)
        activity?.finish()
    }


}