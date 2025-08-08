package com.exp.clonefieldkonnect.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.adapter.LeadopportunityStatusAdapter
import com.exp.clonefieldkonnect.adapter.OpportunityListAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.LeadContactModel
import com.exp.clonefieldkonnect.model.OportunityDetailModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response

class OpportunityViewActivity : AppCompatActivity(),LeadopportunityStatusAdapter.OnEmailClick,
    OpportunityListAdapter.OnEmailClick{
    lateinit var cardBack_activity: CardView
    lateinit var recyclerView_lead_status: RecyclerView
    lateinit var recyclerView_oppoo: RecyclerView
    lateinit var tv_lead_status: TextView
    lateinit var tv_oppoo_count: TextView
    lateinit var tv_oppoo_price: TextView
    lateinit var notification_count: TextView
    lateinit var edtleaduser: AutoCompleteTextView
    lateinit var relll_statuss: RelativeLayout
    lateinit var ic_notification: ImageView



    var leaduserlist: ArrayList<OportunityDetailModel.Users> = ArrayList()
    var leadusersname : ArrayList<String> = ArrayList()
    var leadusersid : ArrayList<String> = ArrayList()

    var leadstatuslist: ArrayList<OportunityDetailModel.Counter> = ArrayList()
    var oppoortunitylist: ArrayList<OportunityDetailModel.Opportunities> = ArrayList()
    var oppoortunitylist2: ArrayList<OportunityDetailModel.Opportunities> = ArrayList()

    var leadcontactlist: ArrayList<LeadContactModel.Contacts> = ArrayList()
    var leadoppstatuslist: ArrayList<LeadContactModel.OpportunityStatuses> = ArrayList()
    var leadcontactname : ArrayList<String> = ArrayList()
    var leadcontactid : ArrayList<String> = ArrayList()
    var leadoppstatusname : ArrayList<String> = ArrayList()
    var leadoppstatusid : ArrayList<String> = ArrayList()
    var selectedcontact_id :String = ""
    var selectedstatus_id :String = ""


    private var lastPosition = -1
    private var isLoading = false
    private var page = 1
    private var pageSize = "30"
    var page_count : String = ""
    var leadststus_id :String = "-1"
    var selecteduser_id :String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opportunity_view)
        initViews()

    }

    private fun initViews() {
        cardBack_activity = findViewById(R.id.cardBack)
        recyclerView_lead_status = findViewById(R.id.recyclerView_lead_status)
        recyclerView_oppoo = findViewById(R.id.recyclerView_oppoo)
        tv_lead_status = findViewById(R.id.tv_lead_status)
        tv_oppoo_count = findViewById(R.id.tv_oppoo_count)
        tv_oppoo_price = findViewById(R.id.tv_oppoo_price)
        edtleaduser = findViewById(R.id.edtleaduser)
        relll_statuss = findViewById(R.id.relll_statuss)
        ic_notification = findViewById(R.id.ic_notification)
        notification_count = findViewById(R.id.notification_count)


        getopportunity(page, leadststus_id, selecteduser_id)

        recyclerView_oppoo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (oppoortunitylist2.size == 30){
                        getopportunity(page, leadststus_id, selecteduser_id)

                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })


        ic_notification.setOnClickListener {
            startActivity(Intent(this@OpportunityViewActivity,NotificationActivity::class.java))
        }

        cardBack_activity.setOnClickListener {
            handelbackpress()
        }

    }


    private fun getopportunity(page: Int, leadststus_id: String, selecteduser_id: String) {
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
        queryParams["user_id"] = selecteduser_id

        println("queryParams=="+queryParams)
        ApiClient.getopportunity(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<OportunityDetailModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<OportunityDetailModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    page_count = ""
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadstatuslist.clear()
                            oppoortunitylist2.clear()
                            leaduserlist.clear()
                            notification_count.text = response.body()!!.notification_count.toString()


                            if (page ==1)
                                oppoortunitylist.clear()

                            leaduserlist.addAll(response.body()!!.data!!.users)

                            leadstatuslist.addAll(response.body()!!.data!!.counter)
                            oppoortunitylist.addAll(response.body()!!.data!!.opportunities)
                            oppoortunitylist2.addAll(response.body()!!.data!!.opportunities)

                            for (item in leaduserlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadusersname.contains(name)) {
                                    leadusersname.add(name)
                                    leadusersid.add(id)
                                }
                            }

                            edtleaduser.setOnClickListener {
                                spinnerusername(edtleaduser)
                            }


                            val matchingStatus = response.body()?.data?.counter?.find { it.statusId.toString() == leadststus_id }

                            println("ssssssss=="+leadstatuslist+"<<"+matchingStatus)


                            if (matchingStatus != null) {
                                tv_lead_status.text = matchingStatus.statusName
                                tv_oppoo_count.text = "${matchingStatus.totalOpportunities} opportunity"
                                tv_oppoo_price.text = "₹${matchingStatus.totalAmount}"

                                when (matchingStatus.statusId) {
                                    -1 -> relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
                                    4 -> relll_statuss.setBackgroundColor(Color.parseColor("#FC4F38"))
                                    3 -> relll_statuss.setBackgroundColor(Color.parseColor("#FDA73E"))
                                    8 -> relll_statuss.setBackgroundColor(Color.parseColor("#1793D1"))
                                    else -> relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
                                }
                            } else {
                                tv_lead_status.text = ""
                                tv_oppoo_count.text = ""
                                tv_oppoo_price.text = ""
                            }


                            setuprecyclerstatuslist()
                            setuprecyclerleadlist()
                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@OpportunityViewActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@OpportunityViewActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun spinnerstatusss(edtstatus: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadoppstatusname.map { it.toString() }.toTypedArray()
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
            edtstatus.setText("")
            selectedstatus_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadoppstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadoppstatusid[selectedPosition].toString()
                val selectedParentName = leadoppstatusname[selectedPosition].toString()

                edtstatus.setText(selectedParentName)
                selectedstatus_id = selectedParentId

                println("Abhi=id=$selectedstatus_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    private fun spinnercontact(edtcontact: AutoCompleteTextView) {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadcontactname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Contact")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtcontact.setText("")
            selectedcontact_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadcontactname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadcontactid[selectedPosition].toString()
                val selectedParentName = leadcontactname[selectedPosition].toString()

                edtcontact.setText(selectedParentName)
                selectedcontact_id = selectedParentId

                println("Abhi=id=$selectedcontact_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
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
                getopportunity(1,leadststus_id,selecteduser_id)


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerleadlist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView_oppoo.layoutManager = mLayoutManager
        val useractivityAdapter = OpportunityListAdapter(this, oppoortunitylist, this)
        recyclerView_oppoo.adapter = useractivityAdapter
        recyclerView_oppoo.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecyclerstatuslist() {
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_lead_status.layoutManager = mLayoutManager
        val useractivityAdapter = LeadopportunityStatusAdapter(this, leadstatuslist, this)
        recyclerView_lead_status.adapter = useractivityAdapter
        recyclerView_lead_status.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }


    private fun handelbackpress() {
        startActivity(Intent(this@OpportunityViewActivity,LeadActivity::class.java))
    }

    override fun onClickEmail1(id: Int?) {
        println("statusssiddd=="+id+"<<"+selecteduser_id)
        /*if (id!!.equals(-1)){
            leadststus_id = ""
        }else{
            leadststus_id = id.toString()
        }*/
        leadststus_id = id.toString()

        getopportunity(1, leadststus_id, selecteduser_id)
    }

    override fun onClickopportunity(
        id: Int?,
        tag: String,
        item: OportunityDetailModel.Opportunities
    ) {
        println("statusssiddd=="+id+"<<"+tag+"<<"+item)
        if (tag.equals("edit")){
            showopportunitypopup(id,item)
        }else if (tag.equals("delete")){
            deleteopportunity(item)
        }
    }

    private fun deleteopportunity(item: OportunityDetailModel.Opportunities) {
        if (!Utilities.isOnline(this@OpportunityViewActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@OpportunityViewActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["opportunity_id"] = item.id.toString()



        ApiClient.deleteleadopportunity(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@OpportunityViewActivity).toString(),
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
                            Toast.makeText(this@OpportunityViewActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            getopportunity(1,"-1","")
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@OpportunityViewActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    @SuppressLint("MissingInflatedId")
    private lateinit var alertDialog: AlertDialog

    private fun showopportunitypopup(id: Int?, item: OportunityDetailModel.Opportunities) {
        val builder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_opportunity_lead_layout, null)
        builder.setCancelable(false)

        val img_close: ImageView = view.findViewById(R.id.img_close)
        val tv_confidence_value: TextView = view.findViewById(R.id.tv_confidence_value)
        val seekBar_confidence: SeekBar = view.findViewById(R.id.seekBar_confidence)
        val edtusername: AutoCompleteTextView = view.findViewById(R.id.edtusername)
        val edtamount: AutoCompleteTextView = view.findViewById(R.id.edtamount)
        val edtcontact: AutoCompleteTextView = view.findViewById(R.id.edtcontact)
        val edtnote: AutoCompleteTextView = view.findViewById(R.id.edtnote)
        val edtdate: AutoCompleteTextView = view.findViewById(R.id.edtdate)
        val edtstatus: AutoCompleteTextView = view.findViewById(R.id.edtstatus)
        val cardcancel: CardView = view.findViewById(R.id.cardcancel)
        val cardsave: CardView = view.findViewById(R.id.cardsave)

        if (item != null){
            edtamount.setText(item.amount!!.toString())
            tv_confidence_value.setText(item.confidence!!.toString()+"%")
            seekBar_confidence.progress = item.confidence!!
            edtusername.setText(item.assignUser!!.name.toString())
            edtcontact.setText(item.leadContact!!.name.toString())
            edtnote.setText(item.note.toString())
            edtdate.setText(item.estimatedCloseDate.toString())
            edtstatus.setText(item.statusIs!!.statusName.toString())
            selecteduser_id = item.assignedTo.toString()
            selectedcontact_id = item.leadContactId.toString()
            selectedstatus_id =  item.status.toString()
        }

        getleadcontact(item.leadId.toString())

        edtusername.setOnClickListener {
            spinnerusername(edtusername)
        }

        edtcontact.setOnClickListener {
            spinnercontact(edtcontact)
        }

        edtstatus.setOnClickListener {
            spinnerstatusss(edtstatus)
        }


        edtdate.setOnClickListener {
            Utilities.datePicker(edtdate,this@OpportunityViewActivity)
        }

        seekBar_confidence.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_confidence_value.text = "$progress%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        cardsave.setOnClickListener {
            var date = Utilities.convertDateFormat(edtdate.text.toString())

            if (edtamount.text.toString().isNullOrEmpty()){
                responsemessage("Please Enter Amount")
            }else if (selecteduser_id.isNullOrEmpty()){
                responsemessage("Please Select User")
            }else if (selectedcontact_id.isNullOrEmpty()){
                responsemessage("Please Select Contact")
            }else if (edtnote.text.toString().isNullOrEmpty()){
                responsemessage("Please Enter Note")
            }else if (date.isNullOrEmpty()){
                responsemessage("Please Select Date")
            }else if (selectedstatus_id.isNullOrEmpty()){
                responsemessage("Please Select Status")
            }else{
                submitopportunity(item.leadId.toString(),edtamount.text.toString(),tv_confidence_value.text.toString(),selecteduser_id,
                    selectedcontact_id,edtnote.text.toString(),date,selectedstatus_id,item.id)
            }
        }


        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        cardcancel.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun submitopportunity(
        leadId: String,
        edtamount: String,
        confidence: String,
        selectedtaskuserid: String,
        selectedcontactId: String,
        note: String,
        date: String,
        selectedstatusId: String,
        id: Int?
    )
    {
        if (!Utilities.isOnline(this@OpportunityViewActivity)) {
            return
        }
        var dialog = DialogClass.progressDialog(this@OpportunityViewActivity)
        val queryParams = java.util.HashMap<String, String>()
        queryParams["opportunity_id"] = id.toString()
        queryParams["lead_id"] = leadId
        queryParams["amount"] = edtamount
        queryParams["confidence"] = confidence
        queryParams["assigned_to"] = selectedtaskuserid
        queryParams["lead_contact_id"] = selectedcontactId
        queryParams["note"] = note
        queryParams["estimated_close_date"] = date
        queryParams["status"] = selectedstatusId


        ApiClient.submitleadopportunity(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this@OpportunityViewActivity).toString(),
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
                            Toast.makeText(this@OpportunityViewActivity, response.body()!!.message, Toast.LENGTH_LONG).show()
                            getopportunity(1,selectedstatus_id,"")
                            alertDialog.dismiss()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    this@OpportunityViewActivity, false
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
        Toast.makeText(this@OpportunityViewActivity,s,Toast.LENGTH_SHORT).show()
    }

    private fun getleadcontact(lead_id: String) {
        isLoading = true

        if (!Utilities.isOnline(this)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(this)
        val queryParams = HashMap<String, String>()
        queryParams["lead_id"] = lead_id

        println("queryParams=="+queryParams)
        ApiClient.getleadcontact(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, this).toString(),
            queryParams,
            object : APIResultLitener<LeadContactModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<LeadContactModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            leadcontactlist.clear()
                            leadoppstatuslist.clear()


                            leadcontactlist.addAll(response.body()!!.data!!.contacts)
                            leadoppstatuslist.addAll(response.body()!!.data!!.opportunityStatuses)

                            for (item in leadcontactlist) {
                                val name = item.name.toString()
                                val id = item.id.toString()

                                if (!leadcontactname.contains(name)) {
                                    leadcontactname.add(name)
                                    leadcontactid.add(id)
                                }
                            }

                            for (item in leadoppstatuslist) {
                                val name = item.statusName.toString()
                                val id = item.id.toString()

                                if (!leadoppstatusname.contains(name)) {
                                    leadoppstatusname.add(name)
                                    leadoppstatusid.add(id)
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
                                    this@OpportunityViewActivity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        isLoading = false
                    }
                    else {
                        Toast.makeText(this@OpportunityViewActivity, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }



}