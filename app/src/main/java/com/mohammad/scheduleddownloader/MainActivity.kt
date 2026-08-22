package com.mohammad.scheduleddownloader

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Notifications.setup(this)

        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                9,
            )
        }

        adapter = TaskAdapter(::run, ::delete)
        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        findViewById<View>(R.id.add).setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        lifecycleScope.launch {
            AppDb.get(this@MainActivity).taskDao().all().collectLatest { tasks ->
                adapter.submitList(tasks)
                findViewById<View>(R.id.empty).visibility =
                    if (tasks.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun run(task: Task) {
        startForegroundService(
            Intent(this, DownloadService::class.java).putExtra("id", task.id),
        )
    }

    private fun delete(task: Task) {
        Scheduler.cancel(this, task.id)
        lifecycleScope.launch {
            AppDb.get(this@MainActivity).taskDao().delete(task.id)
        }
    }
}

class TaskAdapter(
    private val run: (Task) -> Unit,
    private val del: (Task) -> Unit,
) : ListAdapter<Task, TaskAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false),
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.title).text =
            task.filename ?: "Download #${task.id}"
        holder.itemView.findViewById<TextView>(R.id.url).text = task.url
        holder.itemView.findViewById<TextView>(R.id.date).text =
            java.text.DateFormat.getDateTimeInstance()
                .format(java.util.Date(task.scheduledAt))

        val status = holder.itemView.findViewById<TextView>(R.id.status)
        status.text = "${task.status} ${task.message ?: ""}"
        status.setTextColor(
            holder.itemView.context.getColor(
                when (task.status) {
                    "COMPLETED" -> R.color.green
                    "FAILED" -> R.color.red
                    else -> R.color.amber
                },
            ),
        )
        holder.itemView.findViewById<View>(R.id.run).setOnClickListener { run(task) }
        holder.itemView.findViewById<View>(R.id.delete).setOnClickListener { del(task) }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }
}
