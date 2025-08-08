package com.exp.clonefieldkonnect.adapter


import android.app.ActionBar
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.StoreCustomerActivity.Companion.arrList
import com.exp.clonefieldkonnect.model.EnquiryModel

class EnquiryInfoAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_inquiry, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

        statementHandler.tvName.visibility = View.VISIBLE

        if (arrList[position]!!.isRequired == "true")
            statementHandler.tvName.text =
                "*${arrList[position]!!.labelName}"
        else
            statementHandler.tvName.text =
                arrList[position]!!.labelName

        if (arrList[position]!!.fieldType == "Input") {

            statementHandler.edt.setText(arrList[position].value)
            statementHandler.edt.setHint(arrList[position].placeholder)

            //  statementHandler.edt.isFocusable = false

            statementHandler.at.visibility = View.GONE
            statementHandler.radioGroup.visibility = View.GONE
            statementHandler.linearCheckBox.visibility = View.GONE

            statementHandler.edt.visibility = View.VISIBLE

            statementHandler.edt.tag = position
            statementHandler.tvName.visibility = View.GONE


        } else if (arrList[position]!!.fieldType == "Select") {

            statementHandler.at.setText(arrList[position].value)

            statementHandler.at.setHint(arrList[position].placeholder)
            statementHandler.edt.visibility = View.GONE
            statementHandler.radioGroup.visibility = View.GONE
            statementHandler.linearCheckBox.visibility = View.GONE
            statementHandler.at.visibility = View.VISIBLE

            addSpinner(context, statementHandler.at, arrList[position]!!.fieldData, position)
            statementHandler.tvName.visibility = View.GONE
        } else if (arrList[position]!!.fieldType == "Radio") {
            statementHandler.radioGroup.visibility = View.VISIBLE
            statementHandler.edt.visibility = View.GONE
            statementHandler.at.visibility = View.GONE
            statementHandler.linearCheckBox.visibility = View.GONE


            addRadioButtons(
                arrList[position]!!.fieldData,
                statementHandler.radioGroup,
                context,
                position
            )

        } else if (arrList[position]!!.fieldType == "Checkbox") {
            statementHandler.radioGroup.visibility = View.GONE
            statementHandler.edt.visibility = View.GONE
            statementHandler.at.visibility = View.GONE
            statementHandler.linearCheckBox.visibility = View.VISIBLE

            addCheckBox(
                arrList[position]!!.fieldData, statementHandler.linearCheckBox, context, position,
                arrList[position].responseValue
            )

        } else {
            statementHandler.tvName.visibility = View.GONE
            // statementHandler.img.visibility = View.GONE
        }

        statementHandler.edt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                arrList[statementHandler.adapterPosition].value =
                    statementHandler.edt.text.toString()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })



        statementHandler.radioGroup.setTag(position)
        statementHandler.radioGroup.setOnClickListener { view ->
            val pos = view.tag as Int

        }

    }


    override fun getItemCount(): Int {

        return arrList.size
    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val edt: EditText = itemView.findViewById(R.id.edt)
        val at: AutoCompleteTextView = itemView.findViewById(R.id.at)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
        val linearMain: LinearLayout = itemView.findViewById(R.id.linearMain)
        val linearCheckBox: LinearLayout = itemView.findViewById(R.id.linearCheckBox)
    }

    fun addRadioButtons(
        number: List<EnquiryModel.Datum.FieldDatum>?,
        radioGroup: RadioGroup,
        context: Context,
        pos: Int
    ) {
        radioGroup.setOrientation(LinearLayout.VERTICAL)
        //
        for ((index, element) in number!!.withIndex()) {
            val rdbtn = RadioButton(context)
            rdbtn.id = index
            rdbtn.tag = pos
            rdbtn.text = element.value.toString()
            rdbtn.setOnClickListener(this)
            radioGroup.addView(rdbtn)
        }
    }

    fun addCheckBox(
        number: List<EnquiryModel.Datum.FieldDatum>?, checkBoxLayout: LinearLayout,
        context: Context, pos: Int, valueSelected: String
    ) {
        //
        var totalLength = ""
        for ((index, element) in number!!.withIndex()) {
            val row = TableRow(context)
            row.id = index
            row.layoutParams =
                ActionBar.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
            val checkBox = CheckBox(context)
            checkBox.setOnCheckedChangeListener(this)
            checkBox.id = pos
            totalLength = element.value!!
            checkBox.setText(element.value)
            if (valueSelected.contains(element.value!!)) {
                checkBox.isChecked = true
            }
            checkBoxLayout.orientation = LinearLayout.VERTICAL
            row.addView(checkBox)
            checkBoxLayout.addView(row)
        }

    }

    fun addSpinner(
        context: Context,
        at: AutoCompleteTextView,
        number: List<EnquiryModel.Datum.FieldDatum>?,
        pos: Int
    ) {

        val statesName =
            arrayOfNulls<String>(number!!.size)

        for (i in number!!.indices) {

            statesName[i] = number[i].value

        }

        val aa = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            statesName
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        at.setAdapter(aa)

        at.setTag(pos)

        var selectedPos = -1
        at.setOnClickListener { view ->

            selectedPos = view.tag as Int
            at.showDropDown()
        }
        at.setOnItemClickListener { adapterView, view, i, l ->


            arrList[selectedPos].value = arrList[selectedPos].fieldData!![i].value!!

        }

    }

    override fun onClick(p0: View?) {
        arrList[p0!!.getTag().toString().toInt()]!!.value = (p0 as RadioButton).text.toString()
        Log.d(
            "akram",
            " Name " + (p0 as RadioButton).text.toString() + " Id is " + p0.getTag()
        )
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {


        if (!arrList[p0!!.getId()].value.contains((p0 as CheckBox).text.toString())) {

            if (arrList[p0!!.getId()].value == "") {
                arrList[p0!!.getId()].value = (p0 as CheckBox).text.toString()

            } else {
                arrList[p0!!.getId()]!!.value =
                    arrList[p0!!.getId()]!!.value + "," + (p0 as CheckBox).text.toString()
            }

        } else {
            var checkBocValueArr = arrList[p0!!.getId()]!!.value.split(",")
            arrList[p0!!.getId()].value = ""
            for (value in checkBocValueArr) {

                if (value != (p0 as CheckBox).text.toString()) {
                    if (arrList[p0!!.getId()].value == "") {
                        arrList[p0!!.getId()].value = value

                    } else {
                        arrList[p0!!.getId()]!!.value = arrList[p0!!.getId()]!!.value + "," + value
                    }
                }

            }

        }
        Log.d(
            "akram",
            " Name " + arrList[p0!!.getId()].value
        )


    }

}