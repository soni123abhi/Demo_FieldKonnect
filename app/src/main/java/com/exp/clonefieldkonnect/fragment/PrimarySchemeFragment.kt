package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.SchemeDataShowTableAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.PrimaryFilterListModel
import com.exp.clonefieldkonnect.model.PrimarySchemeModel
import com.exp.clonefieldkonnect.model.PrimarySchemeTableModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response


class PrimarySchemeFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment() {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var linearTop_primary_report: CardView
    lateinit var fragment_container_primary_report: FrameLayout
    lateinit var cardBack_primary_report: CardView
    lateinit var cardshowww: CardView
    lateinit var textview3: TextView
    lateinit var tvTitle_primary_report: TextView
    lateinit var rel_main_primary_report: RelativeLayout
    lateinit var rel_dealer_table: RelativeLayout
    lateinit var edtSearchdivision: AutoCompleteTextView
    lateinit var edtyear: AutoCompleteTextView
    lateinit var edtquarter: AutoCompleteTextView
    lateinit var edtType: AutoCompleteTextView
    lateinit var edtScheme: AutoCompleteTextView
    lateinit var recyclerView_primary_scheme: RecyclerView

    var flag : String = ""
    private var isLoading = false

    var primaryschemelist : ArrayList<PrimarySchemeModel.Data> = ArrayList()
    var primaryschemename : ArrayList<String> = ArrayList()
    var primaryschemeid : ArrayList<String> = ArrayList()
    var primarydivisionlist : ArrayList<PrimaryFilterListModel.Divisions> = ArrayList()
    var primarydivisionname : ArrayList<String> = ArrayList()
    var primarydivisionid : ArrayList<String> = ArrayList()
    var primaryyearlist : ArrayList<PrimaryFilterListModel.Years> = ArrayList()
    var primaryyearname : ArrayList<String> = ArrayList()
    var primaryyearid : ArrayList<String> = ArrayList()
    var primaryquarterlist : ArrayList<PrimaryFilterListModel.Quarters> = ArrayList()
    var primaryquartername : ArrayList<String> = ArrayList()
    var primaryquarterid : ArrayList<String> = ArrayList()
    var primarytypelist : ArrayList<PrimaryFilterListModel.Types> = ArrayList()
    var primarytypename : ArrayList<String> = ArrayList()
    var primarytypeid : ArrayList<String> = ArrayList()
    var schemetabledatashow : ArrayList<PrimarySchemeTableModel.Data> = ArrayList()
    var selecteddivision_id = ""
    var selectedyear_id = ""
    var selectedquarter_id = ""
    var selectedtype_id = ""
    var selectedscheme_id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_primary_scheme, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        fragment_container_primary_report = rootView.findViewById(R.id.fragment_container_primary_report)
        cardBack_primary_report = rootView.findViewById(R.id.cardBack_primary_report)
        tvTitle_primary_report = rootView.findViewById(R.id.tvTitle_primary_report)
        rel_main_primary_report = rootView.findViewById(R.id.rel_main_primary_report)
        linearTop_primary_report = rootView.findViewById(R.id.linearTop_primary_report)
        edtSearchdivision = rootView.findViewById(R.id.edtSearchdivision)
        edtyear = rootView.findViewById(R.id.edtyear)
        edtquarter = rootView.findViewById(R.id.edtquarter)
        edtType = rootView.findViewById(R.id.edtType)
        edtScheme = rootView.findViewById(R.id.edtScheme)
        cardshowww = rootView.findViewById(R.id.cardshowww)
        rel_dealer_table = rootView.findViewById(R.id.rel_dealer_table)
        recyclerView_primary_scheme = rootView.findViewById(R.id.recyclerView_primary_scheme)
        textview3 = rootView.findViewById(R.id.textview3)


        getfilterdata()

        cardshowww.setOnClickListener {
            if (selecteddivision_id.isNullOrEmpty()){
                responsemessage("Please Select Division")
            }else if (selectedyear_id.isNullOrEmpty()){
                responsemessage("Please Select Financial Year")
            }else if (!selecteddivision_id.equals("FAN") && selectedquarter_id.isNullOrEmpty() ){
                responsemessage("Please Select Quarter")
            }else if (selectedscheme_id.isNullOrEmpty()){
                responsemessage("Please Select Primary Scheme")
            }else{
                showprimaryscheme(selecteddivision_id,selectedyear_id,selectedquarter_id,selectedtype_id,selectedscheme_id)
            }
        }

        cardBack_primary_report.setOnClickListener {
            handleBackPressed()
        }

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


    private fun showprimaryscheme(
        selecteddivision_id: String,
        selectedyear_id: String,
        selectedquarter_id: String,
        selectedtype_id: String,
        selectedscheme_id: String
    ) {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["division"] = selecteddivision_id
        queryParams["financial_year"] = selectedyear_id
        queryParams["quarter"] = selectedquarter_id
        queryParams["types"] = selectedtype_id
        queryParams["scheme_id"] = selectedscheme_id

        println("Datat=="+selecteddivision_id+"<<"+selectedyear_id+"<<"+selectedquarter_id+"<<"+selectedtype_id+"<<"+
                selectedscheme_id)

        ApiClient.getprimaryschemeshowdata(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<PrimarySchemeTableModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<PrimarySchemeTableModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            rel_dealer_table.visibility = View.VISIBLE
                            schemetabledatashow.clear()
                            schemetabledatashow.addAll(response.body()!!.data)
                            setuprecyclervieww(schemetabledatashow)

                        } else {
                            rel_dealer_table.visibility = View.GONE

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
                        rel_dealer_table.visibility = View.GONE
                        Toast.makeText(activityLocal, resources.getString(R.string.poor_connection), Toast.LENGTH_LONG).show()
                    }
                }
            })


    }

    private fun setuprecyclervieww(schemetabledatashow: ArrayList<PrimarySchemeTableModel.Data>) {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_primary_scheme.layoutManager = mLayoutManager
        val useractivityAdapter = SchemeDataShowTableAdapter(activityLocal, schemetabledatashow)
        recyclerView_primary_scheme.adapter = useractivityAdapter
        useractivityAdapter.notifyDataSetChanged()
    }

    private fun getschemelist(selecteddivisionId: String, selectedquarter_Id: String) {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["division"] = selecteddivisionId
        queryParams["quarter"] = selectedquarter_Id

        ApiClient.getprimaryschemedata(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<PrimarySchemeModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<PrimarySchemeModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            primaryschemelist.clear()
                            primaryschemename.clear()
                            primaryschemeid.clear()

                            primaryschemelist.addAll(response.body()!!.data)

                            for (item in primaryschemelist) {
                                val name = item.schemeName.toString()
                                val id = item.id.toString()

                                if (!primaryschemename.contains(name)) {
                                    primaryschemename.add(name)
                                    primaryschemeid.add(id)
                                }
                            }

                            println("selecteddivision_idselecteddivision_id=="+selecteddivision_id+"<<"+selectedquarter_id+"<<"+primaryschemelist.size)

                            edtScheme.setOnClickListener {
                                if (selecteddivision_id.equals("FAN")){
                                    spinnerscheme()
                                }else if (selecteddivision_id.equals("PUMP") || selecteddivision_id.equals("MOTOR") && selectedquarter_id.isNotEmpty()){
                                    spinnerscheme()
                                }else{
                                    responsemessage("Please Select Quarter")
                                }
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

    private fun getfilterdata() {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()

        ApiClient.getprimaryfilterdata(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams,
            object : APIResultLitener<PrimaryFilterListModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<PrimaryFilterListModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            primarydivisionlist.clear()
                            primaryyearlist.clear()
                            primaryquarterlist.clear()
                            primarytypelist.clear()

                            primarydivisionlist.addAll(response.body()!!.data!!.divisions)
                            primaryyearlist.addAll(response.body()!!.data!!.years)
                            primaryquarterlist.addAll(response.body()!!.data!!.quarters)
                            primarytypelist.addAll(response.body()!!.data!!.types)



                            for (item in primarydivisionlist) {
                                val name = item.value.toString()
                                val id = item.key.toString()

                                if (!primarydivisionname.contains(name)) {
                                    primarydivisionname.add(name)
                                    primarydivisionid.add(id)
                                }
                            }

                            for (item in primaryyearlist) {
                                val name = item.value.toString()
                                val id = item.key.toString()

                                if (!primaryyearname.contains(name)) {
                                    primaryyearname.add(name)
                                    primaryyearid.add(id)
                                }
                            }

                            for (item in primaryquarterlist) {
                                val name = item.value.toString()
                                val id = item.key.toString()

                                if (!primaryquartername.contains(name)) {
                                    primaryquartername.add(name)
                                    primaryquarterid.add(id)
                                }
                            }
                            for (item in primarytypelist) {
                                val name = item.value.toString()
                                val id = item.key.toString()

                                if (!primarytypename.contains(name)) {
                                    primarytypename.add(name)
                                    primarytypeid.add(id)
                                }
                            }
                            var divisoin_id = StaticSharedpreference.getInfo(Constant.DIVISION_ID, activityLocal).toString()
//                            divisoin_id = "3"
//                            println("divisoin_iddivisoin_id=="+divisoin_id)

                            if (divisoin_id == "13") {
                                edtSearchdivision.isClickable = true
                                edtSearchdivision.isFocusable = true
                                edtSearchdivision.setOnClickListener {
                                    spinnerdivision()
                                }
                            } else {
                                edtSearchdivision.isClickable = false
                                edtSearchdivision.isFocusable = false
                                edtSearchdivision.setOnClickListener(null)
                                if (divisoin_id == "10") {
                                    edtSearchdivision.setText("PUMP")
                                    selecteddivision_id = "PUMP"
                                    edtquarter.visibility = View.VISIBLE
                                    textview3.visibility = View.VISIBLE
                                }else if (divisoin_id == "18") {
                                    edtSearchdivision.setText("MOTOR")
                                    selecteddivision_id = "MOTOR"
                                    edtquarter.visibility = View.VISIBLE
                                    textview3.visibility = View.VISIBLE
                                }else if (divisoin_id == "3") {
                                    edtSearchdivision.setText("FAN")
                                    selecteddivision_id = "FAN"
                                    getschemelist(selecteddivision_id, selectedquarter_id)
                                    edtquarter.visibility = View.GONE
                                    textview3.visibility = View.GONE
                                }else if (divisoin_id == "4") {
                                    edtSearchdivision.setText("AGRI")
                                    selecteddivision_id = "AGRI"
                                    edtquarter.visibility = View.VISIBLE
                                    textview3.visibility = View.VISIBLE
                                }
                            }


                           /* edtSearchdivision.setOnClickListener {
                                spinnerdivision()
                            }*/

                            edtyear.setOnClickListener {
                                spinneryear()
                            }

                            edtquarter.setOnClickListener {
                                if (selectedyear_id.isNullOrEmpty()){
                                   responsemessage("Please Select Year")
                                }else{
                                    spinnerquarter()
                                }
                            }

                            edtType.setOnClickListener {
                                spinnertype()
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

    private fun responsemessage(message: String) {
        Toast.makeText(activityLocal,message,Toast.LENGTH_SHORT).show()
    }

    private fun spinnerdivision() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = primarydivisionname.map { it.toString() }.toTypedArray()
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
            edtSearchdivision.setText("")
            selecteddivision_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = primarydivisionname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = primarydivisionid[selectedPosition].toString()
                val selectedParentName = primarydivisionname[selectedPosition].toString()

                edtSearchdivision.setText(selectedParentName)
                selecteddivision_id = selectedParentId

                println("Abhi=id=$selecteddivision_id")

                if (selecteddivision_id.equals("FAN")){
                    getschemelist(selecteddivision_id, selectedquarter_id)
                    edtquarter.visibility = View.GONE
                    textview3.visibility = View.GONE
                }else if (selecteddivision_id.equals("PUMP") || selecteddivision_id.equals("MOTOR") && selectedquarter_id.isNotEmpty()){
                    getschemelist(selecteddivision_id, selectedquarter_id)
                    edtquarter.visibility = View.VISIBLE
                    textview3.visibility = View.VISIBLE
                }
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

        val colorsArray = primaryyearname.map { it.toString() }.toTypedArray()
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
            val selectedPosition = primaryyearname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = primaryyearid[selectedPosition].toString()
                val selectedParentName = primaryyearname[selectedPosition].toString()

                edtyear.setText(selectedParentName)
                selectedyear_id = selectedParentId

                println("Abhi=id=$selectedyear_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinnerquarter() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = primaryquartername.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Quarter")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtquarter.setText("")
            selectedquarter_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = primaryquartername.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = primaryquarterid[selectedPosition].toString()
                val selectedParentName = primaryquartername[selectedPosition].toString()

                edtquarter.setText(selectedParentName)
                selectedquarter_id = selectedParentId

                println("Abhi=id=$selectedquarter_id")

                if (selecteddivision_id.isNotEmpty() && selectedquarter_id.isNotEmpty()){
                    getschemelist(selecteddivision_id,selectedquarter_id)
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinnertype() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = primarytypename.map { it.toString() }.toTypedArray()
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
            edtType.setText("")
            selectedtype_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = primarytypename.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = primarytypeid[selectedPosition].toString()
                val selectedParentName = primarytypename[selectedPosition].toString()

                edtType.setText(selectedParentName)
                selectedtype_id = selectedParentId

                println("Abhi=id=$selectedtype_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinnerscheme() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = primaryschemename.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Scheme")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtScheme.setText("")
            selectedscheme_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = primaryschemename.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = primaryschemeid[selectedPosition].toString()
                val selectedParentName = primaryschemename[selectedPosition].toString()

                edtScheme.setText(selectedParentName)
                selectedscheme_id = selectedParentId

                println("Abhi=id=$selectedscheme_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }


    private fun handleBackPressed() {
        println("flag=="+flag)
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag.equals("")){
            tabPosition = 2
            tvTitle_primary_report.text = "Report"
            rel_main_primary_report.visibility = View.GONE
            linearTop_primary_report.visibility = View.GONE
            fragment_container_primary_report.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }else if (flag.equals("2")){
            flag = ""
            tvTitle_primary_report.text = "Primary Scheme Report"
            rel_main_primary_report.visibility = View.VISIBLE
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_primary_report, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}