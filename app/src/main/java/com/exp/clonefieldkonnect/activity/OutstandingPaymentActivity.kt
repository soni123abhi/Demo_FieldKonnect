package com.exp.clonefieldkonnect.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.OutstandingAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.OutstandingModel
import com.exp.clonefieldkonnect.model.ProductDetailModel
import org.json.JSONObject
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OutstandingPaymentActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var outstandingAdapter : OutstandingAdapter
     var outstandingArr : ArrayList<OutstandingModel.Data?> = arrayListOf()

    lateinit var cardBack : CardView

    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var pageSize = "10"
    private var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outstanding_payment)
        initViews()
    }

    private fun initViews() {


        recyclerView = findViewById(R.id.recyclerView)

        cardBack = findViewById(R.id.cardBack)


        mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager


      //  setRVScrollListener()

        cardBack.setOnClickListener {
            onBackPressed()
        }
        setAdapter()
        //setRVLayoutManager()
        setRVScrollListener()

        getPaymentList(true)
        getProductDetails()

    }

    private fun setAdapter() {

        outstandingAdapter = OutstandingAdapter(outstandingArr)
        recyclerView.adapter = outstandingAdapter

    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                index++
                getPaymentList(false)
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }


    lateinit var dialog : Dialog

    private fun getPaymentList(isProgress: Boolean) {

        if (!Utilities.isOnline(this)) {
            return
        }
        if (isProgress)
       dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["customer_id"] =  StaticSharedpreference.getInfo(
            Constant.CHECKIN_CUST_ID,
            this@OutstandingPaymentActivity
        )
            .toString()

        ApiClient.getPaymentList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<OutstandingModel> {
                override fun onAPIResult(response: Response<OutstandingModel>?, errorMessage: String?) {

                    if (isProgress) {
                        outstandingArr.clear()
                        dialog.dismiss()
                    }
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            var outstandingArrLocal = response.body()!!.data

                            if (outstandingArrLocal!!.size == pageSize.toInt()) {
                                scrollListener.setLoaded()
                                outstandingAdapter.addLoadingView()

                            } else {
                                index = 1
                                scrollListener.setNotLoaded()
                            }

                            outstandingAdapter.removeLoadingView()
                            outstandingAdapter.addData(outstandingArrLocal)

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@OutstandingPaymentActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@OutstandingPaymentActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getProductDetails() {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this)

        val queryParams = java.util.HashMap<String, String>()
        queryParams["payment_id"] = "3"
        ApiClient.getPaymentInfo(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@OutstandingPaymentActivity
            ).toString(), queryParams, object : APIResultLitener<ProductDetailModel> {
                override fun onAPIResult(
                    response: Response<ProductDetailModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {


                        }
                    } else {

                    }
                }
            }
        )
    }



}
