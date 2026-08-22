package com.mohammad.scheduleddownloader
import android.app.*
import android.content.*
import android.os.Build
object Scheduler { fun schedule(c:Context,t:Task){ val i=Intent(c,AlarmReceiver::class.java).putExtra("id",t.id); val p=PendingIntent.getBroadcast(c,t.id.toInt(),i,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE); val a=c.getSystemService(AlarmManager::class.java); if(Build.VERSION.SDK_INT>=31&&!a.canScheduleExactAlarms()){ a.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,t.scheduledAt,p) } else if(Build.VERSION.SDK_INT>=23){ a.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,t.scheduledAt,p) } else a.setExact(AlarmManager.RTC_WAKEUP,t.scheduledAt,p) } fun cancel(c:Context,id:Long){ val p=PendingIntent.getBroadcast(c,id.toInt(),Intent(c,AlarmReceiver::class.java),PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE); if(p!=null)c.getSystemService(AlarmManager::class.java).cancel(p) } }
