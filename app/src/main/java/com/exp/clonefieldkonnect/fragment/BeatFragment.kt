package com.exp.clonefieldkonnect.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.CounterDashboardActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.BeatAdapter
import com.exp.clonefieldkonnect.adapter.BeatCustomerAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.BeatCustomerModel
import com.exp.clonefieldkonnect.model.BeatModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BeatFragment(var relativeHome: RelativeLayout, var three_dot: CardView) : Fragment() {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    var searchcity : ArrayList<String> = ArrayList()
    var searchbranch : ArrayList<String> = ArrayList()

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewCustomer: RecyclerView
    lateinit var edtSearch: EditText
    lateinit var cardCustomer: CardView
    lateinit var beatAdapter: BeatAdapter
    private var pageSize = "10"
    private var index = 1
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mLayoutManagerCustomer: RecyclerView.LayoutManager
    var beatArr: ArrayList<BeatModel?> = arrayListOf();
    lateinit var beatCustomerAdapter : BeatCustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_beat, container, false)
        activityLocal = activity as MainActivity

        initViews()
        return rootView
    }

    private fun initViews() {
        recyclerViewCustomer = rootView.findViewById(R.id.recyclerViewCustomer)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        cardCustomer = rootView.findViewById(R.id.cardCustomer)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        three_dot.visibility=View.GONE

        setAdapter()
        setRVScrollListener()

        var timer: Timer? = null
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                val DELAY: Long = 1000
                timer = Timer()
                timer!!.schedule(
                    object : TimerTask() {
                        override fun run() {
                            activityLocal.runOnUiThread(Runnable {

                                if (edtSearch.text.toString().length > 1) {

                                    getRetailerSearch( edtSearch.text.toString())
                                }

                                if(edtSearch.text.toString()==""){
                                    setRVScrollListener()
                                    setAdapter()
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


        getCheckin()
    }

    private fun setAdapter() {
        recyclerView.visibility = View.VISIBLE
        recyclerViewCustomer.visibility = View.GONE
        beatAdapter = BeatAdapter(beatArr)
        recyclerView.adapter = beatAdapter
    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                index++
                getBeatList()
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

    private fun setAdapter(beatList : ArrayList<BeatCustomerModel?>,isLead : Boolean) {

        recyclerView.visibility = View.GONE
        recyclerViewCustomer.visibility = View.VISIBLE

        mLayoutManagerCustomer= LinearLayoutManager(activityLocal)
        recyclerViewCustomer.layoutManager = mLayoutManagerCustomer
        beatCustomerAdapter = BeatCustomerAdapter(beatList,isLead,true,"")
        recyclerViewCustomer.adapter = beatCustomerAdapter

    }



    lateinit var dialog: Dialog
    private fun getCheckin() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        dialog = DialogClass.progressDialog(activityLocal)

        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("MM-yyyy-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)


        val queryParams = HashMap<String, String>()
        queryParams["page"] = "1"
        queryParams["pageSize"] = "3"

        ApiClient.getCheckin(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            if (response.body()!!.get("data").asJsonArray.size() != 0) {

                                val jsonArr = response.body()!!.get("data").asJsonArray

                                if (jsonArr.get(0).asJsonObject.get("checkout_date").asString == "") {
                                    val checkInId =
                                        jsonArr.get(0).asJsonObject.get("checkin_id").asInt
                                    val customerId =
                                        jsonArr.get(0).asJsonObject.get("customer_id").asString
                                    val customerName =
                                        jsonArr.get(0).asJsonObject.get("customer_name").asString
                                    val isLead = jsonArr.get(0).asJsonObject.get("is_lead").asString
                                    val beatSheducleId = jsonArr.get(0).asJsonObject.get("beat_schedule_id").asInt

                                    dialog.dismiss()

                                    StaticSharedpreference.saveInfo(
                                        Constant.CHECKIN_ID,
                                        checkInId.toString(),
                                        activityLocal
                                    )
                                    StaticSharedpreference.saveInfo(
                                        Constant.CHECKIN_CUST_ID,
                                        customerId.toString(),
                                        activityLocal
                                    )

                                    val intent =
                                        Intent(activityLocal, CounterDashboardActivity::class.java)
                                    intent.putExtra("checkInId", checkInId.toString())
                                    intent.putExtra("customerName", customerName.toString())
                                    intent.putExtra("beat_schedule_id", beatSheducleId.toString())
                                    intent.putExtra("outstanding", "")
                                    intent.putExtra("", "")
                                    if (isLead == "Yes")
                                        intent.putExtra("isLead", true)
                                    else
                                        intent.putExtra("isLead", false)
                                    startActivity(intent)

                                } else {
                                    getBeatList()
                                }
                            } else {
                                getBeatList()
                            }

                        } else {
                            dialog.dismiss()
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            activityLocal,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getBeatList() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        val queryParams = HashMap<String, String>()
        queryParams["page"] = index.toString()
        queryParams["pageSize"] = pageSize
        queryParams["beatDate"] = formattedDate

        ApiClient.getBeatList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {

                    if (index == 1)
                        dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<BeatModel>>() {}.type

                            var beatArr = gson.fromJson<ArrayList<BeatModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            if (beatArr.size == pageSize.toInt()) {
                                scrollListener.setLoaded()
                                beatAdapter.addLoadingView()

                            } else {
                                index = 1
                                scrollListener.setNotLoaded()
                            }

                            beatAdapter.removeLoadingView()
                            beatAdapter.addData(beatArr)


                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        //  dialog.dismiss()
                        Toast.makeText(
                            activityLocal,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getRetailerSearch(search: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

         dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()
        queryParams["search"] = search

        ApiClient.getRetailerSearch(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
            ).toString(), queryParams, searchcity,searchbranch, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                         var jsonArray =   response.body()!!.get("data").asJsonArray
                            var beatCustomerModelArr : ArrayList<BeatCustomerModel?> = arrayListOf()

                            for((index,value) in jsonArray.withIndex()){

                                var beatCustomerModel  = BeatCustomerModel()
                                beatCustomerModel.customerId = jsonArray[index].asJsonObject.get("customer_id").asInt.toString()
                                beatCustomerModel.customerName = jsonArray[index].asJsonObject.get("name").asString.toString()
                                beatCustomerModel.customerMobile = jsonArray[index].asJsonObject.get("mobile").asString.toString()
                                beatCustomerModel.profile_image = jsonArray[index].asJsonObject.get("profile_image").asString.toString()
                                beatCustomerModel.latitude = jsonArray[index].asJsonObject.get("latitude").asString.toString()
                                beatCustomerModel.longitude = jsonArray[index].asJsonObject.get("longitude").asString.toString()
                                beatCustomerModel.address = jsonArray[index].asJsonObject.get("address1").asString.toString()
                                beatCustomerModel.outstanding = jsonArray[index].asJsonObject.get("outstanding").asLong.toString()

                                beatCustomerModelArr.add(beatCustomerModel)
                            }

                            setAdapter(beatCustomerModelArr, false)
                        }
                    } else {
                        Toast.makeText(
                            activityLocal,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                three_dot.visibility=View.VISIBLE
                relativeHome.performClick()
                return@OnKeyListener true
            }
            false
        })
    }
}
