package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.TaskDetailsActivity
import com.exp.clonefieldkonnect.model.PointCollectionModel

class PointCollectionAdapter(var activity: Activity, var categoryArr : ArrayList<PointCollectionModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

     var oldSelectedPos = 0

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mcontext = parent.context

        val v = LayoutInflater.from(mcontext).inflate(R.layout.adapter_point_collection, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

        statementHandler.tvType.text = categoryArr[position].pointType
        try {
            statementHandler.tvDate.text = categoryArr[position].transactionAt!!.split(" ")[0]
        } catch (e: Exception) {
        }
        statementHandler.tvPoint.text = categoryArr[position].points.toString()
        statementHandler.tvQty.text = categoryArr[position].quantity.toString()

        statementHandler.cardMain.tag = position
        statementHandler.cardMain.setOnClickListener { view ->
            val pos = view.tag as Int

            mcontext.startActivity(Intent(mcontext,TaskDetailsActivity::class.java))

        }
    }

    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvType: TextView = itemView.findViewById(R.id.tvType)
        var tvPoint: TextView = itemView.findViewById(R.id.tvPoint)
        var tvQty: TextView = itemView.findViewById(R.id.tvQty)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var cardMain: LinearLayout = itemView.findViewById(R.id.cardMain)


    }

    override fun getItemCount(): Int {
        return categoryArr.size
    }


}