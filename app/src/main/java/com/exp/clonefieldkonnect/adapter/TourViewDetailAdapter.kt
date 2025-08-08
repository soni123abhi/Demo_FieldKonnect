package com.exp.clonefieldkonnect.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.TourViewDetailModel

class TourViewDetailAdapter(
    var activity: Activity,
    var tourviewArr: ArrayList<TourViewDetailModel>,
    var onClickEmail_tour_view: OnEmailClick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var flag = ""
    var touridarray : ArrayList<String> = ArrayList()
    var datearray : ArrayList<String> = ArrayList()
    val statusArray = ArrayList<Int>(List(tourviewArr.size) { 0 })
    val editArray = ArrayList<Int>(List(tourviewArr.size) { 0 })
    val objectiveMap: MutableMap<Int, String> = HashMap()
    val townMap: MutableMap<Int, String> = HashMap()

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_tour_view_table, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val statementHandler = holder as StatementHandler



        statementHandler.edt_view_objective.removeTextChangedListener(statementHandler.textWatcher)
        statementHandler.edt_view_visit.removeTextChangedListener(statementHandler.textWatcher2)

        if (!objectiveMap.containsKey(position)) {
            objectiveMap[position] = tourviewArr[position].objectives.toString()
        }
        if (!townMap.containsKey(position)) {
            townMap[position] = tourviewArr[position].town.toString()
        }

        statementHandler.edt_view_objective.setText(objectiveMap[position])
        statementHandler.edt_view_visit.setText(townMap[position])

        statementHandler.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                objectiveMap[position] = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        statementHandler.edt_view_objective.addTextChangedListener(statementHandler.textWatcher)

        statementHandler.textWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                townMap[position] = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        statementHandler.edt_view_visit.addTextChangedListener(statementHandler.textWatcher2)


        statementHandler.tv_view_date.text = tourviewArr.get(position).date
        statementHandler.tv_view_status.text = tourviewArr.get(position).status

        when (tourviewArr[position].status) {
            "Approved" -> {
                statementHandler.tv_view_status.setTextColor(Color.parseColor("#00D23B"))
            }
            "Rejected" -> {
                statementHandler.tv_view_status.setTextColor(Color.parseColor("#FF0000"))
            }
            "Pending" -> {
                statementHandler.tv_view_status.setTextColor(Color.parseColor("#FFC700"))
            }
        }

        if (tourviewArr.get(position).status == "Approved"){
            statusArray[position] = 1
            editArray[position] = 1
            statementHandler.checkbox.isClickable = false
            statementHandler.checkbox.isChecked = true
            statementHandler.edt_view_visit.setOnFocusChangeListener { v, hasFocus ->
                var origBg: Drawable? = null
                if (!hasFocus) {
                    origBg = v.background
                    statementHandler.edt_view_visit.isEnabled = false
                    statementHandler.edt_view_objective.isEnabled = false
                }
            }
        }
        else if (tourviewArr.get(position).status == "Pending" || tourviewArr.get(position).status == "Rejected") {
            statementHandler.checkbox.isClickable = true
            statementHandler.edt_view_visit.isEnabled = true
            statementHandler.edt_view_objective.isEnabled = true

            statementHandler.checkbox.setOnCheckedChangeListener(null)

            val objIncome: TourViewDetailModel = tourviewArr[position]
            statementHandler.checkbox.isChecked = objIncome.isSelected!!


            statementHandler.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                val currentItem = tourviewArr[position]

                if (currentItem.self == "false") {
                    flag = if (isChecked) "true" else "false"
                    statusArray[position] = if (isChecked) 1 else 0
                    onClickEmail_tour_view.onClickEmail_tour(
                        touridarray, datearray, townMap,objectiveMap, statusArray,
                        flag,
                        editArray
                    )
                } else if (currentItem.self == "true") {
                    flag = if (isChecked) "true" else "false"
                    editArray[position] = if (isChecked) 0 else 0
                    onClickEmail_tour_view.onClickEmail_tour(
                        touridarray, datearray, townMap, objectiveMap, statusArray,
                        flag, editArray
                    )
                }
                currentItem.isSelected = isChecked
            }
        }



        touridarray.add(tourviewArr.get(position).id.toString())
        datearray.add(tourviewArr.get(position).date.toString())
        println("SIZEEEEE="+tourviewArr.size)


    }



    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tv_view_date: TextView = itemView.findViewById(R.id.tv_view_date)
        var edt_view_visit: EditText = itemView.findViewById(R.id.edt_view_visit)
        var edt_view_objective: EditText = itemView.findViewById(R.id.edt_view_objective)
        var tv_view_status: TextView = itemView.findViewById(R.id.tv_view_status)
        var checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        var textWatcher: TextWatcher? = null
        var textWatcher2: TextWatcher? = null

    }



    override fun getItemCount(): Int {
        return tourviewArr.size
    }


    interface OnEmailClick {
        fun onClickEmail_tour(
            touridarray: ArrayList<String>,
            datearray: ArrayList<String>,
            townMap: MutableMap<Int, String>,
            objectiveMap: MutableMap<Int, String>,
            statusArray: ArrayList<Int>,
            flag: String,
            editArray: ArrayList<Int>
        )
    }
}