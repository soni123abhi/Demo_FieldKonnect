package com.exp.clonefieldkonnect.activity

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.ProductSearchAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProductSearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private lateinit var cardBack: CardView
    private lateinit var edtSearch: EditText
    private var pageSize = "10"
    private var index = 1

    companion object{
        var productArr: ArrayList<ProductNewModel?> = arrayListOf();
    }


    lateinit var productAdapter: ProductSearchAdapter

    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_search)
        productArr.clear()
        initViews()
    }

    private fun initViews() {
        edtSearch = findViewById(R.id.edtSearch)
        recyclerView = findViewById(R.id.recyclerView)
        cardBack = findViewById(R.id.cardBack)

        setAdapter()

         setRVScrollListener()

        cardBack.setOnClickListener {
            onBackPressed()
        }


        var timer: Timer? = null
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                /*  Handler().postDelayed(Runnable {
                      index = 1
                      getProductList(true,edtSearch.text.toString())

                  },1000)*/


                //timer  =new Timer();
                val DELAY: Long = 1000 //
                //            timer.cancel();
                timer = Timer()
                timer!!.schedule(
                    object : TimerTask() {
                        override fun run() {
                            runOnUiThread(Runnable {

                                if (edtSearch.text.toString().length >= 1) {

                                    getProductList(true, edtSearch.text.toString(),true,"")
                                }
                            })
                        }
                    },
                    DELAY
                )
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (timer != null) {
                    timer!!.cancel()
                }
            }

        })

    }

    private fun setAdapter() {
        productAdapter = ProductSearchAdapter(intent.getStringExtra("checkin")!!)
        productAdapter.notifyDataSetChanged()
        recyclerView.adapter = productAdapter
    }



    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this@ProductSearchActivity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                index++
                getProductList(false, edtSearch.text.toString(),false,"")
            }
        })
//        4444
        recyclerView.addOnScrollListener(scrollListener)
    }

    lateinit var dialog: Dialog

     fun getProductList(isProgress: Boolean, search: String,isIndexOne : Boolean,subCategory_id : String) {

        if (!Utilities.isOnline(this@ProductSearchActivity)) {
            return
        }

        if (isProgress)
            dialog = DialogClass.progressDialog(this@ProductSearchActivity)

         if (isIndexOne)
             index = 1

        val queryParams = HashMap<String, String>()
        queryParams["page"] = index.toString()
        queryParams["pageSize"] = pageSize
        queryParams["search"] = search

        ApiClient.getProductList(

            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@ProductSearchActivity
            ).toString(), queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    if (isProgress) {
                        productArr.clear()
                        dialog.dismiss()
                    }
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            var productArrLocal: ArrayList<ProductNewModel> = arrayListOf();
                            val gson = Gson()

                            val listTypePro = object :
                                TypeToken<java.util.ArrayList<ProductNewModel>>() {}.type

                            productArrLocal = gson.fromJson<java.util.ArrayList<ProductNewModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listTypePro
                            )
                            println("ABHIII==pro="+response.body()!!.get("data"))

                            if (productArrLocal.size == pageSize.toInt()) {
                                scrollListener.setLoaded()
                                productAdapter.addLoadingView()

                            } else {
                                index = 1
                                scrollListener.setNotLoaded()
                            }

                            productAdapter.removeLoadingView()
                            productAdapter.addData(productArrLocal)

                        }
                    } else {
                        Toast.makeText(
                            this@ProductSearchActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, this@ProductSearchActivity
        )
    }




    override fun onResume() {
        super.onResume()

    }

}