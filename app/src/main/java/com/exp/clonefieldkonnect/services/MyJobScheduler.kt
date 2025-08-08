package com.exp.clonefieldkonnect.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast

class MyJobScheduler  : JobService(){


    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.v("akram","start job")
        Toast.makeText(applicationContext,"Run Job",Toast.LENGTH_LONG).show()
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.v("akram","stop job")
        return false
    }
}