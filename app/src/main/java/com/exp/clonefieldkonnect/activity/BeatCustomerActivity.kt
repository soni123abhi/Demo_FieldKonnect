package com.exp.clonefieldkonnect.activity

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.BeatCustomerAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.BeatCustomerModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BeatCustomerActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var beatAdapter : BeatCustomerAdapter
    lateinit var tvTitle : TextView
    lateinit var tvBeatName : TextView

    lateinit var cardBack : CardView
    lateinit var edtSearch : EditText

    private var pageSize = "10"
    private var index = 1
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    var beatCustomerArr: ArrayList<BeatCustomerModel?> = arrayListOf();
    var beatCustomerArrTemp: ArrayList<BeatCustomerModel?> = arrayListOf();
    var beatLeadArr: ArrayList<BeatCustomerModel?> = arrayListOf();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_beat_customer)
        initViews()
    }

    private fun initViews() {

        edtSearch = findViewById(R.id.edtSearch)
        recyclerView = findViewById(R.id.recyclerView)
        tvBeatName = findViewById(R.id.tvBeatName)
        cardBack = findViewById(R.id.cardBack)
        tvTitle = findViewById(R.id.tvTitle)

//        tvTitle.text= "Customers"
        tvBeatName.visibility= View.VISIBLE
        tvBeatName.text= intent.getStringExtra("name")


        mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager


      //  setRVScrollListener()

        cardBack.setOnClickListener {
            onBackPressed()
        }

        var timer: Timer? = null
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                val DELAY: Long = 1000
                timer = Timer()
                timer!!.schedule(
                    object : TimerTask() {
                        override fun run() {
                            runOnUiThread(Runnable {
                                var beatCustomerArrTemp2: ArrayList<BeatCustomerModel?> = arrayListOf()
                                for(value in beatCustomerArr){
                                    if(value!!.customerName!!.contains(edtSearch.text.toString())||
                                        value!!.customerMobile!!.contains(edtSearch.text.toString())){
                                        beatCustomerArrTemp2.add(value)
                                    }
                                }
                                beatCustomerArrTemp.clear()
                                beatCustomerArrTemp.addAll(beatCustomerArrTemp2)
                                setAdapter(beatCustomerArrTemp,false)
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


        getBeatCustomers()
    }

    private fun setAdapter(beatList : ArrayList<BeatCustomerModel?>,isLead : Boolean) {

        beatAdapter = BeatCustomerAdapter(beatList,isLead,intent.getBooleanExtra("is_today",false),intent.getStringExtra("beat_schedule_id")!!)
        recyclerView.adapter = beatAdapter

    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
               /* index++
                getBeatCustomers()*/
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }
    lateinit var dialog : Dialog

    private fun getBeatCustomers() {

        if (!Utilities.isOnline(this)) {
            return
        }

        if(index==1)
       dialog = DialogClass.progressDialog(this)

        val queryParams = HashMap<String, String>()
        queryParams["beat_id"] = intent.getStringExtra("id")!!

        ApiClient.getBeatCustomers(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    if(index==1)
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<BeatCustomerModel>>() {}.type

                             beatCustomerArr = gson.fromJson<ArrayList<BeatCustomerModel?>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                             beatLeadArr = gson.fromJson<ArrayList<BeatCustomerModel?>>(
                                response.body()!!.get("leads").asJsonArray,
                                listType
                            )
                            beatCustomerArrTemp.addAll(beatCustomerArr)
                            setAdapter(beatCustomerArrTemp,false)

                           /* if (beatCustomerArr.size == pageSize.toInt()) {
                                scrollListener.setLoaded()
                                beatAdapter.addLoadingView()

                            } else {
                                index = 1
                                scrollListener.setNotLoaded()
                            }

                            beatAdapter.removeLoadingView()
                            beatAdapter.addData(beatCustomerArr)*/


                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@BeatCustomerActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            this@BeatCustomerActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

}
