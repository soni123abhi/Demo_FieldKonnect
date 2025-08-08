package com.exp.clonefieldkonnect.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap

class AddTaskActivity : AppCompatActivity() {
    var retailerArr: List<DistriutorModel.Datum>? = arrayListOf();
    lateinit var edtRetailer: AutoCompleteTextView
    lateinit var edtTime: EditText
    lateinit var edtRemark: EditText
    lateinit var edtDate: EditText
    lateinit var edtTitle: EditText
    lateinit var edtDesc: EditText
    lateinit var checkbox: CheckBox
    lateinit var cardSubmit: CardView
    lateinit var cardBack: CardView
    var customerId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        initViews()
        getRetailers()
    }

    private fun initViews() {
        edtDesc = findViewById(R.id.edtDesc)
        edtTitle = findViewById(R.id.edtTitle)
        checkbox = findViewById(R.id.checkbox)
        cardSubmit = findViewById(R.id.cardSubmit)
        edtRetailer = findViewById(R.id.edtRetailer)
        edtTime = findViewById(R.id.edtTime)
        edtDate = findViewById(R.id.edtDate)
        edtRemark = findViewById(R.id.edtRemark)
        cardBack = findViewById(R.id.cardBack)

        cardBack.setOnClickListener {
            onBackPressed()
        }
        edtDate.setOnClickListener {
            Utilities.datePickerFuture(edtDate, this@AddTaskActivity)
        }
        edtTime.setOnClickListener {
            Utilities.timePicker(this@AddTaskActivity, edtTime)
        }

        checkbox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                edtRemark.visibility = View.VISIBLE
            } else {
                edtRemark.visibility = View.GONE
            }
        }

        edtRetailer.setOnTouchListener { view, motionEvent ->
            edtRetailer.showDropDown()
            false
        }

        edtRetailer.setOnItemClickListener { adapterView, view, i, l ->
           // customerId = retailerArr!!.get(i).customer_id.toString()

            for(value in retailerArr!!){

                if(edtRetailer.text.toString()==value.name){
                    customerId = value.customer_id.toString()
                }
            }

        }

        cardSubmit.setOnClickListener {
            when {
                edtRetailer.text.toString() == "" -> {
                    Toast.makeText(
                        this@AddTaskActivity,
                        "Please select Customer",
                        Toast.LENGTH_LONG
                    ).show()
                }
                edtTitle.text.toString() == "" -> {
                    Toast.makeText(this@AddTaskActivity, "Please Enter Title", Toast.LENGTH_LONG)
                        .show()
                }
                edtTime.text.toString() == "" -> {
                    Toast.makeText(this@AddTaskActivity, "Please select Time", Toast.LENGTH_LONG)
                        .show()
                }
                edtDate.text.toString() == "" -> {
                    Toast.makeText(this@AddTaskActivity, "Please select Date", Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    createNewTask()
                }


            }
        }
    }

    private fun createNewTask() {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this)

        var dateTime = edtDate.text.toString()+" "+edtTime.text.toString()+":00"

        val queryParams = HashMap<String, String>()
        queryParams["title"] =
            edtTitle.text.toString()
        queryParams["descriptions"] = edtDesc.text.toString()
        queryParams["remark"] = edtRemark.text.toString()
        if (checkbox.isChecked)
            queryParams["completed"] = "1"
        else
            queryParams["completed"] = "0"
        queryParams["customer_id"] = customerId
        queryParams["datetime"] = dateTime

        ApiClient.createNewTask(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this)
            .toString(),
            queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                                Toast.makeText(
                                    this@AddTaskActivity,
                                    "Task Created Successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                            startActivity(Intent(this@AddTaskActivity,MainActivity::class.java))
                            finishAffinity()

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@AddTaskActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@AddTaskActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }


    private fun getRetailers() {

        if (!Utilities.isOnline(this@AddTaskActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@AddTaskActivity)

        val queryParams = HashMap<String, String>()

        ApiClient.getRetailers(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@AddTaskActivity
            ).toString(), queryParams, object : APIResultLitener<DistriutorModel> {
                override fun onAPIResult(
                    response: Response<DistriutorModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            retailerArr = response.body()!!.data

                            val disName = arrayOfNulls<String>(retailerArr!!.size)

                            for (i in retailerArr!!.indices) {

                                disName[i] =
                                    retailerArr!![i].name

                            }

                            val aa = ArrayAdapter(
                                this@AddTaskActivity,
                                android.R.layout.simple_list_item_1,
                                disName
                            )
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            //Setting the ArrayAdapter data on the Spinner
                            edtRetailer.setAdapter(aa)

                        }
                    } else {
                        Toast.makeText(
                            this@AddTaskActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

}