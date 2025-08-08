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
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.*
import com.exp.clonefieldkonnect.adapter.TopDataAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.TopDataModel
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DashBoardFragment : Fragment(), View.OnClickListener {
    lateinit var activityLocal: Activity

    private lateinit var rootView: View
    private lateinit var cardReport: CardView
    private lateinit var cardSales: CardView
    private lateinit var cardOrders: CardView
    private lateinit var cardCoupon: CardView
  //  private lateinit var tvName: TextView
    private lateinit var tvAcTg: TextView
    private lateinit var tvSKU: TextView
    private lateinit var tvAchievement: TextView
    private lateinit var tvSalesValue: TextView
    private lateinit var tvTcVc: TextView
    private lateinit var tvAdherence: TextView
    private lateinit var tvPC: TextView
    private lateinit var tvProductivity: TextView
    private lateinit var tvOutstanding: TextView
    private lateinit var tvCollection: TextView
    private lateinit var tvNDA: TextView
    private lateinit var tvAvarage: TextView

  //  private lateinit var imgProfile: ImageView

    private lateinit var cardTopCustomer: CardView
    private lateinit var cardTopSKU: CardView
    private lateinit var cardTopMarket: CardView
    private lateinit var cardSalesSummary: CardView
    private lateinit var cardTo: CardView
    private lateinit var cardFrom: CardView

    private lateinit var tvFrom: TextView
    private lateinit var tvTo: TextView
    private lateinit var tvTopCustomer: TextView
    private lateinit var tvTopSKU: TextView
    private lateinit var tvTopMarket: TextView
    private lateinit var tvSalesSummary: TextView
    private lateinit var tvNoData: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var topDataAdapter: TopDataAdapter
    private lateinit var cardSearch: RelativeLayout

    var arrTopCustomer: ArrayList<TopDataModel> = arrayListOf()
    var arrTopSKU: ArrayList<TopDataModel> = arrayListOf()
    var arrTopMarket: ArrayList<TopDataModel> = arrayListOf()
    var arrSalesSummary: ArrayList<TopDataModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_dashboard_new, container, false)
        activityLocal = activity as MainActivity

       // initViews()
        return rootView

    }

    private fun initViews() {
        cardSearch = rootView.findViewById(R.id.cardSearch)
        cardTo = rootView.findViewById(R.id.cardTo)
        cardFrom = rootView.findViewById(R.id.cardFrom)
        tvTo = rootView.findViewById(R.id.tvTo)
        tvFrom = rootView.findViewById(R.id.tvFrom)

        tvNoData = rootView.findViewById(R.id.tvNoData)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        cardTopCustomer = rootView.findViewById(R.id.cardTopCustomer)
        cardTopSKU = rootView.findViewById(R.id.cardTopSKU)
        cardTopMarket = rootView.findViewById(R.id.cardTopMarket)
        cardSalesSummary = rootView.findViewById(R.id.cardSalesSummary)

        tvTopCustomer = rootView.findViewById(R.id.tvTopCustomer)
        tvTopSKU = rootView.findViewById(R.id.tvTopSKU)
        tvTopMarket = rootView.findViewById(R.id.tvTopMarket)
        tvSalesSummary = rootView.findViewById(R.id.tvSalesSummary)

        tvAvarage = rootView.findViewById(R.id.tvAvarage)
        tvNDA = rootView.findViewById(R.id.tvNDA)
        tvCollection = rootView.findViewById(R.id.tvCollection)
        tvOutstanding = rootView.findViewById(R.id.tvOutstanding)
        tvProductivity = rootView.findViewById(R.id.tvProductivity)
        tvPC = rootView.findViewById(R.id.tvPC)
        tvAdherence = rootView.findViewById(R.id.tvAdherence)
        tvTcVc = rootView.findViewById(R.id.tvTcVc)
        tvSalesValue = rootView.findViewById(R.id.tvSalesValue)
        tvAchievement = rootView.findViewById(R.id.tvAchievement)
        tvSKU = rootView.findViewById(R.id.tvSKU)
        tvAcTg = rootView.findViewById(R.id.tvAcTg)

        cardReport = rootView.findViewById(R.id.cardReport)
        cardSales = rootView.findViewById(R.id.cardSales)
        cardCoupon = rootView.findViewById(R.id.cardCoupon)
        cardOrders = rootView.findViewById(R.id.cardOrders)
      //  tvName = rootView.findViewById(R.id.tvName)
      //  imgProfile = rootView.findViewById(R.id.imgProfile)


      //  tvName.text = StaticSharedpreference.getInfo(Constant.FIRM_NAME, activityLocal)

      /*  Glide.with(this)
            .load(StaticSharedpreference.getInfo(Constant.PROFILE_IMAGE, activityLocal))
            .into(imgProfile)*/

        cardReport.setOnClickListener(this)
         cardSales.setOnClickListener(this)
        cardOrders.setOnClickListener(this)
        cardCoupon.setOnClickListener(this)
        cardTopCustomer.setOnClickListener(this)
        cardTopMarket.setOnClickListener(this)
        cardTopSKU.setOnClickListener(this)
        cardSalesSummary.setOnClickListener(this)
        cardTo.setOnClickListener(this)
        cardFrom.setOnClickListener(this)
        cardSearch.setOnClickListener(this)

        recyclerView.isNestedScrollingEnabled = false
        setAdapter(arrTopCustomer)
        dashboard("", "")
    }

    private fun setAdapter(arrTop: ArrayList<TopDataModel>) {
        topDataAdapter = TopDataAdapter(activityLocal, arrTop)
        recyclerView.layoutManager = LinearLayoutManager(activityLocal)
        recyclerView.adapter = topDataAdapter
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.cardReport -> {
                val intent = Intent(activityLocal, VisitReportActivity::class.java)
                intent.putExtra("checkin", "n")
                startActivity(intent)
            }
            R.id.cardCoupon -> {
                startActivity(Intent(activityLocal, StoreCustomerActivity::class.java))
            }
            R.id.cardSales -> {

                val intent = Intent(activityLocal, PaymentReceivedActivity::class.java)
                intent.putExtra("from", "dashboard")
                startActivity(intent)

            }
            R.id.cardOrders -> {
                val intent = Intent(activityLocal, ProductActivity::class.java)
                intent.putExtra("checkin", "n")
                intent.putExtra("beatScheduleId", "")
                startActivity(intent)
            }
            R.id.cardTopCustomer -> {
                inActiveTopData()
                cardTopCustomer.setCardBackgroundColor(Color.parseColor("#685BD6"))
                tvTopCustomer.setTextColor(Color.parseColor("#ffffff"))

                setAdapter(arrTopCustomer)
                tvNoData.visibility = View.GONE
                if (arrTopCustomer.isEmpty()) {
                    tvNoData.visibility = View.VISIBLE
                    tvNoData.text = "No Data available for Customer"
                }
            }
            R.id.cardTopSKU -> {
                inActiveTopData()
                cardTopSKU.setCardBackgroundColor(Color.parseColor("#685BD6"))
                tvTopSKU.setTextColor(Color.parseColor("#ffffff"))
                setAdapter(arrTopSKU)
                tvNoData.visibility = View.GONE
                if (arrTopSKU.isEmpty()) {
                    tvNoData.visibility = View.VISIBLE
                    tvNoData.text = "No Data available for SKU"
                }
            }
            R.id.cardTopMarket -> {
                inActiveTopData()
                cardTopMarket.setCardBackgroundColor(Color.parseColor("#685BD6"))
                tvTopMarket.setTextColor(Color.parseColor("#ffffff"))
                setAdapter(arrTopMarket)
                tvNoData.visibility = View.GONE
                if (arrTopMarket.isEmpty()) {
                    tvNoData.visibility = View.VISIBLE
                    tvNoData.text = "No Data available for Market"
                }
            }
            R.id.cardSalesSummary -> {
                inActiveTopData()
                cardSalesSummary.setCardBackgroundColor(Color.parseColor("#685BD6"))
                tvSalesSummary.setTextColor(Color.parseColor("#ffffff"))

                setAdapter(arrSalesSummary)
                tvNoData.visibility = View.GONE
                if (arrSalesSummary.isEmpty()) {
                    tvNoData.visibility = View.VISIBLE
                    tvNoData.text = "No Data available for Sales Summary"
                }
            }
            R.id.cardFrom -> {

                Utilities.datePicker(tvFrom, tvTo.text.toString(), "", true, activityLocal)

            }
            R.id.cardTo -> {

                Utilities.datePicker(tvTo, "", tvFrom.text.toString(), false, activityLocal)

            }
            R.id.cardSearch -> {

                if (tvFrom.text.toString() != "" || tvTo.text.toString() != "")
                    dashboard(tvFrom.text.toString(), tvTo.text.toString())

            }
        }
    }

    private fun inActiveTopData() {

        cardTopCustomer.setCardBackgroundColor(Color.parseColor("#f2f2f2"))
        tvTopCustomer.setTextColor(Color.parseColor("#685BD6"))
        cardTopSKU.setCardBackgroundColor(Color.parseColor("#f2f2f2"))
        tvTopSKU.setTextColor(Color.parseColor("#685BD6"))
        cardTopMarket.setCardBackgroundColor(Color.parseColor("#f2f2f2"))
        tvTopMarket.setTextColor(Color.parseColor("#685BD6"))
        cardSalesSummary.setCardBackgroundColor(Color.parseColor("#f2f2f2"))
        tvSalesSummary.setTextColor(Color.parseColor("#685BD6"))

    }

    private fun dashboard(from: String, toDate: String) {

        if (!Utilities.isOnline(activityLocal)) {
            return
        }

        var fromDateStr = ""
        var toDateStr = ""
        if(from!="") {
            val dateFrom: Date = SimpleDateFormat("dd-MM-yyyy").parse(from)
             fromDateStr = SimpleDateFormat("yyyy-MM-dd").format(dateFrom)
        }
         if(toDate!="") {
            val dateTo: Date = SimpleDateFormat("dd-MM-yyyy").parse(toDate)
             toDateStr = SimpleDateFormat("yyyy-MM-dd").format(dateTo)
        }



        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["fromdate"] = fromDateStr
        queryParams["todate"] = toDateStr
        ApiClient.dashboard(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<JsonObject> {
                override fun onAPIResult(response: Response<JsonObject>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            StaticSharedpreference.saveInfo(
                                Constant.IS_PUNCH_IN,
                                response.body()!!
                                    .get("data").asJsonObject.get("punchin").asBoolean.toString(),
                                activityLocal
                            )

                            StaticSharedpreference.saveInfo(
                                Constant.IS_PUNCH_OUT,
                                response.body()!!
                                    .get("data").asJsonObject.get("punchout").asBoolean.toString(),
                                activityLocal
                            )

                            var json = response.body()!!.get("data").asJsonObject

                            tvAcTg.setText(
                                json.get("achievement_amount").asString + "/" + json.get(
                                    "target_amount"
                                ).asString
                            )
                            tvSKU.setText(json.get("uniquesku_qty").asInt.toString())
                            tvAchievement.setText(json.get("achievement_percent").asInt.toString())
                            tvSalesValue.setText(json.get("sales_amount").asString.toString())
                            tvTcVc.setText(
                                json.get("totalcounter").asInt.toString() + "/" + json.get(
                                    "visitcounter"
                                ).asInt.toString()
                            ) //Remiaining
                            tvAdherence.setText(json.get("adherence").asString.toString())
                            tvPC.setText(json.get("productive_counter").asString.toString())
                            tvProductivity.setText(json.get("productivity").asInt.toString()) //require percent
                            tvOutstanding.setText(json.get("outstanding_amount").asString.toString())
                            tvCollection.setText(json.get("collection_amount").asString.toString())
                            tvAvarage.setText(json.get("average_sales").asString.toString())
                            tvNDA.setText(json.get("nda").asString.toString())


                            var jsonArrCustomer = json.get("top_customers").asJsonArray

                            for ((index, value) in jsonArrCustomer.withIndex()) {

                                var jsonCustomer = TopDataModel()
                                jsonCustomer.title =
                                    jsonArrCustomer[index].asJsonObject.get("buyers").asJsonObject.get("name").asString
                                jsonCustomer.value =
                                    jsonArrCustomer[index].asJsonObject.get("total").asString

                                arrTopCustomer.add(jsonCustomer)
                            }

                            var jsonArrSKU = json.get("top_sky").asJsonArray

                            for ((index, value) in jsonArrSKU.withIndex()) {
                                if (jsonArrSKU[index].asJsonObject.get("productdetails")!=null&&
                                    jsonArrSKU[index].asJsonObject.get("productdetails").toString()!="null") {
                                    var jsonSKU = TopDataModel()
                                    jsonSKU.title =
                                        jsonArrSKU[index].asJsonObject.get("productdetails").asJsonObject.get("detail_title").asString
                                    jsonSKU.value =
                                        jsonArrSKU[index].asJsonObject.get("total").asString

                                    arrTopSKU.add(jsonSKU)
                                }

                            }

                            var jsonArrMarket = json.get("top_markets").asJsonArray

                            for ((index, value) in jsonArrMarket.withIndex()) {

                                var jsonMarket = TopDataModel()
                                jsonMarket.title =
                                    jsonArrMarket[index].asJsonObject.get("beat_name").asString
                                jsonMarket.value =
                                    jsonArrMarket[index].asJsonObject.get("amount").asString

                                arrTopMarket.add(jsonMarket)
                            }

                            var jsonArrSalesSummary = json.get("sales_summary").asJsonArray
                            for ((index, value) in jsonArrSalesSummary.withIndex()) {

                                var jsonSummary = TopDataModel()
                                jsonSummary.title =
                                    jsonArrSalesSummary[index].asJsonObject.get("invoice_no").asString
                                jsonSummary.value =
                                    jsonArrSalesSummary[index].asJsonObject.get("grand_total").asString

                                arrSalesSummary.add(jsonSummary)
                            }

                            topDataAdapter = TopDataAdapter(activityLocal, arrTopCustomer)
                            recyclerView.adapter = topDataAdapter

                            if (arrTopCustomer.isEmpty()) {
                                tvNoData.visibility = View.VISIBLE
                                tvNoData.text = "No Data available for Customer"
                            }
                        } else {

                            val jsonObject: JSONObject

                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activityLocal
                                    , false
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