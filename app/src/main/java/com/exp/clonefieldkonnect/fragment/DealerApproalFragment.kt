package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.DealerApprovalAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.DealerApprovalListModel
import com.exp.clonefieldkonnect.model.NewDealerViewDetailMOdel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_dealer_approal.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DealerApproalFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment(),View.OnClickListener,DealerApprovalAdapter.OnEmailClick{

    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var tvTitle_dealer_approval: TextView
    lateinit var cardSearch: RelativeLayout
    lateinit var rel_main_dealer: RelativeLayout
    lateinit var cardBack_dealer_approval: CardView
    lateinit var edtSearchbranch: AutoCompleteTextView
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtSearchstatus: AutoCompleteTextView
    lateinit var recyclerView_dealer_approval: RecyclerView
    lateinit var img_create: ImageView


    var start_date = ""
    var end_date = ""
    var flag : String = ""
    var selecteduser_id : String = ""
    var selectedstatus_id : String = ""
    var searchbranch : ArrayList<String> = ArrayList()
    var dealerapprovelist: ArrayList<DealerApprovalListModel.Data> = ArrayList()
    var dealerapprovelist2: ArrayList<DealerApprovalListModel.Data> = ArrayList()
    var dealerbranchlist: ArrayList<DealerApprovalListModel.Braches> = ArrayList()
    var dealeruserlist: ArrayList<DealerApprovalListModel.Users> = ArrayList()
    var dealerstatuslist: ArrayList<DealerApprovalListModel.AllStatus> = ArrayList()
    var dealerusername: ArrayList<String> = ArrayList()
    var dealeruserid: ArrayList<String> = ArrayList()
    var dealerstatusname: ArrayList<String> = ArrayList()
    var dealerstatusid: ArrayList<String> = ArrayList()
    private var items: MutableList<SearchableItem> = ArrayList()
    var dealerviewdetail : NewDealerViewDetailMOdel.Data? = null



    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "15"
    var page_count : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_dealer_approal, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        cardBack_dealer_approval = rootView.findViewById(R.id.cardBack_dealer_approval)
        tvTitle_dealer_approval = rootView.findViewById(R.id.tvTitle_dealer_approval)
        rel_main_dealer = rootView.findViewById(R.id.rel_main_dealer)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchstatus = rootView.findViewById(R.id.edtSearchstatus)
        recyclerView_dealer_approval = rootView.findViewById(R.id.recyclerView_dealer_approval)
        img_create = rootView.findViewById(R.id.img_create)


        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)
        img_create.setOnClickListener(this)

        getdealerapprovallist(
            page,
            start_date,
            end_date,
            searchbranch,
            selecteduser_id,
            selectedstatus_id
        )


        recyclerView_dealer_approval.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (dealerapprovelist2.size == 15){
                        getdealerapprovallist(
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



        cardBack_dealer_approval.setOnClickListener {
            handleBackPressed()
        }

    }

    private fun getdealerapprovallist(
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
        queryParams["startdate"] = start_date
        queryParams["enddate"] = end_date
        queryParams["created_by"] = selecteduser_id
        queryParams["status_id"] = selectedstatus_id

        ApiClient.getdealerapprovelist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,searchbranch,
            object : APIResultLitener<DealerApprovalListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<DealerApprovalListModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            dealerapprovelist2.clear()
                            if (page ==1)
                                dealerapprovelist.clear()

                            dealerapprovelist.addAll(response.body()!!.data)
                            dealerapprovelist2 = response.body()!!.data

                            dealerbranchlist.addAll(response.body()!!.braches)
                            dealeruserlist.addAll(response.body()!!.users)
                            dealerstatuslist.addAll(response.body()!!.allStatus)

                            for (item in dealeruserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!dealerusername.contains(name)) {
                                    dealerusername.add(name)
                                    dealeruserid.add(id)
                                }
                            }
                            for (item in dealerstatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()
                                if (!dealerstatusname.contains(name)) {
                                    dealerstatusname.add(name)
                                    dealerstatusid.add(id)
                                }
                            }

                            edtSearchbranch.setOnClickListener {
                                spinnerbranch()
                            }

                            edtSearch.setOnClickListener {
                                spinneruser()
                            }
                            edtSearchstatus.setOnClickListener {
                                spinnerstatus()
                            }

                            recyclerView_dealer_approval.visibility = View.VISIBLE
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
                        recyclerView_dealer_approval.visibility = View.GONE
                        Toast.makeText(activityLocal, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun spinneruser() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = dealerusername.map { it.toString() }.toTypedArray()
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
            val selectedPosition = dealerusername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = dealeruserid[selectedPosition].toString()
                val selectedParentName = dealerusername[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    getdealerapprovallist(
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

        dialog.show()
    }

    private fun spinnerstatus() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = dealerstatusname.map { it.toString() }.toTypedArray()
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
            val selectedPosition = dealerstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = dealerstatusid[selectedPosition].toString()
                val selectedParentName = dealerstatusname[selectedPosition].toString()

                edtSearchstatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")

                if (selectedstatus_id.isNotEmpty()){
                    page = 1
                    getdealerapprovallist(page, start_date, end_date, searchbranch, selecteduser_id,selectedstatus_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show()
    }


    private fun spinnerbranch() {
        items.clear()
        for (item in dealerbranchlist) {
            items.add(item.branchName.toString(), item.id.toString(), true)
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
                    dealeruserlist.clear()
                    dealerusername.clear()
                    dealeruserid.clear()
                    page = 1
                    getdealerapprovallist(
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
        recyclerView_dealer_approval.layoutManager = mLayoutManager
        val dealerAdapter = DealerApprovalAdapter(activityLocal, dealerapprovelist,this@DealerApproalFragment)
        recyclerView_dealer_approval.adapter = dealerAdapter
        recyclerView_dealer_approval.scrollToPosition(lastPosition)
        dealerAdapter.notifyDataSetChanged()
    }


    private fun handleBackPressed() {
        println("flag=="+flag)
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag.equals("")){
            tabPosition = 2
            tvTitle_dealer_approval.text = "Report"
            rel_main_dealer.visibility = View.GONE
            linearTop_dealer_approval.visibility = View.GONE
            fragment_container_dealer_approval.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }else if (flag.equals("2")){
            flag = ""
            tvTitle_dealer_approval.text = "Dealer Approval"
            rel_main_dealer.visibility = View.VISIBLE
//            rec_user_main2.visibility = View.GONE
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_dealer_approval, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun navigateToFragmentC(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = NewDealerFormFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_dealer_approval, fragmentB)
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
                    page = 1
                    if (start_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                    }else if (end_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                    }else{
                        getdealerapprovallist(page, start_date, end_date, searchbranch, selecteduser_id, selectedstatus_id)
                    }
                }
            }
            R.id.img_create ->{
                Toast.makeText(activityLocal,"Working on it..!!",Toast.LENGTH_SHORT).show()
//                tvTitle_dealer_approval.text = "New Dealer Appointments"
//                rel_main_dealer.visibility = View.GONE
//                navigateToFragmentC(linearTopreport,tabPosition)
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

    override fun onClickEmail_expense(id: String, flag: String, createdById: Int?) {
        println("idid=="+id+"<<"+flag)
        if (flag.equals("view")){
            showviewpopup(id,createdById)
        }
    }

    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: AlertDialog
    private fun showviewpopup(id: String, createdById: Int?) {

        val builder = AlertDialog.Builder(activityLocal)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_dealerapproval_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)

        getviewdetail(id,view,createdById)

        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()

    }


    private fun getviewdetail(id: String, view: View, createdById: Int?) {
        isLoading = true
        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["appointment_id"] = id

        ApiClient.getdealerviewdetail(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<NewDealerViewDetailMOdel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<NewDealerViewDetailMOdel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {

                            dealerviewdetail = response.body()!!.data

                            val tv_branch: TextView = view.findViewById(R.id.tv_branch)
                            val tv_createdby: TextView = view.findViewById(R.id.tv_createdby)
                            val tv_division: TextView = view.findViewById(R.id.tv_division)
                            val tv_cust_type: TextView = view.findViewById(R.id.tv_cust_type)
                            val tv_firm_name: TextView = view.findViewById(R.id.tv_firm_name)
                            val tv_cont_person: TextView = view.findViewById(R.id.tv_cont_person)
                            val tv_mob_no: TextView = view.findViewById(R.id.tv_mob_no)
                            val tv_district: TextView = view.findViewById(R.id.tv_district)
                            val tv_city: TextView = view.findViewById(R.id.tv_city)
                            val tv_place: TextView = view.findViewById(R.id.tv_place)
                            val tv_appoi_date: TextView = view.findViewById(R.id.tv_appoi_date)
                            val tv_security_depo: TextView = view.findViewById(R.id.tv_security_depo)
                            val tv_gst_no: TextView = view.findViewById(R.id.tv_gst_no)
                            val tv_firm_type: TextView = view.findViewById(R.id.tv_firm_type)
                            val tv_payment_term: TextView = view.findViewById(R.id.tv_payment_term)
                            val tv_max_credit_pre: TextView = view.findViewById(R.id.tv_max_credit_pre)
                            val tv_present_turnover: TextView = view.findViewById(R.id.tv_present_turnover)
                            val tv_current_y_val1: TextView = view.findViewById(R.id.tv_current_y_val1)
                            val tv_current_y_val2: TextView = view.findViewById(R.id.tv_current_y_val2)
                            val tv_current_y_val3: TextView = view.findViewById(R.id.tv_current_y_val3)
                            val tv_current_y_val4: TextView = view.findViewById(R.id.tv_current_y_val4)
                            val tv_current_y_val5: TextView = view.findViewById(R.id.tv_current_y_val5)
                            val tv_current_y_val6: TextView = view.findViewById(R.id.tv_current_y_val6)
                            val tv_next_y_val1: TextView = view.findViewById(R.id.tv_next_y_val1)
                            val tv_next_y_val2: TextView = view.findViewById(R.id.tv_next_y_val2)
                            val tv_next_y_val3: TextView = view.findViewById(R.id.tv_next_y_val3)
                            val tv_next_y_val4: TextView = view.findViewById(R.id.tv_next_y_val4)
                            val tv_next_y_val5: TextView = view.findViewById(R.id.tv_next_y_val5)
                            val tv_next_y_val6: TextView = view.findViewById(R.id.tv_next_y_val6)
                            val tv_busi_com_value1: TextView = view.findViewById(R.id.tv_busi_com_value1)
                            val tv_busi_com_value2: TextView = view.findViewById(R.id.tv_busi_com_value2)
                            val tv_busi_pro_value1: TextView = view.findViewById(R.id.tv_busi_pro_value1)
                            val tv_busi_pro_value2: TextView = view.findViewById(R.id.tv_busi_pro_value2)
                            val tv_busi_nature_value1: TextView = view.findViewById(R.id.tv_busi_nature_value1)
                            val tv_busi_nature_value2: TextView = view.findViewById(R.id.tv_busi_nature_value2)
                            val tv_busi_turn_over_value1: TextView = view.findViewById(R.id.tv_busi_turn_over_value1)
                            val tv_busi_turn_over_value2: TextView = view.findViewById(R.id.tv_busi_turn_over_value2)
                            val cardbtn_submit: CardView = view.findViewById(R.id.cardbtn_submit)
                            val cardbtn_reject: CardView = view.findViewById(R.id.cardbtn_reject)
                            val cardbtn_save: CardView = view.findViewById(R.id.cardbtn_save)
                            val lin_remark: LinearLayout = view.findViewById(R.id.lin_remark)
                            val lin_save: LinearLayout = view.findViewById(R.id.lin_save)
                            val edtt_remark: EditText = view.findViewById(R.id.edtt_remark)
                            val remark_title: TextView = view.findViewById(R.id.remark_title)


                            var user_id = StaticSharedpreference.getInfo(Constant.USERID, activityLocal)

                            var roll_id = StaticSharedpreference.getInfo(Constant.ROLL_ID, activityLocal).toString()
                            val gson = Gson()
                            val rollIdListType = object : TypeToken<List<Int>>() {}.type
                            val rollIdList: List<Int> = gson.fromJson(roll_id, rollIdListType)
                            var compare_ids = listOf(1,6)
                            var bm_ids = listOf(3)
                            val containsSimilarValue = rollIdList.any { it in compare_ids }
                            val containsbmValue = rollIdList.any { it in bm_ids }

                            // In case of Pump, we need Branch manager remark then only super admin and cluster head will approve


                            println("roll_idsroll_ids=="+roll_id+"<<"+compare_ids+"<<"+dealerviewdetail!!.id)


                            if (dealerviewdetail!!.division.equals("PUMP&MOTORS")){
                                if (dealerviewdetail!!.approvalStatus!!.equals(0) && createdById!= user_id.toString().toInt()){
                                    if (containsbmValue && dealerviewdetail!!.bm_remark!!.isEmpty()){
                                        lin_remark.visibility = View.VISIBLE
                                        lin_save.visibility = View.VISIBLE
                                        remark_title.text = "BM Remark"
                                    }else if (containsSimilarValue) {
                                        cardbtn_submit.visibility = View.VISIBLE
                                        cardbtn_reject.visibility = View.VISIBLE
                                    }else{
                                        cardbtn_submit.visibility = View.GONE
                                        cardbtn_reject.visibility = View.GONE
                                    }
                                }else{
                                    cardbtn_submit.visibility = View.GONE
                                    cardbtn_reject.visibility = View.GONE
                                }
                            }
                            else{
                                if (dealerviewdetail!!.approvalStatus!!.equals(0) && createdById!= user_id.toString().toInt()){
                                    cardbtn_submit.visibility = View.VISIBLE
                                    cardbtn_reject.visibility = View.VISIBLE
                                }else{
                                    cardbtn_submit.visibility = View.GONE
                                    cardbtn_reject.visibility = View.GONE
                                }
                            }

                            cardbtn_submit.setOnClickListener {
                                lin_remark.visibility = View.GONE
                                var status = "1"
                                approvedealerappointment(dealerviewdetail!!.id, status, "")
                            }
                            cardbtn_reject.setOnClickListener {
                                lin_remark.visibility = View.VISIBLE
                                if (edtt_remark.text.isNullOrEmpty()){
                                    Toast.makeText(activityLocal,"Please enter remark",Toast.LENGTH_SHORT).show()
                                }else{
                                    var status = "4"
                                    approvedealerappointment(dealerviewdetail!!.id,status,edtt_remark.text.toString())                                }
                            }
                            cardbtn_save.setOnClickListener {
                                if (edtt_remark.text.isNullOrEmpty()){
                                    Toast.makeText(activityLocal,"Please enter remark",Toast.LENGTH_SHORT).show()
                                }else{
                                    saveremark(dealerviewdetail!!.id,edtt_remark.text.toString())
                                }
                            }

                            tv_branch.text = dealerviewdetail!!.branch
                            tv_createdby.text = dealerviewdetail!!.createdBy
                            tv_division.text = dealerviewdetail!!.division
                            tv_cust_type.text = dealerviewdetail!!.customertype
                            tv_firm_name.text = dealerviewdetail!!.firmName
                            tv_cont_person.text = dealerviewdetail!!.contactPerson
                            tv_mob_no.text = dealerviewdetail!!.mobileEmail
                            tv_district.text = dealerviewdetail!!.district
                            tv_city.text = dealerviewdetail!!.city
                            tv_place.text = dealerviewdetail!!.place
                            tv_appoi_date.text = dealerviewdetail!!.appointmentDate
                            tv_security_depo.text = dealerviewdetail!!.securityDeposit
                            tv_gst_no.text = dealerviewdetail!!.gstDetails
                            tv_firm_type.text = dealerviewdetail!!.firmType
                            tv_payment_term.text = dealerviewdetail!!.paymentTerm
                            tv_max_credit_pre.text = dealerviewdetail!!.creditPeriod
                            tv_present_turnover.text = dealerviewdetail!!.presentAnnualTurnover

                            tv_current_y_val1.text = dealerviewdetail!!.motorAnticipatedBusiness
                            tv_current_y_val2.text = dealerviewdetail!!.pumpAnticipatedBusiness
                            tv_current_y_val3.text = dealerviewdetail!!.FAAnticipatedBusiness
                            tv_current_y_val4.text = dealerviewdetail!!.lightingAnticipatedBusiness
                            tv_current_y_val5.text = dealerviewdetail!!.agriAnticipatedBusiness
                            tv_current_y_val6.text = dealerviewdetail!!.solarAnticipatedBusiness
                            tv_next_y_val1.text = dealerviewdetail!!.motorNextYearBusiness
                            tv_next_y_val2.text = dealerviewdetail!!.pumpNextYearBusiness
                            tv_next_y_val3.text = dealerviewdetail!!.FANextYearBusiness
                            tv_next_y_val4.text = dealerviewdetail!!.lightingNextYearBusiness
                            tv_next_y_val5.text = dealerviewdetail!!.agriNextYearBusiness
                            tv_next_y_val6.text = dealerviewdetail!!.solarNextYearBusiness

                            tv_busi_com_value1.text = dealerviewdetail!!.manufactureCompany1
                            tv_busi_com_value2.text = dealerviewdetail!!.manufactureCompany2

                            tv_busi_pro_value1.text = dealerviewdetail!!.manufactureProduct1
                            tv_busi_pro_value2.text = dealerviewdetail!!.manufactureProduct2

                            tv_busi_nature_value1.text = dealerviewdetail!!.manufactureBusiness1
                            tv_busi_nature_value2.text = dealerviewdetail!!.manufactureBusiness2

                            tv_busi_turn_over_value1.text = dealerviewdetail!!.manufactureTurnOver1
                            tv_busi_turn_over_value2.text = dealerviewdetail!!.manufactureTurnOver2

                            if (!dealerviewdetail!!.bm_remark.isNullOrEmpty()){
                                lin_remark.visibility = View.VISIBLE
                                remark_title.text = "BM Remark"
                                edtt_remark.setText(dealerviewdetail!!.bm_remark.toString())
                            }
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
                        Toast.makeText(activityLocal, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }



    private fun saveremark(id: Int?, remark: String) {
        isLoading = true
        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["appointment_id"] = id.toString()
        queryParams["bm_remark"] = remark

        ApiClient.saveappointmentremark(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            Toast.makeText(activityLocal,response.body()!!.message,Toast.LENGTH_SHORT).show()
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
                        isLoading = false
                    }
                    else {
                        Toast.makeText(activityLocal, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun approvedealerappointment(id: Int?, status: String, remark: String) {
        isLoading = true
        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["appointment_id"] = id.toString()
        queryParams["status"] = status
        queryParams["remark"] = remark

        ApiClient.approverdealearappointment(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            Toast.makeText(activityLocal,response.body()!!.message,Toast.LENGTH_SHORT).show()
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
                        isLoading = false
                    }
                    else {
                        Toast.makeText(activityLocal, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

}