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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.SpecialOrderListAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.SpecialDiscountModel
import com.exp.import.Utilities
import kotlinx.android.synthetic.main.fragment_cluster_discount.*
import kotlinx.android.synthetic.main.fragment_special_discount.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SpecialDiscountFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment(),View.OnClickListener {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private lateinit var cardBack_special: CardView
    private lateinit var tvTitle_special: TextView
    private lateinit var rel_main_special: RelativeLayout
    private lateinit var recyclerView_special: RecyclerView
    private lateinit var edtSearch: AutoCompleteTextView
    private lateinit var edtSearchstatus: AutoCompleteTextView

    lateinit var cardFrom_special: LinearLayout
    lateinit var cardTo_special: LinearLayout
    lateinit var tvFrom_special: TextView
    lateinit var tvTo_special: TextView
    lateinit var cardSearch: RelativeLayout


    var orderListArr: ArrayList<SpecialDiscountModel.Data> = arrayListOf()
    var orderListArr2: ArrayList<SpecialDiscountModel.Data> = arrayListOf()
    var specialuserlist: ArrayList<SpecialDiscountModel.AllUsers> = arrayListOf()
    var specialstatuslist: ArrayList<SpecialDiscountModel.AllStatus> = arrayListOf()
    var specialusername: ArrayList<String> = arrayListOf()
    var specialuserid: ArrayList<String> = arrayListOf()
    var specialstatusname: ArrayList<String> = arrayListOf()
    var specialstatusid: ArrayList<String> = arrayListOf()


    var selecteduser_id = ""
    var selectedstatus_id = ""
    var start_date = ""
    var end_date = ""
    var page = 1
    private var pageSize = "20"
    private var isLoading = false
    private var lastPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_special_discount, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        cardBack_special = rootView.findViewById(R.id.cardBack_special)
        tvTitle_special = rootView.findViewById(R.id.tvTitle_special)
        rel_main_special = rootView.findViewById(R.id.rel_main_special)
        recyclerView_special = rootView.findViewById(R.id.recyclerView_special)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchstatus = rootView.findViewById(R.id.edtSearchstatus)

        cardFrom_special = rootView.findViewById(R.id.cardFrom_special)
        cardTo_special = rootView.findViewById(R.id.cardTo_special)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom_special = rootView.findViewById(R.id.tvFrom_special)
        tvTo_special = rootView.findViewById(R.id.tvTo_special)

        cardBack_special.setOnClickListener {
            handleBackPressed()
        }
        cardFrom_special.setOnClickListener(this)
        cardTo_special.setOnClickListener(this)
        cardSearch.setOnClickListener(this)

        getspecialOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)

        recyclerView_special.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (orderListArr2.size == 20){
                        getspecialOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })


    }

    private fun getspecialOrderList(
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
        queryParams["page"] = page.toString()
        queryParams["pageSize"] = pageSize
        queryParams["startdate"] = start_date
        queryParams["enddate"] = end_date
        queryParams["user_id"] = selecteduser_id
        queryParams["status_id"] = selectedstatus_id
        ApiClient.getspecialOrderList(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
            ).toString(), queryParams, object : APIResultLitener<SpecialDiscountModel> {
                override fun onAPIResult(
                    response: Response<SpecialDiscountModel>?,
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


                            specialuserlist.addAll(response.body()!!.allUsers)
                            specialstatuslist.addAll(response.body()!!.allStatus)

                            for (item in specialuserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!specialusername.contains(name)) {
                                    specialusername.add(name)
                                    specialuserid.add(id)
                                }
                            }

                            for (item in specialstatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!specialstatusname.contains(name)) {
                                    specialstatusname.add(name)
                                    specialstatusid.add(id)
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

    private fun spinnerstatus() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = specialstatusname.map { it.toString() }.toTypedArray()
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
            val selectedPosition = specialstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = specialstatusid[selectedPosition].toString()
                val selectedParentName = specialstatusname[selectedPosition].toString()

                edtSearchstatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")

                if (selectedstatus_id.isNotEmpty()){
                    page = 1
                    getspecialOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
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

        val colorsArray = specialusername.map { it.toString() }.toTypedArray()
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
            val selectedPosition = specialusername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = specialuserid[selectedPosition].toString()
                val selectedParentName = specialusername[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    getspecialOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerlist() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_special.layoutManager = mLayoutManager
        val orderadapter = SpecialOrderListAdapter(activityLocal, orderListArr)
        recyclerView_special.adapter = orderadapter
        recyclerView_special.scrollToPosition(lastPosition)
        orderadapter.notifyDataSetChanged()
    }

    private fun handleBackPressed() {
        println("tabPositiontabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }else{
            tvTitle_special.text = "Report"
            rel_main_special.visibility = View.GONE
            fragment_container_special.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
            tabPosition = 2
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_special, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cardFrom_special -> {
                Utilities.datePicker(tvFrom_special, tvTo_special.text.toString(), "", true, activityLocal)
            }
            R.id.cardTo_special -> {
                Utilities.datePicker(tvTo_special, "", tvFrom_special.text.toString(), false, activityLocal)
            }
            R.id.cardSearch -> {
                if (tvFrom_special.text.toString().isNullOrEmpty()){
                    Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                }else if (tvTo_special.text.toString().isNullOrEmpty()){
                    Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                }else{
                    val convertedDate = convertDateFormats(tvFrom_special.text.toString(),tvTo_special.text.toString())
                    start_date = convertedDate.first
                    end_date = convertedDate.second
                    println("from=="+start_date+"To="+end_date)
                    page = 1
                    if (start_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                    }else if (end_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                    }else{
                        getspecialOrderList(page, start_date, end_date,selecteduser_id, selectedstatus_id)
                    }
                }
            }
        }
    }
    fun convertDateFormats(tvFrom_special: String, tvTo_special: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val tvFromParsed = inputFormat.parse(tvFrom_special)

        val tvToParsed = inputFormat.parse(tvTo_special)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val tvfrom = outputFormat.format(tvFromParsed)
        val tvTo = outputFormat.format(tvToParsed)

        return Pair(tvfrom,tvTo)
    }


}