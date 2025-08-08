package com.exp.clonefieldkonnect.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.TaskModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response

class TaskDetailsActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var tvTitle : TextView
    var beatArrDashboard: ArrayList<TaskModel> = arrayListOf()
    lateinit var cardBack : CardView
    lateinit var tvName : TextView
    lateinit var tvTime : TextView
    lateinit var tvRemark : TextView
    lateinit var tvDesc : TextView
    lateinit var tvDate : TextView
    lateinit var edtRemark : EditText
    lateinit var cardSubmit : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        initViews()
    }

    private fun initViews() {
        cardSubmit = findViewById(R.id.cardSubmit)
        edtRemark = findViewById(R.id.edtRemark)
        tvTitle = findViewById(R.id.tvTitle)
        tvName = findViewById(R.id.tvName)
        tvTime = findViewById(R.id.tvTime)
        tvRemark = findViewById(R.id.tvRemark)
        tvDesc = findViewById(R.id.tvDesc)
        tvDate = findViewById(R.id.tvDate)
        cardBack = findViewById(R.id.cardBack)
        if(intent.getStringExtra("isComplete")=="0"){
            tvRemark.visibility = View.GONE
            edtRemark.visibility = View.VISIBLE
            cardSubmit.visibility = View.VISIBLE
        }else{
            edtRemark.visibility = View.GONE
        }
        cardBack.setOnClickListener {
            onBackPressed()
        }
        cardSubmit.setOnClickListener {
            if(edtRemark.text.toString()==""){
                Toast.makeText(this@TaskDetailsActivity,"Please enter remark",Toast.LENGTH_LONG).show()
            }else{
                taskMarkComplite()
            }
        }

        getTaskInfo()
    }

    private fun getTaskInfo() {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this@TaskDetailsActivity)

        val queryParams = HashMap<String, String>()

        queryParams["task_id"] = intent.getStringExtra("id").toString()
        queryParams["remark"] = edtRemark.text.toString()

        ApiClient.getTaskInfo(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<TaskModel>() {}.type

                           var tasks = gson.fromJson<TaskModel>(
                                response.body()!!.get("data").asJsonObject,
                                listType
                            )

                            tvName.text = tasks.customers!!.name
                            try {
                                tvTime.text = tasks.datetime!!.split(" ")[1].toString()
                                tvDate.text = tasks.datetime!!.split(" ")[0].toString()
                            } catch (e: Exception) {
                            }
                            tvTitle.text = tasks.title
                            tvDesc.text = tasks.descriptions
                            tvRemark.text = tasks.remark
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@TaskDetailsActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@TaskDetailsActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun taskMarkComplite() {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this@TaskDetailsActivity)

        val queryParams = HashMap<String, String>()

        queryParams["task_id"] = intent.getStringExtra("id").toString()
        queryParams["remark"] = edtRemark.text.toString()

        ApiClient.taskMarkComplite(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            startActivity(Intent(this@TaskDetailsActivity,MainActivity::class.java))
                            finishAffinity()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@TaskDetailsActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@TaskDetailsActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

}
