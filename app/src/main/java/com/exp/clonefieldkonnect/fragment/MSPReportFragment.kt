package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.MSPTableAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.Activities
import com.exp.clonefieldkonnect.model.Dataaaaa
import com.exp.clonefieldkonnect.model.MSPFilterDataModel
import com.exp.clonefieldkonnect.model.MspTabledataModel
import com.exp.import.Utilities
import kotlinx.android.synthetic.main.fragment_m_s_p_report.fragment_container_msp_report
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap


class MSPReportFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment() {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var linearTop_msp_report: CardView
    lateinit var cardBack_msp_report: CardView
    lateinit var cardshowww: CardView
    lateinit var tvTitle_msp_report: TextView
    lateinit var rel_main_msp_report: RelativeLayout
    lateinit var edtuser: AutoCompleteTextView
    lateinit var edtSearchbranch: AutoCompleteTextView
    lateinit var edtyear: AutoCompleteTextView
    lateinit var recyclerView_msp_scheme: RecyclerView

    var flag = ""
    var selecteduser_id = ""
    var selectedbranch_id = ""
    var selectedyear_id = ""
    private var isLoading = false
    private var userlist :ArrayList<MSPFilterDataModel.Users> = ArrayList()
    private var branchlist :ArrayList<MSPFilterDataModel.Branches> = ArrayList()
    private var yearlist :ArrayList<MSPFilterDataModel.FinancialYear> = ArrayList()
    private var tablelist :ArrayList<Dataaaaa> = ArrayList()
    private var tableactivitylist :ArrayList<Activities> = ArrayList()
    private var pdf_url : String = ""

    private var userlistname :ArrayList<String> = ArrayList()
    private var branchlistame :ArrayList<String> = ArrayList()
    private var yearlistlistame :ArrayList<String> = ArrayList()
    private var userlistid :ArrayList<String> = ArrayList()
    private var branchlistid :ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_m_s_p_report, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        linearTop_msp_report = rootView.findViewById(R.id.linearTop_msp_report)
        cardBack_msp_report = rootView.findViewById(R.id.cardBack_msp_report)
        tvTitle_msp_report = rootView.findViewById(R.id.tvTitle_msp_report)
        rel_main_msp_report = rootView.findViewById(R.id.rel_main_msp_report)
        edtuser = rootView.findViewById(R.id.edtuser)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        edtyear = rootView.findViewById(R.id.edtyear)
        cardshowww = rootView.findViewById(R.id.cardshowww)
        recyclerView_msp_scheme = rootView.findViewById(R.id.recyclerView_msp_scheme)


        getfilterdata()
        gettabledata("","","")

        cardshowww.setOnClickListener {
            if (pdf_url.isNullOrEmpty()){
                responsemessage("Pdf is not available")
            }else{
                downloadPdf(pdf_url)
            }
        }

        cardBack_msp_report.setOnClickListener {
            handleBackPressed()
        }
    }

    private fun responsemessage(s: String) {
        Toast.makeText(activityLocal,s,Toast.LENGTH_SHORT).show()
    }

    private fun downloadPdf(pdfUrl: String) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "MSP_Report.pdf")
        val downloadManager = activityLocal.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(activityLocal,"Successfully downloaded",Toast.LENGTH_SHORT).show()
    }

    private fun gettabledata(
        selecteduser_id: String,
        selectedbranch_id: String,
        selectedyear_id: String
    ) {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["emp_code"] = selecteduser_id
        queryParams["branch_id"] = selectedbranch_id
        queryParams["financial_year"] = selectedyear_id

        ApiClient.getmsptabledata(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<MspTabledataModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<MspTabledataModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            tablelist.clear()
                            tableactivitylist.clear()
                            pdf_url = ""

                            tablelist.add(response.body()!!.data!!)
                            tableactivitylist = response.body()!!.activities

                            pdf_url = response!!.body()!!.pdfUrl!!.pdfUrl.toString()

                           setuprecyclerview(tablelist,tableactivitylist)

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



    private fun setuprecyclerview(
        tablelist: ArrayList<Dataaaaa>,
        tableactivitylist: ArrayList<Activities>
    ) {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_msp_scheme.layoutManager = mLayoutManager
        val useractivityAdapter = MSPTableAdapter(activityLocal, tablelist,tableactivitylist)
        recyclerView_msp_scheme.adapter = useractivityAdapter
        useractivityAdapter.notifyDataSetChanged()
    }

    private fun getfilterdata() {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()

        ApiClient.getmspfilterdata(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<MSPFilterDataModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<MSPFilterDataModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            userlist.clear()
                            branchlist.clear()
                            yearlist.clear()

                            userlist.addAll(response.body()!!.users)
                            branchlist.addAll(response.body()!!.branches)
                            yearlist.addAll(response.body()!!.financialYear)



                            for (item in userlist) {
                                val name = item.name.toString()
                                val employee_codes = item.employeeCodes.toString()

                                if (!userlistname.contains(name)) {
                                    userlistname.add(name)
                                    userlistid.add(employee_codes)
                                }
                            }

                            for (item in branchlist) {
                                val name = item.branchName.toString()
                                val id = item.id.toString()

                                if (!branchlistame.contains(name)) {
                                    branchlistame.add(name)
                                    branchlistid.add(id)
                                }
                            }

                            for (item in yearlist) {
                                val name = item.year.toString()

                                if (!yearlistlistame.contains(name)) {
                                    yearlistlistame.add(name)
                                }
                            }

                            edtuser.setOnClickListener {
                                spinneruser()
                            }

                            edtSearchbranch.setOnClickListener {
                               spinnerbranch()
                            }

                            edtyear.setOnClickListener {
                                spinneryear()
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


    private fun spinnerbranch() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = branchlistame.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Barnch")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtSearchbranch.setText("")
            selectedbranch_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = branchlistame.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = branchlistid[selectedPosition].toString()
                val selectedParentName = branchlistame[selectedPosition].toString()

                edtSearchbranch.setText(selectedParentName)
                selectedbranch_id = selectedParentId

                gettabledata(selecteduser_id,selectedbranch_id,selectedyear_id)

                println("Abhi=id=$selectedbranch_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinneryear() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = yearlistlistame.map { it.toString() }.toTypedArray()
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
            edtyear.setText("")
            selectedyear_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = yearlistlistame.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentName = yearlistlistame[selectedPosition].toString()

                edtyear.setText(selectedParentName)
                selectedyear_id = selectedParentName

                gettabledata(selecteduser_id,selectedbranch_id,selectedyear_id)


                println("Abhi=id=$selectedyear_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }



    private fun spinneruser() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = userlistname.map { it.toString() }.toTypedArray()
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
            edtuser.setText("")
            selecteduser_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = userlistname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = userlistid[selectedPosition].toString()
                val selectedParentName = userlistname[selectedPosition].toString()

                edtuser.setText(selectedParentName)
                selecteduser_id = selectedParentId

                println("Abhi=id=$selecteduser_id")
                gettabledata(selecteduser_id,selectedbranch_id,selectedyear_id)



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
        println("flag=="+flag)
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag.equals("")){
            tabPosition = 2
            tvTitle_msp_report.text = "Report"
            rel_main_msp_report.visibility = View.GONE
            linearTop_msp_report.visibility = View.GONE
            fragment_container_msp_report.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }else if (flag.equals("2")){
            flag = ""
            tvTitle_msp_report.text = "MSP Report"
            rel_main_msp_report.visibility = View.VISIBLE
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_msp_report, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}