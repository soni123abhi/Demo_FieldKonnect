package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.LoginActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.OrderListNewAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.OrderListModel
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OrderListFragment(
    var relativeHome: RelativeLayout,
    var three_dot: CardView,
    var customer_id: String
) : Fragment(),View.OnClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoDta: TextView
    private var pageSize = "20"
    private var page = 1
    var orderListArr: ArrayList<OrderListModel.Datum?> = arrayListOf()
    var orderListArr2: ArrayList<OrderListModel.Datum?> = arrayListOf()
    var orderuserlist: ArrayList<OrderListModel.Users> = arrayListOf()
    var orderstatuslist: ArrayList<OrderListModel.AllStatus> = arrayListOf()
    var orderusername: ArrayList<String> = arrayListOf()
    var orderuserid: ArrayList<String> = arrayListOf()
    var orderstatusname: ArrayList<String> = arrayListOf()
    var orderstatusid: ArrayList<String> = arrayListOf()
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var cardSearch: RelativeLayout
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtSearchstatus: AutoCompleteTextView

    var selecteduser_id : String = ""
    var selectedstatus_id : String = ""
    var start_date = ""
    var end_date = ""
    private var isLoading = false
    private var lastPosition = -1



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_order_list, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        tvNoDta = rootView.findViewById(R.id.tvNoDta)
        recyclerView = rootView.findViewById(R.id.recyclerView_orderr)
        three_dot.visibility=View.GONE
        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchstatus = rootView.findViewById(R.id.edtSearchstatus)

        println("customer_idcustomer_id=="+customer_id)


        getOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id,customer_id)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (orderListArr2.size == 20){
                        getOrderList(page, start_date, end_date, selecteduser_id, selectedstatus_id, customer_id)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })


        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)

    }


    private fun getOrderList(
        page: Int,
        start_date: String,
        end_date: String,
        selecteduser_id: String,
        selectedstatus_id: String,
        customer_id: String
    ) {
        isLoading = true
        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["page"] = page.toString()
        queryParams["pageSize"] = pageSize
        queryParams["startdate"] = start_date
        queryParams["enddate"] = end_date
        queryParams["user_id"] = selecteduser_id
        queryParams["status_id"] = selectedstatus_id
        queryParams["customer_id"] = customer_id
        ApiClient.getOrderList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams, object : APIResultLitener<OrderListModel> {
                override fun onAPIResult(
                    response: Response<OrderListModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            orderListArr2.clear()

                            if (page ==1)
                                orderListArr.clear()

                            tvNoDta.visibility = View.GONE
                            orderListArr.addAll(response.body()!!.data)
                            orderListArr2 = response.body()!!.data


                            orderuserlist.addAll(response.body()!!.users)
                            orderstatuslist.addAll(response.body()!!.allStatus)

                            for (item in orderuserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!orderusername.contains(name)) {
                                    orderusername.add(name)
                                    orderuserid.add(id)
                                }
                            }

                            for (item in orderstatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!orderstatusname.contains(name)) {
                                    orderstatusname.add(name)
                                    orderstatusid.add(id)
                                }
                            }

                            edtSearch.setOnClickListener {
                                spinneruser()
                            }

                            edtSearchstatus.setOnClickListener {
                                spinnerstatus()
                            }
                            setuprecyclerlist()
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
                        isLoading = false
                    } else {
                        Toast.makeText(
                            activityLocal,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, activityLocal
        )
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerlist() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView.layoutManager = mLayoutManager
        val orderadapter = OrderListNewAdapter(activityLocal, orderListArr)
        recyclerView.adapter = orderadapter
        recyclerView.scrollToPosition(lastPosition)
        orderadapter.notifyDataSetChanged()
    }


    private fun spinnerstatus() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = orderstatusname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Status")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtSearchstatus.setText("")
            selectedstatus_id = ""
            page = 1
            getOrderList(page, start_date, end_date, selecteduser_id, selectedstatus_id, customer_id)
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = orderstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = orderstatusid[selectedPosition].toString()
                val selectedParentName = orderstatusname[selectedPosition].toString()

                edtSearchstatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")

                if (selectedstatus_id.isNotEmpty()){
                    page = 1
                    getOrderList(
                        page,
                        start_date,
                        end_date,
                        selecteduser_id,
                        selectedstatus_id,
                        customer_id
                    )
                }

                dialog.dismiss()
            }
        }

        dialog.show()
    }


    private fun spinneruser() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = orderusername.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select User")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtSearch.setText("")
            selecteduser_id = ""
            page = 1
            getOrderList(page, start_date, end_date, selecteduser_id, selectedstatus_id, customer_id)
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = orderusername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = orderuserid[selectedPosition].toString()
                val selectedParentName = orderusername[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    getOrderList(page, start_date, end_date, selecteduser_id, selectedstatus_id, customer_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show()
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

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cardFrom -> {
                Utilities.datePicker(tvFrom, tvTo.text.toString(), "", true, activityLocal)
            }
            R.id.cardTo -> {
                Utilities.datePicker(tvTo, "", tvFrom.text.toString(), false, activityLocal)
            }
            R.id.cardSearch -> {
                if (tvFrom.text.toString().isNullOrEmpty()){
                    Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                }else if (tvTo.text.toString().isNullOrEmpty()){
                    Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                }else{
                    val convertedDate = convertDateFormats(tvFrom.text.toString(),tvTo.text.toString())
                    start_date = convertedDate.first
                    end_date = convertedDate.second
                    println("from=="+start_date+"To="+end_date)
                    page = 1
                    if (start_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                    }else if (end_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                    }else{
                        getOrderList(
                            page,
                            start_date,
                            end_date,
                            selecteduser_id,
                            selectedstatus_id,
                            customer_id
                        )
                    }
                }
            }
        }
    }
    fun convertDateFormats(tvFrom: String, tvTo: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val tvFromParsed = inputFormat.parse(tvFrom)

        val tvToParsed = inputFormat.parse(tvTo)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val tvfrom = outputFormat.format(tvFromParsed)
        val tvTo = outputFormat.format(tvToParsed)

        return Pair(tvfrom,tvTo)
    }
}