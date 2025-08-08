package com.exp.clonefieldkonnect.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.SalesImageAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.SalesImageModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap


class SalesDetailsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var edtInvoiceNumber: AutoCompleteTextView
    lateinit var edtInvoiceDate: AutoCompleteTextView
    lateinit var edtBuyer: AutoCompleteTextView
    lateinit var edtGrandTotal: AutoCompleteTextView
    lateinit var recyclerView: RecyclerView
     lateinit var cardBack: CardView
    lateinit var tvStatus: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_details)

        initViews()
        getSalesDetails()
    }

    private fun initViews() {
          recyclerView = findViewById(R.id.recyclerView)
        edtGrandTotal = findViewById(R.id.edtGrandTotal)
        edtBuyer = findViewById(R.id.edtBuyer)
        edtInvoiceDate = findViewById(R.id.edtInvoiceDate)
        edtInvoiceNumber = findViewById(R.id.edtInvoiceNumber)
        tvStatus = findViewById(R.id.tvStatus)
        cardBack = findViewById(R.id.cardBack)

        edtInvoiceNumber.setText(intent.getStringExtra("number"))
        edtInvoiceDate.setText(intent.getStringExtra("date")!!.split(" ")[0])
        edtBuyer.setText(intent.getStringExtra("name"))
        edtGrandTotal.setText(intent.getStringExtra("total"))

        if(intent.getStringExtra("status")=="12"){
             tvStatus.visibility = View.VISIBLE
            tvStatus.text = intent.getStringExtra("statusName")
           tvStatus.setTextColor(Color.parseColor("#99cc00"))
        }else if(intent.getStringExtra("status")=="6"){

           tvStatus.visibility = View.GONE

        }else{
             tvStatus.visibility = View.VISIBLE
            tvStatus.text = intent.getStringExtra("statusName")
            tvStatus.setTextColor(Color.parseColor("#cc0000"))
        }


        cardBack.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {

            R.id.cardBack -> {
                onBackPressed()
            }
        }
    }


    private fun getSalesDetails() {

        if (!Utilities.isOnline(this)) {
            return
        }
        var dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["sales_id"] = intent.getIntExtra("id", 0).toString()
        ApiClient.getSalesDetails(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@SalesDetailsActivity
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
                                TypeToken<ArrayList<SalesImageModel>>() {}.type

                            val salesImageList = gson.fromJson<ArrayList<SalesImageModel>>(
                                response.body()!!
                                    .get("data").asJsonObject.get("invoiceimages").asJsonArray,
                                listType
                            )

                            val layoutManager = GridLayoutManager(this@SalesDetailsActivity, 3)
                            recyclerView.layoutManager = layoutManager
                            recyclerView.setNestedScrollingEnabled(false);
                            val imageAdapter = SalesImageAdapter(salesImageList)
                            recyclerView.adapter = imageAdapter

                        }
                    } else {
                        Toast.makeText(
                            this@SalesDetailsActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

}


