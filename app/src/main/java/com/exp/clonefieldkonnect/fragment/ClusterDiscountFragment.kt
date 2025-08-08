package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.ClusterOrderListAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.*
import com.exp.clonefieldkonnect.model.ClusterOrderListModel
import com.exp.import.Utilities
import kotlinx.android.synthetic.main.fragment_cluster_discount.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ClusterDiscountFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment(),View.OnClickListener {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private lateinit var rel_main_cluster: RelativeLayout
    private lateinit var recyclerView_cluster: RecyclerView
    private lateinit var cardBack_cluster: CardView
    private lateinit var tvTitle_cluster: TextView
    private var pageSize = "20"
    private var index = 1
    var orderListArr: ArrayList<ClusterOrderListModel.Datum?> = arrayListOf()
    var orderListArr2: ArrayList<ClusterOrderListModel.Datum?> = arrayListOf()
    var clusteruserlist: ArrayList<ClusterOrderListModel.Users> = arrayListOf()
    var clusterstatuslist: ArrayList<ClusterOrderListModel.AllStatus> = arrayListOf()
    var clusterusername: ArrayList<String> = arrayListOf()
    var clusteruserid: ArrayList<String> = arrayListOf()
    var clusterstatusname: ArrayList<String> = arrayListOf()
    var clusterstatusid: ArrayList<String> = arrayListOf()
    lateinit var statementAdapter: ClusterOrderListAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var cardSearch: RelativeLayout
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtSearchstatus: AutoCompleteTextView

    var selecteduser_id = ""
    var selectedstatus_id = ""
    var start_date = ""
    var end_date = ""
    var page = 1
    private var isLoading = false
    private var lastPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_cluster_discount, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        rel_main_cluster = rootView.findViewById(R.id.rel_main_cluster)
        recyclerView_cluster = rootView.findViewById(R.id.recyclerView_cluster)
        tvTitle_cluster = rootView.findViewById(R.id.tvTitle_cluster)
        cardBack_cluster = rootView.findViewById(R.id.cardBack_cluster)

        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchstatus = rootView.findViewById(R.id.edtSearchstatus)

        rel_main_cluster.visibility = View.VISIBLE

        getclusterOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)

        recyclerView_cluster.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (orderListArr2.size == 20){
                        getclusterOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })


        cardBack_cluster.setOnClickListener {
            handleBackPressed()
        }
        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun handleBackPressed() {
        println("tabPositiontabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }else{
            tvTitle_cluster.text = "Report"
            rel_main_cluster.visibility = View.GONE
            fragment_container_cluster.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
            tabPosition = 2
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_cluster, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    private fun getclusterOrderList(
        page: Int,
        start_date: String,
        end_date: String,
        selecteduser_id: String,
        selectedstatus_id: String
    ) {

        isLoading = true
        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["page"] = index.toString()
        queryParams["pageSize"] = pageSize
        queryParams["startdate"] = start_date
        queryParams["enddate"] = end_date
        queryParams["user_id"] = selecteduser_id
        queryParams["status_id"] = selectedstatus_id
        ApiClient.getclusterOrderList(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
            ).toString(), queryParams, object : APIResultLitener<ClusterOrderListModel> {
                override fun onAPIResult(
                    response: Response<ClusterOrderListModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            orderListArr2.clear()

                            if (page ==1)
                                orderListArr.clear()

                            orderListArr.addAll(response.body()!!.data)
                            orderListArr2 = response.body()!!.data


                            clusteruserlist.addAll(response.body()!!.users)
                            clusterstatuslist.addAll(response.body()!!.allStatus)

                            for (item in clusteruserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!clusterusername.contains(name)) {
                                    clusterusername.add(name)
                                    clusteruserid.add(id)
                                }
                            }

                            for (item in clusterstatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!clusterstatusname.contains(name)) {
                                    clusterstatusname.add(name)
                                    clusterstatusid.add(id)
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

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
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
        recyclerView_cluster.layoutManager = mLayoutManager
        val orderadapter = ClusterOrderListAdapter(activityLocal, orderListArr)
        recyclerView_cluster.adapter = orderadapter
        recyclerView_cluster.scrollToPosition(lastPosition)
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

        val colorsArray = clusterstatusname.map { it.toString() }.toTypedArray()
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
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = clusterstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = clusterstatusid[selectedPosition].toString()
                val selectedParentName = clusterstatusname[selectedPosition].toString()

                edtSearchstatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")

                if (selectedstatus_id.isNotEmpty()){
                    page = 1
                    getclusterOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
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

        val colorsArray = clusterusername.map { it.toString() }.toTypedArray()
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
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = clusterusername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = clusteruserid[selectedPosition].toString()
                val selectedParentName = clusterusername[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    getclusterOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show()
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
                        getclusterOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
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