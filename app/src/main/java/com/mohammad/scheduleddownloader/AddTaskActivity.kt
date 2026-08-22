package com.mohammad.scheduleddownloader
import android.app.*
import android.content.*
import android.os.*
import android.provider.Settings
import android.net.Uri
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar
class AddTaskActivity:ComponentActivity(){private val cal=Calendar.getInstance();override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(R.layout.activity_add_task);val u=findViewById<EditText>(R.id.url);val n=findViewById<EditText>(R.id.filename);findViewById<Button>(R.id.date).setOnClickListener{DatePickerDialog(this,{_,y,m,d->cal.set(y,m,d)},cal.get(1),cal.get(2),cal.get(5)).show()};findViewById<Button>(R.id.time).setOnClickListener{TimePickerDialog(this,{_,h,m->cal.set(Calendar.HOUR_OF_DAY,h);cal.set(Calendar.MINUTE,m);cal.set(Calendar.SECOND,0)},cal.get(11),cal.get(12),true).show()};findViewById<Button>(R.id.save).setOnClickListener{val url=u.text.toString().trim();if(!url.startsWith("http://")&&!url.startsWith("https://")){u.error="URL نامعتبر";return@setOnClickListener};if(cal.timeInMillis<=System.currentTimeMillis()){Toast.makeText(this,"زمان باید در آینده باشد",Toast.LENGTH_SHORT).show();return@setOnClickListener};lifecycleScope.launch{val t=Task(url=url,filename=n.text.toString().trim().ifBlank{null},scheduledAt=cal.timeInMillis);val id=AppDb.get(this@AddTaskActivity).taskDao().insert(t);val saved=t.copy(id=id);Scheduler.schedule(this@AddTaskActivity,saved);Notifications.show(this@AddTaskActivity,id.toInt(),"زمان‌بندی شد",url);if(Build.VERSION.SDK_INT>=31&&!getSystemService(AlarmManager::class.java).canScheduleExactAlarms())startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:$packageName")));finish()}}}}
