package com.qlang.game.demo.ktx

import kotlinx.coroutines.*

/**
 * 使用协程进行异步任务，IO线程执行，UI线程返回 ，或者可以使用[disp]指定调度线程；
 * 通过[job]自行任务管理
 *
 * @param block 执行块
 * @param rtn 返回监听
 * @param disp 调度者
 * @param job 任务
 */
fun <T, R> T.execAsync(block: suspend T.() -> R?, rtn: ((R?) -> Unit)? = null,
                       job: Job? = null,
                       disp: CoroutineDispatcher = Dispatchers.Default): T {
    val _job by lazy { job ?: Job() }
    val result = CoroutineScope(Dispatchers.IO + _job).async { block() }
    rtn?.let { CoroutineScope(disp + _job).launch { it.invoke(result.await()) } }
    return this
}