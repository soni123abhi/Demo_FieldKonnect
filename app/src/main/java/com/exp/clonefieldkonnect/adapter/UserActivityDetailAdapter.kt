package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.UserActivityDetailModel

class UserActivityDetailAdapter(var activity: Activity, var useractivitydetail: ArrayList<UserActivityDetailModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_userdetail, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.time.setText(useractivitydetail.get(position).time)
        statementHandler.tv_tittle.setText(useractivitydetail.get(position).title)

        val inputString = useractivitydetail.get(position).msg
        val result = insertLineBreakBeforeRemark(inputString.toString())
        statementHandler.tv_msg.setText(result)

        if (useractivitydetail.get(position).latitude.equals("") && useractivitydetail.get(position).longitude.equals("")){
            statementHandler.card_loc.visibility = View.GONE
        }else{
            statementHandler.card_loc.visibility = View.VISIBLE
            statementHandler.card_loc.setOnClickListener {
                var lat = useractivitydetail[position].latitude
                var long = useractivitydetail[position].longitude
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "geo:<" + lat.toString() + ">,<" + long.toString()
                                + ">?q=<" + lat.toString() + ">,<" + long.toString()
                    )
                )
                activity.startActivity(intent)
            }
        }

/*
        if (position == useractivitydetail.size - 1) {
            statementHandler.view1.visibility = View.GONE
        } else {
            statementHandler.view1.visibility = View.VISIBLE
        }
*/

    }

    fun insertLineBreakBeforeRemark(inputString: String): String {
        val keyword = "Remark"

        val index = inputString.indexOf(keyword)
        return if (index != -1) {
            val firstPart = inputString.substring(0, index)
            val secondPart = inputString.substring(index)
            "$firstPart\n$secondPart"
        } else {
            inputString
        }
    }


    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tv_tittle: TextView = itemView.findViewById(R.id.tv_tittle)
        var tv_msg: TextView = itemView.findViewById(R.id.tv_msg)
        var time: TextView = itemView.findViewById(R.id.time)
        var card_loc: CardView = itemView.findViewById(R.id.card_loc)
        var view1: View = itemView.findViewById(R.id.view1)
    }

    override fun getItemCount(): Int {
        return useractivitydetail.size
    }

}