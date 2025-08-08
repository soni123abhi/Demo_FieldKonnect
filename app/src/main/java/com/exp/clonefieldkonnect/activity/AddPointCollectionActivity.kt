package com.exp.clonefieldkonnect.activity

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
import com.exp.clonefieldkonnect.model.PointCollectionRequest
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response

class AddPointCollectionActivity : AppCompatActivity() {
    lateinit var edtPointType: AutoCompleteTextView
    lateinit var edtPoints: EditText
    lateinit var edtQuantity: EditText
    lateinit var cardSubmit: CardView
    lateinit var cardBack: CardView
    lateinit var cardAdd: RelativeLayout
    lateinit var linear1: LinearLayout
    lateinit var linear2: LinearLayout
    lateinit var linearShow: LinearLayout
    lateinit var tvType: TextView
    lateinit var tvPoint: TextView
    lateinit var tvQty: TextView
    lateinit var tvType2: TextView
    lateinit var tvPoint2: TextView
    lateinit var tvQty2: TextView
    var customerId: String = ""
    val typeArr = arrayListOf<String>("Gift Coupon", "MRP Lable")
    var collectionRequest: ArrayList<PointCollectionRequest.Collection> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_point_collection)
        initViews()
    }

    private fun initViews() {
        cardBack = findViewById(R.id.cardBack)
        cardSubmit = findViewById(R.id.cardSubmit)
        edtPointType = findViewById(R.id.edtPointType)
        edtPoints = findViewById(R.id.edtPoints)
        edtQuantity = findViewById(R.id.edtQuantity)
        cardAdd = findViewById(R.id.cardAdd)
        linear1 = findViewById(R.id.linear1)
        linear2 = findViewById(R.id.linear2)
        linearShow = findViewById(R.id.linearShow)
        tvType = findViewById(R.id.tvType)
        tvPoint = findViewById(R.id.tvPoint)
        tvQty = findViewById(R.id.tvQty)
        tvType2 = findViewById(R.id.tvType2)
        tvPoint2 = findViewById(R.id.tvPoint2)
        tvQty2 = findViewById(R.id.tvQty2)

        val aa = ArrayAdapter(
            this@AddPointCollectionActivity,
            android.R.layout.simple_list_item_1,
            typeArr
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        edtPointType.setAdapter(aa)

        edtPointType.setOnTouchListener { view, motionEvent ->
            edtPointType.showDropDown()
            false
        }

        cardBack.setOnClickListener {
            onBackPressed()
        }

        cardSubmit.setOnClickListener {
            pointsCollection()
        }

        cardAdd.setOnClickListener {
            when {
                edtPointType.text.toString() == "" -> {
                    Toast.makeText(
                        this@AddPointCollectionActivity,
                        "Please select Point Type",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                edtPoints.text.toString() == "" || edtPoints.text.toString() == "0" -> {
                    Toast.makeText(
                        this@AddPointCollectionActivity,
                        "Please Enter Point",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                edtQuantity.text.toString() == "" || edtQuantity.text.toString() == "0" -> {
                    Toast.makeText(
                        this@AddPointCollectionActivity,
                        "Please Enter Quantity",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                else -> {
                    var collection = PointCollectionRequest.Collection()

                    collection.points = edtPoints.text.toString().toInt()
                    collection.point_type = edtPointType.text.toString().toLowerCase()
                    collection.quantity = edtQuantity.text.toString()

                    var isAvailable = false
                    if (collectionRequest.size != 0) {
                        for ((index, value) in collectionRequest.withIndex()) {
                            if (value.point_type == edtPointType.text.toString()
                                    .toLowerCase()
                            ) {
                                collectionRequest[index] = collection
                                isAvailable = true

                            }
                        }
                    }
                    edtPoints.setText("")
                    edtQuantity.setText("")

                    if (!isAvailable) {
                        collectionRequest.add(collection)
                    }
                    linearShow.visibility = View.VISIBLE
                    cardSubmit.visibility = View.VISIBLE
                    for (value in collectionRequest) {
                        if (value.point_type == typeArr[0].toLowerCase()) {
                            tvType.text = value.point_type
                            tvPoint.text = value.points.toString()
                            tvQty.text = value.quantity
                            linear1.visibility = View.VISIBLE
                        }
                        if (value.point_type == typeArr[1].toLowerCase()) {
                            tvType2.text = value.point_type
                            tvPoint2.text = value.points.toString()
                            tvQty2.text = value.quantity
                            linear2.visibility = View.VISIBLE
                        }
                    }


                }
            }
        }
    }

    private fun pointsCollection() {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this)

        var storeCustomerRequestModel = PointCollectionRequest()

        storeCustomerRequestModel.checkinid =
            StaticSharedpreference.getInfo(Constant.CHECKIN_ID, this@AddPointCollectionActivity)
                .toString()
        storeCustomerRequestModel.customer_id = StaticSharedpreference.getInfo(
            Constant.CHECKIN_CUST_ID,
            this@AddPointCollectionActivity
        )
            .toString()
        storeCustomerRequestModel.collection = collectionRequest

        ApiClient.pointsCollection(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this)
            .toString(),
            storeCustomerRequestModel, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            Toast.makeText(
                                this@AddPointCollectionActivity,
                                "Data inserted successfully.",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@AddPointCollectionActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@AddPointCollectionActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }


}