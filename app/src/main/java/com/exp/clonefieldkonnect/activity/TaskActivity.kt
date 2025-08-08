package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
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
import com.exp.clonefieldkonnect.adapter.LeadTaskListAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.LeadTaskModel
import com.exp.clonefieldkonnect.model.TaskDropdownModel
import com.exp.import.Utilities
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class TaskActivity : AppCompatActivity(),LeadTaskListAdapter.OnEmailClick{
    lateinit var cardBack_activity: CardView
    lateinit var recyclerView_task: RecyclerView
    lateinit var searchIcon: ImageView
    lateinit var searchInput: EditText
    lateinit var filter: ImageView
    lateinit var ic_notification: ImageView
    lateinit var notification_count: TextView

    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "30"
    var page_count : String = ""

    var start_date : String = ""
    var end_date : String = ""

    var selectedtaskuser_id : String = ""
    var selectedtaskuser_name : String = ""
    var selectedtaskstatus_id : String = ""
    var selectedtaskstatus_name : String = ""

    var leadtasklist: ArrayList<LeadTaskModel.Data> = ArrayList()
    var leadtasklist2: ArrayList<LeadTaskModel.Data> = ArrayList()

    var leaduserlist: ArrayList<TaskDropdownModel.Users> = ArrayList()
    var leadusersname : ArrayList<String> = ArrayList()
    var leadusersid : ArrayList<String> = ArrayList()

    var leadstatuslist: ArrayList<TaskDropdownModel.Status> = ArrayList()
    var leadstatusname : ArrayList<String> = ArrayList()
    var leadstatusid : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        initViews()
    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack)
        recyclerView_task = findViewById(R.id.recyclerView_task)
        searchIcon = findViewById(R.id.searchIcon)
        searchInput = findViewById(R.id.searchInput)
        filter = findViewById(R.id.filter)
        ic_notification = findViewById(R.id.ic_notification)
        notification_count = findViewById(R.id.notification_count)

        cardBack_activity.setOnClickListener {
            handelbackpress()
        }

        ic_notification.setOnClickListener {
            startActivity(Intent(this,NotificationActivity::class.java))
        }

        filter.setOnClickListener {
            showfilters()
        }

        gettaskdropdown()


        searchIcon.setOnClickListener {
            page = 1
            getleadtask(page, searchInput.text.toString(), selectedtaskuser_id, start_date, end_date, selectedtaskstatus_id)
        }

        getleadtask(page, searchInput.text.toString(), selectedtaskuser_id, start_date, end_date, selectedtaskstatus_id)

        recyclerView_task.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (leadtasklist2.size == 30){
                        getleadtask(page, searchInput.text.toString(), selectedtaskuser_id, start_date, end_date, selectedtaskstatus_id)

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
                            leadstatuslist.clear()


                            leaduserlist.addAll(response.body()!!.data!!.users)
                            leadstatuslist.addAll(response.body()!!.data!!.status)

                            for (item in leaduserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadusersname.contains(name)) {
                                    leadusersname.add(name)
                                    leadusersid.add(id)
                                }
                            }

                            for (item in leadstatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadstatusname.contains(name)) {
                                    leadstatusname.add(name)
                                    leadstatusid.add(id)
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
                                    this@TaskActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@TaskActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
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
        var btnReset = dialog.findViewById<CardView>(R.id.btnReset)
        var btnApply = dialog.findViewById<CardView>(R.id.btnApply)

        if (start_date.isNotEmpty()){
            tvFrom!!.text = start_date
        }
        if (end_date.isNotEmpty()){
            tvTo!!.text = start_date
        }
        if (selectedtaskuser_name.isNotEmpty()){
            edtSearch!!.setText(selectedtaskuser_name)
        }
        if (selectedtaskstatus_name.isNotEmpty()){
            edtStatus!!.setText(selectedtaskstatus_name)
        }


        edtSearch!!.setOnClickListener {
            spinnerusername(edtSearch!!)
        }

        edtStatus!!.setOnClickListener {
            spinnerstatusname(edtStatus!!)
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
            selectedtaskstatus_id = ""
            selectedtaskuser_name = ""
            selectedtaskstatus_name = ""
            page = 1
            getleadtask(page,searchInput.text.toString(),selectedtaskuser_id,start_date,end_date,selectedtaskstatus_id)
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
            getleadtask(page,searchInput.text.toString(),selectedtaskuser_id,start_date,end_date,selectedtaskstatus_id)
            dialog.dismiss()

        }

        
        
        dialog.show()
    }

    private fun spinnerstatusname(edtStatus: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadstatusname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
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
            edtStatus.setText("")
            selectedtaskstatus_id = ""
            selectedtaskstatus_name = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadstatusid[selectedPosition].toString()
                val selectedParentName = leadstatusname[selectedPosition].toString()

                edtStatus.setText(selectedParentName)
                selectedtaskstatus_id = selectedParentId
                selectedtaskstatus_name = selectedParentName

                println("Abhi=id=$selectedtaskstatus_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
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


    fun convertDateFormats(tvFrom: String, tvTo: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val tvFromParsed = inputFormat.parse(tvFrom)

        val tvToParsed = inputFormat.parse(tvTo)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val tvfrom = outputFormat.format(tvFromParsed)
        val tvTo = outputFormat.format(tvToParsed)

        return Pair(tvfrom,tvTo)
    }


    private fun getleadtask(
        page: Int,
        searchInput: String,
        selectedtaskuser_id: String,
        start_date: String,
        end_date: String,
        selectedtaskstatus_id: String
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
        queryParams["search"] = searchInput
        queryParams["user_id"] = selectedtaskuser_id
        queryParams["start_date"] = start_date
        queryParams["end_date"] = end_date
        queryParams["status_id"] = selectedtaskstatus_id

        println("queryParams=="+queryParams)
        ApiClient.getleadtask(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadTaskModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadTaskModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadtasklist2.clear()

                            if (page ==1)
                                leadtasklist.clear()

                            leadtasklist.addAll(response.body()!!.data)
                            leadtasklist2.addAll(response.body()!!.data)
                            notification_count.text = response.body()!!.notification_count.toString()
                            setuprecyclerleadtasklist()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@TaskActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@TaskActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerleadtasklist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_task.layoutManager = mLayoutManager
        val useractivityAdapter = LeadTaskListAdapter(this, leadtasklist, this)
        recyclerView_task.adapter = useractivityAdapter
        recyclerView_task.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        handelbackpress()
    }

    private fun handelbackpress() {
        startActivity(Intent(this@TaskActivity,MainActivity::class.java))
    }

    override fun onClicklead(id: Int?, status: String?) {
        var task_status = ""
        if (status.equals("Pending")){
            task_status = "open"
        }else if (status.equals("Open")){
            task_status = "in_progress"
        }else if (status.equals("In Progress")){
            task_status = "completed"
        }

        println("ididid=="+id+"<<"+status+"<<"+task_status)

        if (status.equals("Completed")){
            responsemessage("Task is already Completed!")
        }else{
            openalertdialog(id.toString(),task_status)
        }
    }

    private fun responsemessage(s: String) {
        Toast.makeText(this@TaskActivity,s,Toast.LENGTH_SHORT).show()
    }

    private fun openalertdialog(task_id: String, task_status: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_confirm_with_remark, null)

        val editTextRemark = view.findViewById<EditText>(R.id.etRemark)
        val btnYes = view.findViewById<CardView>(R.id.btnYes)
        val btnCancel = view.findViewById<CardView>(R.id.btnCancel)

        if (task_status.equals("completed")){
            editTextRemark.visibility = View.VISIBLE
        }else{
            editTextRemark.visibility = View.GONE
        }

        val dialog = builder.setView(view).setCancelable(false).create()

        btnYes.setOnClickListener {
            var remark = ""
            if (task_status.equals("completed")){
                remark = editTextRemark.text.toString()
            }else{
                remark = ""
            }
            submittaskstatus(task_id,task_status,remark,dialog)
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun submittaskstatus(
        taskId: String,
        taskStatus: String,
        remark: String,
        dialog2222: AlertDialog
    ) {

        if (!Utilities.isOnline(this@TaskActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@TaskActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["task_id"] = taskId
        queryParams["status"] = taskStatus
        queryParams["remark"] = remark

        ApiClient.submittaskstatus(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@TaskActivity).toString(),
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
                            Toast.makeText(this@TaskActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            page = 1
                            getleadtask(
                                page,
                                "",
                                selectedtaskuser_id,
                                start_date,
                                end_date,
                                selectedtaskstatus_id
                            )
                            searchInput.text.clear()
                            dialog2222.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@TaskActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

}