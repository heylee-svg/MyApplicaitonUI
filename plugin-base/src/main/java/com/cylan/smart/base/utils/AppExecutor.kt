package com.cylan.smart.base.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 * @author yanzhendong
 * @since 2019/1/11 上午9:50
 */
@Suppress("unused")
object AppExecutor {
    private val mIOThreadExecutor: IOThreadExecutor = IOThreadExecutor()
    private val mMainThreadExecutor: MainThreadExecutor = MainThreadExecutor()
    fun executeOnDiskIO(command: Runnable) {
        mIOThreadExecutor.execute(command)
    }

    fun postToMainThread(command: Runnable, delayMillis: Long = 0, clear: Boolean = false) {
        if (clear) {
            mMainThreadExecutor.removeCallbacks(command)
        }
        if (delayMillis <= 0) {
            mMainThreadExecutor.execute(command)
        } else {
            mMainThreadExecutor.postDelayed(command, delayMillis)
        }
    }

    fun getMainThreadExecutor(): MainThreadExecutor = mMainThreadExecutor

    fun getIOThreadExecutor(): IOThreadExecutor = mIOThreadExecutor

    class MainThreadExecutor internal constructor() : Executor {
        private val mHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mHandler.post(command)
        }

        fun postDelayed(command: Runnable, delayMillis: Long): Boolean {
            return mHandler.postDelayed(command, delayMillis)
        }

        fun removeCallbacks(command: Runnable) {
            return mHandler.removeCallbacks(command)
        }

    }

    class DirectExecutor internal constructor() : Executor {
        override fun execute(command: Runnable) {
            command.run()
        }
    }

    class IOThreadExecutor internal constructor() : Executor {
        private val mThreadId = AtomicInteger()
        private val mExecutor = Executors.newCachedThreadPool {
            Thread(it, "AppExecutor Thread:${mThreadId.getAndIncrement()}")
        }

        override fun execute(command: Runnable) {
            mExecutor.execute(command)
        }
    }
}