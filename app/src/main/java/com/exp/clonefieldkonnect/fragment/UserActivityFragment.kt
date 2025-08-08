package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import com.exp.clonefieldkonnect.adapter.UserActivityAdapter
import com.exp.clonefieldkonnect.adapter.UserActivityDetailAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.UserActivityDetailModel
import com.exp.clonefieldkonnect.model.UserActivityListModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_user_activity.fragment_container_activity
import kotlinx.android.synthetic.main.fragment_user_activity.linearTop_activity
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class UserActivityFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment(),View.OnClickListener,UserActivityAdapter.OnEmailClick {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var cardBack_activity: CardView
    lateinit var recyclerView_useractivity: RecyclerView
    lateinit var recyclerView_user_detail: RecyclerView
    lateinit var rec_user_main: RelativeLayout
    lateinit var rec_user_main2: RelativeLayout
    lateinit var cardFrom: LinearLayout
    lateinit var cardTo: LinearLayout
    lateinit var tvFrom: TextView
    lateinit var tvTo: TextView
    lateinit var tv_user_date: TextView
    lateinit var cardSearch: RelativeLayout
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtSearchbranch: AutoCompleteTextView
    private var items: MutableList<SearchableItem> = ArrayList()

    var searchbranch : ArrayList<String> = ArrayList()
    var useractivityusername : ArrayList<String> = ArrayList()
    var useractivityuserid : ArrayList<String> = ArrayList()
    var useractivitylist: ArrayList<UserActivityListModel.Data> = ArrayList()
    var useractivitybranch: ArrayList<UserActivityListModel.Branches> = ArrayList()
    var useractivityuser: ArrayList<UserActivityListModel.Users> = ArrayList()
    var useractivitylist2: java.util.ArrayList<UserActivityListModel.Data> = arrayListOf()
    var useractivitydetail: java.util.ArrayList<UserActivityDetailModel> = arrayListOf()
    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "50"
    var page_count : String = ""
    var flag : String = ""
    var start_date : String = ""
    var end_date : String = ""
    var selecteduser_id : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_user_activity, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        cardBack_activity = rootView.findViewById(R.id.cardBack_activity)
        recyclerView_useractivity = rootView.findViewById(R.id.recyclerView_useractivity)
        recyclerView_user_detail = rootView.findViewById(R.id.recyclerView_user_detail)
        rec_user_main = rootView.findViewById(R.id.rec_user_main)
        rec_user_main2 = rootView.findViewById(R.id.rec_user_main2)
        cardFrom = rootView.findViewById(R.id.cardFrom)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardSearch = rootView.findViewById(R.id.cardSearch)
        tvFrom = rootView.findViewById(R.id.tvFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        tv_user_date = rootView.findViewById(R.id.tv_user_date)


        cardFrom.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardSearch.setOnClickListener(this)

        linearTopreport.visibility = View.GONE
        rec_user_main.visibility = View.VISIBLE
        rec_user_main2.visibility = View.GONE

        cardBack_activity.setOnClickListener {
//            Toast.makeText(activityLocal, "activity backkk", Toast.LENGTH_LONG).show()
            handleBackPressed()
        }
        println("tabbbbbbbbb=="+tabPosition)
        getuseractivity(page, start_date, end_date, searchbranch, selecteduser_id)

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
                        getuseractivity(page, start_date, end_date, searchbranch, selecteduser_id)
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
        println("flag=="+flag)
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag.equals("")){
            tabPosition = 2
            tvTitle.text = "Report"
            rec_user_main.visibility = View.GONE
            linearTop_activity.visibility = View.GONE
            fragment_container_activity.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }
        else if (flag.equals("2")){
            flag = ""
            rec_user_main.visibility = View.VISIBLE
            rec_user_main2.visibility = View.GONE
        }
    }


    private fun navigateToFragmentB(
        linearTopreport: CardView,
        tabPosition: Int,
    ) {
        val fragmentB = ReportFragment(linearTopreport,tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_activity, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
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
                if (start_date.equals("")){
                    Toast.makeText(activityLocal,"Please Select Start Date", Toast.LENGTH_LONG).show()
                }else if (end_date.equals("")){
                    Toast.makeText(activityLocal,"Please Select End Date", Toast.LENGTH_LONG).show()
                }else{
                    val convertedDate = convertDateFormats(tvFrom.text.toString(),tvTo.text.toString())
                    start_date = convertedDate.first
                    end_date = convertedDate.second
                    println("from=="+start_date+"To="+end_date)
                    page = 1
                    getuseractivity(page, start_date, end_date, searchbranch, selecteduser_id)
                }
            }
        }
    }

    private fun getuseractivity(page: Int, start_date: String, end_date: String, searchbranch: ArrayList<String>, selecteduser_id: String) {
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
        queryParams["search_name"] = selecteduser_id

        ApiClient.getUserActivity(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,searchbranch,
            object : APIResultLitener<UserActivityListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<UserActivityListModel>?, errorMessage: String?) {
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
        val useractivityAdapter = UserActivityAdapter(activityLocal, useractivitylist,this)
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
                    getuseractivity(page,start_date,end_date,searchbranch,selecteduser_id)
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
                    getuseractivity(page, start_date, end_date, searchbranch, selecteduser_id)
                }
                println("selectedCodes=1="+searchbranch)
            }
        })
    }

    private fun getuseractivitydetail(id: Int?, date: String?) {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()

        var inputttt = date.toString()
        val formattedDate2 = formatDate2(inputttt)
        println("id="+id)
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
                                TypeToken<java.util.ArrayList<UserActivityDetailModel>>() {}.type

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

    private fun formatDate2(inputttt: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(inputttt)
        return outputFormat.format(date)
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
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

    override fun onClickEmail1(id: Int?, date: String?) {
        println("id=="+id)
        flag = "2"
        rec_user_main.visibility = View.GONE
        rec_user_main2.visibility = View.VISIBLE
        getuseractivitydetail(id,date)
    }

}