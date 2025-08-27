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
import com.exp.clonefieldkonnect.adapter.DealerdegrowthAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DealergrowthModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response


class DealerGrowthFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment(),DealerdegrowthAdapter.OnEmailClick {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var cardBack_dealer_growth: CardView
    lateinit var tvTitle_dealer_growth: TextView
    lateinit var rel_main_dealer_growth: RelativeLayout
    lateinit var linearTop_dealer_growth: CardView
    lateinit var fragment_container_dealer_growth: FrameLayout
    lateinit var recyclerView_dealer_growth: RecyclerView
    lateinit var edtSearchdivision: AutoCompleteTextView
    lateinit var edtSearchbranch: AutoCompleteTextView
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtSearchyear: AutoCompleteTextView
    lateinit var edtstatus: AutoCompleteTextView

    private var lastPosition = -1
    var flag : String = ""
    private var isLoading = false
    private var page = 1
    private var pageSize = "20"
    private var selecteduser_id = ""
    private var selectedbranch_id = ""
    private var selectedyear_id = ""
    private var selecteddivision_id = ""
    private var selectedremark_id = ""

    var dealergrowthlist : ArrayList<DealergrowthModel.Data> = ArrayList()
    var dealergrowthlist2 : ArrayList<DealergrowthModel.Data> = ArrayList()
    var dealerbranchlist : ArrayList<DealergrowthModel.Branches> = ArrayList()
    var dealeruserlist : ArrayList<DealergrowthModel.Users> = ArrayList()
    var dealerremarklist : ArrayList<DealergrowthModel.Remarks> = ArrayList()
    var dealerdivisionlist : ArrayList<String> = ArrayList()
    var dealeryearlist : ArrayList<DealergrowthModel.YearRang> = ArrayList()
    var dealeryearnamelist : ArrayList<String> = ArrayList()
    var dealerusernamelist : ArrayList<String> = ArrayList()
    var dealeruseridlist : ArrayList<String> = ArrayList()
    var dealerbranchnamelist : ArrayList<String> = ArrayList()
    var dealerbranchidlist : ArrayList<String> = ArrayList()
    var dealerremarknamelist : ArrayList<String> = ArrayList()
    var dealerremarkidlist : ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_dealer_growth, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        fragment_container_dealer_growth = rootView.findViewById(R.id.fragment_container_dealer_growth)
        cardBack_dealer_growth = rootView.findViewById(R.id.cardBack_dealer_growth)
        tvTitle_dealer_growth = rootView.findViewById(R.id.tvTitle_dealer_growth)
        rel_main_dealer_growth = rootView.findViewById(R.id.rel_main_dealer_growth)
        linearTop_dealer_growth = rootView.findViewById(R.id.linearTop_dealer_growth)
        recyclerView_dealer_growth = rootView.findViewById(R.id.recyclerView_dealer_growth)
        edtSearchdivision = rootView.findViewById(R.id.edtSearchdivision)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchyear = rootView.findViewById(R.id.edtSearchyear)
        edtstatus = rootView.findViewById(R.id.edtstatus)

        getdealergrowthreport(
            page,
            selecteduser_id,
            selectedbranch_id,
            selectedyear_id,
            selecteddivision_id,
 selectedremark_id
        )

        recyclerView_dealer_growth.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (dealergrowthlist2.size == 20){
                        getdealergrowthreport(
                            page,
                            selecteduser_id,
                            selectedbranch_id,
                            selectedyear_id,
                            selecteddivision_id,
                            selectedremark_id
                        )
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })



        cardBack_dealer_growth.setOnClickListener {
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


    private fun getdealergrowthreport(
        page: Int,
        selecteduser_id: String,
        selectedbranch_id: String,
        selectedyear_id: String,
        selecteddivision_id: String,
        selectedremark_id: String
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
        queryParams["dealer_id"] = selecteduser_id
        queryParams["branch_id"] = selectedbranch_id
        queryParams["financial_year"] = selectedyear_id
        queryParams["division_id"] = selecteddivision_id
        queryParams["remark"] = selectedremark_id

        ApiClient.getdealergrowthreport(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<DealergrowthModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<DealergrowthModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            dealergrowthlist2.clear()
                            dealerbranchlist.clear()
                            dealeruserlist.clear()
                            dealerdivisionlist.clear()
                            dealerremarklist.clear()

                            if (page ==1)
                                dealergrowthlist.clear()

                            dealergrowthlist.addAll(response.body()!!.data)
                            dealergrowthlist2 = response.body()!!.data

                            dealerbranchlist.addAll(response.body()!!.branches)
                            dealeruserlist.addAll(response.body()!!.users)
                            dealeryearlist.addAll(response.body()!!.yearRang)
                            dealerdivisionlist.addAll(response.body()!!.psDivisions)
                            dealerremarklist.addAll(response.body()!!.remarks)

                            for (item in dealeruserlist) {
                                val name = item.dealer.toString()
                                val id = item.id.toString()

                                if (!dealerusernamelist.contains(name)) {
                                    dealerusernamelist.add(name)
                                    dealeruseridlist.add(id)
                                }
                            }

                            for (item in dealerbranchlist) {
                                val name = item.finalBranch.toString()
                                val id = item.id.toString()

                                if (!dealerbranchnamelist.contains(name)) {
                                    dealerbranchnamelist.add(name)
                                    dealerbranchidlist.add(id)
                                }
                            }
                            for (item in dealerremarklist) {
                                val name = item.title.toString()
                                val id = item.id.toString()

                                if (!dealerremarknamelist.contains(name)) {
                                    dealerremarknamelist.add(name)
                                    dealerremarkidlist.add(id)
                                }
                            }
                            for (item in dealeryearlist) {
                                val name = item.range.toString()

                                if (!dealeryearnamelist.contains(name)) {
                                    dealeryearnamelist.add(name)
                                }
                            }



                            edtSearchbranch.setOnClickListener {
                                spinnerbranch()
                            }

                            edtSearch.setOnClickListener {
                                spinneruser()
                            }
                            edtSearchyear.setOnClickListener {
                                spinneryear()
                            }
                            edtSearchdivision.setOnClickListener {
                                spinnerdivision()
                            }
                            edtstatus.setOnClickListener {
                                spinnerstatus()
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

    private fun spinnerstatus() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = dealerremarknamelist.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Remark")

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
            selectedremark_id = ""
            dialog.dismiss()
//            page = 1
//            getdelaersalesreport(
//                page,
//                selecteduser_id,
//                selectedbranch_id,
//                selectedyear_id,
//                selecteddivision_id
//            )
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = dealerremarknamelist.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = dealerremarkidlist[selectedPosition].toString()
                val selectedParentName = dealerremarknamelist[selectedPosition].toString()

                edtstatus.setText(selectedParentName)
                selectedremark_id = selectedParentId

                println("Abhi=id=$selectedremark_id")

                if (selectedremark_id.isNotEmpty()){
                    page = 1
                    getdealergrowthreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id,selectedremark_id
                    )
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

        val colorsArray = dealeryearnamelist.map { it.toString() }.toTypedArray()
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
            edtSearchyear.setText("")
            selectedyear_id = ""
            dialog.dismiss()
//            page = 1
//            getdelaersalesreport(
//                page,
//                selecteduser_id,
//                selectedbranch_id,
//                selectedyear_id,
//                selecteddivision_id
//            )
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = dealeryearnamelist.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
//                val selectedParentId = dealerbranchidlist[selectedPosition].toString()
                val selectedParentName = dealeryearnamelist[selectedPosition].toString()

                edtSearchyear.setText(selectedParentName)
                selectedyear_id = selectedParentName

                println("Abhi=id=$selectedyear_id")

                if (selectedyear_id.isNotEmpty()){
                    page = 1
                    getdealergrowthreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id,
 selectedremark_id
                    )
                }

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

        val colorsArray = dealerusernamelist.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(activityLocal, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Dealer/Distributor")

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
//            page = 1
//            getdelaersalesreport(
//                page,
//                selecteduser_id,
//                selectedbranch_id,
//                selectedyear_id,
//                selecteddivision_id
//            )
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = dealerusernamelist.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = dealeruseridlist[selectedPosition].toString()
                val selectedParentName = dealerusernamelist[selectedPosition].toString()

                edtSearch.setText(selectedParentName)
                selecteduser_id = selectedParentName

                println("Abhi=id=$selecteduser_id")

                if (selecteduser_id.isNotEmpty()){
                    page = 1
                    getdealergrowthreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id,
 selectedremark_id
                    )
                }
                dialog.dismiss()
            }
        }
        dialog.show() // Show the dialog
    }

    private fun spinnerbranch() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = dealerbranchnamelist.map { it.toString() }.toTypedArray()
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
            edtSearchbranch.setText("")
            selectedbranch_id = ""
            dialog.dismiss()
//            page = 1
//            getdelaersalesreport(
//                page,
//                selecteduser_id,
//                selectedbranch_id,
//                selectedyear_id,
//                selecteddivision_id
//            )
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = dealerbranchnamelist.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = dealerbranchidlist[selectedPosition].toString()
                val selectedParentName = dealerbranchnamelist[selectedPosition].toString()

                edtSearchbranch.setText(selectedParentName)
                selectedbranch_id = selectedParentName

                println("Abhi=id=$selectedbranch_id")

                if (selectedbranch_id.isNotEmpty()){
                    page = 1
                    getdealergrowthreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id,
 selectedremark_id
                    )
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun spinnerdivision() {
        val builder = AlertDialog.Builder(activityLocal)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = dealerdivisionlist.map { it.toString() }.toTypedArray()
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
//            page = 1
//            getdelaersalesreport(page, selecteduser_id, selectedbranch_id, selectedyear_id, selecteddivision_id)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = dealerdivisionlist.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
//                val selectedParentId = dealerbranchidlist[selectedPosition].toString()
                val selectedParentName = dealerdivisionlist[selectedPosition].toString()

                edtSearchdivision.setText(selectedParentName)
                selecteddivision_id = selectedParentName

                println("Abhi=id=$selecteddivision_id")

                if (selecteddivision_id.isNotEmpty()){
                    page = 1
                    getdealergrowthreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id,
                        selectedremark_id
                    )
                }

                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setuprecycleruserlist() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView_dealer_growth.layoutManager = mLayoutManager
        val useractivityAdapter = DealerdegrowthAdapter(activityLocal, dealergrowthlist,this@DealerGrowthFragment)
        recyclerView_dealer_growth.adapter = useractivityAdapter
        recyclerView_dealer_growth.scrollToPosition(lastPosition)
        useractivityAdapter.notifyDataSetChanged()
    }


    private fun handleBackPressed() {
        println("flag=="+flag)
        println("flag=tabPosition="+tabPosition)
        if (tabPosition.equals(2)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
        }
        else if (flag.equals("")){
            tabPosition = 2
            tvTitle_dealer_growth.text = "Report"
            rel_main_dealer_growth.visibility = View.GONE
            linearTop_dealer_growth.visibility = View.GONE
            fragment_container_dealer_growth.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }else if (flag.equals("2")){
            flag = ""
            tvTitle_dealer_growth.text = "Dealer Growth"
            rel_main_dealer_growth.visibility = View.VISIBLE
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_dealer_growth, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onClickEmail_expense(name: String) {
        println("namename=="+name)
    }

}