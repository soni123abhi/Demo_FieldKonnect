package com.exp.clonefieldkonnect.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.PointCollectionAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.PointCollectionModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class PointCollectionActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var tvTitle : TextView
    lateinit var cardBack : CardView
    var pointCollectionArr: ArrayList<PointCollectionModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_collection_list)
        initViews()
    }

    private fun initViews() {

        recyclerView = findViewById(R.id.recyclerView)
        cardBack = findViewById(R.id.cardBack)
        cardBack.setOnClickListener {
            onBackPressed()
        }
        getCollectedPoints()
    }

    fun setAdapter() {
        var mLayoutManager = LinearLayoutManager(this@PointCollectionActivity)
        recyclerView.layoutManager = mLayoutManager

       val taskAdapter = PointCollectionAdapter(this@PointCollectionActivity, pointCollectionArr)
        recyclerView.adapter = taskAdapter
    }

    lateinit var dialog : Dialog
    private fun getCollectedPoints() {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this)

        val queryParams = java.util.HashMap<String, String>()
        queryParams["customer_id"] = StaticSharedpreference.getInfo(Constant.CHECKIN_CUST_ID,this@PointCollectionActivity).toString()
        ApiClient.getCollectedPoints(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this
            ).toString(), queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<PointCollectionModel>>() {}.type

                            pointCollectionArr = gson.fromJson<ArrayList<PointCollectionModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            setAdapter()
                        }
                    } else {

                    }
                }
            }
        )
    }


}
