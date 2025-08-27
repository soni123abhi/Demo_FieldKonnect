package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.ExpenseApprovalAdapter
import com.exp.clonefieldkonnect.adapter.ExpenseImagePathViewAdapter
import com.exp.clonefieldkonnect.adapter.UserActivityDetailAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.ExpenseApprovalModel
import com.exp.clonefieldkonnect.model.ExpenseApprovalSubmitModel
import com.exp.clonefieldkonnect.model.UserActivityDetailModel
import com.exp.clonefieldkonnect.model.UserExpenseDetailModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseApprovalFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment(),View.OnClickListener,ExpenseApprovalAdapter.OnEmailClick {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private lateinit var recyclerView_expense_approval: RecyclerView
    private lateinit var recyclerView_user_detail: RecyclerView
    private lateinit var linearTop_expense_approval: CardView
    private lateinit var fragment_container_expense_approval: FrameLayout
    private lateinit var cardBack_expense_approval: CardView
    lateinit var tvTitle_expense_approval: TextView
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var cardSearch: RelativeLayout
    lateinit var rel_main_expense: RelativeLayout
    lateinit var rec_user_main2: RelativeLayout
    lateinit var edtSearchbranch: AutoCompleteTextView
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtexpensetype: AutoCompleteTextView
    var start_date = ""
    var end_date = ""
    var expense_id : String = ""
    var selecteduser_id : String = ""
    var selectedstatus_id : String = ""
    var flag : String = ""
    var searchbranch : ArrayList<String> = ArrayList()
    private var items: MutableList<SearchableItem> = ArrayList()

    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "20"
    var page_count : String = ""
    var expenseapprovelist: ArrayList<ExpenseApprovalModel.Data> = ArrayList()
    var expenseapprovelist2: ArrayList<ExpenseApprovalModel.Data> = ArrayList()
    var expensebranchlist: ArrayList<ExpenseApprovalModel.Branches> = ArrayList()
    var expenseuserlist: ArrayList<ExpenseApprovalModel.Users> = ArrayList()
    var expensestatusist: ArrayList<ExpenseApprovalModel.AllStatus> = ArrayList()
    var expenseusername : ArrayList<String> = ArrayList()
    var expenseuserid : ArrayList<String> = ArrayList()
    var expensestatusname : ArrayList<String> = ArrayList()
    var expensestatusid : ArrayList<String> = ArrayList()
    var useractivitydetail: ArrayList<UserActivityDetailModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_expense_approval, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun getexpenseapprovallist(
        page: Int,
        start_date: String,
        end_date: String,
        searchbranch: ArrayList<String>,
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
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["start_date"] = start_date
        queryParams["end_date"] = end_date
        queryParams["user_id"] = selecteduser_id
        queryParams["status"] = selectedstatus_id

        ApiClient.getexpenseaprovelist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,searchbranch,
            object : APIResultLitener<ExpenseApprovalModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<ExpenseApprovalModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            expenseapprovelist2.clear()
                            expensebranchlist.clear()
                            expenseuserlist.clear()
                            expensestatusist.clear()

                            if (this@ExpenseApprovalFragment.page ==1)
                                expenseapprovelist.clear()

                            expenseapprovelist.addAll(response.body()!!.data)
                            expensebranchlist.addAll(response.body()!!.branches)
                            expenseuserlist.addAll(response.body()!!.users)
                            expensestatusist.addAll(response.body()!!.allStatus)

                            for (item in expenseuserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!expenseusername.contains(name)) {
                                    expenseusername.add(name)
                                    expenseuserid.add(id)
                                }
                            }
                            for (item in expensestatusist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!expensestatusname.contains(name)) {
                                    expensestatusname.add(name)
                                    expensestatusid.add(id)
                                }
                            }
                            expenseapprovelist2 = response.body()!!.data

                            edtSearchbranch.setOnClickListener {
                                spinnerbranch()
                            }
                            edtSearch.setOnClickListener {
                                spinneruser()
                            }
                            edtexpensetype.setOnClickListener {
                                spinnertype()
                            }
                            recyclerView_expense_approval.visibility = View.VISIBLE
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
                    }
                    else {
                        recyclerView_expense_approval.visibility = View.GONE
                        Toast.makeText(activityLocal, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun spinnertype() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = expensestatusname.map { it.toString() }.toTypedArray()
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
            edtexpensetype.setText("")
            selectedstatus_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = expensestatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = expensestatusid[selectedPosition].toString()
                val selectedParentName = expensestatusname[selectedPosition].toString()

                edtexpensetype.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")

                if (selectedstatus_id.isNotEmpty()){
                    page = 1
                    getexpenseapprovallist(page, start_date, end_date, searchbranch, selecteduser_id,selectedstatus_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinneruser() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = expenseusername.map { it.toString() }.toTypedArray()
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
            val selectedPosition = expenseusername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = expenseuserid[selectedPosition].toString()
                val selectedParentName = expenseusername[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    getexpenseapprovallist(
                        page,
                        start_date,
                        end_date,
                        searchbranch,
                        selecteduser_id,
                        selectedstatus_id
                    )
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    private fun spinnerbranch() {
        items.clear()
        for (item in expensebranchlist) {
            items.add(item.name.toString(), item.id.toString(), true)
        }

        SearchableMultiSelectSpinner.show(activityLocal, "Select Branch", "Search", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: java.util.ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                searchbranch.clear()
                val selectedCity = selectedItems.map { it.text }
                val selectedCityText = selectedCity.joinToString(",")
                edtSearchbranch.setText(selectedCityText)

                val selectedCodes = selectedItems.map { it.code }
                for (item in selectedCodes){
                    searchbranch.add(item)
                }
                if (searchbranch.size != 0){
                    expenseuserlist.clear()
                    expenseusername.clear()
                    expenseuserid.clear()
                    page = 1

                    getexpenseapprovallist(
                        page,
                        start_date,
                        end_date,
                        searchbranch,
                        selecteduser_id,
                        selectedstatus_id
                    )
                }
                println("selectedCodes=1="+searchbranch)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerlist() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_expense_approval.layoutManager = mLayoutManager
        val useractivityAdapter = ExpenseApprovalAdapter(activityLocal, expenseapprovelist,this@ExpenseApprovalFragment)
        recyclerView_expense_approval.adapter = useractivityAdapter
        recyclerView_expense_approval.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        fragment_container_expense_approval = rootView.findViewById(R.id.fragment_container_expense_approval)
        cardBack_expense_approval = rootView.findViewById(R.id.cardBack_expense_approval)
        linearTop_expense_approval = rootView.findViewById(R.id.linearTop_expense_approval)
        recyclerView_expense_approval = rootView.findViewById(R.id.recyclerView_expense_approval)
        tvTitle_expense_approval = rootView.findViewById(R.id.tvTitle_expense_approval)
        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        rel_main_expense = rootView.findViewById(R.id.rel_main_expense)
        rec_user_main2 = rootView.findViewById(R.id.rec_user_main2)
        recyclerView_user_detail = rootView.findViewById(R.id.recyclerView_user_detail)
        edtexpensetype = rootView.findViewById(R.id.edtexpensetype)

        rec_user_main2.visibility = View.GONE
        rel_main_expense.visibility = View.VISIBLE
        tvTitle_expense_approval.text = "Expense Approval"


        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)


        getexpenseapprovallist(
            page,
            start_date,
            end_date,
            searchbranch,
            selecteduser_id,
            selectedstatus_id
        )

        recyclerView_expense_approval.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (expenseapprovelist2.size == 20){
                        getexpenseapprovallist(
                            page,
                            start_date,
                            end_date,
                            searchbranch,
                            selecteduser_id,
                            selectedstatus_id
                        )
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

        cardBack_expense_approval.setOnClickListener {
            handleBackPressed()
        }



    }

    private fun handleBackPressed() {
        println("flag=="+flag)
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag.equals("")){
            tabPosition = 2
            tvTitle_expense_approval.text = "Report"
            rel_main_expense.visibility = View.GONE
            rec_user_main2.visibility = View.GONE
            linearTop_expense_approval.visibility = View.GONE
            fragment_container_expense_approval.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }else if (flag.equals("2")){
            flag = ""
            tvTitle_expense_approval.text = "Expense Approval"
            rel_main_expense.visibility = View.VISIBLE
            rec_user_main2.visibility = View.GONE
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_expense_approval, fragmentB)
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
            R.id.cardSearch -> {0
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
                        getexpenseapprovallist(
                            page,
                            start_date,
                            end_date,
                            searchbranch,
                            selecteduser_id,
                            selectedstatus_id
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

    override fun onClickEmail_expense(id: String, status: String, date: String, self: Boolean?) {
        println("id==app==" + id)
        println("id=" + date)
        expense_id = id
        getexpensedetail(id,status,date,self)
    }

    private fun getexpensedetail(id: String, status: String, date: String, self: Boolean?) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["expense_id"] = id
        ApiClient.expensedetail(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<UserExpenseDetailModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<UserExpenseDetailModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            println("ressssssssssss=="+response.body()!!.data)

                            showPopupDialog(response.body()!!.data,status,self)


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
                                println("Errrororo="+e.toString())
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: AlertDialog

    @SuppressLint("MissingInflatedId")
    private fun showPopupDialog(data: UserExpenseDetailModel.Data?, status: String, self: Boolean?) {

        val builder = AlertDialog.Builder(activityLocal)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_expense_approve_detail_layout, null)
        builder.setCancelable(false)

        val tv_expense_approve_by: TextView = view.findViewById(R.id.tv_expense_approve_by)
        val img_close: ImageView = view.findViewById(R.id.img_close)
        val tv_expense_num: TextView = view.findViewById(R.id.tv_expense_num)
        val tv_expense_status: TextView = view.findViewById(R.id.tv_expense_status)
        val tv_expense_date: TextView = view.findViewById(R.id.tv_expense_date)
        val tv_expense_type: TextView = view.findViewById(R.id.tv_expense_type)
        val tv_claim_amt: TextView = view.findViewById(R.id.tv_claim_amt)
        val tv_approve_amt: TextView = view.findViewById(R.id.tv_approve_amt)
        val tv_note_msg: TextView = view.findViewById(R.id.tv_note_msg)
        val tv_note_reason: TextView = view.findViewById(R.id.tv_note_reason)
        val tv_note_reason_msg: TextView = view.findViewById(R.id.tv_note_reason_msg)
        val tv_user_date: TextView = view.findViewById(R.id.tv_user_date)
        val listView_attach_view: ListView = view.findViewById(R.id.listView_attach_view)

        val tv_today_plan: TextView = view.findViewById(R.id.tv_today_plan)
        val tv_today_visit: TextView = view.findViewById(R.id.tv_today_visit)
        val tv_total_visit: TextView = view.findViewById(R.id.tv_total_visit)
        val tv_total_km: TextView = view.findViewById(R.id.tv_total_km)

        val radioGroup: RadioGroup = view.findViewById(R.id.radioGroup)
        val lin_type: LinearLayout = view.findViewById(R.id.lin_type)
        val cardbtn_submit: CardView = view.findViewById(R.id.cardbtn_submit)
        val cardbtn_sub: CardView = view.findViewById(R.id.cardbtn_sub)
        var status : String = ""
        var id : Int = 0
        var date : String = ""



        tv_expense_approve_by.text = data!!.userName.toString()
        tv_expense_num.text = "#"+data!!.id.toString()
        tv_expense_status.text = data.status.toString()
        tv_expense_date.text = data.date.toString()
        tv_expense_type.text = data.expensesTypeName.toString()
        tv_claim_amt.text = data.claimAmount.toString()
        tv_note_msg.text = data.note.toString()

        if (data.status.toString() == "Pending"){
            tv_approve_amt.text = data.claimAmount.toString()
        }else{
            tv_approve_amt.text = data.approveAmount.toString()
        }

        id = data!!.userId!!
        date = data!!.date.toString()

        if (data!!.status.equals("Pending") && self == false){
            lin_type.visibility = View.VISIBLE
            cardbtn_submit.visibility = View.VISIBLE
        }else{
            lin_type.visibility = View.GONE
            cardbtn_submit.visibility = View.GONE
        }

        if (data.plan == null) {
        }else{
            tv_today_plan.text = "Today's Plan :"+data.plan!!.town.toString()
            tv_today_visit.text ="Today's Visit :"+data.plan!!.town.toString()
            tv_total_visit.text ="Total Visit :"+data.totalVisit.toString()
            tv_total_km.text ="Total Km :"+data.totalDis.toString()
        }

       /* tv_approve_amt.setOnClickListener {

        }*/

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.check_approve -> {
                    status = "1"
                }
                R.id.check_reject -> {
                    status = "2"
                }
            }
        }

        cardbtn_submit.setOnClickListener {
            if (status.equals("")){
                Toast.makeText(context, "Please select Approve/Reject", Toast.LENGTH_SHORT).show()
            }else {
                var remark = ""
                showCustomDialog(activityLocal, tv_approve_amt,status) { enteredText, remarkText ->
                    remark = remarkText
                    if (enteredText.isNotEmpty()) {
                        tv_approve_amt.text = enteredText.toString()
                        if (status.equals("1")) {
                            submitapproval(expense_id, remark, tv_approve_amt.text.toString(), alertDialog)
                        } else if (status.equals("2")) {
                            if (remark.isEmpty()) {
                                Toast.makeText(context, "Please enter a reason", Toast.LENGTH_SHORT).show()
                            } else {
                                submitrejection(expense_id, remark, tv_approve_amt.text.toString(), alertDialog)
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        cardbtn_sub.setOnClickListener {
            rec_user_main2.visibility = View.VISIBLE
            rel_main_expense.visibility = View.GONE
            tvTitle_expense_approval.text = "User Activity"
            alertDialog.dismiss()
            flag = "2"
            getuseractivitydetail(id,date,tv_user_date)
        }

        when (data.status.toString()) {
            "Approved" -> {
                tv_expense_status.setTextColor(Color.parseColor("#00D23B"))
            }
            "Rejected" -> {
                tv_expense_status.setTextColor(Color.parseColor("#FF0000"))
            }
            "Checked" -> {
                tv_expense_status.setTextColor(Color.parseColor("#813F0B"))
            }
            "Pending" -> {
                tv_expense_status.setTextColor(Color.parseColor("#FFC700"))
            }
            "Checked By Reporting" -> {
                tv_expense_status.setTextColor(Color.parseColor("#DF8F18"))
            }
        }
        if (data.expenseImage.size>0){
            listView_attach_view.visibility = View.VISIBLE
            val adapter = ExpenseImagePathViewAdapter(activityLocal,data.expenseImage)
            listView_attach_view.adapter = adapter

        }else{
            listView_attach_view.visibility = View.GONE
        }
        if (data.reason!!.isNotEmpty()){
            tv_note_reason.visibility = View.VISIBLE
            tv_note_reason_msg.visibility = View.VISIBLE
            tv_note_reason_msg.text = data.reason.toString()
        }else{
            tv_note_reason.visibility = View.GONE
            tv_note_reason_msg.visibility = View.GONE
        }

        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()
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


    private fun getuseractivitydetail(id: Int?, date: String?, tv_user_date: TextView) {
        println("ABhbbb="+id+"<<<"+date)
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()

        var inputttt = date.toString()
        val formattedDate2 = Utilities.convertDateFormat(inputttt)
        queryParams["user_id"] = id.toString()
        queryParams["date"] = formattedDate2

        ApiClient.getuseractivitydetail(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            useractivitydetail.clear()

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ArrayList<UserActivityDetailModel>>() {}.type

                            useractivitydetail.addAll(gson.fromJson<java.util.ArrayList<UserActivityDetailModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            ))
                            println("useractivitydetail="+useractivitydetail)
                            println("useractivitydetail="+useractivitydetail.size)
                            var date = useractivitydetail.get(0).date.toString()
                            val formattedDate = formatDate(date)
                            tv_user_date.text = formattedDate

                            setuprecycler(useractivitydetail)
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
                    }
                }
            })
    }

    private fun setuprecycler(useractivitydetail: ArrayList<UserActivityDetailModel>) {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_user_detail.layoutManager = mLayoutManager
        val useractivity = UserActivityDetailAdapter(activityLocal, useractivitydetail)
        recyclerView_user_detail.adapter = useractivity
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    private fun submitrejection(
        expenseId: String,
        remark: String,
        claim_amt: String,
        alertDialog: AlertDialog
    ) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["expense_id"] = expenseId.toString()
        queryParams["reasons"] = remark

        ApiClient.expenserejectionsubmit(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<ExpenseApprovalSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<ExpenseApprovalSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(activityLocal,response.body()!!.message,Toast.LENGTH_LONG).show()
                            getexpenseapprovallist(1, start_date, end_date, searchbranch, selecteduser_id, selectedstatus_id)
                            alertDialog.dismiss()
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
                    }
                }
            })
    }


    private fun submitapproval(
        expenseId: String,
        remark: String,
        claim_amt: String,
        alertDialog: AlertDialog
    ) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["expense_id"] = expenseId.toString()
        queryParams["reasons"] = remark
        queryParams["approve_amnt"] = claim_amt

        ApiClient.expenseapprovalsubmit(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<ExpenseApprovalSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<ExpenseApprovalSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(activityLocal,response.body()!!.message,Toast.LENGTH_LONG).show()
                            getexpenseapprovallist(1, start_date, end_date, searchbranch, selecteduser_id, selectedstatus_id)
                            alertDialog.dismiss()
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
                    }
                }
            })
    }



    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog(
        context: Activity,
        tvApproveAmt: TextView,
        status: String,
        callback: (String,String) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.edittext_dialog_layout2, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val edtt_remark = dialogView.findViewById<EditText>(R.id.edtt_remark)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)
        if (status.equals("2")){
            editText.isEnabled = false
        }else{
            editText.isEnabled = true
        }

        editText.setText(tvApproveAmt.text.toString())

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Enter Value")
            .create()

        submitBtn.setOnClickListener {
            val enteredText = editText.text.toString()
            val edtt_remark = edtt_remark.text.toString()
            tvApproveAmt.text = enteredText
            callback(enteredText,edtt_remark)
            dialog.dismiss()
        }
        dialog.show()
    }
}