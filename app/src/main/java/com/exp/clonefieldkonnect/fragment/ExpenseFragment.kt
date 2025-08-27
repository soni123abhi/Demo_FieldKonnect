package com.exp.clonefieldkonnect.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.CameraCustomActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.activity.StoreCustomerActivity
import com.exp.clonefieldkonnect.adapter.ExpenseImagePathEditAdapter
import com.exp.clonefieldkonnect.adapter.ExpenseImagePathViewAdapter
import com.exp.clonefieldkonnect.adapter.ExpenselistAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.ExpenseTypeModel
import com.exp.clonefieldkonnect.model.UserExpenseDetailModel
import com.exp.clonefieldkonnect.model.UserExpenseListModel
import com.exp.import.Utilities
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ExpenseFragment(var cardBack: CardView, var linearTopreport: CardView, var tabPosition: Int, var tvTitle: TextView
) : Fragment(),ExpenselistAdapter.OnEmailClick,View.OnClickListener,ExpenseImagePathEditAdapter.OnEmailClick {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private lateinit var rel_create_expense: NestedScrollView
    private lateinit var fragment_container_expense: FrameLayout
    private lateinit var rel_main_expense: RelativeLayout
    private lateinit var recyclerView_expense: RecyclerView
    private lateinit var edtexpensetype: AutoCompleteTextView
    private lateinit var edtexpensetypelist: AutoCompleteTextView
    private lateinit var edtexpensestatus: AutoCompleteTextView
    private lateinit var rel_expenseee: RelativeLayout
    private lateinit var tv_claim_rate: TextView
    private lateinit var edt_start_km: EditText
    private lateinit var edt_stop_km: EditText
    private lateinit var edt_exp_note: EditText
    private lateinit var tv_total_km: TextView
    private lateinit var tv_claim_amt: TextView
    lateinit var cardBack_expense: CardView
    lateinit var linearTop_expense: CardView
    lateinit var cardFromm: LinearLayout
    lateinit var tvFrom111: TextView
    lateinit var img_create: ImageView
    lateinit var cardFrom_list: LinearLayout
    lateinit var cardTo_list: LinearLayout
    lateinit var tvFrom_list: TextView
    lateinit var tvTo_list: TextView
    lateinit var cardSearch_list: RelativeLayout
    lateinit var img_add_attachment: ImageView
    lateinit var tvTitle_expense: TextView
    lateinit var listView_attach: ListView
    lateinit var card_sub: CardView
    private val PICK_IMAGE_OR_DOCUMENT_REQUEST = 123
    private val PICK_IMAGE_OR_DOCUMENT_REQUEST_edit = 456
    private var expensetypelist: ArrayList<ExpenseTypeModel.Data> = ArrayList()
    var expensetype_id : String = ""
    var expensetype_rate : String = ""
    var expensetype_allow_typeid : String = ""
    var flag : String = ""
    var total_reading : String = ""
    var start_reading : String = ""
    var start_date : String = ""
    var end_date : String = ""
    var end_reading : String = ""
    var claim_amt : String = ""
    var selected_image_path: String =""
    var selected_image_path2 : ArrayList<String> = ArrayList()
    val DELAY_MILLIS = 1000
    private val handler = Handler(Looper.getMainLooper())
    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "40"
    var page_count : String = ""
    var userexpenselist: ArrayList<UserExpenseListModel.Data> = ArrayList()
    var userexpenselist2: ArrayList<UserExpenseListModel.Data> = ArrayList()
    var userexpensetypelist: ArrayList<UserExpenseListModel.ExpenceTypes> = ArrayList()
    var userexpensestatuslist: ArrayList<UserExpenseListModel.AllStatus> = ArrayList()
    var edit_total_reading : String = ""
    var edit_start_reading : String = ""
    var edit_end_reading : String = ""
    var edit_claim_amt : String = ""
    var edit_expensetype_rate : String = ""
    var edit_expensetype_id : String = ""
    var edit_expensetype_allow_typeid : String = ""
    var expense_id : String = ""
    var date_expire: String = ""
    var selectedtype_id: String = ""
    var selectedstatus_id: String = ""
    var edit_select_imagepath: ArrayList<String> = ArrayList()
    var edit_select_imagepath_remove: ArrayList<String> = ArrayList()
    var userexpensename: ArrayList<String> = ArrayList()
    var userexpenseid: ArrayList<String> = ArrayList()
    var userexpensestatusname: ArrayList<String> = ArrayList()
    var userexpensestatusid: ArrayList<String> = ArrayList()

    val INTENTCAMERA = 4
    val INTENTGALLERY = 5
    val INTENTCAMERAEDIT = 6
    val INTENTGALLERYEDIT = 7

    lateinit var cameraFile: File
    var imageFile: String = ""
    var base64: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_expense, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        cardBack_expense = rootView.findViewById(R.id.cardBack_expense)
        linearTop_expense = rootView.findViewById(R.id.linearTop_expense)
        recyclerView_expense = rootView.findViewById(R.id.recyclerView_expense)
        img_create = rootView.findViewById(R.id.img_create)
        rel_create_expense = rootView.findViewById(R.id.rel_create_expense)
        edt_exp_note = rootView.findViewById(R.id.edt_exp_note)
        fragment_container_expense = rootView.findViewById(R.id.fragment_container_expense)
        rel_main_expense = rootView.findViewById(R.id.rel_main_expense)
        tvTitle_expense = rootView.findViewById(R.id.tvTitle_expense)
        img_add_attachment = rootView.findViewById(R.id.img_add_attachment)
        card_sub = rootView.findViewById(R.id.card_sub)
        edtexpensetype = rootView.findViewById(R.id.edtexpensetype)
        rel_expenseee = rootView.findViewById(R.id.rel_expenseee)
        tv_claim_rate = rootView.findViewById(R.id.tv_claim_rate)
        edt_start_km = rootView.findViewById(R.id.edt_start_km)
        edt_stop_km = rootView.findViewById(R.id.edt_stop_km)
        tv_total_km = rootView.findViewById(R.id.tv_total_km)
        tv_claim_amt = rootView.findViewById(R.id.tv_claim_amt)
        cardFromm = rootView.findViewById(R.id.cardFromm)
        tvFrom111 = rootView.findViewById(R.id.tvFrom111)
        listView_attach = rootView.findViewById(R.id.listView_attach)
        cardFrom_list = rootView.findViewById(R.id.cardFrom_list)
        cardTo_list = rootView.findViewById(R.id.cardTo_list)
        cardSearch_list = rootView.findViewById(R.id.cardSearch_list)
        tvFrom_list = rootView.findViewById(R.id.tvFrom_list)
        tvTo_list = rootView.findViewById(R.id.tvTo_list)
        edtexpensetypelist = rootView.findViewById(R.id.edtexpensetypelist)
        edtexpensestatus = rootView.findViewById(R.id.edtexpensestatus)


        rel_main_expense.visibility = View.VISIBLE
        rel_create_expense.visibility = View.GONE
        tvTitle_expense.text = "Expense"

        cardFrom_list.setOnClickListener(this)
        cardTo_list.setOnClickListener(this)
        cardSearch_list.setOnClickListener(this)

        img_create.setOnClickListener(this)

        cardBack_expense.setOnClickListener {
            handleBackPressed()
        }
        getexpenselist(page, start_date, end_date, selectedtype_id, selectedstatus_id)

        recyclerView_expense.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (userexpenselist2.size == 40){
                        getexpenselist(
                            page,
                            start_date,
                            end_date,
                            selectedtype_id,
                            selectedstatus_id
                        )
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })
    }


    private fun getexpenselist(
        page: Int,
        start_date: String,
        end_date: String,
        selectedtype_id: String,
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
        queryParams["start_date"] = start_date.toString()
        queryParams["end_date"] = end_date.toString()
        queryParams["expenses_type"] = selectedtype_id
        queryParams["payroll id"] = StaticSharedpreference.getInfo(Constant.PAYROLL_ID, activityLocal).toString()
        queryParams["status"] = selectedstatus_id

        ApiClient.getexpenselist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<UserExpenseListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<UserExpenseListModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            userexpenselist2.clear()
                            userexpensetypelist.clear()
                            userexpensestatuslist.clear()

                            if (page==1)
                                userexpenselist.clear()

                            userexpenselist.addAll(response.body()!!.data)
                            userexpensetypelist.addAll(response.body()!!.expenceTypes)
                            userexpensestatuslist.addAll(response.body()!!.allStatus)

                            userexpenselist2.addAll(response.body()!!.data)

                            for (item in userexpensetypelist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!userexpensename.contains(name)) {
                                    userexpensename.add(name)
                                    userexpenseid.add(id)
                                }
                            }

                            for (item in userexpensestatuslist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!userexpensestatusname.contains(name)) {
                                    userexpensestatusname.add(name)
                                    userexpensestatusid.add(id)
                                }
                            }

                            edtexpensetypelist.setOnClickListener {
                                spinnertype()
                            }

                            edtexpensestatus.setOnClickListener {
                                spinnerstatus()
                            }

                            setuprecyclerexpenselist()

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
                        recyclerView_expense.visibility = View.GONE
                        Toast.makeText(activityLocal,"No Record Found", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun spinnerstatus() {
        val builder = android.app.AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = userexpensestatusname.map { it.toString() }.toTypedArray()
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
            edtexpensestatus.setText("")
            selectedstatus_id = ""
            getexpenselist(page, start_date, end_date, selectedtype_id, selectedstatus_id)
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = userexpensestatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = userexpensestatusid[selectedPosition].toString()
                val selectedParentName = userexpensestatusname[selectedPosition].toString()

                edtexpensestatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")

                if (selectedstatus_id.isNotEmpty()){
                    page = 1
                    getexpenselist(page,start_date,end_date,selectedtype_id,selectedstatus_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinnertype() {
        val builder = android.app.AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = userexpensename.map { it.toString() }.toTypedArray()
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
            edtexpensetypelist.setText("")
            selectedtype_id = ""
            getexpenselist(page, start_date, end_date, selectedtype_id, selectedstatus_id)
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = userexpensename.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = userexpenseid[selectedPosition].toString()
                val selectedParentName = userexpensename[selectedPosition].toString()

                edtexpensetypelist.setText(selectedParentName)
                selectedtype_id = selectedParentId

                println("Abhi=id=$selectedtype_id")

                if (selectedtype_id.isNotEmpty()){
                    page = 1
                    getexpenselist(page, start_date, end_date, selectedtype_id, selectedstatus_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
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
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        } else if (flag.equals("create")){
            page = 1
            getexpenselist(page, start_date, end_date, selectedtype_id, selectedstatus_id)
            tvTitle_expense.text = "Expense"
            rel_main_expense.visibility = View.VISIBLE
            rel_create_expense.visibility = View.GONE
            flag = ""
        }else if (flag.equals("")){
            tvTitle_expense.text = "Report"
            rel_main_expense.visibility = View.GONE
            rel_create_expense.visibility = View.GONE
            fragment_container_expense.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
            tabPosition = 2
        }
    }


    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_expense, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerexpenselist() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_expense.layoutManager = mLayoutManager
        val expenselistAdapter = ExpenselistAdapter(activityLocal, userexpenselist, this)
        recyclerView_expense.adapter = expenselistAdapter
        recyclerView_expense.scrollToPosition(lastPosition)
        expenselistAdapter.notifyDataSetChanged()
    }

    override fun onClickEmail_expense(id: String, status: String, date: String) {
        println("id===" + id)
        println("id=" + date)
        expense_id = id
        getexpensedetail(id,status,date)
    }

    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: AlertDialog

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    private fun showeditdialog(data: UserExpenseDetailModel.Data?) {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_expense_edit_layout, null)
        builder.setCancelable(false)

        val img_close_edit: ImageView = view.findViewById(R.id.img_close_edit)
        val edit_edtexpensetype: AutoCompleteTextView = view.findViewById(R.id.edit_edtexpensetype)
        val tvFrom111: TextView = view.findViewById(R.id.edit_tvFrom111)
        val tv_claim_rate: TextView = view.findViewById(R.id.edit_tv_claim_rate)
        val edt_start_km: EditText = view.findViewById(R.id.edit_edt_start_km)
        val edt_stop_km: EditText = view.findViewById(R.id.edit_edt_stop_km)
        val tv_total_km: TextView = view.findViewById(R.id.edit_tv_total_km)
        val tv_claim_amt: TextView = view.findViewById(R.id.edit_tv_claim_amt)
        val edt_exp_note: EditText = view.findViewById(R.id.edt_exp_note)
        val rel_expenseee: RelativeLayout = view.findViewById(R.id.edit_rel_expenseee)
        val edit_card_sub: CardView = view.findViewById(R.id.edit_card_sub)
        val listView_attach_edit: ListView = view.findViewById(R.id.listView_attach_edit)
        val listView_attach_edit_upload: ListView = view.findViewById(R.id.listView_attach_edit_upload)
        val img_add_attachment_edit: ImageView = view.findViewById(R.id.img_add_attachment_edit)
        val textview5: CardView = view.findViewById(R.id.textview5)

        getexpensetypelist2(edit_edtexpensetype)

        edit_edtexpensetype.setText(data!!.expensesTypeName!!)
        tvFrom111.text = data!!.date!!

        if (data.allowanceTypeId!!.equals(1)){
            rel_expenseee.visibility = View.VISIBLE
        }else{
            rel_expenseee.visibility = View.GONE
        }
        edt_start_km.setText(data!!.startKm)
        edt_stop_km.setText(data!!.stopKm)
        tv_total_km.setText(data!!.totalKm)
        tv_claim_rate.text = data!!.rate.toString()
        tv_claim_amt.text = data!!.claimAmount.toString()
        edt_exp_note.setText(data!!.note)

        edit_expensetype_id = data.expensesType.toString()
        edit_start_reading = data.startKm.toString()
        edit_end_reading = data.stopKm.toString()
        edit_total_reading = data.totalKm.toString()
        edit_claim_amt = data.claimAmount.toString()
        edit_expensetype_rate = data.rate.toString()

        if (data.expenseImage.size > 0){
            listView_attach_edit.visibility = View.VISIBLE
            val adapter = ExpenseImagePathEditAdapter(activityLocal,data.expenseImage,this,data.imageId)
            listView_attach_edit.adapter = adapter
        }else{
            listView_attach_edit.visibility = View.GONE
        }

        edit_edtexpensetype.setOnTouchListener { view, motionEvent ->
            edit_edtexpensetype.showDropDown()
            false
        }

        edit_edtexpensetype.setOnItemClickListener { adapterView, view, position, id ->

            expensetypelist.let {
                edit_expensetype_id = it[position].id.toString()
                edit_expensetype_rate = it[position].rate.toString()
                edit_expensetype_allow_typeid = it[position].allowanceTypeId.toString()

                if (edit_expensetype_allow_typeid.equals("1")){
                    rel_expenseee.visibility = View.VISIBLE
                    tv_claim_rate.text = edit_expensetype_rate
                    tv_claim_amt.isEnabled = false
                    edt_start_km.setText("")
                    edt_stop_km.setText("")
                    tv_total_km.setText("")
                    tv_claim_amt.text = ""
                }else{
                    tv_claim_amt.isEnabled = true
                    rel_expenseee.visibility = View.GONE
                }
            }
        }
        edt_start_km.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                edit_start_reading = charSequence.toString()
                tv_total_km.text =""
                tv_claim_amt.text = ""
            }

            override fun afterTextChanged(editable: Editable?) {
                val checkTotalRunnable = Runnable {
                    editcheckTotal(tv_total_km,tv_claim_amt)
                }
                handler.removeCallbacks(checkTotalRunnable)
                handler.postDelayed(checkTotalRunnable, DELAY_MILLIS.toLong())
            }
        })

        edt_stop_km.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                edit_end_reading = charSequence.toString()
                tv_total_km.text =""
                tv_claim_amt.text = ""
            }

            override fun afterTextChanged(editable: Editable?) {
                val checkTotalRunnable = Runnable {
                    editcheckTotal(tv_total_km, tv_claim_amt)
                }
                handler.removeCallbacks(checkTotalRunnable)
                handler.postDelayed(checkTotalRunnable, DELAY_MILLIS.toLong())
            }
        })
        img_add_attachment_edit.setOnClickListener {
            selectImageedit()
        }
        textview5.setOnClickListener {
            if (edit_select_imagepath.size == 0){
                listView_attach_edit_upload.visibility = View.GONE
            }else{
                listView_attach_edit_upload.visibility = View.VISIBLE
                val adapter = ImagePathAdapteredit(activityLocal, edit_select_imagepath)
                listView_attach_edit_upload.adapter = adapter
            }
        }


        edit_card_sub.setOnClickListener {
//            println("listtttttt=="+edit_select_imagepath_remove)
            println("listtttttt=="+edit_select_imagepath)
            println("listtttttt=reee="+edit_select_imagepath_remove)

            if (expense_id.isNullOrEmpty()){
                response_message("Something went wrong")
            }else if (edit_expensetype_id.isNullOrEmpty()){
                response_message("Please select expense type")
            }else if (edt_exp_note.text.toString().isNullOrEmpty()){
                response_message("Please enter expense note")
            }else if (expensetype_allow_typeid.equals(1)){
                if (edit_start_reading.isNullOrEmpty()){
                    response_message("Please enter start reading")
                } else if (edit_end_reading.isNullOrEmpty()){
                    response_message("Please enter stop reading")
                }
                else{
                    println("DATTaaa=1="+expense_id+"<<"+edit_expensetype_id+"<<"+edit_start_reading+"<<"+
                    edit_end_reading+"<<"+edit_total_reading+"<<"+edit_total_reading+"<<"+edit_claim_amt+
                    "<<"+edit_select_imagepath+"<<"+edt_exp_note.text.toString()+"<<"+edit_select_imagepath_remove)
                    updateexpensedetail(expense_id,edit_expensetype_id,edit_start_reading,edit_end_reading,edit_total_reading,tv_claim_amt.text.toString(),
                    edt_exp_note.text.toString(),alertDialog,edit_select_imagepath,edit_select_imagepath_remove)
                }
            }else{
                println("DATTaaa=2="+expense_id+"<<"+edit_expensetype_id+"<<"+edit_start_reading+"<<"+
                        edit_end_reading+"<<"+edit_total_reading+"<<"+edit_claim_amt+
                        "<<"+edit_select_imagepath+"<<"+edt_exp_note.text.toString()+"<<"+edit_select_imagepath_remove)
                updateexpensedetail(expense_id, edit_expensetype_id, edit_start_reading, edit_end_reading, edit_total_reading,
                    tv_claim_amt.text.toString(), edt_exp_note.text.toString(), alertDialog, edit_select_imagepath, edit_select_imagepath_remove
                )
            }

        }


        img_close_edit.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()
    }


/*
    private fun downloadPdf(context: Activity, pdfUrl: String?) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Attachment")
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        response_message("Successfully downloaded")
    }
*/



    private fun editcheckTotal(tv_total_km: TextView, tv_claim_amt: TextView) {
        if (edit_start_reading.isNotEmpty() && edit_end_reading.isNotEmpty()) {
            try {
                val startReadingValue = edit_start_reading.toDouble()
                val endReadingValue = edit_end_reading.toDouble()

                if (endReadingValue < startReadingValue) {
                    Toast.makeText(requireContext(), "End reading should be greater than start reading", Toast.LENGTH_SHORT).show()
                } else {
                    edit_total_reading = (endReadingValue - startReadingValue).toString()
                    tv_total_km.text = edit_total_reading
                    println("ABHIIIIIII=="+startReadingValue+"<<"+endReadingValue)
                    println("ABHIIIIIII=3=="+edit_start_reading+"<<"+edit_end_reading+"<<"+edit_total_reading+"<<"+edit_expensetype_rate)

                    if (edit_expensetype_rate.isNotEmpty()) {
                        edit_claim_amt = (edit_total_reading.toDouble() * edit_expensetype_rate.toDouble()).toString()
                        tv_claim_amt.text = edit_claim_amt
                    }
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Invalid input. Please enter valid numeric values", Toast.LENGTH_SHORT).show()
                println("Error: " + e.message)
            }
        }
    }



    private fun getexpensetypelist2(edit_edtexpensetype: AutoCompleteTextView) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["payroll id"] = StaticSharedpreference.getInfo(Constant.PAYROLL_ID, activityLocal).toString()


        ApiClient.getexpensetypelist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<ExpenseTypeModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<ExpenseTypeModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            val expenseTypeList = response.body()?.data ?: emptyList()
                            val disName = expenseTypeList.map { it.name }.toTypedArray()
                            expensetypelist = response.body()!!.data

                            val adapter = ArrayAdapter(
                                activityLocal, android.R.layout.simple_list_item_1, disName).apply {
                                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            }
                            edit_edtexpensetype.setAdapter(adapter)
                        } else {
                            try {
                                val jsonObject = JSONObject(response.errorBody()?.string())
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopupDialog(data: UserExpenseDetailModel.Data?) {

        val builder = AlertDialog.Builder(activityLocal)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_expense_detail_layout, null)
        builder.setCancelable(false)

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
        val listView_attach_view: ListView = view.findViewById(R.id.listView_attach_view)

        tv_expense_num.text = "#"+data!!.id.toString()
        tv_expense_status.text = data.status.toString()
        tv_expense_date.text = data.date.toString()
        tv_expense_type.text = data.expensesTypeName.toString()
        tv_claim_amt.text = data.claimAmount.toString()
        tv_approve_amt.text = data.approveAmount.toString()
        tv_note_msg.text = data.note.toString()

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cardFrom_list -> {
                Utilities.datePicker(tvFrom_list, tvTo_list.text.toString(), "", true, activityLocal)
            }
            R.id.cardTo_list -> {
                Utilities.datePicker(tvTo_list, "", tvFrom_list.text.toString(), false, activityLocal)
            }
            R.id.cardSearch_list -> {
                if (tvFrom_list.text.toString().isNullOrEmpty()){
                    Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                }else if (tvTo_list.text.toString().isNullOrEmpty()){
                    Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                }else{
                    val convertedDate = convertDateFormats(tvFrom_list.text.toString(),tvTo_list.text.toString())
                    start_date = convertedDate.first
                    end_date = convertedDate.second
                    println("from=="+start_date+"To="+end_date)
                    page = 1
                    if (start_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                    }else if (end_date.equals("")){
                        Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                    }else{
                        getexpenselist(
                            page,
                            start_date,
                            end_date,
                            selectedtype_id,
                            selectedstatus_id
                        )
                    }
                }
            }

            R.id.img_create -> {
                selected_image_path = ""
                rel_main_expense.visibility = View.GONE
                rel_create_expense.visibility = View.VISIBLE
                tvTitle_expense.text = "Add New Expense"
                flag = "create"
                getexpensetypelist()

                edtexpensetype.setOnTouchListener { view, motionEvent ->
                    edtexpensetype.showDropDown()
                    false
                }

                edtexpensetype.setOnItemClickListener { adapterView, view, position, id ->

                    expensetypelist.let {
                        expensetype_id = it[position].id.toString()
                        expensetype_rate = it[position].rate.toString()
                        expensetype_allow_typeid = it[position].allowanceTypeId.toString()

                        if (expensetype_allow_typeid.equals("1")){
                            rel_expenseee.visibility = View.VISIBLE
                            tv_claim_rate.text = expensetype_rate
                            tv_claim_amt.isEnabled = false
                        }else{
                            rel_expenseee.visibility = View.GONE
                            tv_claim_amt.isEnabled = true
                            tv_claim_rate.text = ""
                        }


                    }
                }
                edt_start_km.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                        start_reading = charSequence.toString()
                        tv_total_km.text =""
                    }

                    override fun afterTextChanged(editable: Editable?) {
                        val checkTotalRunnable = Runnable {
                            checkTotal()
                        }
                        handler.removeCallbacks(checkTotalRunnable)
                        handler.postDelayed(checkTotalRunnable, DELAY_MILLIS.toLong())
                    }
                })

                edt_stop_km.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                        end_reading = charSequence.toString()
                        tv_total_km.text =""
                    }

                    override fun afterTextChanged(editable: Editable?) {
                        val checkTotalRunnable = Runnable {
                            checkTotal()
                        }
                        handler.removeCallbacks(checkTotalRunnable)
                        handler.postDelayed(checkTotalRunnable, DELAY_MILLIS.toLong())
                    }
                })


                cardFromm.setOnClickListener {
                    Utilities.datePickercurrentlastday(tvFrom111, activityLocal)
                }


                img_add_attachment.setOnClickListener {
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_AUDIO,
                                ),
                            2
                        )
                    }*/
                    selectImage()
                }

                card_sub.setOnClickListener {
                    println("PATHHHHHHHH=="+selected_image_path2)
                    val inputDate = tvFrom111.text.toString()
                    val outputDate = Utilities.convertDateFormat(inputDate)
                    println("ABHIII=="+selected_image_path)
                    if (expensetype_id.isNullOrEmpty()) {
                    response_message("Please Select Expense Type")
                    } else if (outputDate.isNullOrEmpty()) {
                    response_message("Please Select Date")
                    } else if (edt_exp_note.text.toString().isNullOrEmpty()) {
                    response_message("Please Expense note")
                    }else if (expensetype_allow_typeid.equals("1")){
                        if (start_reading.isNullOrEmpty()){
                            response_message("Please Enter Start Reading")
                        } else if (end_reading.isNullOrEmpty()){
                            response_message("Please Enter Stop Reading")
                        }else{
                            submitexpense(expensetype_id,outputDate,start_reading,end_reading,total_reading,claim_amt,selected_image_path2)
                        }
                    }else if (tv_claim_amt.text.isNullOrEmpty()) {
                        response_message("please Enter Claim Amount")
                    }else if (tv_claim_amt.text.toString() == "0") {
                        response_message("Please Enter Valid Amount")
                    }else{
                        submitexpense(expensetype_id,outputDate,start_reading,end_reading,total_reading,claim_amt,selected_image_path2)
                    }
                }

            }

        }
    }
    private fun getexpensedetail(id: String, status: String, date: String) {
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

                            datecheck(date)

                            if (date_expire.equals("true") && status.equals("Pending")){
                                showeditdialog(response.body()!!.data)
                            }else{
                                showPopupDialog(response.body()!!.data)
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
                                println("Errrororo="+e.toString())
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }

    private fun datecheck(inputDateStr: String) {
        println("datedate="+inputDateStr)

        var dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        try {
            val inputDate = dateFormat.parse(inputDateStr)
            val currentDate = Date()

            val inputCalendar = Calendar.getInstance().apply {
                time = inputDate
            }

            val currentCalendar = Calendar.getInstance().apply {
                time = currentDate
            }

            clearTimeFields(inputCalendar)
            clearTimeFields(currentCalendar)

            val yesterdayCalendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -1)
                clearTimeFields(this)
            }

            if (inputCalendar == currentCalendar || inputCalendar == yesterdayCalendar) {
//                response_message("Same date!")
                date_expire = "true"
            } else {
                date_expire = "false"
//                response_message("Different dates.")
            }

        } catch (e: Exception) {
            response_message("Error occurred: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun clearTimeFields(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    private fun updateexpensedetail(
        expense_id: String,
        edit_expensetype_id: String,
        edit_start_reading: String,
        edit_end_reading: String,
        edit_total_reading: String,
        edit_claim_amt: String,
        note: String,
        alertDialog: AlertDialog,
        edit_select_imagepath: ArrayList<String>,
        edit_select_imagepath_remove: ArrayList<String>
    ) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["expense_id"] = expense_id
        queryParams["expenses_type"] = edit_expensetype_id
        queryParams["start_km"] = edit_start_reading
        queryParams["stop_km"] = edit_end_reading
        queryParams["total_km"] = edit_total_reading
        queryParams["claim_amount"] = edit_claim_amt
        queryParams["note"] = note

        val multipartprofile = createMultipartParts(edit_select_imagepath)

        ApiClient.expenseupdate(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,multipartprofile,edit_select_imagepath_remove,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            response_message(response.body()!!.message)
                            this@ExpenseFragment.expense_id = ""
                            this@ExpenseFragment.edit_expensetype_id = ""
                            this@ExpenseFragment.edit_start_reading = ""
                            this@ExpenseFragment.edit_end_reading = ""
                            this@ExpenseFragment.edit_total_reading = ""
                            this@ExpenseFragment.edit_claim_amt = ""
                            this@ExpenseFragment.edit_select_imagepath.clear()
                            this@ExpenseFragment.edit_select_imagepath_remove.clear()
                            alertDialog.dismiss()
                            page = 1
                            getexpenselist(
                                page,
                                start_date,
                                end_date,
                                selectedtype_id,
                                selectedstatus_id
                            )
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



    fun submitexpense(
        expensetypeId: String,
        outputDate: String,
        startReading: String,
        endReading: String,
        totalReading: String,
        claimAmt: String,
        selectedImagePath: ArrayList<String>
    ) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["expenses_type"] = expensetypeId
        queryParams["date"] = outputDate
        queryParams["start_km"] = startReading
        queryParams["stop_km"] = endReading
        queryParams["total_km"] = totalReading
        queryParams["claim_amount"] = tv_claim_amt.text.toString()
        queryParams["note"] = edt_exp_note.text.toString()

        println("ABHIIIIII=" + expensetypeId + "<<" + outputDate + ">>" + startReading + "<<" + endReading + "<<" + totalReading + ">>" +
                    tv_claim_amt.text.toString() + "<<" + edt_exp_note.text.toString() + "<<" + selectedImagePath)

        val multipartParts = createMultipartParts(selectedImagePath)

        ApiClient.expensesubmit(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams, multipartParts,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            response_message(response.body()!!.message)
                            resetFields()
                        } else {
                            handleErrorResponse(response)
                        }
                    } else {
                        handleApiCallFailure(errorMessage)
                    }
                }
            })
    }

    fun createMultipartParts(selectedImagePaths: ArrayList<String>): List<MultipartBody.Part> {
        val multipartParts = mutableListOf<MultipartBody.Part>()

        val emptyRequestBody: RequestBody = RequestBody.create(null, ByteArray(0))
        val emptyPart = MultipartBody.Part.createFormData("expense_file[]", "", emptyRequestBody)
        multipartParts.add(emptyPart)

        for (selectedImagePath in selectedImagePaths) {
            if (selectedImagePath.isNotEmpty()) {
                val file = File(selectedImagePath)
                val reqbodyFileD: RequestBody = RequestBody.create(MediaType.parse("*/*"), file)
                val fileName = "expense_file[]"
                val part = MultipartBody.Part.createFormData(fileName, file.name, reqbodyFileD)
                multipartParts.add(part)
            }
        }

        return multipartParts
    }


    private fun resetFields() {
        expensetype_id = ""
        start_reading = ""
        end_reading = ""
        total_reading = ""
        claim_amt = ""
        selected_image_path2.clear()
        edtexpensetype.setText("")
        tvFrom111.text = ""
        rel_expenseee.visibility = View.GONE
        tv_claim_rate.text = ""
        edt_start_km.setText("")
        edt_stop_km.setText("")
        tv_total_km.text = ""
        tv_claim_amt.text = ""
        edt_exp_note.setText("")
        listView_attach.visibility = View.GONE
    }

    private fun handleErrorResponse(response: Response<AttendanceSubmitModel>) {
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(response.errorBody()!!.string())
            DialogClass.alertDialog(
                jsonObject.getString("status"),
                jsonObject.getString("message"),
                activityLocal, false
            )
        } catch (e: Exception) {
            println("Errrororo=" + e.toString())
            e.printStackTrace()
        }
    }

    private fun handleApiCallFailure(errorMessage: String?) {
        println("ERRRRRRRRRRR=$errorMessage")
    }


    fun response_message(message: String?) {
        Toast.makeText(activityLocal, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkTotal() {
        if (start_reading.isNotEmpty() && end_reading.isNotEmpty()) {
            val startReadingValue = start_reading.toDouble()
            val endReadingValue = end_reading.toDouble()

            if (endReadingValue < startReadingValue) {
                Toast.makeText(requireContext(), "End reading should be greater than start reading", Toast.LENGTH_SHORT).show()
            } else {
                total_reading = (endReadingValue - startReadingValue).toString()
                tv_total_km.text = total_reading.toString()
                if (total_reading.isNotEmpty()){
                    claim_amt = (total_reading.toDouble() * expensetype_rate.toDouble()).toString()
                    tv_claim_amt.text = claim_amt
                }
            }
        }

    }

    private fun getexpensetypelist() {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["payroll id"] = StaticSharedpreference.getInfo(Constant.PAYROLL_ID, activityLocal).toString()
//        println("ABHIIIIII==="+StaticSharedpreference.getInfo(Constant.PAYROLL_ID, activityLocal))

        ApiClient.getexpensetypelist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<ExpenseTypeModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<ExpenseTypeModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            val expenseTypeList = response.body()?.data ?: emptyList()
                            val disName = expenseTypeList.map { it.name }.toTypedArray()
                            expensetypelist = response.body()!!.data

                            val adapter = ArrayAdapter(
                                activityLocal, android.R.layout.simple_list_item_1, disName).apply {
                                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            }
                            edtexpensetype.setAdapter(adapter)
                        } else {
                            try {
                                val jsonObject = JSONObject(response.errorBody()?.string())
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val requiredPermissions = listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO)
                handlePermissionsResult(permissions, grantResults, requiredPermissions,requestCode)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val requiredPermissions = listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                handlePermissionsResult(permissions, grantResults, requiredPermissions,requestCode)
            }
            else -> {
                val requiredPermissions = listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                handlePermissionsResult(permissions, grantResults, requiredPermissions,requestCode)
            }
        }

    }

    private fun handlePermissionsResult(permissions: Array<String>, grantResults: IntArray, requiredPermissions: List<String>,requestCode :Int) {
        val perms = HashMap<String, Int>().apply {
            // Initialize with PERMISSION_GRANTED
            requiredPermissions.forEach { this[it] = PackageManager.PERMISSION_GRANTED }
        }

        for (i in permissions.indices) {
            perms[permissions[i]] = grantResults[i]
        }

        val allPermissionsGranted = requiredPermissions.all { perms[it] == PackageManager.PERMISSION_GRANTED }

        if (allPermissionsGranted) {

            if (requestCode == 2){
                selectImage()
            }else if (requestCode == 1){
            }

        } else {
            Toast.makeText(activityLocal, "Some Permission is Denied", Toast.LENGTH_SHORT).show()
        }
    }


    private fun selectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(activityLocal)
        builder.setTitle("Select!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Camera" -> {
                    //openCamera()
                    //launchCamera()
                    var intent = Intent(activityLocal, CameraCustomActivity::class.java)
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

        println("codeeeeee==$requestCode<<$INTENTCAMERA<<$INTENTGALLERY<<$INTENTCAMERAEDIT<<$INTENTGALLERYEDIT")

        when {
            requestCode == INTENTCAMERA && resultCode == Activity.RESULT_OK -> {
                try {
                    val path: File = data!!.getSerializableExtra("image") as File
                    Log.v("akram", "path ${StoreCustomerActivity.arrList.size}")
                    cameraFile = path
                    imageFile = path.path
                    base64 = ""

                    lifecycleScope.launch {
                        try {
                            val compressedImageFile = withContext(Dispatchers.IO) {
                                Compressor.compress(activityLocal, path) {
                                    default() // apply default compression options
                                    resolution(200, 200)  // sets max width & height
                                    quality(90)           // sets compression quality
                                }
                            }

                            // Update UI on main thread
                            selected_image_path2.add(compressedImageFile.absolutePath)
                            listView_attach.visibility =
                                if (selected_image_path2.isEmpty()) View.GONE else View.VISIBLE
                            updateListView()

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                } catch (e: Exception) {
                    Log.v("akram", "try inside")
                    e.printStackTrace()
                }
            }

            requestCode == INTENTGALLERY && resultCode == Activity.RESULT_OK -> {
                val photoURI = data?.data
                val imagePath = getDriveFile(photoURI)
                selected_image_path2.add(imagePath)
                listView_attach.visibility =
                    if (selected_image_path2.isEmpty()) View.GONE else View.VISIBLE
                updateListView()
            }

            requestCode == INTENTCAMERAEDIT && resultCode == Activity.RESULT_OK -> {
                try {
                    val path: File = data!!.getSerializableExtra("image") as File
                    Log.v("akram", "path ${StoreCustomerActivity.arrList.size}")
                    cameraFile = path
                    imageFile = path.path
                    base64 = ""

                    lifecycleScope.launch {
                        try {
                            val compressedImageFile = withContext(Dispatchers.IO) {
                                Compressor.compress(activityLocal, path) {
                                    default() // apply default compression options
                                    resolution(200, 200)  // sets max width & height
                                    quality(90)
                                }
                            }

                            edit_select_imagepath.add(compressedImageFile.absolutePath)

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                } catch (e: Exception) {
                    Log.v("akram", "try inside")
                    e.printStackTrace()
                }
            }

            requestCode == INTENTGALLERYEDIT && resultCode == Activity.RESULT_OK -> {
                val photoURI = data?.data
                val editImagePath = getDriveFileedit(photoURI)
                edit_select_imagepath.add(editImagePath)
            }
        }
    }

    private fun isImageFile(file: File): Boolean {
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        return (mimeType?.startsWith("image/") ?: false) ||
                file.extension.equals("pdf", ignoreCase = true) ||
                file.extension.equals("doc", ignoreCase = true) ||
                file.extension.equals("docx", ignoreCase = true)
    }
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_OR_DOCUMENT_REQUEST && resultCode == Activity.RESULT_OK) {

            var photoURI = data?.data
            val uriString: String = photoURI.toString()
            var pdfName: String? = null

            val imagePath = getDriveFile(photoURI)

            selected_image_path2.add(imagePath)
            if (selected_image_path2.size == 0){
                listView_attach.visibility = View.GONE
            }else{
                listView_attach.visibility = View.VISIBLE
                updateListView()
            }
        } else{
            var photoURI = data?.data
            var edit_image_paths = getDriveFileedit(photoURI)
            edit_select_imagepath.add(edit_image_paths)
        }

}
*/


    class ImagePathAdapteredit(var activityLocal: Activity, var editSelectImagepath: ArrayList<String>) :
        ArrayAdapter<String>(activityLocal, R.layout.item_image_path_expense, editSelectImagepath) {

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
                editSelectImagepath.removeAt(position)
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



    private fun getDriveFileedit(photoURI: Uri?): String {
        val returnUri: Uri = photoURI!!
        val returnCursor: Cursor =
            requireContext().getContentResolver().query(returnUri, null, null, null, null)!!

        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(activityLocal.getCacheDir(), name)
        try {
            val inputStream: InputStream = photoURI?.let {
                activityLocal.getContentResolver().openInputStream(
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


    private fun updateListView() {
        val adapter = ImagePathAdapter(activityLocal, selected_image_path2)
        listView_attach.adapter = adapter
    }

    class ImagePathAdapter(var activityLocal: Activity, var selected_image_path2: ArrayList<String>) :
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
            requireContext().getContentResolver().query(returnUri, null, null, null, null)!!

        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(activityLocal.getCacheDir(), name)
        try {
            val inputStream: InputStream = photoURI?.let {
                activityLocal.getContentResolver().openInputStream(
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

    override fun onClickEmail1(
        imageViewFile: ImageView,
        textViewPath: TextView,
        position: Int,
        selected_image_path2: ArrayList<String>
    ) {
        println("selected_image_path2==$edit_select_imagepath")
//        edit_image_path = ""
//        imageViewFile.setOnClickListener {
//        position_edit = position
//        selectImageedit()
//        }
/*
        if (edit_image_path.isNullOrEmpty()){
        }else{
            edit_select_imagepath[position] = edit_image_path.toString()
        }
*/


/*
        textViewPath.setOnClickListener {
            edit_select_imagepath[position] = edit_image_path.toString()
            println("Abjooooooo=po=$position")
            println("Abjooooooo=$edit_image_path")
            println("Abjooooooo=old=$selected_image_path2")
            println("Abjooooooo=final=$edit_select_imagepath")
        }
*/
    }

    override fun onClickEmail2(
        selectedImagePath2: Int
    ) {
        edit_select_imagepath_remove.add(selectedImagePath2.toString())
        println("REMOVEEEEE=bbb="+selectedImagePath2)
        println("REMOVEEEEE=="+edit_select_imagepath_remove)
    }

    private fun selectImageedit() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(activityLocal)
        builder.setTitle("Select!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Camera" -> {
                    var intent = Intent(activityLocal, CameraCustomActivity::class.java)
                    intent.putExtra("camera", "1")
                    startActivityForResult(intent, INTENTCAMERAEDIT)
                }
                items[item] == "Gallery" -> {
                    openGalleryedit()
                }
                items[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openGalleryedit() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, getString(R.string.please_select_image)), INTENTGALLERYEDIT)
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



