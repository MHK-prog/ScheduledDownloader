package com.mohammad.scheduleddownloader
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tasks")
data class Task(@PrimaryKey(autoGenerate=true) val id:Long=0, val url:String, val filename:String?, val scheduledAt:Long, val status:String="SCHEDULED", val message:String?=null, val completedAt:Long?=null)
