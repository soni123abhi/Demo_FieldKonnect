package com.exp.clonefieldkonnect.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.TaskAdapter
import com.exp.clonefieldkonnect.model.TaskModel

class TaskListActivity : AppCompatActivity() {
    lateinit var recyclerView : RecyclerView
    lateinit var tvTitle : TextView
    var beatArrDashboard: ArrayList<TaskModel> = arrayListOf()
    lateinit var cardBack : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        beatArrDashboard = intent.getSerializableExtra("arr") as ArrayList<TaskModel>
        initViews()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        cardBack = findViewById(R.id.cardBack)
        cardBack.setOnClickListener {
            onBackPressed()
        }
        setAdapter()
    }

    fun setAdapter() {
        var mLayoutManager = LinearLayoutManager(this@TaskListActivity)
        recyclerView.layoutManager = mLayoutManager
       val taskAdapter = TaskAdapter(this@TaskListActivity, beatArrDashboard)
        recyclerView.adapter = taskAdapter
    }
    lateinit var dialog : Dialog
}
