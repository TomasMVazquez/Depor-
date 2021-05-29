package com.applications.toms.depormas.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

interface Scope : CoroutineScope {

    class ImplementJob(override val uiDispatcher: CoroutineDispatcher): Scope{
        override lateinit var job: Job
    }

    var job: Job
    val uiDispatcher: CoroutineDispatcher

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun initScope(){
        job = SupervisorJob()
    }

    fun cancelScope(){
        job.cancel()
    }

}