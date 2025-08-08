package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.exp.clonefieldkonnect.adapter.TourViewDetailAdapter
import com.exp.clonefieldkonnect.adapter.UserTourUseradapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.TourViewDetailModel
import com.exp.clonefieldkonnect.model.UserCityModel
import com.exp.clonefieldkonnect.model.UserTourListModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_tour.*
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TourFragment(var cardBack: CardView, var linearTopreport: CardView,var tabPosition: Int, tvTitle: TextView) : Fragment(),View.OnClickListener,UserTourUseradapter.OnEmailClick,TourViewDetailAdapter.OnEmailClick {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var recyclerView_useractivity: RecyclerView
    lateinit var recyclerview_tour_view_table: LinearLayout
    lateinit var rec_user_main: RelativeLayout
    lateinit var rec_tour_create: RelativeLayout
    lateinit var rec_tour_view: RelativeLayout
    lateinit var tour_view_table: RelativeLayout
    lateinit var cardSearch: RelativeLayout
    lateinit var linearTop_tour: CardView
    lateinit var cardBack_tour: CardView
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtSearchbranch: AutoCompleteTextView
    private var items: MutableList<SearchableItem> = ArrayList()
    var userCityArr: java.util.ArrayList<UserCityModel> = arrayListOf()
    var tourviewArr: java.util.ArrayList<TourViewDetailModel> = arrayListOf()
    var searchcity : ArrayList<String> = ArrayList()
    var searchbranch : ArrayList<String> = ArrayList()
    var useractivityusername : ArrayList<String> = ArrayList()
    var useractivityuserid : ArrayList<String> = ArrayList()
    var useractivitylist: ArrayList<UserTourListModel.Data> = ArrayList()
    var useractivitybranch: ArrayList<UserTourListModel.Branches> = ArrayList()
    var useractivityuser: ArrayList<UserTourListModel.Users> = ArrayList()
    var useractivitylist2: java.util.ArrayList<UserTourListModel.Data> = arrayListOf()
    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "50"
    var page_count : String = ""
    var flag : String = ""
    var flag_back : String = ""
    var start_date : String = ""
    var end_date : String = ""
    var selecteduser_id : String = ""
    var date_check : String = ""
    var user_id : String = ""
    var selff : String = ""

    lateinit var tvTitle_tour : TextView
    lateinit var tv_date_create : TextView
    lateinit var tv_visit_create : EditText
    lateinit var edt_objective_create : EditText
    lateinit var btn_addmore : ImageView
    lateinit var recyc_visit_table_input : LinearLayout
    lateinit var cardbtn_submit_tour : CardView
    var tour_create_date : ArrayList<String> = ArrayList()
    var tour_create_city : ArrayList<String> = ArrayList()
    var tour_create_objective : ArrayList<String> = ArrayList()
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var cardview_tour_view_table: CardView
    lateinit var tv_tour_user : TextView
    lateinit var cardbtn_submit_tour_view : CardView
    lateinit var cardbtn_edit_tour_view : CardView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_tour, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        cardBack_tour = rootView.findViewById(R.id.cardBack_tour)
        linearTop_tour = rootView.findViewById(R.id.linearTop_tour)
        recyclerView_useractivity = rootView.findViewById(R.id.recyclerView_useractivity)
        recyclerview_tour_view_table = rootView.findViewById(R.id.recyclerview_tour_view_table)
        tvTitle_tour = rootView.findViewById(R.id.tvTitle_tour)
        rec_user_main = rootView.findViewById(R.id.rec_user_main)
        rec_tour_create = rootView.findViewById(R.id.rec_tour_create)
        rec_tour_view = rootView.findViewById(R.id.rec_tour_view)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        tv_date_create = rootView.findViewById(R.id.tv_date_create)
        tv_visit_create = rootView.findViewById(R.id.tv_visit_create)
        edt_objective_create = rootView.findViewById(R.id.edt_objective_create)
        btn_addmore = rootView.findViewById(R.id.btn_addmore)
        recyc_visit_table_input = rootView.findViewById(R.id.recyc_visit_table_input)
        cardbtn_submit_tour = rootView.findViewById(R.id.cardbtn_submit_tour)
        tv_tour_user = rootView.findViewById(R.id.tv_tour_user)
        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        cardview_tour_view_table = rootView.findViewById(R.id.cardview_tour_view_table)
        cardbtn_submit_tour_view = rootView.findViewById(R.id.cardbtn_submit_tour_view)
        cardbtn_edit_tour_view = rootView.findViewById(R.id.cardbtn_edit_tour_view)
        tour_view_table = rootView.findViewById(R.id.tour_view_table)

        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)

        linearTop_tour.visibility = View.VISIBLE
        rec_user_main.visibility = View.VISIBLE
        rec_tour_create.visibility = View.GONE
        rec_tour_view.visibility = View.GONE

        cardBack_tour.setOnClickListener {
//            response_message("Tourrr_backkk")
            handleBackPressed()
        }

        println("tabbbbbbbbb=="+tabPosition)
        getusertourlist(page,searchbranch,"")

        recyclerView_useractivity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (useractivitylist2.size == 50){
                        getusertourlist(page,searchbranch,selecteduser_id)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })
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
        println("flag_back=="+flag_back)
        println("flag=tabPosition="+tabPosition)

        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag_back.equals("Create")){
            rec_user_main.visibility = View.VISIBLE
            rec_tour_create.visibility = View.GONE
            rec_tour_view.visibility = View.GONE
            flag_back = ""
        }
        else if (flag_back.equals("View")){
            rec_user_main.visibility = View.VISIBLE
            rec_tour_create.visibility = View.GONE
            rec_tour_view.visibility = View.GONE
            flag_back = ""
            start_date = ""
            end_date = ""
            tvFrom.text = ""
            tvTo.text = ""
            user_id = ""
            tourviewArr.clear()
            tour_view_table.visibility = View.GONE
            cardbtn_submit_tour_view.visibility = View.GONE
            cardbtn_edit_tour_view.visibility = View.GONE

        }else if (flag_back.equals("")){
            tabPosition = 2
            tvTitle_tour.text = "Report"
            rec_user_main.visibility = View.GONE
            rec_tour_create.visibility = View.GONE
            rec_tour_view.visibility = View.GONE
            fragment_container_tour.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }

    }


    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_tour, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun getusertourlist(page: Int, searchbranch: ArrayList<String>, selecteduser_id: String) {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["search_name"] = selecteduser_id

        ApiClient.getUsertourlist(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,searchbranch,
            object : APIResultLitener<UserTourListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<UserTourListModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            useractivitylist2.clear()
                            useractivitybranch.clear()
                            useractivityuser.clear()

                            if (page==1)
                                useractivitylist.clear()

                            useractivitylist.addAll(response.body()!!.data)
                            useractivitybranch.addAll(response.body()!!.branches)
                            useractivityuser.addAll(response.body()!!.users)

                            for (item in useractivityuser) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!useractivityusername.contains(name)) {
                                    useractivityusername.add(name)
                                    useractivityuserid.add(id)
                                }
                            }


                            useractivitylist2 = response.body()!!.data
                            page_count = response.body()!!.pageCount.toString()


                            edtSearchbranch.setOnClickListener {
                                spinnerbranch()
                            }

                            edtSearch.setOnClickListener {
                                spinneruser()
                            }

                            setuprecycleruserlist()
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
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecycleruserlist() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_useractivity.layoutManager = mLayoutManager
        val useractivityAdapter = UserTourUseradapter(activityLocal, useractivitylist,this)
        recyclerView_useractivity.adapter = useractivityAdapter
        recyclerView_useractivity.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
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

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    getusertourlist(page,searchbranch,selecteduser_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinnerbranch() {
        items.clear()
        for (item in useractivitybranch) {
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
                    useractivityuser.clear()
                    useractivityusername.clear()
                    useractivityuserid.clear()
                    page = 1
                    getusertourlist(page,searchbranch,selecteduser_id)
                }
                println("selectedCodes=1="+searchbranch)
            }
        })
    }

    override fun onClickEmail_tour(id: Int?, flag: String?, name: String?) {
        println("ididid="+id+"<<"+ flag)
        user_id = id.toString()
        if (flag.equals("Create")){
            flag_back = "Create"
            rec_user_main.visibility = View.GONE
            rec_tour_view.visibility = View.GONE
            rec_tour_create.visibility = View.VISIBLE
//            userCityList("")
            createtourplan()
        }else if (flag.equals("View")){
            flag_back = "View"
            rec_user_main.visibility = View.GONE
            rec_tour_create.visibility = View.GONE
            rec_tour_view.visibility = View.VISIBLE
            tv_tour_user.text = name
        }
    }


    private fun gettourviewdetail(userId: String, start_date: String, end_date: String) {
        tourviewArr.clear()
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = java.util.HashMap<String, String>()

        queryParams["user_id"] = userId
        queryParams["start_date"] = start_date
        queryParams["end_date"] = end_date

        ApiClient.Tourviewdetails(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            cardview_tour_view_table.visibility = View.VISIBLE
                            cardbtn_edit_tour_view.visibility = View.VISIBLE
                            tourviewArr.clear()
                            selff = ""
                            val gson = Gson()

                            val listType = object :
                                TypeToken<java.util.ArrayList<TourViewDetailModel>>() {}.type

                            tourviewArr  = gson.fromJson<java.util.ArrayList<TourViewDetailModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )

                            for (item in tourviewArr){
                                selff = item.self.toString()
                            }

                            if (selff.equals("false")){
                                cardbtn_submit_tour_view.visibility = View.VISIBLE
                            }else{
                                cardbtn_submit_tour_view.visibility = View.GONE
                            }

                            println("tourviewArr="+tourviewArr)
                            tour_view_table.visibility = View.VISIBLE
                            setuprecyclertourview()

                        } else {
                            cardview_tour_view_table.visibility = View.GONE
                            cardbtn_submit_tour_view.visibility = View.GONE
                            cardbtn_edit_tour_view.visibility = View.GONE

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
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

    private fun setuprecyclertourview() {
        recyclerview_tour_view_table.removeAllViews()


        val statusArray = ArrayList<Int>(List(tourviewArr.size) { 0 })
        val editArray = ArrayList<Int>(List(tourviewArr.size) { 0 })
        val checkboxcheck = ArrayList<Boolean>(List(tourviewArr.size) { false })
        var townarray:ArrayList<String> = ArrayList()
        var objectivearray:ArrayList<String> = ArrayList()
        var townIDarray:ArrayList<String> = ArrayList()
        var towndatearray:ArrayList<String> = ArrayList()

        for (item in tourviewArr){
            townarray.add(item.town.toString())
            objectivearray.add(item.objectives.toString())
            townIDarray.add(item.id.toString())
            towndatearray.add(item.date.toString())
        }

        for (position in tourviewArr.indices){
            val inflater = LayoutInflater.from(activityLocal)
            val layout = inflater.inflate(R.layout.report_tour_view_table, recyclerview_tour_view_table, false)

            var tv_view_date: TextView = layout.findViewById(R.id.tv_view_date)
            var edt_view_visit: EditText = layout.findViewById(R.id.edt_view_visit)
            var edt_view_objective: EditText = layout.findViewById(R.id.edt_view_objective)
            var tv_view_status: TextView = layout.findViewById(R.id.tv_view_status)
            var checkbox: CheckBox = layout.findViewById(R.id.checkbox)


            tv_view_date.text = tourviewArr[position].date
            tv_view_status.text = tourviewArr[position].status
            edt_view_visit.setText(tourviewArr[position].town)
            edt_view_objective.setText(tourviewArr[position].objectives)

            println("Statuss="+tourviewArr[position].status)
            when (tourviewArr[position].status) {
                "Approved" -> {
                    tv_view_status.setTextColor(Color.parseColor("#00D23B"))
                }
                "Rejected" -> {
                    tv_view_status.setTextColor(Color.parseColor("#FF0000"))
                }
                "Pending" -> {
                    tv_view_status.setTextColor(Color.parseColor("#FFC700"))
                }
            }

            if (tourviewArr.get(position).status == "Approved"){
                statusArray[position] = 1
                editArray[position] = 1
                checkbox.isClickable = false
                checkbox.isChecked = true
                edt_view_visit.setOnFocusChangeListener { v, hasFocus ->
                    var origBg: Drawable? = null
                    if (!hasFocus) {
                        origBg = v.background
                        edt_view_visit.isEnabled = false
                        edt_view_objective.isEnabled = false
                    }
                }
            }
            else if (tourviewArr.get(position).status == "Pending" || tourviewArr.get(position).status == "Rejected") {
                checkbox.isClickable = true
                edt_view_visit.isEnabled = true
                edt_view_objective.isEnabled = true

                checkbox.setOnCheckedChangeListener(null)

                val objIncome: TourViewDetailModel = tourviewArr[position]
                checkbox.isChecked = objIncome.isSelected!!


                checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    val currentItem = tourviewArr[position]

                    if (currentItem.self == "false") {
                        flag = if (isChecked) "true" else "false"
                        statusArray[position] = if (isChecked) 1 else 0
                        editArray[position] = if (isChecked) 0 else 0

                        if (isChecked){
                            checkboxcheck[position] = true
                            townarray[position] = edt_view_visit.text.toString()
                            objectivearray[position] = edt_view_objective.text.toString()
                            edt_view_visit.isEnabled = false
                            edt_view_objective.isEnabled = false
                        }else{
                            checkboxcheck[position] = false
                            edt_view_visit.isEnabled = true
                            edt_view_objective.isEnabled = true
                            townarray[position] = edt_view_visit.text.toString()//+" "+position
                            objectivearray[position] = edt_view_objective.text.toString()
                        }



                    } else if (currentItem.self == "true") {
                        flag = if (isChecked) "true" else "false"
                        editArray[position] = if (isChecked) 0 else 0

                        if (isChecked){
                            checkboxcheck[position] = true
                            townarray[position] = edt_view_visit.text.toString()//+" "+position
                            objectivearray[position] = edt_view_objective.text.toString()
                            edt_view_visit.isEnabled = false
                            edt_view_objective.isEnabled = false
                        }else{
                            checkboxcheck[position] = false
                            edt_view_visit.isEnabled = true
                            edt_view_objective.isEnabled = true
                            townarray[position] = edt_view_visit.text.toString()//+" "+position
                            objectivearray[position] = edt_view_objective.text.toString()
                        }

                    }

                    currentItem.isSelected = isChecked
                }
            }

            recyclerview_tour_view_table.addView(layout, recyclerview_tour_view_table.childCount)
        }
        cardbtn_submit_tour_view.setOnClickListener {
//            println("DATTTaa=status="+statusArray)
//            println("DATTTaa=edit="+editArray)
//            println("DATTTaa=townn="+townarray)
//            println("DATTTaa=objectivearray="+objectivearray)
//            println("DATTTaa=townID="+townIDarray)
//            println("DATTTaa=towndate="+towndatearray)
            println("DATTTaa=checkedddddddd="+checkboxcheck)
            if (checkboxcheck.contains(true)){
                submittourapproval(user_id, townIDarray, towndatearray,townarray,objectivearray,statusArray)
            }else{
                response_message("Please select any checkbox for approval details")
            }
        }
        cardbtn_edit_tour_view.setOnClickListener {
            println("townarray=editArray="+ editArray)
            if (checkboxcheck.contains(true)){
            submittourapproval(user_id, townIDarray, towndatearray,townarray,objectivearray,editArray)
            }else{
                response_message("Please select any checkbox for edit details")
            }

        }



//        var mLayoutManager = LinearLayoutManager(activityLocal)
//        recyclerview_tour_view_table.layoutManager = mLayoutManager
//        val tourViewDetailAdapter = TourViewDetailAdapter(activityLocal, tourviewArr,this)
//        recyclerview_tour_view_table.adapter = tourViewDetailAdapter
        //tourViewDetailAdapter!!.notifyDataSetChanged()
    }

    private fun createtourplan() {
        tv_date_create.setOnClickListener {
            Utilities.datePickerFuture2(tv_date_create, activityLocal)
        }
       /* tv_visit_create.setOnClickListener {
            spinnerfilCity()
        }*/
        btn_addmore.setOnClickListener {
            if (tv_date_create.text.toString().isEmpty()){
                response_message("Please Select Date")
            }else if (date_check.equals(tv_date_create.text.toString())){
                response_message("Date Already Selected")
            } else if (tv_visit_create.text.toString().isEmpty()) {
                response_message("Please Select City")
            }else if (edt_objective_create.text.toString().isEmpty()) {
                response_message("Please Enter Objective")
            }else{
                date_check = tv_date_create.text.toString()
                println("date_check="+date_check)
                recyc_visit_table_input.visibility = View .VISIBLE
                showaddmorelayout(edt_objective_create)
                tv_date_create.text = ""
                tv_visit_create.setText("")
                edt_objective_create.setText("")
            }
        }
        cardbtn_submit_tour.setOnClickListener {
            println("DAttttttaaaa="+user_id+"<<<"+tour_create_date+"<<<"+tour_create_city+"<<"+tour_create_objective)
            submitcreateplan(user_id,tour_create_date,tour_create_city,tour_create_objective)
        }
    }


    private fun submitcreateplan(userId: String, tourCreateDate: ArrayList<String>, tourCreateCity: ArrayList<String>, tourCreateObjective: ArrayList<String>) {
        if (tv_date_create.text.isEmpty()) {
            response_message("please Select Date")
        } else if (tv_visit_create.text.isEmpty()) {
            response_message("please Enter Town")
        } else if (edt_objective_create.text.isEmpty()) {
            response_message("please Enter Objective")
        } else {
            tourCreateDate.add(tv_date_create.text.toString())
            tourCreateCity.add(tv_visit_create.text.toString())
            tourCreateObjective.add(edt_objective_create.text.toString())
            if (!Utilities.isOnline(activityLocal)) {
                return
            }
            var dialog = DialogClass.progressDialog(activityLocal)
            val queryParams = HashMap<String, String>()
            queryParams["user_id"] = userId

            ApiClient.createtoursubmit(
                StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
                queryParams, tourCreateDate, tourCreateCity, tourCreateObjective,
                object : APIResultLitener<AttendanceSubmitModel> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onAPIResult(
                        response: Response<AttendanceSubmitModel>?,
                        errorMessage: String?
                    ) {
                        dialog.dismiss()
                        if (response != null && errorMessage == null) {

                            if (response.code() == 200) {
                                Toast.makeText(
                                    activityLocal,
                                    response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                rec_user_main.visibility = View.VISIBLE
                                rec_tour_view.visibility = View.GONE
                                rec_tour_create.visibility = View.GONE
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
    }


    @SuppressLint("MissingInflatedId")
    private fun showaddmorelayout(edtObjectiveCreate: EditText) {
        val inflater = LayoutInflater.from(activityLocal)
        val layout = inflater.inflate(R.layout.add_more_tour_layout, recyc_visit_table_input, false)
        recyc_visit_table_input.addView(layout, recyc_visit_table_input.childCount)

        val tv_date_create_more = layout.findViewById<TextView>(R.id.tv_date_create_more)
        val tv_visit_create_more = layout.findViewById<TextView>(R.id.tv_visit_create_more)
        val tv_objective_create_more = layout.findViewById<TextView>(R.id.tv_objective_create_more)
        val inputDate = tv_date_create.text.toString()
        val outputDate = Utilities.convertDateFormat(inputDate)

        tv_date_create_more.text = tv_date_create.text.toString()
        tv_visit_create_more.text = tv_visit_create.text.toString()
        tv_objective_create_more.text = edtObjectiveCreate.text.toString()
        tour_create_date.add(outputDate)
        tour_create_city.add(tv_visit_create.text.toString())
        tour_create_objective.add(edtObjectiveCreate.text.toString())

        }


/*
    private fun spinnerfilCity() {
        items.clear()
        for (item in userCityArr) {
            items.add(item.city_name.toString(), item.id.toString(), item.isSelected)
        }

        SearchableMultiSelectSpinner.show(requireContext(), "Select City", "Search", items, object :
            SelectionCompleteListener {
            override fun onCompleteSelection(selectedItems: java.util.ArrayList<SearchableItem>) {
                Log.e("data", selectedItems.toString())
                searchcity.clear()

                val selectedCity = selectedItems.map { it.text }
                val selectedCityText = selectedCity.joinToString(",")
                tv_visit_create.setText(selectedCityText)
                val selectedCodes = selectedItems.map { it.code }
                for (item in selectedCodes){
                    searchcity.add(item)
                }

                println("CItyyyid="+searchcity)

            }
        })
    }
*/


/*
    private fun userCityList(searchText : String) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = java.util.HashMap<String, String>()

        queryParams["cityname"] = searchText
        println("searchText ${searchText}")
        ApiClient.userCityList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<java.util.ArrayList<UserCityModel>>() {}.type

                            userCityArr  = gson.fromJson<java.util.ArrayList<UserCityModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )
                            println("userCityArr="+userCityArr)

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal,
                                    false
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
*/
    fun response_message(message: String?) {
        Toast.makeText(activityLocal, message, Toast.LENGTH_SHORT).show()
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cardFrom -> {
                Utilities.datePickerconditional(tvFrom, tvTo.text.toString(), "", true, activityLocal)
            }
            R.id.cardTo -> {
                Utilities.datePickerconditional(tvTo, "", tvFrom.text.toString(), false, activityLocal)
            }
            R.id.cardSearch -> {
                page = 1
                if (tvFrom.text.toString().isEmpty()){
                    Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                }else if (tvTo.text.toString().isEmpty()){
                    Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                }else{
                    val convertedDate = convertDateFormats(tvFrom.text.toString(),tvTo.text.toString())
                    start_date = convertedDate.first
                    end_date = convertedDate.second
                    println("from=="+start_date+"To="+end_date)
                    gettourviewdetail(user_id,start_date,end_date)
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

    override fun onClickEmail_tour(
        touridarray: ArrayList<String>,
        datearray: ArrayList<String>,
        townMap: MutableMap<Int, String>,
        objectiveMap: MutableMap<Int, String>,
        statusArray: ArrayList<Int>,
        flag: String,
        editArray: ArrayList<Int>
    ) {

        val uniquetownArrayList: ArrayList<String> = ArrayList()
        val uniqueObjectivesArrayList: ArrayList<String> = ArrayList()

        for (vale in townMap){
            uniquetownArrayList.add(vale.value)
        }
        for (vale in objectiveMap){
            uniqueObjectivesArrayList.add(vale.value)
        }
        val uniqueDateArray = datearray.distinct()
        val uniqueIDArray = touridarray.distinct()

        cardbtn_submit_tour_view.setOnClickListener {

            if (flag.equals("true")) {
                println("townarray=userid="+user_id)
                println("townarray=touridarray="+uniqueIDArray)
                println("townarray=datearray2="+uniqueDateArray)
                println("townarray=townarray2="+uniquetownArrayList)
                println("townarray=objectivearray2="+uniqueObjectivesArrayList)
                println("townarray=objectivearray="+objectiveMap)
                println("townarray=statusArray="+statusArray)
                println("townarray=flag="+flag)
//                submittourapproval(user_id,
//                    uniqueIDArray as ArrayList<String>,
//                    uniqueDateArray as ArrayList<String>,uniquetownArrayList,uniqueObjectivesArrayList,statusArray)
            } else {
                response_message("Please select any checkbox for submit approval")
            }

        }
        cardbtn_edit_tour_view.setOnClickListener {
            println("townarray=userid="+user_id)
            println("townarray=touridarray="+uniqueIDArray)
            println("townarray=datearray2="+uniqueDateArray)
            println("townarray=townarray"+uniquetownArrayList)
            println("townarray=objectivearray"+uniqueObjectivesArrayList)
            println("townarray=objectivearray="+objectiveMap)
            println("townarray=flag="+ flag)
            println("townarray=editArray="+ editArray)
            if (flag.equals("true")){
//                submittourapproval(user_id,
//                    uniqueIDArray as ArrayList<String>,
//                    uniqueDateArray as ArrayList<String>,uniquetownArrayList,uniqueObjectivesArrayList,editArray)
            }else{
                response_message("Please select any checkbox for edit details")
            }
        }

    }
    private fun submittourapproval(
        userId: String, touridarray: ArrayList<String>, datearray: ArrayList<String>, townarray: ArrayList<String>, objectivearray: ArrayList<String>, statusArray: ArrayList<Int>
    ) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["user_id"] = userId

        ApiClient.submittourapproval(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams, touridarray, datearray, townarray,objectivearray,statusArray,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            Toast.makeText(activityLocal, response.body()!!.message, Toast.LENGTH_LONG).show()
                            cardview_tour_view_table.visibility = View.GONE
                            cardbtn_submit_tour_view.visibility = View.GONE
                            cardbtn_edit_tour_view.visibility = View.GONE
                            tvTo.text = ""
                            tvFrom.text = ""
                            start_date = ""
                            end_date = ""
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


}