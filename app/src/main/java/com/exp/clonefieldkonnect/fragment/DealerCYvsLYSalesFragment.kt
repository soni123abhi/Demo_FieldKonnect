package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.DealerSalesReportAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.DealerMonthlySalesReport
import com.exp.clonefieldkonnect.model.DealerSalesReportModel
import com.exp.import.Utilities
import kotlinx.android.synthetic.main.fragment_dealer_c_yvs_l_y_sales.*
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap


class DealerCYvsLYSalesFragment(
    var cardBack: CardView,
    var linearTopreport: CardView,
    var tabPosition: Int,
    var tvTitle: TextView
) : Fragment(),DealerSalesReportAdapter.OnEmailClick{

    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    lateinit var cardBack_dealer_sales: CardView
    lateinit var tvTitle_dealer_sales: TextView
    lateinit var linearTop_dealer_sales: CardView
    lateinit var rel_main_dealer_sales: RelativeLayout
    lateinit var recyclerView_dealer_sale: RecyclerView
    lateinit var edtSearch: AutoCompleteTextView
    lateinit var edtSearchbranch: AutoCompleteTextView
    lateinit var edtSearchyear: AutoCompleteTextView
    lateinit var edtSearchdivision: AutoCompleteTextView

    var searchbranch : ArrayList<String> = ArrayList()
    var dealersaleslist : ArrayList<DealerSalesReportModel.Data> = ArrayList()
    var dealersaleslist2 : ArrayList<DealerSalesReportModel.Data> = ArrayList()
    var dealerbranchlist : ArrayList<DealerSalesReportModel.Branches> = ArrayList()
    var dealeruserlist : ArrayList<DealerSalesReportModel.Users> = ArrayList()
    var dealerdivisionlist : ArrayList<String> = ArrayList()
    var dealeryearlist : ArrayList<DealerSalesReportModel.YearRang> = ArrayList()
    var dealeryearnamelist : ArrayList<String> = ArrayList()
    var dealerusernamelist : ArrayList<String> = ArrayList()
    var dealeruseridlist : ArrayList<String> = ArrayList()
    var dealerbranchnamelist : ArrayList<String> = ArrayList()
    var dealerbranchidlist : ArrayList<String> = ArrayList()

    private var lastPosition = -1
    var flag : String = ""
    private var isLoading = false
    private var page = 1
    private var pageSize = "4"
    private var selecteduser_id = ""
    private var selectedbranch_id = ""
    private var selectedyear_id = ""
    private var selecteddivision_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_dealer_c_yvs_l_y_sales, container, false)
        activityLocal = activity as MainActivity
        initViews()
        return rootView
    }

    private fun initViews() {
        linearTopreport.visibility = View.GONE
        cardBack_dealer_sales = rootView.findViewById(R.id.cardBack_dealer_sales)
        tvTitle_dealer_sales = rootView.findViewById(R.id.tvTitle_dealer_sales)
        linearTop_dealer_sales = rootView.findViewById(R.id.linearTop_dealer_sales)
        rel_main_dealer_sales = rootView.findViewById(R.id.rel_main_dealer_sales)
        recyclerView_dealer_sale = rootView.findViewById(R.id.recyclerView_dealer_sale)
        edtSearch = rootView.findViewById(R.id.edtSearch)
        edtSearchbranch = rootView.findViewById(R.id.edtSearchbranch)
        edtSearchyear = rootView.findViewById(R.id.edtSearchyear)
        edtSearchdivision = rootView.findViewById(R.id.edtSearchdivision)

        getdelaersalesreport(
            page,
            selecteduser_id,
            selectedbranch_id,
            selectedyear_id,
            selecteddivision_id
        )

        recyclerView_dealer_sale.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= firstVisibleItemPosition + visibleItemCount) {
                    page++
                    if (dealersaleslist2.size == 16){
                        getdelaersalesreport(page, selecteduser_id, selectedbranch_id,selectedyear_id, selecteddivision_id)
                        lastPosition = firstVisibleItemPosition
                    }
                }
            }
        })

        cardBack_dealer_sales.setOnClickListener {
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


    private fun getdelaersalesreport(
        page: Int,
        selecteduser_id: String,
        selectedbranch_id: String,
        selectedyear_id: String,
        selecteddivision_id: String
    ) {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["per_page"] = pageSize
        queryParams["page"] = page.toString()
        queryParams["dealer_id"] = selecteduser_id
        queryParams["branch_id"] = selectedbranch_id
        queryParams["financial_year"] = selectedyear_id
        queryParams["division_id"] = selecteddivision_id

        ApiClient.getdealersaleseport(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<DealerSalesReportModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<DealerSalesReportModel>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            dealersaleslist2.clear()
                            dealerbranchlist.clear()
                            dealeruserlist.clear()
                            dealerdivisionlist.clear()

                            if (page ==1)
                                dealersaleslist.clear()

                            dealersaleslist.addAll(response.body()!!.data)
                            dealersaleslist2 = response.body()!!.data
                            dealerbranchlist.addAll(response.body()!!.branches)
                            dealeruserlist.addAll(response.body()!!.users)
                            dealeryearlist.addAll(response.body()!!.yearRang)
                            dealerdivisionlist.addAll(response.body()!!.psDivisions)

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
                            for (item in dealeryearlist) {
                                val name = item.range.toString()

                                if (!dealeryearnamelist.contains(name)) {
                                    dealeryearnamelist.add(name)
                                }
                            }

                            println("dealersaleslist2dealersaleslist2=="+dealersaleslist2.size)


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
                    getdelaersalesreport(page,selecteduser_id,selectedbranch_id,selectedyear_id,selecteddivision_id)
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
                    getdelaersalesreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id
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
                    getdelaersalesreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id
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
                    getdelaersalesreport(
                        page,
                        selecteduser_id,
                        selectedbranch_id,
                        selectedyear_id,
                        selecteddivision_id
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
        recyclerView_dealer_sale.layoutManager = mLayoutManager
        val useractivityAdapter = DealerSalesReportAdapter(activityLocal, dealersaleslist,this@DealerCYvsLYSalesFragment)
        recyclerView_dealer_sale.adapter = useractivityAdapter
        recyclerView_dealer_sale.scrollToPosition(lastPosition)
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
            tvTitle_dealer_sales.text = "Report"
            rel_main_dealer_sales.visibility = View.GONE
            linearTop_dealer_sales.visibility = View.GONE
            fragment_container_dealer_sales.visibility = View.VISIBLE
            navigateToFragmentB(linearTopreport,tabPosition)
        }else if (flag.equals("2")){
            flag = ""
            tvTitle_dealer_sales.text = "Dealer CY vs LY Sales"
            rel_main_dealer_sales.visibility = View.VISIBLE
        }
    }

    private fun navigateToFragmentB(linearTopreport: CardView, tabPosition: Int) {
        val fragmentB = ReportFragment(linearTopreport, tabPosition,"")
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_dealer_sales, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onClickEmail_expense(name: String) {
        if (selectedyear_id.isNullOrEmpty()){
            Toast.makeText(activityLocal,"Please select year",Toast.LENGTH_SHORT).show()
        }else{
            getmonthlydetail(name,selectedyear_id)
            println("namename=="+name+"<<"+selectedyear_id)
        }
    }

    private fun getmonthlydetail(name: String, selectedyearId: String) {
        isLoading = true

        if (!Utilities.isOnline(activityLocal)) {
            isLoading = false
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["dealer_id"] = name
        queryParams["financial_year"] = selectedyearId

        ApiClient.getmonthlysaleseport(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<DealerMonthlySalesReport> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<DealerMonthlySalesReport>?, errorMessage: String?) {
                    dialog.dismiss()

                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            showPopupDialog(response.body()!!.data)
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

    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showPopupDialog(data: DealerMonthlySalesReport.Data?) {

        val builder = androidx.appcompat.app.AlertDialog.Builder(activityLocal)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_monthlydetail_layout, null)
        builder.setCancelable(false)


        val img_close: ImageView = view.findViewById(R.id.img_close)
        val tv_jan_val: TextView = view.findViewById(R.id.tv_jan_val)
        val tv_feb_value: TextView = view.findViewById(R.id.tv_feb_value)
        val tv_mar_val: TextView = view.findViewById(R.id.tv_mar_val)
        val tv_apr_val: TextView = view.findViewById(R.id.tv_apr_val)
        val tv_may_val: TextView = view.findViewById(R.id.tv_may_val)
        val tv_jun_val: TextView = view.findViewById(R.id.tv_jun_val)
        val tv_jul_val: TextView = view.findViewById(R.id.tv_jul_val)
        val tv_aug_val: TextView = view.findViewById(R.id.tv_aug_val)
        val tv_sep_val: TextView = view.findViewById(R.id.tv_sep_val)
        val tv_oct_val: TextView = view.findViewById(R.id.tv_oct_val)
        val tv_nov_val: TextView = view.findViewById(R.id.tv_nov_val)
        val tv_dec_val: TextView = view.findViewById(R.id.tv_dec_val)

        val tv_jan_tar_val: TextView = view.findViewById(R.id.tv_jan_tar_val)
        val tv_feb_tar_val: TextView = view.findViewById(R.id.tv_feb_tar_val)
        val tv_mar_tar_val: TextView = view.findViewById(R.id.tv_mar_tar_val)
        val tv_apr_tar_val: TextView = view.findViewById(R.id.tv_apr_tar_val)
        val tv_may_tar_val: TextView = view.findViewById(R.id.tv_may_tar_val)
        val tv_jun_tar_val: TextView = view.findViewById(R.id.tv_jun_tar_val)
        val tv_jul_tar_val: TextView = view.findViewById(R.id.tv_jul_tar_val)
        val tv_aug_tar_val: TextView = view.findViewById(R.id.tv_aug_tar_val)
        val tv_sep_tar_val: TextView = view.findViewById(R.id.tv_sep_tar_val)
        val tv_oct_tar_val: TextView = view.findViewById(R.id.tv_oct_tar_val)
        val tv_nov_tar_val: TextView = view.findViewById(R.id.tv_nov_tar_val)
        val tv_dev_tar_val: TextView = view.findViewById(R.id.tv_dev_tar_val)

        val tv_jan_per_val: TextView = view.findViewById(R.id.tv_jan_per_val)
        val tv_feb_per_val: TextView = view.findViewById(R.id.tv_feb_per_val)
        val tv_mar_per_val: TextView = view.findViewById(R.id.tv_mar_per_val)
        val tv_apr_per_val: TextView = view.findViewById(R.id.tv_apr_per_val)
        val tv_may_per_val: TextView = view.findViewById(R.id.tv_may_per_val)
        val tv_jun_per_val: TextView = view.findViewById(R.id.tv_jun_per_val)
        val tv_jul_per_val: TextView = view.findViewById(R.id.tv_jul_per_val)
        val tv_aug_per_val: TextView = view.findViewById(R.id.tv_aug_per_val)
        val tv_sep_per_val: TextView = view.findViewById(R.id.tv_sep_per_val)
        val tv_oct_per_val: TextView = view.findViewById(R.id.tv_oct_per_val)
        val tv_nov_per_val: TextView = view.findViewById(R.id.tv_nov_per_val)
        val tv_dev_per_val: TextView = view.findViewById(R.id.tv_dev_per_val)

        tv_jan_val.text = data!!.January!!.achiv.toString()
        tv_feb_value.text = data!!.February!!.achiv.toString()
        tv_mar_val.text = data!!.March!!.achiv.toString()
        tv_apr_val.text = data!!.April!!.achiv.toString()
        tv_may_val.text = data!!.May!!.achiv.toString()
        tv_jun_val.text = data!!.June!!.achiv.toString()
        tv_jul_val.text = data!!.July!!.achiv.toString()
        tv_aug_val.text = data!!.August!!.achiv.toString()
        tv_sep_val.text = data!!.September!!.achiv.toString()
        tv_oct_val.text = data!!.October!!.achiv.toString()
        tv_nov_val.text = data!!.November!!.achiv.toString()
        tv_dec_val.text = data!!.December!!.achiv.toString()

        tv_jan_tar_val.text = data!!.January!!.terg.toString()
        tv_feb_tar_val.text = data!!.February!!.terg.toString()
        tv_mar_tar_val.text = data!!.March!!.terg.toString()
        tv_apr_tar_val.text = data!!.April!!.terg.toString()
        tv_may_tar_val.text = data!!.May!!.terg.toString()
        tv_jun_tar_val.text = data!!.June!!.terg.toString()
        tv_jul_tar_val.text = data!!.July!!.terg.toString()
        tv_aug_tar_val.text = data!!.August!!.terg.toString()
        tv_sep_tar_val.text = data!!.September!!.terg.toString()
        tv_oct_tar_val.text = data!!.October!!.terg.toString()
        tv_nov_tar_val.text = data!!.November!!.terg.toString()
        tv_dev_tar_val.text = data!!.December!!.terg.toString()

        tv_jan_per_val.text = data!!.January!!.achivper.toString()+" %"
        tv_feb_per_val.text = data!!.February!!.achivper.toString()+" %"
        tv_mar_per_val.text = data!!.March!!.achivper.toString()+" %"
        tv_apr_per_val.text = data!!.April!!.achivper.toString()+" %"
        tv_may_per_val.text = data!!.May!!.achivper.toString()+" %"
        tv_jun_per_val.text = data!!.June!!.achivper.toString()+" %"
        tv_jul_per_val.text = data!!.July!!.achivper.toString()+" %"
        tv_aug_per_val.text = data!!.August!!.achivper.toString()+" %"
        tv_sep_per_val.text = data!!.September!!.achivper.toString()+" %"
        tv_oct_per_val.text = data!!.October!!.achivper.toString()+" %"
        tv_nov_per_val.text = data!!.November!!.achivper.toString()+" %"
        tv_dev_per_val.text = data!!.December!!.achivper.toString()+" %"

        img_close.setOnClickListener {
            alertDialog.dismiss()
        }

        builder.setView(view)

        alertDialog = builder.create()
        alertDialog.show()
    }


}