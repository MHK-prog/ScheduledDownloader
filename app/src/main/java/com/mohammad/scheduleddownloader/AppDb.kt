package com.mohammad.scheduleddownloader
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities=[Task::class],version=1,exportSchema=false)
abstract class AppDb:RoomDatabase(){ abstract fun taskDao():TaskDao; companion object { @Volatile private var instance:AppDb?=null; fun get(c:Context)=instance?:synchronized(this){instance?:Room.databaseBuilder(c.applicationContext,AppDb::class.java,"scheduled.db").build().also{instance=it}} } }
