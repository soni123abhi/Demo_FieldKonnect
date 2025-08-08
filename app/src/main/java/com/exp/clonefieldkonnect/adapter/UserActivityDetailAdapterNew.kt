package com.exp.clonefieldkonnect.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.UserActivityDetailModel

class UserActivityDetailAdapterNew (var activity: Activity, var useractivitydetail: ArrayList<UserActivityDetailModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.new_user_activity_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.tv_user_time.setText(useractivitydetail.get(position).time)
        statementHandler.tv_user_punchINPunchOut.setText(useractivitydetail.get(position).title)

        val inputString = useractivitydetail.get(position).msg
        val result = insertLineBreakBeforeRemark(inputString.toString())
        statementHandler.tv_user_address.setText(result)

        if (useractivitydetail.get(position).latitude.equals("") && useractivitydetail.get(position).longitude.equals("")){
            statementHandler.iv_user_location.setImageResource(R.drawable.ic_user_location_red)
        }else{
            statementHandler.iv_user_location.setImageResource(R.drawable.ic_user_location_green)
            statementHandler.iv_user_location.setOnClickListener {
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

        var tv_user_time: TextView = itemView.findViewById(R.id.tv_user_time)
        var tv_user_punchINPunchOut: TextView = itemView.findViewById(R.id.tv_user_punchINPunchOut)
        var tv_user_address: TextView = itemView.findViewById(R.id.tv_user_address)
        var iv_user_location: ImageView = itemView.findViewById(R.id.iv_user_location)

    }

    override fun getItemCount(): Int {
        return useractivitydetail.size
    }

}