package io.forus.me.services.base

import android.app.IntentService
import android.arch.lifecycle.Observer
import android.content.Intent
import android.util.Log
import io.forus.me.entities.base.EthereumItem
import io.forus.me.web3.base.Transaction
import rx.Observable
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by martijn.doornik on 30/04/2018.
 */
abstract class WatcherService<ITEM: EthereumItem, EVENT: Transaction>(name:String, private val service: EthereumItemService<ITEM>) : IntentService(name) {
    private val UPDATE_INTERVAL = 15000L
    private val LOG_LABEL = "WatcherService"

    private val threadExecutionPool = Executors.newFixedThreadPool(1)
    private val observer = Observer(this)
    private var subscribers = mutableMapOf<ITEM, Subscriber<ITEM, EVENT>>()

    abstract protected fun onAdded(item: ITEM): Observable<EVENT>
    abstract protected fun onEventTrigger(event: EVENT)
    abstract protected fun onRemoved(item:ITEM)

    protected open fun onChange(item: ITEM) {
        service.update(item)
    }

    override fun onHandleIntent(intent: Intent?) {
        service.getLiveData().observeForever(observer)
        val thread = Thread(Runnable {
            while (true) {
                Log.d(LOG_LABEL, "Start update circuit")
                val list = service.getList()
                for (item in list) {
                    val changed = item.sync()
                    if (changed) {
                        Log.d(LOG_LABEL, "Change detected. Applying change")
                        onChange(item)
                    }
                }
                Log.d(LOG_LABEL, "End update circuit")
                Thread.sleep(UPDATE_INTERVAL)
            }
        })
        thread.start()
    }

    fun setNewList(newList: List<ITEM>) {
        /*val removedItems = subscribers.keys.filterNot { newList.contains(it) }
        for (item in removedItems) {
            this.onRemoved(item)
            subscribers[item]!!.unsubscribe()
            subscribers.remove(item)
            //listener.onItemDetach(item)
        }
        val addedItems = newList.filterNot { subscribers.keys.contains(it) }
        for (item in addedItems) {
            var observable: Observable<EVENT>? = null
            try {
                observable = threadExecutionPool.submit(Callable<Observable<EVENT>> { this.onAdded(item) }).get()
            } catch (e: Exception){
                val message = e.localizedMessage
                if (item.errorMessage != message) {
                    item.errorMessage = message?: "Error with no message"
                    service.update(item)
                }
            }
            val subscriber = Subscriber(this, observable == null)
            if (!subscriber.error) {
                observable!!.subscribe(subscriber)
            }
            this.subscribers.put(item, subscriber)
        }*/
    }

    private class Observer<ITEM:EthereumItem, EVENT: Transaction>(private val watcher: WatcherService<ITEM, EVENT>): android.arch.lifecycle.Observer<List<ITEM>> {
        override fun onChanged(newList: List<ITEM>?) {
            if (newList != null) {
                watcher.setNewList(newList)
            }
        }
    }

    protected class Subscriber<ITEM: EthereumItem, EVENT: Transaction>(private val watcher: WatcherService<ITEM, EVENT>, val error: Boolean = false): rx.Subscriber<EVENT>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable?) {}

        override fun onNext(event: EVENT) {
            watcher.onEventTrigger(event)
        }
    }
}