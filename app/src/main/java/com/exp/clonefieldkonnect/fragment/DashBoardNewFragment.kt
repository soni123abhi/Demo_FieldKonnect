package com.exp.clonefieldkonnect.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.*
import com.exp.clonefieldkonnect.adapter.TaskAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.TaskModel
import com.exp.clonefieldkonnect.model.TopDataModel
import com.exp.import.Utilities
import com.ekndev.gaugelibrary.HalfGauge
import com.ekndev.gaugelibrary.Range
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response

class DashBoardNewFragment : Fragment(), View.OnClickListener, DialogClass.FilterData {
    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private lateinit var cardAddCustomer: CardView
    private lateinit var cardViewFilter: CardView
    private lateinit var tvADhVisited: TextView
    private lateinit var tvADhTarget: TextView
    private lateinit var tvADhPercentage: TextView
    private lateinit var tvProVisited: TextView
    private lateinit var tvProSold: TextView
    private lateinit var tvProPercentage: TextView
    private lateinit var tvSalesAchieved: TextView
    private lateinit var tvSalesValue: TextView
    private lateinit var tvNewCustomer: TextView
    private lateinit var tvActiveCustomer: TextView
    private lateinit var tvTotalCustomer: TextView
    private lateinit var tvCustomerCustomer: TextView
    private lateinit var cardOrder: CardView
    private lateinit var cardTask: CardView
    private lateinit var rel_task: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    var beatArr: ArrayList<TaskModel> = arrayListOf()
    var beatArrDashboard: ArrayList<TaskModel> = arrayListOf()
    var punchInDetails: PunchInDetails? = null
    var arrTopCustomer: ArrayList<TopDataModel> = arrayListOf()
    var arrTopSKU: ArrayList<TopDataModel> = arrayListOf()
    var arrTopMarket: ArrayList<TopDataModel> = arrayListOf()
    var arrSalesSummary: ArrayList<TopDataModel> = arrayListOf()
    lateinit var taskAdapter: TaskAdapter
    lateinit var tvViewAll: TextView
    lateinit var halfGauge : HalfGauge
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_dashboard_new, container, false)
        activityLocal = activity as MainActivity

        initViews()
        return rootView
    }

    private fun initViews() {
        halfGauge = rootView.findViewById(R.id.halfGauge)
        tvViewAll = rootView.findViewById(R.id.tvViewAll)
        cardViewFilter = rootView.findViewById(R.id.cardViewFilter)
        cardAddCustomer = rootView.findViewById(R.id.cardAddCustomer)
        cardOrder = rootView.findViewById(R.id.cardOrder)
        cardTask = rootView.findViewById(R.id.cardTask)
        rel_task = rootView.findViewById(R.id.rel_task)
        tvADhVisited = rootView.findViewById(R.id.tvADhVisited)
        tvADhTarget = rootView.findViewById(R.id.tvADhTarget)
        tvADhPercentage = rootView.findViewById(R.id.tvADhPercentage)
        tvProVisited = rootView.findViewById(R.id.tvProVisited)
        tvProSold = rootView.findViewById(R.id.tvProSold)
        tvProPercentage = rootView.findViewById(R.id.tvProPercentage)
        tvSalesAchieved = rootView.findViewById(R.id.tvSalesAchieved)
        tvSalesValue = rootView.findViewById(R.id.tvSalesValue)
        tvNewCustomer = rootView.findViewById(R.id.tvNewCustomer)
        tvActiveCustomer = rootView.findViewById(R.id.tvActiveCustomer)
        tvTotalCustomer = rootView.findViewById(R.id.tvTotalCustomer)
        tvCustomerCustomer = rootView.findViewById(R.id.tvCustomerPer)
        recyclerView = rootView.findViewById(R.id.recyclerView)

        cardOrder.setOnClickListener(this)
        cardAddCustomer.setOnClickListener(this)
        cardViewFilter.setOnClickListener(this)
        cardTask.setOnClickListener(this)
        tvViewAll.setOnClickListener(this)

        dashboard()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cardAddCustomer -> {
                startActivity(Intent(activityLocal, StoreCustomerActivity::class.java))
            }
            R.id.cardTask -> {
                startActivity(Intent(activityLocal, AddTaskActivity::class.java))
            }
            R.id.cardViewFilter -> {
                DialogClass.filterDialog(activity, this)
            }
            R.id.cardOrder -> {
                val intent = Intent(activityLocal, ProductActivity::class.java)
                intent.putExtra("checkin", "n")
                intent.putExtra("beatScheduleId", "")
                startActivity(intent)
            }
            R.id.tvViewAll -> {
                val intent = Intent(activityLocal, TaskListActivity::class.java)
                intent.putExtra("arr", beatArr)
                startActivity(intent)
            }
        }
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
                                punchInDetails!!.onPunchInDetails(response.body()!!.get("data").asJsonObject.get("punchin").asBoolean, response.body()!!.get("data").asJsonObject.get("punchout").asBoolean)
                                var json = response.body()!!.get("data").asJsonObject
                                tvADhVisited.text = json.get("visitcounter").asInt.toString()
                                tvADhTarget.text = json.get("totalcounter").asInt.toString()
                                tvADhPercentage.text = json.get("adherence").asString.toString()
                                tvProVisited.text = json.get("visitcounter").asInt.toString()
                                tvProSold.text = json.get("productive_counter").asString.toString()
                                tvProPercentage.text = json.get("productivity").asString.toString()
                                tvSalesAchieved.text = json.get("achievement_amount").asString.toString()
                                tvSalesValue.text = json.get("orders_count").asInt.toString()
                                tvNewCustomer.text = json.get("new_added_counter").asInt.toString()
                                tvActiveCustomer.text = json.get("productive_counter").asString.toString()
                                tvTotalCustomer.text = json.get("totalcounter").asInt.toString()
                                tvCustomerCustomer.text = json.get("active_counter_percent").asString.toString()
                                var target = json.get("target").asFloat.toString()
                                var acheive = json.get("achievement").asFloat.toString()
                                setGuage(target.toDouble(),acheive.toDouble())
                                getUpcomingTasks()
                            }
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                DialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message"), activityLocal, false)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
    }


    private fun setGuage( target: Double,achieve : Double ) {
        val range = Range()
        range.color = Color.parseColor("#395299")
        range.from =0.0
        range.to = achieve

        val range3 = Range()
        range3.color = Color.parseColor("#00000000")
        range3.from = achieve
        range3.to = target

        //add color ranges to gauge
        //add color ranges to gauge
        halfGauge.addRange(range)
        halfGauge.addRange(range3)

        //set min max and current value
        //set min max and current value
        halfGauge.minValue = 0.0
        halfGauge.maxValue = (target).toDouble()
        halfGauge.value = (achieve).toDouble()

    }

    private fun getUpcomingTasks() {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        ApiClient.getUpcomingTasks(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(), queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            beatArrDashboard.clear()
                            val gson = Gson()
                            val listType = object : TypeToken<ArrayList<TaskModel>>() {}.type
                            beatArr = gson.fromJson(response.body()!!.get("data").asJsonArray, listType)
                            for(value in beatArr){
                                if(value.completed==0&&beatArrDashboard.size<5){
                                  beatArrDashboard.add(value)
                                }
                                if(beatArrDashboard.size==5){
                                    break
                                }
                            }
                            setAdapter()
                        }
                    } else {}
                }
            }
        )
    }

    fun setAdapter() {
        var mLayoutManager = LinearLayoutManager(activityLocal)
        recyclerView.layoutManager = mLayoutManager
        taskAdapter = TaskAdapter(activityLocal, beatArrDashboard)
        recyclerView.adapter = taskAdapter
    }

    public interface PunchInDetails {
        fun onPunchInDetails(punchIn: Boolean, punchOut: Boolean)
    }

    public fun punchInDetails(punchInDetails: PunchInDetails) {
        this.punchInDetails = punchInDetails
    }

    override fun filterClick(value: String) {
        dashboard(value)
    }
}
