package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.LeadTaskListAdapter
import com.exp.clonefieldkonnect.adapter.TaskManagementAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.LeadTaskModel
import com.exp.clonefieldkonnect.model.TaskDropdownModel
import com.exp.clonefieldkonnect.model.TaskManagemnetModel
import com.exp.import.Utilities
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

class TaskActivity : AppCompatActivity(),LeadTaskListAdapter.OnEmailClick,TaskManagementAdapter.OnEmailClick{
    lateinit var cardBack_activity: CardView
    lateinit var card_leadtask: CardView
    lateinit var relll_lead_task: RelativeLayout
    lateinit var tv_lead_task: TextView
    lateinit var card_task_managemnet: CardView
    lateinit var relll_task: RelativeLayout
    lateinit var tv_task: TextView
    lateinit var recyclerView_task: RecyclerView
    lateinit var recyclerView_task_managment: RecyclerView
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
    var flag = "lead_task"

    var start_date : String = ""
    var end_date : String = ""

    var selectedtaskuser_id : String = ""
    var selectedtaskuser_name : String = ""
    var selectedtaskstatus_id : String = ""
    var selectedtaskstatus_name : String = ""

    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    val INTENTCAMERAEDIT = 6
    val INTENTGALLERYEDIT = 7
    var selected_image_path2 : java.util.ArrayList<String> = java.util.ArrayList()
    var selected_image_path: String =""

    var edit_select_imagepath: java.util.ArrayList<String> = java.util.ArrayList()

    lateinit var cameraFile: File
    var imageFile: String = ""
    var base64: String = ""
    private var listView_attach: ListView? = null


    var leadtasklist: ArrayList<LeadTaskModel.Data> = ArrayList()
    var leadtasklist2: ArrayList<LeadTaskModel.Data> = ArrayList()


    var taskmanagementlist: ArrayList<TaskManagemnetModel.Data> = ArrayList()
    var taskmanagementlist2: ArrayList<TaskManagemnetModel.Data> = ArrayList()

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
        recyclerView_task_managment = findViewById(R.id.recyclerView_task_managment)
        searchIcon = findViewById(R.id.searchIcon)
        searchInput = findViewById(R.id.searchInput)
        filter = findViewById(R.id.filter)
        ic_notification = findViewById(R.id.ic_notification)
        notification_count = findViewById(R.id.notification_count)

        card_leadtask = findViewById(R.id.card_leadtask)
        relll_lead_task = findViewById(R.id.relll_lead_task)
        tv_lead_task = findViewById(R.id.tv_lead_task)
        card_task_managemnet = findViewById(R.id.card_task_managemnet)
        relll_task = findViewById(R.id.relll_task)
        tv_task = findViewById(R.id.tv_task)


        var notification_tagggg = intent.getStringExtra("notification_tag")
        println("notification_tagggg=="+notification_tagggg)

        if (!notification_tagggg.isNullOrEmpty()){
            if (notification_tagggg.equals("task")){
                flag = "lead_task"

                relll_lead_task.setBackgroundColor(Color.parseColor("#182D69"))
                tv_lead_task.setTextColor(Color.parseColor("#FFFFFF"))

                relll_task.setBackgroundColor(Color.parseColor("#FFFFFF"))
                tv_task.setTextColor(Color.parseColor("#182D69"))

                recyclerView_task_managment.visibility = View.GONE
                recyclerView_task.visibility = View.VISIBLE
            }else if (notification_tagggg.equals("task_management")){

                flag = "task_management"
                relll_task.setBackgroundColor(Color.parseColor("#182D69"))
                tv_task.setTextColor(Color.parseColor("#FFFFFF"))

                relll_lead_task.setBackgroundColor(Color.parseColor("#FFFFFF"))
                tv_lead_task.setTextColor(Color.parseColor("#182D69"))


                recyclerView_task.visibility = View.GONE
                recyclerView_task_managment.visibility = View.VISIBLE
            }
        }else{
            recyclerView_task_managment.visibility = View.GONE
            recyclerView_task.visibility = View.VISIBLE
        }




        cardBack_activity.setOnClickListener {
            handelbackpress()
        }

        ic_notification.setOnClickListener {
            startActivity(Intent(this,NotificationActivity::class.java))
        }

        filter.setOnClickListener {
            showfilters()
        }

        card_task_managemnet.setOnClickListener {

            flag = "task_management"
            relll_task.setBackgroundColor(Color.parseColor("#182D69"))
            tv_task.setTextColor(Color.parseColor("#FFFFFF"))

            relll_lead_task.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tv_lead_task.setTextColor(Color.parseColor("#182D69"))


            recyclerView_task.visibility = View.GONE
            recyclerView_task_managment.visibility = View.VISIBLE

        }
        card_leadtask.setOnClickListener {
            flag = "lead_task"

            relll_lead_task.setBackgroundColor(Color.parseColor("#182D69"))
            tv_lead_task.setTextColor(Color.parseColor("#FFFFFF"))

            relll_task.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tv_task.setTextColor(Color.parseColor("#182D69"))

            recyclerView_task_managment.visibility = View.GONE
            recyclerView_task.visibility = View.VISIBLE

        }

        gettaskdropdown()


        searchIcon.setOnClickListener {
            println("flllllll=="+flag)
            page = 1
            if (flag.equals("lead_task")){
                getleadtask(page, searchInput.text.toString(), selectedtaskuser_id, start_date, end_date, selectedtaskstatus_id)
            } else if (flag.equals("task_management")){
                gettaskmanagemnet(
                    page,
                    searchInput.text.toString(),
                    selectedtaskuser_id,
                    start_date,
                    end_date,
                    selectedtaskstatus_id
                )
            }
        }

        getleadtask(page, searchInput.text.toString(), selectedtaskuser_id, start_date, end_date, selectedtaskstatus_id)
        gettaskmanagemnet(
            page,
            searchInput.text.toString(),
            selectedtaskuser_id,
            start_date,
            end_date,
            selectedtaskstatus_id
        )



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


        recyclerView_task_managment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (taskmanagementlist2.size == 30){
                        gettaskmanagemnet(
                            page,
                            searchInput.text.toString(),
                            selectedtaskuser_id,
                            start_date,
                            end_date,
                            selectedtaskstatus_id
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
            if (flag.equals("lead_task")){
                getleadtask(page,searchInput.text.toString(),selectedtaskuser_id,start_date,end_date,selectedtaskstatus_id)
            } else if (flag.equals("task_management")){
                gettaskmanagemnet(page,searchInput.text.toString(),selectedtaskuser_id,start_date,end_date,selectedtaskstatus_id)
            }
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
            if (flag.equals("lead_task")){
                getleadtask(page,searchInput.text.toString(),selectedtaskuser_id,start_date,end_date,selectedtaskstatus_id)
            } else if (flag.equals("task_management")){
                gettaskmanagemnet(page,searchInput.text.toString(),selectedtaskuser_id,start_date,end_date,selectedtaskstatus_id)
            }
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

    private fun gettaskmanagemnet(
        page: Int,
        searchInput: String,
        selectedtaskuser_id: String,
        start_date: String,
        end_date: String,
        selectedtaskstatus_id: String,
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
        ApiClient.gettaskmanagemnet(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<TaskManagemnetModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<TaskManagemnetModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            taskmanagementlist2.clear()

                            if (page ==1)
                                taskmanagementlist.clear()

                            taskmanagementlist.addAll(response.body()!!.data)
                            taskmanagementlist2.addAll(response.body()!!.data)

                            setuprecyclertaskmanagementlist()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclertaskmanagementlist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_task_managment.layoutManager = mLayoutManager
        val useractivityAdapter = TaskManagementAdapter(this, taskmanagementlist, this)
        recyclerView_task_managment.adapter = useractivityAdapter
        recyclerView_task_managment.scrollToPosition(lastPosition)
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
    private fun opentaskalertdialog(task_id: String, task_status: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_confirm_with_remark, null)

        val editTextRemark = view.findViewById<EditText>(R.id.etRemark)
        val btnYes = view.findViewById<CardView>(R.id.btnYes)
        val btnCancel = view.findViewById<CardView>(R.id.btnCancel)
        val rell_attachement = view.findViewById<RelativeLayout>(R.id.rell_attachement)
        val img_add_attachment = view.findViewById<ImageView>(R.id.img_add_attachment)
        listView_attach = view.findViewById<ListView>(R.id.listView_attach)

        rell_attachement.visibility = View.VISIBLE
        editTextRemark.visibility = View.VISIBLE
        editTextRemark.setHint("Enter Comment")

        img_add_attachment.setOnClickListener {
            selectImage()
        }


        val dialog = builder.setView(view).setCancelable(false).create()

        btnYes.setOnClickListener {
            var remark = ""
            remark = editTextRemark.text.toString()
            println("aaaaaaaaa=="+remark+"<<"+selected_image_path2+"<<"+task_status)
            if (remark.isNullOrEmpty()){
                response_message("Please Enter Comment")
            }else{
                submittaskmanagementstatus(task_id,task_status,remark,dialog,selected_image_path2)
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Camera" -> {
                    var intent = Intent(this, CameraCustomActivity::class.java);
                    intent.putExtra("camera", "1")
                    startActivityForResult(intent, INTENTCAMERA)
                }
                items[item] == "Gallery" -> {
                    openGallery()
                }
                items[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.please_select_image)), INTENTGALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("codeeeeee=="+requestCode+"<<"+INTENTCAMERA+"<<"+INTENTGALLERY+"<<"+INTENTCAMERAEDIT+"<<"+INTENTGALLERYEDIT)


        if (requestCode == INTENTCAMERA) {
            if (resultCode == Activity.RESULT_OK) {

                lifecycleScope.launch {
                    try {
                        var path: File = data!!.getSerializableExtra("image") as File
                        val compressedImageFile = Compressor.compress(this@TaskActivity, path) {
                            resolution(200, 200)   // max width & height
                            quality(90)            // image quality
                            format(Bitmap.CompressFormat.JPEG)
                        }

                        base64 = ""
                        selected_image_path2.add(compressedImageFile.path)

                        if (selected_image_path2.isEmpty()) {
                            listView_attach?.visibility = View.GONE
                        } else {
                            listView_attach?.visibility = View.VISIBLE
                            updateListView()
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }


            }
        }

        else if (requestCode == INTENTGALLERY && resultCode == Activity.RESULT_OK) {
            var photoURI = data?.data
            val uriString: String = photoURI.toString()
            var pdfName: String? = null

            val imagePath = getDriveFile(photoURI)

            selected_image_path2.add(imagePath)
            if (selected_image_path2.size == 0){
                listView_attach!!.visibility = View.GONE
            } else{
                listView_attach!!.visibility = View.VISIBLE
                updateListView()
            }
        }
        else if (requestCode == INTENTCAMERAEDIT) {
            try {
                val path: File = data!!.getSerializableExtra("image") as File
                Log.v("akram", "path " + StoreCustomerActivity.arrList.size)
                cameraFile = path
                imageFile = path.path

                // Launch coroutine for compression
                lifecycleScope.launch {
                    try {
                        val compressedImageFile = Compressor.compress(this@TaskActivity, path) {
                            resolution(200, 200)   // set max width & height
                            quality(90)            // compression quality
                            format(Bitmap.CompressFormat.JPEG)
                        }

                        base64 = ""
                        edit_select_imagepath.add(compressedImageFile.path)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            } catch (e: Exception) {
                Log.v("akram", "try inside: ${e.message}")
            }
        }
        else if (requestCode == INTENTGALLERYEDIT && resultCode == Activity.RESULT_OK) {
            var photoURI = data?.data
            var edit_image_paths = getDriveFileedit(photoURI)
            edit_select_imagepath.add(edit_image_paths)
        }

    }

    private fun updateListView() {
        val adapter = ImagePathAdapter(this, selected_image_path2)
        listView_attach!!.adapter = adapter
    }

    class ImagePathAdapter(var activityLocal: Activity, var selected_image_path2: java.util.ArrayList<String>) :
        ArrayAdapter<String>(activityLocal, R.layout.item_image_path_expense, selected_image_path2) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val path = getItem(position) ?: ""

            val inflater = LayoutInflater.from(context)
            val view = convertView ?: inflater.inflate(R.layout.item_image_path_expense, parent, false)

            val imageViewFile: ImageView = view.findViewById(R.id.imageViewFile)
            val textViewPath: TextView = view.findViewById(R.id.textViewPath)
            val buttonRemove: ImageButton = view.findViewById(R.id.buttonRemove)

            val file = File(path)
            if (file.extension.equals("pdf", ignoreCase = true)) {
                imageViewFile.setImageResource(R.drawable.ic_img_pdf)
            } else {
                loadImageIntoImageView(file, imageViewFile)
            }

            textViewPath.text = file.name

            buttonRemove.setOnClickListener {
                selected_image_path2.removeAt(position)
                notifyDataSetChanged()
            }

            return view
        }

        private fun loadImageIntoImageView(file: File, imageView: ImageView) {
            try {
                val bitmap = BitmapFactory.decodeFile(file.path)
                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                imageView.setImageResource(R.drawable.ic_img_pdf)
            }
        }
    }

    private fun getDriveFile(photoURI: Uri?): String {
        val returnUri: Uri = photoURI!!
        val returnCursor: Cursor =
            this.getContentResolver().query(returnUri, null, null, null, null)!!

        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(this.getCacheDir(), name)
        try {
            val inputStream: InputStream = photoURI?.let {
                this.getContentResolver().openInputStream(
                    it
                )
            }!!
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            selected_image_path = file.path

        } catch (e: Exception) {
        }
        return file.path

    }


    private fun getDriveFileedit(photoURI: Uri?): String {
        val returnUri: Uri = photoURI!!
        val returnCursor: Cursor =
            this.getContentResolver().query(returnUri, null, null, null, null)!!

        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(this.getCacheDir(), name)
        try {
            val inputStream: InputStream = photoURI?.let {
                this.getContentResolver().openInputStream(
                    it
                )
            }!!
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            response_message("Sucessfully Attach")

        } catch (e: Exception) {
        }
        return file.path

    }

    private fun response_message(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
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


    private fun submittaskmanagementstatus(
        taskId: String,
        taskStatus: String,
        remark: String,
        dialog2222: AlertDialog,
        selected_image_path2: java.util.ArrayList<String>
    ) {

        if (!Utilities.isOnline(this@TaskActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@TaskActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["task_id"] = taskId
        queryParams["task_status"] = taskStatus
        queryParams["comment"] = remark

        val multipartParts = createMultipartParts(selected_image_path2)

        ApiClient.submittaskmanagementstatus(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@TaskActivity).toString(),
            queryParams,multipartParts,
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
                            gettaskmanagemnet(page, searchInput.text.toString(), selectedtaskuser_id, start_date, end_date, selectedtaskstatus_id)
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

    fun createMultipartParts(selectedImagePaths: java.util.ArrayList<String>): List<MultipartBody.Part> {
        val multipartParts = mutableListOf<MultipartBody.Part>()

        val emptyRequestBody: RequestBody = RequestBody.create(null, ByteArray(0))
        val emptyPart = MultipartBody.Part.createFormData("files[]", "", emptyRequestBody)
        multipartParts.add(emptyPart)

        for (selectedImagePath in selectedImagePaths) {
            if (selectedImagePath.isNotEmpty()) {
                val file = File(selectedImagePath)
                val reqbodyFileD: RequestBody = RequestBody.create(MediaType.parse("*/*"), file)
                val fileName = "files[]"
                val part = MultipartBody.Part.createFormData(fileName, file.name, reqbodyFileD)
                multipartParts.add(part)
            }
        }

        return multipartParts
    }

    override fun onClicktaskmanagement(id: Int?, status: String?) {
        var task_status = ""
        if (status.equals("Pending")){
            task_status = "open"
        }else if (status.equals("Open")){
            task_status = "in_progress"
        }else if (status.equals("In progress")){
            task_status = "completed"
        }

        println("ididid=="+id+"<<"+status+"<<"+task_status)

        if (status.equals("Completed")){
            responsemessage("Task is already Completed!")
        }else{
            opentaskalertdialog(id.toString(),task_status)
        }
    }

}