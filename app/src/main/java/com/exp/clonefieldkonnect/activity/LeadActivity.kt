package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.LeadListAdapter
import com.exp.clonefieldkonnect.adapter.LeadStatusAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.LeadModel
import com.exp.clonefieldkonnect.model.LeadStatusSourceModel
import com.exp.clonefieldkonnect.model.TaskDropdownModel
import com.exp.import.Utilities
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class LeadActivity : AppCompatActivity(), View.OnClickListener,LeadStatusAdapter.OnEmailClick,
    LeadListAdapter.OnEmailClick{
    lateinit var cardBack_activity: CardView
    lateinit var card_opoo_btn: CardView
    lateinit var img_create: ImageView
    lateinit var ic_notification: ImageView
    lateinit var recyclerView_lead_status: RecyclerView
    lateinit var recyclerView_useractivity: RecyclerView
    lateinit var searchIcon: ImageView
    lateinit var filter: ImageView
    lateinit var searchInput: EditText
    lateinit var notification_count: TextView


    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "30"
    var page_count : String = ""

    var leadstatuslist: ArrayList<LeadModel.Counts> = ArrayList()
    var leadlist: ArrayList<LeadModel.Data_lead> = ArrayList()
    var leadlist2: ArrayList<LeadModel.Data_lead> = ArrayList()


    var leaduserlist: ArrayList<TaskDropdownModel.Users> = ArrayList()
    var leadusersname : ArrayList<String> = ArrayList()
    var leadusersid : ArrayList<String> = ArrayList()

    var leadcreatestatuslist: ArrayList<LeadStatusSourceModel.Status> = ArrayList()
    var leadcreatesourcelist: ArrayList<LeadStatusSourceModel.Source> = ArrayList()

    var leadcreatestatusname : ArrayList<String> = ArrayList()
    var leadcreatestatusid : ArrayList<String> = ArrayList()
    var leadcreatesourcename : ArrayList<String> = ArrayList()
    var leadcreatesourceid : ArrayList<String> = ArrayList()

    var selectedtaskuser_id :String = ""
    var selectedtaskuser_name :String = ""
    var leadststus_id :String = ""
    var selectedtype_id :String = ""
    var selectedsource_id :String = ""
    var selectedsource_name :String = ""
    var pincode_id : String = ""
    var city_id : String = ""
    var state_id : String = ""
    var district_id : String = ""
    var lead_checkin_lead_id : String = ""

    var start_date : String = ""
    var end_date : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lead)
        initViews()
    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack)
        card_opoo_btn = findViewById(R.id.card_opoo_btn)
        img_create = findViewById(R.id.img_create)
        recyclerView_lead_status = findViewById(R.id.recyclerView_lead_status)
        recyclerView_useractivity = findViewById(R.id.recyclerView_useractivity)
        searchIcon = findViewById(R.id.searchIcon)
        searchInput = findViewById(R.id.searchInput)
        filter = findViewById(R.id.filter)
        ic_notification = findViewById(R.id.ic_notification)
        notification_count = findViewById(R.id.notification_count)

        lead_checkin_lead_id = StaticSharedpreference.getInfo(Constant.Lead_check_in_leadID, this@LeadActivity).toString()

        println("Lead_check_in"+lead_checkin_lead_id)

        if (!lead_checkin_lead_id.isNullOrEmpty()){
            var intent = Intent(this@LeadActivity,LeadDetailActivity::class.java)
            intent.putExtra("lead_id",lead_checkin_lead_id)
            startActivity(intent)
        }

        filter.setOnClickListener {
            showfilters()
        }

        ic_notification.setOnClickListener {
            startActivity(Intent(this@LeadActivity,NotificationActivity::class.java))
        }

        gettaskdropdown()

        cardBack_activity.setOnClickListener {
            handelbackpress()
        }
        card_opoo_btn.setOnClickListener {
            startActivity(Intent(this@LeadActivity,OpportunityViewActivity::class.java))
        }

        img_create.setOnClickListener(this)
        searchIcon.setOnClickListener(this)

        getlead(page, leadststus_id, searchInput.text.toString(), selectedtaskuser_id, selectedsource_id, start_date, end_date)
        getleadstatussource()

        recyclerView_useractivity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (leadlist2.size == 30){
                        getlead(
                            page,
                            leadststus_id,
                            searchInput.text.toString(),
                            selectedtaskuser_id,
                            selectedsource_id,
                            start_date,
                            end_date
                        )
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

    }

    private fun gettaskdropdown() {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()

        println("queryParams=="+queryParams)
        ApiClient.getleadtaskdropdown(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<TaskDropdownModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<TaskDropdownModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leaduserlist.clear()


                            leaduserlist.addAll(response.body()!!.data!!.users)

                            for (item in leaduserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadusersname.contains(name)) {
                                    leadusersname.add(name)
                                    leadusersid.add(id)
                                }
                            }

                        }
                        else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun showfilters() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_filter_bottomsheet, null)
        dialog.setContentView(view)

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.setBackgroundResource(android.R.color.transparent)
        }

        var edtSearch = dialog.findViewById<AutoCompleteTextView>(R.id.edtSearch)
        var edtStatus = dialog.findViewById<AutoCompleteTextView>(R.id.edtStatus)
        var cardFrom = dialog.findViewById<LinearLayout>(R.id.cardFrom)
        var cardTo = dialog.findViewById<LinearLayout>(R.id.cardTo)
        var tvFrom = dialog.findViewById<TextView>(R.id.tvFrom)
        var tvTo = dialog.findViewById<TextView>(R.id.tvTo)
        var textview11 = dialog.findViewById<TextView>(R.id.textview11)
        var btnReset = dialog.findViewById<CardView>(R.id.btnReset)
        var btnApply = dialog.findViewById<CardView>(R.id.btnApply)

        textview11!!.text = "Select Source"
        edtStatus!!.hint = "Select Source"

        if (start_date.isNotEmpty()){
            tvFrom!!.text = start_date
        }
        if (end_date.isNotEmpty()){
            tvTo!!.text = start_date
        }
        if (selectedtaskuser_name.isNotEmpty()){
            edtSearch!!.setText(selectedtaskuser_name)
        }
        if (selectedsource_name.isNotEmpty()){
            edtStatus!!.setText(selectedsource_name)
        }


        edtSearch!!.setOnClickListener {
            spinnerusername(edtSearch!!)
        }

        edtStatus!!.setOnClickListener {
            spinnersource(edtStatus!!)
        }

        cardFrom!!.setOnClickListener {
            Utilities.datePicker(tvFrom!!, tvTo!!.text.toString(), "", true, this)
        }

        cardTo!!.setOnClickListener {
            Utilities.datePicker(tvTo!!, "", tvFrom!!.text.toString(), false, this)
        }

        btnReset!!.setOnClickListener {
            tvTo!!.text = ""
            tvFrom!!.text = ""
            edtSearch!!.setText("")
            start_date = ""
            end_date = ""
            selectedtaskuser_id = ""
            selectedsource_id = ""
            selectedtaskuser_name = ""
            selectedsource_name = ""
            page = 1

            getlead(page, leadststus_id, searchInput.text.toString(),selectedtaskuser_id,selectedsource_id,start_date,end_date)
            dialog.dismiss()
        }


        btnApply!!.setOnClickListener {

            if (tvFrom!!.text.toString().isNotEmpty() && tvTo!!.text.toString().isNotEmpty()){
                val convertedDate = convertDateFormats(tvFrom.text.toString(),tvTo.text.toString())
                start_date = convertedDate.first
                end_date = convertedDate.second
                println("from=="+start_date+"To="+end_date)
            }

            page = 1
            getlead(page, leadststus_id, searchInput.text.toString(),selectedtaskuser_id,selectedsource_id,start_date,end_date)
            dialog.dismiss()

        }



        dialog.show()
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

    private fun spinnerusername(edtSearch: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadusersname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Username")

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
            selectedtaskuser_id = ""
            selectedtaskuser_name = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadusersname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadusersid[selectedPosition].toString()
                val selectedParentName = leadusersname[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selectedtaskuser_id = selectedParentId
                selectedtaskuser_name = selectedParentName

                println("Abhi=id=$selectedtaskuser_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    private fun getlead(
        page: Int,
        leadststus_id: String,
        searchinput: String,
        selectedtaskuser_id: String,
        selectedsource_id: String,
        start_date: String,
        end_date: String
    ) {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }

        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["status"] = leadststus_id
        queryParams["search"] = searchinput
        queryParams["user_id"] = selectedtaskuser_id
        queryParams["lead_source"] = selectedsource_id
        queryParams["start_date"] = start_date
        queryParams["end_date"] = end_date

        println("queryParams=="+queryParams)
        ApiClient.getlead(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadstatuslist.clear()
                            leadlist2.clear()

                            if (page ==1)
                                leadlist.clear()

                            leadstatuslist.addAll(response.body()!!.counts)
                            leadlist.addAll(response.body()!!.data)
                            leadlist2.addAll(response.body()!!.data)
                            notification_count.text = response.body()!!.notification_count.toString()

                            setuprecyclerstatuslist()
                            setuprecyclerleadlist()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun getleadstatussource() {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()

        ApiClient.getleadstatussource(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadStatusSourceModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadStatusSourceModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadcreatestatuslist.clear()
                            leadcreatesourcelist.clear()


                            leadcreatestatuslist.addAll(response.body()!!.data!!.status)
                            leadcreatesourcelist.addAll(response.body()!!.data!!.source)

                            for (item in leadcreatestatuslist) {
                                val name = item.displayName.toString()
                                val id = item.id.toString()

                                if (!leadcreatestatusname.contains(name)) {
                                    leadcreatestatusname.add(name)
                                    leadcreatestatusid.add(id)
                                }
                            }

                            for (item in leadcreatesourcelist) {
                                val name = item.value.toString()
                                val id = item.key.toString()

                                if (!leadcreatesourcename.contains(name)) {
                                    leadcreatesourcename.add(name)
                                    leadcreatesourceid.add(id)
                                }
                            }


                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerleadlist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_useractivity.layoutManager = mLayoutManager
        val useractivityAdapter = LeadListAdapter(this, leadlist, this)
        recyclerView_useractivity.adapter = useractivityAdapter
        recyclerView_useractivity.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerstatuslist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_lead_status.layoutManager = mLayoutManager
        val useractivityAdapter = LeadStatusAdapter(this, leadstatuslist, this)
        recyclerView_lead_status.adapter = useractivityAdapter
        recyclerView_lead_status.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }



    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_create -> {
                showcreateleadpopup()
            }
            R.id.searchIcon -> {
                    getlead(
                        1,
                        leadststus_id,
                        searchInput.text.toString(),
                        selectedtaskuser_id,
                        selectedsource_id,
                        start_date,
                        end_date
                    )
            }



        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handelbackpress()
    }

    private fun handelbackpress() {
        startActivity(Intent(this@LeadActivity,MainActivity::class.java))
    }

    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: AlertDialog

    @SuppressLint("MissingInflatedId")
    private fun showcreateleadpopup() {

        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_create_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val edtleadtype: AutoCompleteTextView = view.findViewById(R.id.edtleadtype)
        val edtleadsource: AutoCompleteTextView = view.findViewById(R.id.edtleadsource)
        val edtfirmname: AutoCompleteTextView = view.findViewById(R.id.edtfirmname)
        val edtcustomername: AutoCompleteTextView = view.findViewById(R.id.edtcustomername)
        val edtmobno: AutoCompleteTextView = view.findViewById(R.id.edtmobno)
        val edtemail: AutoCompleteTextView = view.findViewById(R.id.edtemail)
        val edtadd: AutoCompleteTextView = view.findViewById(R.id.edtadd)
        val edtotherrr: AutoCompleteTextView = view.findViewById(R.id.edtotherrr)
        val edtnote: AutoCompleteTextView = view.findViewById(R.id.edtnote)
        val cardSubmit: CardView = view.findViewById(R.id.cardSubmit)
        val spinnerPin: EditText = view.findViewById(R.id.spinnerPin)
        val edtState: EditText = view.findViewById(R.id.edtState)
        val edtCity: EditText = view.findViewById(R.id.edtCity)
        val edtdistric: EditText = view.findViewById(R.id.edtdistric)

        cardSubmit.setOnClickListener {
            println("AAAAAAA=="+selectedtype_id+"<<"+edtfirmname.text.toString()+"<<"+edtcustomername.text.toString()+"<<"+
                    edtmobno.text.toString()+"<<"+selectedsource_id)
            if (selectedtype_id.isNullOrEmpty()){
                responsemessage("Please Select Lead Type")
            }else if (edtfirmname.text.isNullOrEmpty()){
                responsemessage("Please Enter Firm Name")
            }else if (edtcustomername.text.isNullOrEmpty()){
                responsemessage("Please Enter Customer Name")
            }else if (edtmobno.text.isNullOrEmpty()){
                responsemessage("Please Enter Mobile No.")
            }else if (selectedsource_id.isNullOrEmpty()){
                responsemessage("Please Select Lead Source")
            }else{
                submitcreate(selectedtype_id,edtfirmname.text.toString(),edtcustomername.text.toString(),
                    edtmobno.text.toString(),edtemail.text.toString(),edtadd.text.toString(),
                    pincode_id,state_id,city_id,district_id,edtotherrr.text.toString(),selectedsource_id,edtnote.text.toString(),
                    alertDialog)
            }
        }

        edtleadtype.setOnClickListener {
            spinnerstatus(edtleadtype)
        }
        edtleadsource.setOnClickListener {
            spinnersource(edtleadsource)
        }

        spinnerPin.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (spinnerPin.text.toString().length == 6) {
                    getPincodeInfo(spinnerPin.text.toString(),edtState,edtCity,edtdistric)
                } else if (spinnerPin.text.toString().length < 6) {
                    edtState.setText("")
                    edtCity.setText("")
                    edtdistric.setText("")
                    pincode_id = ""
                    city_id  = ""
                    state_id  = ""
                    district_id = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })



        img_close.setOnClickListener {
            alertDialog.dismiss()
        }
        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun submitcreate(
        selectedtypeId: String,
        edtfirmname: String,
        edtcustomername: String,
        edtmobno: String,
        edtemail: String,
        edtadd: String,
        pincodeId: String,
        stateId: String,
        cityId: String,
        districtId: String,
        edtotherrr: String,
        selectedsourceId: String,
        edtnote: String,
        alertDialog: AlertDialog
    ) {
        if (!Utilities.isOnline(this@LeadActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@LeadActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["status"] = selectedtypeId
        queryParams["company_name"] = edtfirmname
        queryParams["contact_name"] = edtcustomername
        queryParams["phone_number"] = edtmobno
        queryParams["email"] = edtemail
        queryParams["address"] = edtadd
        queryParams["pincode_id"] = pincodeId
        queryParams["state_id"] = stateId
        queryParams["city_id"] = cityId
        queryParams["district_id"] = districtId
        queryParams["other"] = edtotherrr
        queryParams["lead_source"] = selectedsourceId
        queryParams["note"] = edtnote

        ApiClient.submitcreatelead(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@LeadActivity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(this@LeadActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            getlead(
                                1,
                                leadststus_id,
                                searchInput.text.toString(),
                                selectedtaskuser_id,
                                selectedsource_id,
                                start_date,
                                end_date
                            )
                            alertDialog.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun responsemessage(s: String) {
        Toast.makeText(this@LeadActivity,s,Toast.LENGTH_SHORT).show()
    }

    lateinit var dialog: Dialog
    private fun getPincodeInfo(
        pincode: String,
        edtState: EditText,
        edtCity: EditText,
        edtdistric: EditText
    ) {
        if (!Utilities.isOnline(this)) {
            return
        }
        dialog = DialogClass.progressDialog(this)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["pincode"] = pincode
        ApiClient.getPincodeInfo(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object :
                APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            dialog.dismiss()

                            try {
                                var city = response.body()!!
                                    .get("data").asJsonObject.get("city_name").asString
                                var state = response.body()!!
                                    .get("data").asJsonObject.get("state_name").asString
                                var distric = response.body()!!
                                    .get("data").asJsonObject.get("district_name").asString

                                pincode_id = response.body()!!
                                    .get("data").asJsonObject.get("id").asString
                                city_id = response.body()!!
                                    .get("data").asJsonObject.get("city_id").asString
                                state_id = response.body()!!
                                    .get("data").asJsonObject.get("state_id").asString
                                district_id = response.body()!!
                                    .get("data").asJsonObject.get("district_id").asString

                                edtState.setText(state)
                                edtCity.setText(city)
                                edtdistric.setText(distric)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@LeadActivity,
                                    "Please enter correct pin",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } else {
                            dialog.dismiss()

                            var jsonObject: JSONObject? = null
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadActivity,
                                    false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        dialog.dismiss()

                    }
                }
            })
    }



    private fun spinnersource(edtleadsource: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadcreatesourcename.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Source")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtleadsource.setText("")
            selectedsource_id = ""
            selectedsource_name = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadcreatesourcename.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadcreatesourceid[selectedPosition].toString()
                val selectedParentName = leadcreatesourcename[selectedPosition].toString()

                edtleadsource.setText(selectedParentName)
                selectedsource_id = selectedParentId
                selectedsource_name = selectedParentName

                println("Abhi=id=$selectedsource_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    private fun spinnerstatus(edtleadtype: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadcreatestatusname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Type")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtleadtype.setText("")
            selectedtype_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadcreatestatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadcreatestatusid[selectedPosition].toString()
                val selectedParentName = leadcreatestatusname[selectedPosition].toString()

                edtleadtype.setText(selectedParentName)
                selectedtype_id = selectedParentId

                println("Abhi=id=$selectedtype_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    override fun onClickEmail1(id: Int?) {
        println("statusssiddd=="+id)
        if (id!!.equals(-1)){
            leadststus_id = ""
        }else{
            leadststus_id = id.toString()
        }
        getlead(
            1,
            leadststus_id,
            searchInput.text.toString(),
            selectedtaskuser_id,
            selectedsource_id,
            start_date,
            end_date
        )
    }

    override fun onClicklead(id: Int?) {
        if (id.toString().isNotEmpty()){
            var intent = Intent(this@LeadActivity,LeadDetailActivity::class.java)
            intent.putExtra("lead_id",id.toString())
            startActivity(intent)
        }
    }
}