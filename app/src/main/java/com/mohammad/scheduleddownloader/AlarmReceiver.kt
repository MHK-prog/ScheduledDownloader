package com.mohammad.scheduleddownloader
import android.content.*
class AlarmReceiver:BroadcastReceiver(){ override fun onReceive(c:Context,i:Intent){ val id=i.getLongExtra("id",0); c.startForegroundService(Intent(c,DownloadService::class.java).putExtra("id",id)) } }
