package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.LeadLogDetailAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.LastCallRemarkListener
import com.exp.clonefieldkonnect.helper.MyApplication
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.LastCallRemarkModel
import com.exp.clonefieldkonnect.model.LeadCallLogDetailModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LeadCallDetailActivity : AppCompatActivity(),LastCallRemarkListener {
    lateinit var cardBack_activity: CardView
    lateinit var lin_date: LinearLayout
    lateinit var lin_user: LinearLayout
    lateinit var edtleaddate: TextView
    lateinit var call_dial_count: TextView
    lateinit var call_connect_count: TextView
    lateinit var call_noresponse_count: TextView
    lateinit var call_total_duration: TextView
    lateinit var recyclerView_callhistroy: RecyclerView
    lateinit var edtleaduser: AutoCompleteTextView

    private var page = 1
    private var pageSize = "20"
    private var formattedDate = ""
    private var lastPosition = -1
    private var isLoading = false

    var leadloglist: ArrayList<LeadCallLogDetailModel.Data_log> = ArrayList()
    var leadloglist2: ArrayList<LeadCallLogDetailModel.Data_log> = ArrayList()
    var leaduserlist: ArrayList<LeadCallLogDetailModel.Users> = ArrayList()
    var leadusersname : ArrayList<String> = ArrayList()
    var leadusersid : ArrayList<String> = ArrayList()
    var selecteduser_id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lead_call_detail)
        (application as MyApplication).lastCallRemarkListener = this // ✅ This works because MainActivity implements the interface
        initViews()
    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack)
        lin_date = findViewById(R.id.lin_date)
        lin_user = findViewById(R.id.lin_user)
        edtleaddate = findViewById(R.id.edtleaddate)
        recyclerView_callhistroy = findViewById(R.id.recyclerView_callhistroy)
        call_dial_count = findViewById(R.id.call_dial_count)
        call_connect_count = findViewById(R.id.call_connect_count)
        call_noresponse_count = findViewById(R.id.call_noresponse_count)
        call_total_duration = findViewById(R.id.call_total_duration)
        edtleaduser = findViewById(R.id.edtleaduser)


        lin_date.setOnClickListener {
            // Get today’s date as default
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this, // or requireContext() if inside Fragment
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format date to yyyy-MM-dd
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    formattedDate = sdf.format(calendar.time)

                    // Set selected date to TextView (optional)
                    edtleaddate.text = formattedDate
                    page = 1
                    // Pass formattedDate to API
                    getleadlogdetail(page, selecteduser_id,formattedDate)

                },
                year, month, day
            )
            datePicker.show()
        }



        cardBack_activity.setOnClickListener {
            handelbackpress()
        }

        lin_user.setOnClickListener {
            spinnerusername(edtleaduser)
        }

        edtleaduser.setOnClickListener {
            spinnerusername(edtleaduser)
        }


        getleadlogdetail(page, selecteduser_id, formattedDate)


        recyclerView_callhistroy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (leadloglist2.size == 20){
                        getleadlogdetail(page, selecteduser_id, formattedDate)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })


    }


    private fun getleadlogdetail(
        page: Int,
        selecteduser_id: String,
        formattedDate: String,
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
        queryParams["user_id"] = selecteduser_id.toString()
        queryParams["date"] = formattedDate.toString()

        println("queryParams=="+queryParams)
        ApiClient.getleadlogdetail(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadCallLogDetailModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadCallLogDetailModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadloglist2.clear()
                            leaduserlist.clear()

                            if (page ==1)
                                leadloglist.clear()

                            leaduserlist.addAll(response.body()!!.users)
                            leadloglist.addAll(response.body()!!.data)
                            leadloglist2.addAll(response.body()!!.data)

                            call_dial_count.text = response.body()!!.callDialted.toString()
                            call_connect_count.text = response.body()!!.connected.toString()
                            call_noresponse_count.text = response.body()!!.noResponse.toString()
                            call_total_duration.text = response.body()!!.totalDuration.toString()

                            for (item in leaduserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadusersname.contains(name)) {
                                    leadusersname.add(name)
                                    leadusersid.add(id)
                                }
                            }
                            setuprecyclerleadlist()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@LeadCallDetailActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@LeadCallDetailActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun spinnerusername(edtleaduser: AutoCompleteTextView) {
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
            edtleaduser.setText("")
            selecteduser_id = ""
            page = 1
            getleadlogdetail(page, selecteduser_id, formattedDate)
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadusersname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadusersid[selectedPosition].toString()
                val selectedParentName = leadusersname[selectedPosition].toString()

                edtleaduser.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")
                page = 1
                getleadlogdetail(page, selecteduser_id, formattedDate)

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerleadlist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_callhistroy.layoutManager = mLayoutManager
        val useractivityAdapter = LeadLogDetailAdapter(this, leadloglist)
        recyclerView_callhistroy.adapter = useractivityAdapter
        recyclerView_callhistroy.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handelbackpress()
    }

    private fun handelbackpress() {
        startActivity(Intent(this@LeadCallDetailActivity,LeadActivity::class.java))
    }

    override fun onLastCallRemarkRequired(data: LastCallRemarkModel.Data) {
        runOnUiThread {
            MyApplication.getInstance().showRemarkPopupGlobal(data, this)
        }
    }
}