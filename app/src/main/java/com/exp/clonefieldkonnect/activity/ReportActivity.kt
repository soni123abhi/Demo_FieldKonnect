package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.AttendanceReportAdapter
import com.exp.clonefieldkonnect.adapter.ReportAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.fragment.add
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceDetailModel
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.UserAttendanceListModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() ,View.OnClickListener,ReportAdapter.OnEmailClick,AttendanceReportAdapter.OnEmailClick{
//    private val nameWithImageList = ArrayList<NameWithImage>()
    var id_main : Int = 0
    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "40"
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerView_attendance: RecyclerView
    lateinit var rec_attendance_main: RelativeLayout
    lateinit var cardBack: CardView
    lateinit var tvTitle: TextView
    lateinit var edtSearchbranch: AutoCompleteTextView
    lateinit var edtSearch: AutoCompleteTextView
    var userattendancelist: ArrayList<UserAttendanceListModel.Data> = ArrayList()
    var userattendancelist2: ArrayList<UserAttendanceListModel.Data> = ArrayList()
    var userattendancebranch: ArrayList<UserAttendanceListModel.Branches> = ArrayList()
    var userattendanceuser: ArrayList<UserAttendanceListModel.Users> = ArrayList()
    private var items: MutableList<SearchableItem> = ArrayList()
    var searchbranch : ArrayList<String> = ArrayList()
    var userattendanceusername : ArrayList<String> = ArrayList()
    var userattendanceuserid : ArrayList<String> = ArrayList()
    var selecteduser_id : String = ""
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var cardSearch: RelativeLayout
    var start_date : String = ""
    var end_date : String = ""
    var selecteIDSList : MutableList<Int> = ArrayList()
    var selecteIDSList3 : MutableList<Int> = ArrayList()
    var selecteIDSList2: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        initViews()
    }

    private fun initViews() {

//        nameWithImageList.add(NameWithImage("User Activity", R.drawable.ic_report_user_activity,1))
//        nameWithImageList.add(NameWithImage("Attendance", R.drawable.ic_report_attendance,2))


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView_attendance = findViewById(R.id.recyclerView_attendance)
        rec_attendance_main = findViewById(R.id.rec_attendance_main)
        cardBack = findViewById(R.id.cardBack)
        tvTitle = findViewById(R.id.tvTitle)
        edtSearchbranch = findViewById(R.id.edtSearchbranch)
        edtSearch = findViewById(R.id.edtSearch)
        cardFrom = findViewById(R.id.cardFrom)
        cardTo = findViewById(R.id.cardTo)
        cardSearch = findViewById(R.id.cardSearch)
        tvFrom = findViewById(R.id.tvFrom)
        tvTo = findViewById(R.id.tvTo)

        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)

        recyclerView.visibility = View.VISIBLE
        rec_attendance_main.visibility = View.GONE
        cardBack.setOnClickListener {
            println("IDDD+="+id_main)
            if (id_main.equals(0)) {
                startActivity(Intent(this@ReportActivity, MainActivity::class.java))
            }else{
                startActivity(Intent(this@ReportActivity, ReportActivity::class.java))
            }
        }
        var mLayoutManager = LinearLayoutManager(this@ReportActivity)
        recyclerView.layoutManager = mLayoutManager
//        val taskAdapter = ReportAdapter(this@ReportActivity, nameWithImageList,this)
//        recyclerView.adapter = taskAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (id_main.equals(0)) {
            startActivity(Intent(this@ReportActivity, MainActivity::class.java))
        }else{
            startActivity(Intent(this@ReportActivity, ReportActivity::class.java))
        }

    }


    lateinit var dialog: Dialog



    override fun onClickEmail(id: Int) {
        id_main = id
        if (id.equals(1)){
           startActivity(Intent(this@ReportActivity,UserReportActivity::class.java))
        }
        else if (id.equals(2)){
            tvTitle.text = "Attendance"
            recyclerView.visibility = View.GONE
            rec_attendance_main.visibility = View.VISIBLE
            userattendancelist(page, searchbranch, selecteduser_id, start_date, end_date)

            recyclerView_attendance.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                        page++
                        if (userattendancelist2.size == 40){
                            userattendancelist(page, searchbranch, selecteduser_id, start_date, end_date)
                            lastPosition = firstVisibleItemPosition
                        }
                    }
                }
            })

        }
    }

    private fun userattendancelist(
        page: Int,
        searchbranch: ArrayList<String>,
        selecteduser_id: String,
        start_date: String,
        end_date: String
    ) {
        isLoading = true
        if (!Utilities.isOnline(this@ReportActivity)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this@ReportActivity)
        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["start_date"] = start_date
        queryParams["end_date"] = end_date
        queryParams["search_name"] = selecteduser_id

        ApiClient.getUserAttendance(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@ReportActivity).toString(),
            queryParams,searchbranch,
            object : APIResultLitener<UserAttendanceListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<UserAttendanceListModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            userattendancelist2.clear()
                            userattendancebranch.clear()
                            userattendanceuser.clear()

                            if (this@ReportActivity.page ==1)
                                userattendancelist.clear()

                            userattendancelist.addAll(response.body()!!.data)
                            userattendancelist2.addAll(response.body()!!.data)
                            userattendancebranch.addAll(response.body()!!.branches)
                            userattendanceuser.addAll(response.body()!!.users)

                            println("userattendancelist="+userattendancelist)
                            for (item in userattendanceuser) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                // Check if the name is not already in the list before adding
                                if (!userattendanceusername.contains(name)) {
                                    userattendanceusername.add(name)
                                    userattendanceuserid.add(id)
                                }
                            }

                            edtSearchbranch.setOnClickListener {
                                spinnerbranch()
                            }

                            edtSearch.setOnClickListener {
                                spinneruser()
                            }


                            setuprecyclerview()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@ReportActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@ReportActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun spinneruser() {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = userattendanceusername.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
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
            val selectedPosition = userattendanceusername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = userattendanceuserid[selectedPosition].toString()
                val selectedParentName = userattendanceusername[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    userattendancelist(page, searchbranch, selecteduser_id, start_date, end_date)
                }

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun spinnerbranch() {
        items.clear()
        for (item in userattendancebranch) {
            items.add(item.name.toString(), item.id.toString(), true)
        }

        SearchableMultiSelectSpinner.show(this, "Select Branch", "Search", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: java.util.ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                searchbranch.clear()
                userattendanceuser.clear()
                userattendanceusername.clear()
                userattendanceuserid.clear()
                val selectedCity = selectedItems.map { it.text }
                val selectedCityText = selectedCity.joinToString(",") // Use your preferred delimiter
                edtSearchbranch.setText(selectedCityText)

                val selectedCodes = selectedItems.map { it.code }
                for (item in selectedCodes){
                    searchbranch.add(item)
                }
                if (searchbranch.size != 0){
                    page = 1
                    userattendancelist(page, searchbranch, selecteduser_id, start_date, end_date)
                }
                println("selectedCodes=1="+searchbranch)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerview() {
        var mLayoutManager = LinearLayoutManager(this@ReportActivity)
        recyclerView_attendance.layoutManager = mLayoutManager
        val attendanceAdapter = AttendanceReportAdapter(this@ReportActivity, userattendancelist,this)
        recyclerView_attendance.adapter = attendanceAdapter
        recyclerView_attendance.scrollToPosition(lastPosition)
        attendanceAdapter.notifyDataSetChanged()
    }

    override fun onClickEmail1(id: Int, self: Boolean?, status: String?,selectedIds : MutableList<Int>) {
        println("ID="+id)
        selecteIDSList3.add(id)
        selecteIDSList2= selecteIDSList3.joinToString(separator = ",") { it.toString() }
        println("selecteIDSList2 ${selecteIDSList2} ....${selecteIDSList.size}")
        getuserattendancedetail(id,self,status)
    }
    override fun onClickEmail2(selectedIds: MutableList<Int>) {
        selecteIDSList = selectedIds
        selecteIDSList2= selecteIDSList.joinToString(separator = ",") { it.toString() }
        println("selecteIDSList2 ${selecteIDSList2} ....${selecteIDSList.size}")

    }

    private fun getuserattendancedetail(id: Int, self: Boolean?, status: String?) {
            if (!Utilities.isOnline(this@ReportActivity)) {
                return
            }
            var dialog = DialogClass.progressDialog(this@ReportActivity)
            val queryParams = HashMap<String, String>()
            queryParams["attendance_id"] = id.toString()

            ApiClient.getuserattendancedetail(
                StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@ReportActivity).toString(),
                queryParams,
                object : APIResultLitener<AttendanceDetailModel> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onAPIResult(response: Response<AttendanceDetailModel>?, errorMessage: String?) {
                        dialog.dismiss()
                        if (response != null && errorMessage == null) {

                            if (response.code() == 200) {

                                showPopupDialog(response.body()!!.data,id,self,status)
                            } else {

                                val jsonObject: JSONObject
                                try {
                                    jsonObject = JSONObject(response.errorBody()!!.string())

                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@ReportActivity, false
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            isLoading = false
                        }
                    }
                })
        }

    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showPopupDialog(
        data: AttendanceDetailModel.Data?,
        id: Int,
        self: Boolean?,
        status: String?
    ) {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_submit_layout, null)
        builder.setCancelable(false)


        var Status: String = "0"
        var remark: String = ""
        val cardbtn_submit: CardView = view.findViewById(R.id.cardbtn_submit)
        val tv_user_id: TextView = view.findViewById(R.id.tv_user_id)
        val tv_emp_code: TextView = view.findViewById(R.id.tv_emp_code)
        val tv_punchin_date: TextView = view.findViewById(R.id.tv_punchin_date)
        val tv_punchin_time: TextView = view.findViewById(R.id.tv_punchin_time)
        val tv_punchout_time: TextView = view.findViewById(R.id.tv_punchout_time)
        val tv_working_time: TextView = view.findViewById(R.id.tv_working_time)
        val tv_working_type: TextView = view.findViewById(R.id.tv_working_type)
        val cardpuchinloc: CardView = view.findViewById(R.id.cardpuchinloc)
        val cardpuchoutloc: CardView = view.findViewById(R.id.cardpuchoutloc)
        val tv_punchin_summary: TextView = view.findViewById(R.id.tv_punchin_summary)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val edtt_remark = view.findViewById<EditText>(R.id.edtt_remark)
        val lin_remark = view.findViewById<LinearLayout>(R.id.lin_remark)
        val tv_punchin_address: TextView = view.findViewById(R.id.tv_punchin_address)
        val lin_type: LinearLayout = view.findViewById(R.id.lin_type)
        val reeell: RelativeLayout = view.findViewById(R.id.reeell)
        val img_close: ImageView = view.findViewById(R.id.img_close)


        if (self == true){
            lin_type.visibility = View.GONE
            cardbtn_submit.visibility = View.GONE
            val params = reeell!!.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 0, 0, 30)
            reeell!!.layoutParams = params
        }else if (status == "Approve"){
            lin_type.visibility = View.GONE
            cardbtn_submit.visibility = View.GONE
            val params = reeell!!.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 0, 0, 30)
            reeell!!.layoutParams = params
        }else{
            lin_type.visibility = View.VISIBLE
            cardbtn_submit.visibility = View.VISIBLE
        }

        tv_user_id.text = data!!.userId.toString()
        tv_emp_code.text = data.users!!.employeeCodes.toString()
        tv_punchin_date.text = data.punchinDate.toString()
        tv_punchin_time.text = data.punchinTime.toString()
        tv_punchout_time.text = data.punchoutTime.toString()
        tv_working_time.text = data.workedTime.toString()
        tv_working_type.text = data.workingType.toString()
        tv_punchin_summary.text = data.punchinSummary.toString()
        tv_punchin_address.text = data.punchinAddress.toString()

        if (data.punchinLatitude.equals("") && data.punchinLongitude.equals("")) {
            cardpuchinloc.visibility = View.GONE
        } else {
            cardpuchinloc.visibility = View.VISIBLE
            cardpuchinloc.setOnClickListener {
                var lat = data.punchinLatitude
                var long = data.punchinLongitude
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "geo:<" + lat.toString() + ">,<" + long.toString()
                                + ">?q=<" + lat.toString() + ">,<" + long.toString()
                    )
                )
                startActivity(intent)
            }
        }
        if (data.punchoutLatitude.equals("") && data.punchoutLongitude.equals("")) {
            cardpuchoutloc.visibility = View.GONE
        } else {
            cardpuchoutloc.visibility = View.VISIBLE
            cardpuchoutloc.setOnClickListener {
                var lat = data.punchoutLatitude
                var long = data.punchoutLongitude
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "geo:<" + lat.toString() + ">,<" + long.toString()
                                + ">?q=<" + lat.toString() + ">,<" + long.toString()
                    )
                )
                startActivity(intent)
            }
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.check_approve -> {
                    Status = "1"
                    lin_remark.visibility = View.GONE
                }
                R.id.check_reject -> {
                    Status = "2"
                    lin_remark.visibility = View.VISIBLE
                }
            }
        }
        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        cardbtn_submit.setOnClickListener {
            if (Status.equals("1")) {
                remark = ""
            } else if (Status.equals("2")) {
                remark = edtt_remark.text.toString()
            }
            if (Status.equals("0")){
                Toast.makeText(this@ReportActivity,"Please Check Any Checkbox",Toast.LENGTH_SHORT).show()
            }else if (Status.equals("1")){
                submitattendance(id,Status,remark)
                userattendancelist(page, searchbranch, selecteduser_id, start_date, end_date)
                alertDialog.dismiss()
            }else if (Status.equals("2")){
                if (remark.isNullOrEmpty()){
                    Toast.makeText(this@ReportActivity,"Please Enter Remark",Toast.LENGTH_SHORT).show()
                }else{
                    submitattendance(id,Status,remark)
                    userattendancelist(page, searchbranch, selecteduser_id, start_date, end_date)
                    alertDialog.dismiss()
                }
            }
        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun submitattendance(id: Int, status: String, remark: String) {
        if (!Utilities.isOnline(this@ReportActivity)) {
                return
            }
            var dialog = DialogClass.progressDialog(this@ReportActivity)
            val queryParams = HashMap<String, String>()
            queryParams["attendance_id"] = selecteIDSList2
            queryParams["status"] = status
            queryParams["remark_status"] = remark

            ApiClient.userattendancesubmit(
                StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@ReportActivity).toString(),
                queryParams,
                object : APIResultLitener<AttendanceSubmitModel> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                        dialog.dismiss()
                        if (response != null && errorMessage == null) {

                            if (response.code() == 200) {
                                Toast.makeText(this@ReportActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                                selecteIDSList.clear()
                                selecteIDSList3.clear()
                            } else {
                                val jsonObject: JSONObject
                                try {
                                    jsonObject = JSONObject(response.errorBody()!!.string())

                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        this@ReportActivity, false
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                })
        }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cardFrom -> {
                Utilities.datePicker(tvFrom, tvTo.text.toString(), "", true, this)
            }
            R.id.cardTo -> {
                Utilities.datePicker(tvTo, "", tvFrom.text.toString(), false, this)
            }
            R.id.cardSearch -> {
                val convertedDate = convertDateFormats(tvFrom.text.toString(),tvTo.text.toString())
                start_date = convertedDate.first
                end_date = convertedDate.second
                println("from=="+start_date+"To="+end_date)
                page = 1
                if (start_date.equals("")){
                    Toast.makeText(this@ReportActivity,"Please Select Start Date",Toast.LENGTH_LONG).show()
                }else if (end_date.equals("")){
                    Toast.makeText(this@ReportActivity,"Please Select End Date",Toast.LENGTH_LONG).show()
                }else{
                    userattendancelist(page, searchbranch, selecteduser_id,start_date,end_date)
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

//class NameWithImage(val name: String, @DrawableRes val imageRes: Int,val Id :Int)


