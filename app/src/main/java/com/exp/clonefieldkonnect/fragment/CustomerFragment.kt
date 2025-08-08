package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.CounterDashboardActivity
import com.exp.clonefieldkonnect.activity.LoginActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.BeatAdapter
import com.exp.clonefieldkonnect.adapter.BeatCustomerAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.*
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomerFragment(var relativeHome: RelativeLayout, var three_dot: CardView) : Fragment() {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    var searchcity : ArrayList<String> = ArrayList()
    lateinit var recyclerViewCustomer: RecyclerView
    lateinit var edtSearch: EditText
    lateinit var spinnerfilterCity: AutoCompleteTextView
    lateinit var spinnerfilterbranch: AutoCompleteTextView
    lateinit var spinnercustomertype: AutoCompleteTextView
    lateinit var beatAdapter: BeatAdapter
    private var pageSize = "10"
    private var index = 1
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManagerCustomer: RecyclerView.LayoutManager
    var beatArr: ArrayList<BeatCustomerModel?> = arrayListOf();
    lateinit var beatCustomerAdapter: BeatCustomerAdapter
    var userCityArr: java.util.ArrayList<UserCityModel> = arrayListOf()
    var userbranch: ArrayList<UserActivityListModel.Branches> = ArrayList()
    private var items: MutableList<SearchableItem> = ArrayList()
    var searchbranch : ArrayList<String> = ArrayList()
    var customertypename : ArrayList<String> = ArrayList()
    var customertypeid : ArrayList<Int> = ArrayList()
    var selectedtype_id = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_customer, container, false)
        activityLocal = activity as MainActivity

        initViews()
        return rootView
    }

    private fun initViews() {
        recyclerViewCustomer = rootView.findViewById(R.id.recyclerView)
        spinnerfilterCity = rootView.findViewById(R.id.spinnerfilterCity)
        spinnerfilterbranch = rootView.findViewById(R.id.spinnerfilterbranch)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        spinnercustomertype = rootView.findViewById(R.id.spinnercustomertype)
        three_dot.visibility=View.GONE
        userCityList("")
        getuseractivity()

        setAdapter(false)
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
                                    index = 1
                                    getRetailerSearch(
                                        edtSearch.text.toString(),
                                        searchcity,
                                        searchbranch,
                                        selectedtype_id
                                    )
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

    private fun getuseractivity() {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = ""
        queryParams["page"] = ""
        queryParams["start_date"] = ""
        queryParams["end_date"] = ""
        queryParams["search_name"] = ""
        var searchbranch : ArrayList<String> = ArrayList()

        ApiClient.getUserActivity(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,searchbranch,
            object : APIResultLitener<UserActivityListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<UserActivityListModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            userbranch.clear()

                            userbranch.addAll(response.body()!!.branches)

                            spinnerfilterbranch.setOnClickListener {
                                spinnerbranch()
                            }

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        activityLocal, false
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun spinnerbranch() {
        items.clear()
        for (item in userbranch) {
            items.add(item.name.toString(), item.id.toString(), true)
        }

        SearchableMultiSelectSpinner.show(activityLocal, "Select Branch", "Search", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: java.util.ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                searchbranch.clear()
                val selectedCity = selectedItems.map { it.text }
                val selectedbranchText = selectedCity.joinToString(",")
                spinnerfilterbranch.setText(selectedbranchText)

                val selectedCodes = selectedItems.map { it.code }
                for (item in selectedCodes){
                    searchbranch.add(item)
                }
                println("selectedCodes=1="+searchbranch)
                getRetailerSearch("", searchcity, searchbranch, selectedtype_id)
            }
        })
    }

    private fun userCityList(searchText : String) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = java.util.HashMap<String, String>()

        queryParams["cityname"] = searchText
        println("searchText ${searchText}")
        ApiClient.userCityList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<java.util.ArrayList<UserCityModel>>() {}.type

                            userCityArr  = gson.fromJson<java.util.ArrayList<UserCityModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )
                            println("userCityArr="+userCityArr)

                            spinnerfilterCity.setOnClickListener {
                                spinnerfilCity()
                            }
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        activityLocal,
                                        false
                                    )
                                }

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

    private fun spinnerfilCity() {
        items.clear()
        for (item in userCityArr) {
            items.add(item.city_name.toString(), item.id.toString(), item.isSelected)
        }

        SearchableMultiSelectSpinner.show(requireContext(), "Select City", "Search", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: java.util.ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                searchcity.clear()
                val selectedCity = selectedItems.map { it.text }
                val selectedCityText = selectedCity.joinToString(",") // Use your preferred delimiter
                spinnerfilterCity.setText(selectedCityText)

                val selectedCodes = selectedItems.map { it.code }
                for (item in selectedCodes){
                    searchcity.add(item)
                }
                println("selectedCodes=1="+searchcity)
                getRetailerSearch("", searchcity, searchbranch, selectedtype_id)

            }
        })
    }


    private fun setRVScrollListener() {
        mLayoutManagerCustomer = LinearLayoutManager(activityLocal)
        recyclerViewCustomer.layoutManager = mLayoutManagerCustomer
        recyclerViewCustomer.setHasFixedSize(true)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManagerCustomer as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                index++
                getRetailerSearch("", searchcity, searchbranch, selectedtype_id)
            }
        })
        recyclerViewCustomer.addOnScrollListener(scrollListener)
    }

    private fun setAdapter(isLead: Boolean) {

        beatCustomerAdapter = BeatCustomerAdapter(beatArr, isLead, true, "")
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
                                    val checkInId = jsonArr.get(0).asJsonObject.get("checkin_id").asInt
                                    val customerId = jsonArr.get(0).asJsonObject.get("customer_id").asString
                                    val customerName = jsonArr.get(0).asJsonObject.get("customer_name").asString
                                    val isLead = jsonArr.get(0).asJsonObject.get("is_lead").asString
                                    val beatSheducleId =
                                        jsonArr.get(0).asJsonObject.get("beat_schedule_id").asInt


                                    dialog.dismiss()

                                    StaticSharedpreference.saveInfo(Constant.CHECKIN_ID, checkInId.toString(), activityLocal)
                                    StaticSharedpreference.saveInfo(Constant.CHECKIN_CUST_ID, customerId.toString(), activityLocal)
//                                    println("customertypename="+customertype)

                                    val intent = Intent(activityLocal, CounterDashboardActivity::class.java)
                                    intent.putExtra("checkInId", checkInId.toString())
                                    intent.putExtra("customerName", customerName.toString())
                                    intent.putExtra("beat_schedule_id", beatSheducleId.toString())
                                    intent.putExtra("outstanding", "")
                                    intent.putExtra("", "")
//                                    intent.putExtra("customertype", customertype.toString())
                                    if (isLead == "Yes")
                                        intent.putExtra("isLead", true)
                                    else
                                        intent.putExtra("isLead", false)
                                    activityLocal.startActivity(intent)

                                } else {
                                    getRetailerSearch("", searchcity, searchbranch, selectedtype_id)
                                }
                            } else {
                                getRetailerSearch("", searchcity, searchbranch, selectedtype_id)
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
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
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

    private fun getRetailerSearch(
        search: String,
        searchcity: ArrayList<String>,
        searchbranch: ArrayList<String>,
        selectedtype_id: String
    ) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val queryParams = HashMap<String, String>()
        queryParams["search"] = search
        queryParams["page"] = index.toString()
        queryParams["pageSize"] = pageSize
        queryParams["customertype"] = selectedtype_id

        println("CITYYYY=="+search)
        ApiClient.getRetailerSearch(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            searchcity,searchbranch,object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    if (index == 1)
                        dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            customertypeid.clear()
                            customertypename.clear()
                            if(index==1)
                                beatCustomerAdapter.clear()

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<BeatCustomerModel>>() {}.type

                            var beatArr = gson.fromJson<ArrayList<BeatCustomerModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            val listType2 = object : TypeToken<ArrayList<AllCustomerTypeModel>>() {}.type
                            val body = response.body()

                            if (body != null && body.has("customerTypes") && !body.get("customerTypes").isJsonNull) {
                                val customerTypeList: ArrayList<AllCustomerTypeModel> = gson.fromJson(body.get("customerTypes").asJsonArray, listType2)
                                for (customerType in customerTypeList) {
                                    customerType.id?.let { customertypeid.add(it) }
                                    customerType.customertypeName?.let { customertypename.add(it) }
                                }
                            } else {
                                Log.e("CustomerFragment", "customerTypes not found or null in response")
                            }


                            spinnercustomertype.setOnClickListener {
                                spinnertype()
                            }

                            println("List==IDDDDDDDD: $customertypeid")
                            println("List==NAMEEEE: $customertypename")



                            if (beatArr.size == pageSize.toInt()) {
                                scrollListener.setLoaded()
                                beatCustomerAdapter.addLoadingView()

                            } else {
                                index = 1
                                scrollListener.setNotLoaded()
                            }
                            println("beatArr="+beatArr)
                            beatCustomerAdapter.removeLoadingView()
                            beatCustomerAdapter.addData(beatArr)


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


    private fun spinnertype() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = customertypename.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Customer Type")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            spinnercustomertype.setText("")
            selectedtype_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = customertypename.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = customertypeid[selectedPosition].toString()
                val selectedParentName = customertypename[selectedPosition].toString()

                spinnercustomertype.setText(selectedParentName)
                selectedtype_id = selectedParentId

                println("Abhi=id=$selectedtype_id")

                if (selectedtype_id.isNotEmpty()){
                    index = 1
                    getRetailerSearch("",searchcity,searchbranch,selectedtype_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
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
