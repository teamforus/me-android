package io.forus.me.helpers

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class ThreadHelper {

    companion object {
        val ASSET_THREAD: String = "DATA_ASSET"
        val IDENTITY_THREAD: String = "DATA_IDENTITY"
        val MAIN_THREAD: String = "DATA_MAIN"
        val RECORD_THREAD: String = "DATA_RECORD"
        val SERVICE_THREAD: String = "DATA_SERVICE"
        val TOKEN_THREAD: String = "DATA_TOKEN"
        val WEB3_THREAD: String = "DATA_WEB3"
        private val threadMap: MutableMap<Any, DataThread> = mutableMapOf()

        private val executor = Executors.newFixedThreadPool(4)
        fun <T>await(callable: Callable<T>): T {
            val result = executor.submit(callable)
            val ret = result.get()
            return ret
        }

        fun dispense(key:String): DataThread {
            if (!threadMap.containsKey(key)) {
                threadMap.put(key, DataThread(key).also { it.start() })
            }
            return threadMap[key]!!
        }
    }

    class DataThread(name: String): HandlerThread(name) {
        private lateinit var mHandler: Handler
        private var loaded: Boolean = false

        override fun onLooperPrepared() {
            super.onLooperPrepared()
            mHandler = Handler(looper)
            loaded = true
        }

        fun postTask(task: Runnable) {
            while (!loaded) {}
            mHandler.post(task)
        }
    }
}