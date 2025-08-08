package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import com.exp.clonefieldkonnect.adapter.CustomerretailerAdapter2
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.MspActivityTypeModel
import com.exp.clonefieldkonnect.model.UserCityModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap
import kotlin.collections.ArrayList

class MSPFragment(
   var cardBack: CardView,
   var linearTopreport: CardView,
   var tabPosition: Int,
   var tvTitle: TextView
) : Fragment() {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var cardBack_msp: CardView
    lateinit var cardSubmit: CardView
    lateinit var tvTitle_dealer_sales: TextView
    lateinit var linearTop_msp: CardView
    lateinit var carddate: LinearLayout
    lateinit var tvactivitydate: TextView
    lateinit var edtSearchbranch: EditText
    lateinit var edtcityyy: AutoCompleteTextView
    lateinit var edtSearchyear: AutoCompleteTextView
    lateinit var edtselectmsptype: AutoCompleteTextView

    var userCityArr: ArrayList<UserCityModel> = arrayListOf()
    var usermsptypeArr: ArrayList<MspActivityTypeModel.Data> = arrayListOf()
    private var items: MutableList<SearchableItem> = ArrayList()
    var searchcity : ArrayList<String> = ArrayList()
    var usermsptypename : ArrayList<String> = ArrayList()
    var usermsptypeid : ArrayList<String> = ArrayList()
    private var page_retailer = 1
    private var pageSize_retailer = "200"
    private var selectedtype_id = ""
    private var date = ""
    private var parent_search = ""
    private var customer_ids :ArrayList<String> = ArrayList()
    private var isLoading = false
    private var lastPosition = -1
    var retailerArr: ArrayList<DistriutorModel.Datum> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_m_s_p, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        cardBack_msp = rootView.findViewById(R.id.cardBack_msp)
        tvTitle_dealer_sales = rootView.findViewById(R.id.tvTitle_dealer_sales)
        linearTop_msp = rootView.findViewById(R.id.linearTop_msp)
        carddate = rootView.findViewById(R.id.carddate)
        tvactivitydate = rootView.findViewById(R.id.tvactivitydate)
        edtcityyy = rootView.findViewById(R.id.edtcityyy)
        edtSearchyear = rootView.findViewById(R.id.edtSearchyear)
        cardSubmit = rootView.findViewById(R.id.cardSubmit)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        edtselectmsptype = rootView.findViewById(R.id.edtselectmsptype)

        userCityList("")
        usermsptypeList()
        getRetailers(page_retailer,pageSize_retailer,parent_search)

        cardBack_msp.setOnClickListener {
            handleBackPressed()
        }
        edtSearchyear.setOnClickListener {
            showretailerdropdown()
        }

        carddate.setOnClickListener {
            Utilities.datePicker2(tvactivitydate,activityLocal)
        }
        cardSubmit.setOnClickListener {
            date = Utilities.convertDateFormat(tvactivitydate.text.toString())
            if (date.isNullOrEmpty()){
                responsemessage("Please select date")
            } else if (selectedtype_id.isNullOrEmpty()){
                responsemessage("Please select msp activity type")
            }else if (edtSearchbranch.text.toString().isNullOrEmpty()){
                responsemessage("Please enter no. of participant")
            }else if (searchcity.isEmpty()){
                responsemessage("Please select city")
            }else if (customer_ids.isEmpty()){
                responsemessage("Please select customer under event")
            }else{
                submitmsp(date,selectedtype_id,edtSearchbranch.text.toString(),searchcity,customer_ids)
            }
        }
    }

    private fun submitmsp(date: String, selectedtypeId: String, particiapnt_count: String, searchcity: ArrayList<String>, customerIds: ArrayList<String>) {

        println("DDD=="+date+"<<"+edtSearchbranch.text.toString()+"<<"+searchcity+"<<"+customer_ids+"<<"+selectedtype_id)

        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }

        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["activity_date"] = date
        queryParams["activity_type"] = selectedtypeId
        queryParams["msp_count"] = particiapnt_count

        ApiClient.submitmspactivity(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,searchcity,customerIds,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(
                    response: Response<AttendanceSubmitModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            responsemessage(response.body()!!.message!!)
                            startActivity(Intent(activityLocal, MainActivity::class.java))
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

    private fun responsemessage(message: String) {
        Toast.makeText(activityLocal,message,Toast.LENGTH_SHORT).show()
    }

    private lateinit var currentDialog: Dialog

    private fun showretailerdropdown() {
        currentDialog = Dialog(activityLocal)
        currentDialog.setContentView(R.layout.dialog_custom_retaier_dropdownnn_22)

        val searchInput = currentDialog.findViewById<EditText>(R.id.search_input)
        val searchIcon = currentDialog.findViewById<ImageView>(R.id.search_icon)
        val recyclerView = currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown)
        val btnDone = currentDialog.findViewById<Button>(R.id.btn_done)


        val adapter = CustomerretailerAdapter2(retailerArr, currentDialog) { selectedItem ->
//            val selectedCustomerId = selectedItem.customer_id
//            val selectedCustomerName = selectedItem.name
//            Log.d("SelectedItem", "ID: $selectedCustomerId")
//            edtSearchyear.setText(selectedCustomerName)
//            customer_id = selectedCustomerId.toString()
//            getsalesList(page, start_date, end_date, selectedstatus_id,customer_id)

        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activityLocal)
        recyclerView.scrollToPosition(lastPosition)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page_retailer++
                    if (retailerArr.size >= 200) {
                        getRetailers(page_retailer, pageSize_retailer, "") // Call the API to load more items
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

        searchIcon.setOnClickListener {
            val parent_search = searchInput.text.toString()
            page_retailer = 1
            getRetailers(page_retailer, pageSize_retailer, parent_search)
        }
        btnDone.setOnClickListener {
            customer_ids.clear()
            val selectedItems = adapter.getSelectedItems()

            val selectedNames = selectedItems.map { it.name }
            val selectedIds = selectedItems.map { it.customer_id }

            val selectedCustomerName = selectedNames.joinToString(", ")
            val customer_iddd = selectedIds.joinToString(",")
            edtSearchyear.setText(selectedCustomerName)
            customer_ids.addAll(selectedIds.map { it.toString() })
            println("Selected Customer IDs: $customer_ids"+"<<"+selectedCustomerName+"<<"+selectedIds)

            currentDialog.dismiss()
        }
        currentDialog.show()
    }

    private fun getRetailers(page: Int, pageSize: String, parent_search: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = HashMap<String, String>()
        queryParams["pageSize"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["search"] = parent_search

        ApiClient.getRetailers(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                activityLocal
            ).toString(), queryParams, object : APIResultLitener<DistriutorModel> {
                override fun onAPIResult(
                    response: Response<DistriutorModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            if (page == 1) {
                                retailerArr.clear() // Clear the previous results for the new search
                            }
                            retailerArr.addAll(response.body()!!.data!!)



                            if (::currentDialog.isInitialized && currentDialog.isShowing) {
                                retailerArr?.let {
                                    (currentDialog.findViewById<RecyclerView>(R.id.recycler_dropdown).adapter as CustomerretailerAdapter2)
                                        .updateList(it)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }


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
                            val listType = object : TypeToken<ArrayList<UserCityModel>>() {}.type

                            userCityArr  = gson.fromJson<java.util.ArrayList<UserCityModel>>(
                                response.body()!!.get("data").asJsonArray,
                                listType
                            )
                            edtcityyy.setOnClickListener {
                                spinnerfilCity()
                            }
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
                        //  dialog.dismiss()
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun usermsptypeList() {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        val dialog = DialogClass.progressDialog(activityLocal)

        val queryParams = java.util.HashMap<String, String>()

        ApiClient.usermsptypeList(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<MspActivityTypeModel> {
                override fun onAPIResult(response: Response<MspActivityTypeModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            usermsptypeArr.clear()
                            usermsptypeArr  = response.body()!!.data


                            for (item in usermsptypeArr) {
                                val name = item.type.toString()
                                val id = item.id.toString()

                                if (!usermsptypename.contains(name)) {
                                    usermsptypename.add(name)
                                    usermsptypeid.add(id)
                                }
                            }

                            edtselectmsptype.setOnClickListener {
                                spinnertype()
                            }
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
                        //  dialog.dismiss()
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
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

        val colorsArray = usermsptypename.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
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
            edtselectmsptype.setText("")
            selectedtype_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { adapterView, view, i, l ->
            val selectedPosition = usermsptypename.indexOf(adapter.getItem(i).toString())
            if (selectedPosition != -1) {
                val selectedParentId = usermsptypeid[selectedPosition].toString()
                val selectedParentName = usermsptypename[selectedPosition].toString()

                edtselectmsptype.setText(selectedParentName)
                selectedtype_id = selectedParentId

                println("Abhi=id=$selectedtype_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


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
                edtcityyy.setText(selectedCityText)

                val selectedCodes = selectedItems.map { it.code }
                for (item in selectedCodes){
                    searchcity.add(item)
                }
                println("selectedCodes=1="+searchcity)
//                getRetailerSearch("", searchcity, searchbranch, selectedtype_id)
            }
        })
    }


    private fun handleBackPressed() {
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
    }
}