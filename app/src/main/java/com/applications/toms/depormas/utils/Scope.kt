package com.applications.toms.depormas.utils

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

interface Scope : CoroutineScope {

    var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun initScope(){
        job = SupervisorJob()
    }

    fun cancelScope(){
        job.cancel()
    }

    class ImplementJob: Scope{
        override lateinit var job: Job
    }

}