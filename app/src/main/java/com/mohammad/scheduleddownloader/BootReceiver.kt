package com.mohammad.scheduleddownloader
import android.content.*
import kotlinx.coroutines.*
class BootReceiver:BroadcastReceiver(){ override fun onReceive(c:Context,i:Intent){ val pending=goAsync(); CoroutineScope(Dispatchers.IO).launch{ AppDb.get(c).taskDao().scheduled().filter{it.scheduledAt>System.currentTimeMillis()}.forEach{Scheduler.schedule(c,it)}; pending.finish() } } }
