package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstune.searchablemultiselectspinner.SearchableItem
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.LeadActivity
import com.exp.clonefieldkonnect.activity.LoginActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.activity.ProductActivity
import com.exp.clonefieldkonnect.activity.StoreCustomerActivity
import com.exp.clonefieldkonnect.adapter.UserActivityDetailAdapterNew
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.Branches
import com.exp.clonefieldkonnect.model.Divisions
import com.exp.clonefieldkonnect.model.MonthModel
import com.exp.clonefieldkonnect.model.UserActivityDetailModel
import com.exp.clonefieldkonnect.model.UserActivityListModel
import com.exp.clonefieldkonnect.model.UserDataModel
import com.exp.import.Utilities
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class DashBoardNewDesingFragment : Fragment(), View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var activityLocal: Activity
    private lateinit var pieChart: PieChart
    private lateinit var edtSearch: AutoCompleteTextView
    private lateinit var edtyearly: AutoCompleteTextView
    private lateinit var edtmonth: AutoCompleteTextView
    private lateinit var edtbranch: AutoCompleteTextView
    private lateinit var edtdivision: AutoCompleteTextView
    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "50"
    private var page_count: String = ""
    private var selecteduser_id: String = ""
    private var selectedbranch_id: String = ""
    private var selecteddivision_id: String = ""
    private var selected_year: String = ""
    private var selected_month: String = ""
    private var searchbranch: ArrayList<String> = ArrayList()
    private var selectedmonth : ArrayList<String> = ArrayList()
    private var monthlist: ArrayList<MonthModel> = ArrayList()
    private var yearlist: ArrayList<String> = ArrayList()
    private var useractivityuser: ArrayList<UserActivityListModel.Users> = ArrayList()
    private var userbranchlist: ArrayList<Branches> = ArrayList()
    private var userdivisionlist: ArrayList<Divisions> = ArrayList()
    private var userbranchname: ArrayList<String> = ArrayList()
    private var userbranchid: ArrayList<String> = ArrayList()
    private var userdivisionname: ArrayList<String> = ArrayList()
    private var userdivisionid: ArrayList<String> = ArrayList()
    private var useractivityusername: ArrayList<String> = ArrayList()
    private var useractivityuserid: ArrayList<String> = ArrayList()
    private var items: MutableList<SearchableItem> = ArrayList()
    private lateinit var cardFrom: LinearLayout
    private lateinit var cardTo: LinearLayout
    private lateinit var tvFrom: TextView
    private lateinit var tvTo: TextView
    private lateinit var cardSearch: RelativeLayout
    private lateinit var textTarget: TextView
    private lateinit var textAchievement: TextView
    private lateinit var textOrderValue: TextView
    private lateinit var textOrderQty: TextView
    private lateinit var texttotalcoustomerValue: TextView
    private lateinit var recyclerView_user_detail: RecyclerView
    private lateinit var linearlayout_tour_plan: LinearLayout
    private lateinit var linearlayout_new_coustomerr: LinearLayout
    private lateinit var linearlayout_coustomer_visit: LinearLayout
    private lateinit var linearlayout_add_order: LinearLayout
    private lateinit var linearlayout_expenses: LinearLayout
    private lateinit var linearlayout_useractivity: LinearLayout
    private var start_date: String = ""
    var punchInDetails: PunchInDetails? = null
    private var end_date: String = ""
    var Date :String = ""
    var useractivitydetail: java.util.ArrayList<UserActivityDetailModel> = arrayListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_dash_board_new_desing, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        pieChart = rootView.findViewById(R.id.pieChart)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        textTarget = rootView.findViewById(R.id.textTarget)
        textAchievement = rootView.findViewById(R.id.textAchievement)
        textOrderValue = rootView.findViewById(R.id.textOrderValue)
        textOrderQty = rootView.findViewById(R.id.textOrderQty)
        texttotalcoustomerValue = rootView.findViewById(R.id.texttotalcoustomerValue)
        recyclerView_user_detail = rootView.findViewById(R.id.recyclerView_user_detail)
        linearlayout_tour_plan = rootView.findViewById(R.id.linearlayout_tour_plan)
        linearlayout_new_coustomerr = rootView.findViewById(R.id.linearlayout_new_coustomerr)
        linearlayout_coustomer_visit = rootView.findViewById(R.id.linearlayout_coustomer_visit)
        linearlayout_add_order = rootView.findViewById(R.id.linearlayout_add_order)
        linearlayout_expenses = rootView.findViewById(R.id.linearlayout_expenses)
        linearlayout_useractivity = rootView.findViewById(R.id.linearlayout_useractivity)
        edtyearly = rootView.findViewById(R.id.edtyearly)
        edtmonth = rootView.findViewById(R.id.edtmonth)
        edtbranch = rootView.findViewById(R.id.edtbranch)
        edtdivision = rootView.findViewById(R.id.edtdivision)


        val monthAbbreviations = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        val monthTitles = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val months = monthAbbreviations.zip(monthTitles) { id, title ->
            MonthModel(title, id.lowercase())
        }

        val yearList = getYearList()

        for (item in months){
            monthlist.add(item)
        }
        for (item in yearList){
            yearlist.add(item.toString())
        }


        println("yearListyearList=="+yearlist+"<<"+monthlist)
        
        
        linearlayout_new_coustomerr.setOnClickListener {
            startActivity(Intent(activityLocal, StoreCustomerActivity::class.java))
        }
        linearlayout_add_order.setOnClickListener {
            val intent = Intent(activityLocal, ProductActivity::class.java)
            intent.putExtra("checkin", "n")
            intent.putExtra("beatScheduleId", "")
            startActivity(intent)
        }
        linearlayout_coustomer_visit.setOnClickListener {
            val intent = Intent(activityLocal, MainActivity::class.java)
            intent.putExtra("CustomerVisit", "CustomerVisit")
            startActivity(intent)
        }
        linearlayout_tour_plan.setOnClickListener {
            val intent = Intent(activityLocal, MainActivity::class.java)
            intent.putExtra("CustomerVisit", "Tour Plan")
            startActivity(intent)
        }
        linearlayout_expenses.setOnClickListener {
            val intent = Intent(activityLocal, MainActivity::class.java)
            intent.putExtra("CustomerVisit", "Expense")
            startActivity(intent)
        }
        linearlayout_useractivity.setOnClickListener {
            startActivity(Intent(activityLocal, LeadActivity::class.java))
        }

        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {

        }
         Date = currentDate.toString()
        getuseractivity(page, "", "", searchbranch, "")
        selecteduser_id = StaticSharedpreference.getInfo(Constant.USERID, requireContext()).toString()
        callUserDataAPI(selecteduser_id, start_date, end_date, selected_year, selectedbranch_id, selecteddivision_id, selectedmonth)
        getuseractivityData(selecteduser_id,Date)
        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)
        edtyearly.setOnClickListener(this)
        edtmonth.setOnClickListener(this)
        dashboard()
    }

    private fun setupyear() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = yearlist.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Year")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtyearly.setText("")
            selected_year = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = yearlist.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentName = yearlist[selectedPosition].toString()

                edtyearly.setText(selectedParentName)
                selected_year = selectedParentName

                callUserDataAPI(selecteduser_id, start_date, end_date, selected_year, selectedbranch_id, selecteddivision_id, selectedmonth)

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun getYearList(): List<Int> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return listOf(currentYear - 1, currentYear, currentYear + 1)
    }


    private fun dashboard(filterDate: String = "") {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["filter_date"] = filterDate
        ApiClient.dashboard(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            StaticSharedpreference.saveInfo(Constant.IS_PUNCH_IN, response.body()!!.get("data").asJsonObject.get("punchin").asBoolean.toString(), activityLocal)
                            StaticSharedpreference.saveInfo(Constant.IS_PUNCH_OUT, response.body()!!.get("data").asJsonObject.get("punchout").asBoolean.toString(), activityLocal)
                            if (punchInDetails != null) {
                                punchInDetails!!.onPunchInDetails(response.body()!!.get("data").asJsonObject.get("punchin").asBoolean,
                                    response.body()!!.get("data").asJsonObject.get("punchout").asBoolean,
                                    response.body()!!.get("data").asJsonObject.get("working_type").asString,
                                    response.body()!!.get("data").asJsonObject.get("punchin_flag").asBoolean,
                                    response.body()!!.get("data").asJsonObject.get("punchin_id").asString,
                                )
                            }
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message"), activityLocal, false)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    private fun getuseractivityData(selecteduserId: String, currentDate: String) {

        val queryParams = HashMap<String, String>()

        queryParams["user_id"] = selecteduserId.toString()
        queryParams["date"] = currentDate
        ApiClient.getuseractivitydetail(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            useractivitydetail.clear()
                            val gson = Gson()
                            val listType = object : TypeToken<java.util.ArrayList<UserActivityDetailModel>>() {}.type
                            val allDetails: ArrayList<UserActivityDetailModel> = gson.fromJson(response.body()!!.get("data").asJsonArray, listType)
                            val filteredDetails = allDetails.filter { it.title != "Customer Edit" }
                            useractivitydetail.addAll(filteredDetails)

                            println("useractivitydetail="+useractivitydetail)
                            println("useractivitydetail="+useractivitydetail.size)
                            setuprecycler(useractivitydetail)
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        activityLocal, false
                                    )
                                }
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
        val useractivity = UserActivityDetailAdapterNew(activityLocal, useractivitydetail)
        recyclerView_user_detail.adapter = useractivity
    }
    private fun getuseractivity(page: Int, start_date: String, end_date: String, searchbranch: ArrayList<String>, selecteduser_id: String) {
        isLoading = true
        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        val dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>().apply {
            put("pageSize", "")
            put("page", "")
            put("start_date", start_date)
            put("end_date", end_date)
            put("search_name", selecteduser_id)
        }

        ApiClient.getUserActivity(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams, searchbranch,
            object : APIResultLitener<UserActivityListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<UserActivityListModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""

                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            useractivityuser.clear()
                            useractivityuser.addAll(response.body()!!.users)

                            for (item in useractivityuser) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!useractivityusername.contains(name)) {
                                    useractivityusername.add(name)
                                    useractivityuserid.add(id)
                                }
                            }
                            edtSearch.setOnClickListener {
                                spinneruser()
                            }
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        activityLocal, false
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    } else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun callUserDataAPI(
        selecteduserId: String,
        start_date: String,
        end_date: String,
        selected_year: String,
        selectedbranch_id: String,
        selecteddivision_id: String,
        selectedmonth: ArrayList<String>
    ) {
        isLoading = true
        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()
        queryParams["user_id"] = selecteduserId
        queryParams["start_date"] = start_date
        queryParams["end_date"] = end_date
        queryParams["tayear"] = selected_year
        queryParams["branch_id"] = selectedbranch_id
        queryParams["division_id"] = selecteddivision_id

        ApiClient.getUserData(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,selectedmonth,
            object : APIResultLitener<UserDataModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<UserDataModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            val userData = response.body()?.data
                            println("responseresponse ${userData}")

                            userbranchlist = response.body()!!.Branches
                            userdivisionlist = response.body()!!.divisions

                            StaticSharedpreference.saveBoolean(Constant.todayBeatSchedule, response.body()!!.data!!.todayBeatSchedule!!,activityLocal)
                            StaticSharedpreference.saveBoolean(Constant.beatUser, response.body()!!.data!!.beatUser!!, activityLocal)

                            if (userData?.target != ""){
                                textTarget.text = "Target\n"+(userData?.target + "(Lac)")
                            }else{
                                textTarget.text = "Target\n0 (Lac)"
                            }
                            if (userData?.achievement != ""){
                                textAchievement.text = "Achievement\n"+(userData?.achievement + "(Lac)")
                            }else{
                                textAchievement.text = "Achievement\n0 (Lac)"
                            }

                            if (userData?.orderValue != ""){
                                textOrderValue.text = userData?.orderValue +" (Lac)"
                            }else{
                                textOrderValue.text = "0 (Lac)"
                            }
                            for (item in userbranchlist) {
                                val name = item.branchName.toString()
                                val id = item.id.toString()
                                if (!userbranchname.contains(name)) {
                                    userbranchname.add(name)
                                    userbranchid.add(id)
                                }
                            }
                            for (item in userdivisionlist) {
                                val name = item.divisionName.toString()
                                val id = item.id.toString()
                                if (!userdivisionname.contains(name)) {
                                    userdivisionname.add(name)
                                    userdivisionid.add(id)
                                }
                            }

                            edtbranch.setOnClickListener {
                                spinnerbranch()
                            }
                            edtdivision.setOnClickListener {
                                spinnerdivision()
                            }



                            textOrderQty.text = userData?.orderQty?.takeIf { it.isNotEmpty() } ?: "0"
                            texttotalcoustomerValue.text = userData?.customerVisit?.takeIf { it.isNotEmpty() } ?: "0"
                            setUpSelectionPieChart(userData?.targetPer, userData?.achivPer)
                        }
                    } else {
                        val jsonObject: JSONObject
                        try {
                            jsonObject = JSONObject(response?.errorBody()!!.string())
                            if (response.code() == 401){
                                Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                StaticSharedpreference.deleteSharedPreference(activityLocal)
                                startActivity(Intent(activityLocal, LoginActivity::class.java))
                                activityLocal.finishAffinity()
                                println("Erroorr=="+jsonObject.getString("message"))
                            }else{
                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal, false
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    isLoading = false
                }
            })
    }

    private fun spinnerdivision() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = userdivisionname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Division")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtdivision.setText("")
            selecteddivision_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = userdivisionname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = userdivisionid[selectedPosition].toString()
                val selectedParentName = userdivisionname[selectedPosition].toString()

                edtdivision.setText(selectedParentName)
                selecteddivision_id = selectedParentId

                println("Abhi=id=$selecteddivision_id")

                if (selecteddivision_id.isNotEmpty()){
                    edtSearch.setText("")
                    selecteduser_id = StaticSharedpreference.getInfo(Constant.USERID, requireContext()).toString()
                    page = 1
                    callUserDataAPI(
                        selecteduser_id,
                        start_date,
                        end_date,
                        selected_year,
                        selectedbranch_id,
                        selecteddivision_id,
                        selectedmonth
                    )
                }

                dialog.dismiss()
            }
        }

        dialog.show()    }


    private fun spinnerbranch() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = userbranchname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Branch")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtbranch.setText("")
            selectedbranch_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = userbranchname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = userbranchid[selectedPosition].toString()
                val selectedParentName = userbranchname[selectedPosition].toString()

                edtbranch.setText(selectedParentName)
                selectedbranch_id = selectedParentId

                println("Abhi=id=$selectedbranch_id")

                if (selectedbranch_id.isNotEmpty()){
                    edtSearch.setText("")
                    selecteduser_id = StaticSharedpreference.getInfo(Constant.USERID, requireContext()).toString()
                    page = 1
                    callUserDataAPI(
                        selecteduser_id,
                        start_date,
                        end_date,
                        selected_year,
                        selectedbranch_id,
                        selecteddivision_id,
                        selectedmonth
                    )
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

        val colorsArray = useractivityusername.map { it.toString() }.toTypedArray()
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
            val selectedPosition = useractivityusername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = useractivityuserid[selectedPosition].toString()
                val selectedParentName = useractivityusername[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentId

                getuseractivityData(selecteduser_id,Date)
                println("Abhi=id=$selecteduser_id")
                if (selecteduser_id.isNotEmpty()){
                    edtdivision.setText("")
                    edtbranch.setText("")
                    selectedbranch_id = ""
                    selecteddivision_id = ""
                    callUserDataAPI(
                        selecteduser_id,
                        start_date,
                        end_date,
                        selected_year,
                        selectedbranch_id,
                        selecteddivision_id,
                        selectedmonth
                    )
                }

                dialog.dismiss()
            }
        }

        dialog.show()
    }

private fun setUpSelectionPieChart(targetPer: String?, achivPer: String?) {
    val dataArray = ArrayList<PieEntry>()
    val colorSet = ArrayList<Int>()

    if (!targetPer.isNullOrEmpty() && !achivPer.isNullOrEmpty()) {
        dataArray.add(PieEntry(targetPer.toFloat()))
        dataArray.add(PieEntry(achivPer.toFloat() ))
        colorSet.add(Color.rgb(231, 91, 34)) // blue
        colorSet.add(Color.rgb(52, 177, 226)) // red
    } else {
        dataArray.add(PieEntry(0f ))
        dataArray.add(PieEntry(0f))
        colorSet.add(Color.rgb(255, 255, 255)) // white
        colorSet.add(Color.rgb(255, 255, 255)) // white
    }

    val dataSet = PieDataSet(dataArray, "")

    dataSet.valueTextSize = 10f
    dataSet.valueTextColor = Color.WHITE
    dataSet.colors = colorSet

    val data = PieData(dataSet)

    // Custom ValueFormatter to append "%" symbol
    data.setValueFormatter(object : ValueFormatter() {
        override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
            return "${value}%"
        }
    })

    pieChart.data = data
    pieChart.invalidate() // Refresh the chart

    pieChart.description.text = ""
    pieChart.description.textSize = 1f
    pieChart.centerTextRadiusPercent = 0f
    pieChart.isDrawHoleEnabled = true
    pieChart.legend.isEnabled = false
    pieChart.description.isEnabled = true
}
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cardFrom -> {
                Utilities.datePicker(tvFrom, tvTo.text.toString(), "", true, activityLocal)
            }
            R.id.cardTo -> {
                Utilities.datePicker(tvTo, "", tvFrom.text.toString(), false, activityLocal)
            }
            R.id.edtyearly -> {
                setupyear()
            }
            R.id.edtmonth -> {
                if (selected_year.isNullOrEmpty()){
                    Toast.makeText(activityLocal,"Please select year",Toast.LENGTH_SHORT).show()
                }else{
                    setupmonth()
                }
            }
            R.id.cardSearch -> {
                if (tvFrom.text.isNotEmpty() && tvTo.text.isNotEmpty()) {
                    val convertedDate = convertDateFormats(tvFrom.text.toString(), tvTo.text.toString())
                    start_date = convertedDate.first
                    end_date = convertedDate.second
                    println("from=$start_date To=$end_date")
                    page = 1
                    when {
                        start_date.isEmpty() -> Toast.makeText(activityLocal, "Please Select Start Date", Toast.LENGTH_LONG).show()
                        end_date.isEmpty() -> Toast.makeText(activityLocal, "Please Select End Date", Toast.LENGTH_LONG).show()
                        else -> callUserDataAPI(
                            selecteduser_id,
                            start_date,
                            end_date,
                            selected_year,
                            selectedbranch_id, selecteddivision_id, selectedmonth
                        )
                    }
                } else {
                    Toast.makeText(context, "Please select date first", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupmonth() {
        items.clear()
        for (item in monthlist) {
            items.add(item.title.toString(), item.id.toString(), true)
        }

        SearchableMultiSelectSpinner.show(activityLocal, "Select Month", "Done", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: java.util.ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                selectedmonth.clear()
                val selectedCity = selectedItems.map { it.text }
                val selectedCityText = selectedCity.joinToString(",")
                edtmonth.setText(selectedCityText)

                val selectedCodes = selectedItems.map { it.code }
                for (item in selectedCodes){
                    selectedmonth.add(item)
                }
                if (selectedmonth.size != 0){
                    page = 1
                    callUserDataAPI(selecteduser_id, start_date, end_date, selected_year, selectedbranch_id, selecteddivision_id,selectedmonth)
                }
                println("selectedCodes=1="+selectedmonth)
            }
        })
    }



    private fun convertDateFormats(tvFrom: String, tvTo: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val tvFromParsed = inputFormat.parse(tvFrom)
        val tvToParsed = inputFormat.parse(tvTo)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Pair(outputFormat.format(tvFromParsed), outputFormat.format(tvToParsed))
    }

    public interface PunchInDetails {
        fun onPunchInDetails(punchIn: Boolean, punchOut: Boolean,working_type:String,punchin_flag:Boolean,punchin_id:String)
    }

    public fun punchInDetails(punchInDetails: PunchInDetails) {
        this.punchInDetails = punchInDetails
    }

}
