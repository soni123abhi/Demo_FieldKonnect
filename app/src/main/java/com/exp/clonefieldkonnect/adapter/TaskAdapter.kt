package com.exp.clonefieldkonnect.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.TaskDetailsActivity
import com.exp.clonefieldkonnect.model.TaskModel

class TaskAdapter(var activity: Activity, var categoryArr: ArrayList<TaskModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var mcontext: Context
    var oldSelectedPos = 0
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        val v = LayoutInflater.from(mcontext).inflate(R.layout.adapter_task, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val statementHandler = holder as StatementHandler
        if (categoryArr[position].completed == 1) {
            statementHandler.tvName.paintFlags = statementHandler.tvName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            statementHandler.tvTime.paintFlags = statementHandler.tvTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            statementHandler.tvDate.paintFlags = statementHandler.tvDate.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            statementHandler.tvTitle.paintFlags = statementHandler.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        try {
            statementHandler.tvName.text = categoryArr[position].customers!!.name
            statementHandler.tvTime.text = categoryArr[position].datetime!!.split(" ")[1]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        statementHandler.tvTitle.text = categoryArr[position].title
        try {
            statementHandler.tvDate.text = categoryArr[position].datetime!!.split(" ")[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        statementHandler.cardMain.tag = position
        statementHandler.cardMain.setOnClickListener { view ->
            val pos = view.tag as Int
            val intent = Intent(mcontext, TaskDetailsActivity::class.java)
            intent.putExtra("id", categoryArr[pos].id.toString())
            intent.putExtra("isComplete", categoryArr[pos].completed.toString())
            mcontext.startActivity(intent)

        }
    }

    private inner class StatementHandler(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var cardMain: LinearLayout = itemView.findViewById(R.id.cardMain)
    }

    override fun getItemCount(): Int {
        return categoryArr.size
    }
}