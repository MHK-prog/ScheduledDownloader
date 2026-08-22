package com.mohammad.scheduleddownloader
import android.app.*
import android.content.Context
import android.os.Build
object Notifications { const val CHANNEL="downloads"; fun setup(c:Context){ if(Build.VERSION.SDK_INT>=26){ val n=NotificationChannel(CHANNEL,"Downloads",NotificationManager.IMPORTANCE_DEFAULT); c.getSystemService(NotificationManager::class.java).createNotificationChannel(n) } } fun show(c:Context,id:Int,title:String,text:String){ setup(c); val n=Notification.Builder(c,CHANNEL).setSmallIcon(com.mohammad.scheduleddownloader.R.drawable.ic_launcher).setContentTitle(title).setContentText(text).setAutoCancel(true).build(); c.getSystemService(NotificationManager::class.java).notify(id,n) } }
