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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.CustomerretailerAdapter
import com.exp.clonefieldkonnect.adapter.SalesListNewAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.SalessListModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class NewSalesReportFragment(
   var cardBack: CardView,
   var linearTopreport: CardView,
   var tabPosition: Int,
   var tvTitle: TextView
) : Fragment(),View.OnClickListener {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var tvTitle_sales_report: TextView
    lateinit var tvNoDta: TextView
    lateinit var linearTop_sales_report: CardView
    lateinit var fragment_container_sales_report: FrameLayout
    lateinit var cardBack_sales_back: CardView
    lateinit var rel_main_sales_report: RelativeLayout
    lateinit var recyclerView_orderr: RecyclerView
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var edtstatus: AutoCompleteTextView
    lateinit var edtSearchcustomer: AutoCompleteTextView
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var cardSearch: RelativeLayout
    var flag : String = ""
    private var pageSize = "20"
    private var pageSize_retailer = "200"
    private var customer_id = ""
    private var parent_search = ""
    private var selectedstatus_id = ""
    private var page = 1
    private var page_retailer = 1
    var start_date = ""
    var end_date = ""
    private var isLoading = false
    private var lastPosition = -1
    var orderListArr: ArrayList<SalessListModel.sales_Data> = arrayListOf()
    var orderListArr2: ArrayList<SalessListModel.sales_Data> = arrayListOf()
    var orderstatuslist: ArrayList<SalessListModel.AllStatus> = arrayListOf()
    var orderstatusname: ArrayList<String> = arrayListOf()
    var orderstatusid: ArrayList<String> = arrayListOf()
    var retailerArr: ArrayList<DistriutorModel.Datum> = arrayListOf()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_new_sales_report, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        cardBack_sales_back = rootView.findViewById(R.id.cardBack_sales_back)
        fragment_container_sales_report = rootView.findViewById(R.id.fragment_container_sales_report)
        tvTitle_sales_report = rootView.findViewById(R.id.tvTitle_sales_report)
        linearTop_sales_report = rootView.findViewById(R.id.linearTop_sales_report)
        rel_main_sales_report = rootView.findViewById(R.id.rel_main_sales_report)
        recyclerView_orderr = rootView.findViewById(R.id.recyclerView_orderr)
        tvNoDta = rootView.findViewById(R.id.tvNoDta)
        edtstatus = rootView.findViewById(R.id.edtstatus)
        edtSearchcustomer = rootView.findViewById(R.id.edtSearchcustomer)

        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)


        getsalesList(page, start_date, end_date, selectedstatus_id, customer_id)
        getRetailers(page_retailer,pageSize_retailer,parent_search)

        recyclerView_orderr.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (orderListArr2.size == 20){
                        getsalesList(page, start_date, end_date, selectedstatus_id, customer_id)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })


        cardBack_sales_back.setOnClickListener {
            handleBackPressed()
        }
        edtSearchcustomer.setOnClickListener {
            showretailerdropdown()
        }

        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)

    }

    private lateinit var currentDialog: Dialog

    private fun showretailerdropdown() {
        currentDialog = Dialog(activityLocal)
        currentDialog.setContentView(R.layout.dialog_custom_retaier_dropdown)

        val searchInput = currentDialog.findViewById<EditText>(R.id.search_input)
        val searchIcon = currentDialog.findViewById<ImageView>(R.id.search_icon)
        val recyclerView = currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown)


        val adapter = CustomerretailerAdapter(retailerArr, currentDialog) { selectedItem ->
            val selectedCustomerId = selectedItem.customer_id
            val selectedCustomerName = selectedItem.name
            Log.d("SelectedItem", "ID: $selectedCustomerId")
            edtSearchcustomer.setText(selectedCustomerName)
            customer_id = selectedCustomerId.toString()
            getsalesList(page, start_date, end_date, selectedstatus_id,customer_id)

        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activityLocal)
        recyclerView.scrollToPosition(lastPosition)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page_retailer++
                    if (retailerArr.size >= 200) {
                        getRetailers(page_retailer, pageSize_retailer, "") // Call the API to load more items
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

        searchIcon.setOnClickListener {
            val parent_search = searchInput.text.toString()
            page_retailer = 1 // Reset page to 1 for a new search
            getRetailers(page_retailer, pageSize_retailer, parent_search) // Call API with the search query
        }
        currentDialog.show()
    }


    private fun getRetailers(page: Int, pageSize: String, parent_search: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["search"] = parent_search

        ApiClient.getRetailers(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
            ).toString(), queryParams, object : APIResultLitener<DistriutorModel> {
                override fun onAPIResult(
                    response: Response<DistriutorModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            if (page == 1) {
                                retailerArr.clear() // Clear the previous results for the new search
                            }
                            retailerArr.addAll(response.body()!!.data!!)

                            if (::currentDialog.isInitialized && currentDialog.isShowing) {
                                retailerArr?.let {
                                    (currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown).adapter as CustomerretailerAdapter)
                                        .updateList(it)
                                }
                            }


                            /* retailerArr = response.body()!!.data

                             val disName = arrayOfNulls<String>(retailerArr!!.size)

                             for (i in retailerArr!!.indices) {

                                 disName[i] = retailerArr!![i].name

                             }

                             val aa = ArrayAdapter(
                                 this@AddToCartActivity,
                                 android.R.layout.simple_list_item_1,
                                 disName
                             )
                             aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                             edtRetailer.setAdapter(aa)*/

                        }
                    } else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }


    private fun getsalesList(
        page: Int,
        start_date: String,
        end_date: String,
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
        queryParams["start_date"] = start_date
        queryParams["end_date"] = end_date
        queryParams["status_id"] = selectedstatus_id
        queryParams["buyer_id"] = customer_id
        ApiClient.getsalessList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams, object :
                APIResultLitener<SalessListModel> {
                override fun onAPIResult(
                    response: Response<SalessListModel>?,
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


                            orderstatuslist.addAll(response.body()!!.allStatus)


                            for (item in orderstatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!orderstatusname.contains(name)) {
                                    orderstatusname.add(name)
                                    orderstatusid.add(id)
                                }
                            }


                            edtstatus.setOnClickListener {
                                spinnerstatus()
                            }
                            setuprecyclerlist()
                        } else {
                            tvNoDta.visibility = View.VISIBLE
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
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }, activityLocal
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerlist() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_orderr.layoutManager = mLayoutManager
        val orderadapter = SalesListNewAdapter(activityLocal, orderListArr)
        recyclerView_orderr.adapter = orderadapter
        recyclerView_orderr.scrollToPosition(lastPosition)
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
            edtstatus.setText("")
            selectedstatus_id = ""
            page = 1
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = orderstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = orderstatusid[selectedPosition].toString()
                val selectedParentName = orderstatusname[selectedPosition].toString()

                edtstatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")

                if (selectedstatus_id.isNotEmpty()){
                    page = 1
                    getsalesList(page, start_date, end_date, selectedstatus_id, customer_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show()
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
        println("flag=="+flag)
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag.equals("")){
            tabPosition = 2
            tvTitle_sales_report.text = "Report"
            rel_main_sales_report.visibility = View.GONE
            linearTop_sales_report.visibility = View.GONE
            fragment_container_sales_report.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }else if (flag.equals("2")){
            flag = ""
            tvTitle_sales_report.text = "Dispatch Report"
            rel_main_sales_report.visibility = View.VISIBLE
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_sales_report, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
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
                    if (start_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                    }else if (end_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                    }else{
                        page = 1
                        getsalesList(page, start_date, end_date, selectedstatus_id, customer_id)
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