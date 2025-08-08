package com.exp.clonefieldkonnect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.LoginActivity
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.adapter.ReportAdapter
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.NameWithImage
import com.exp.clonefieldkonnect.model.ReportcountModel
import com.exp.import.Utilities
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap


class ReportFragment(var linearTop: CardView, var tabPosition: Int,var customerVisitFlag : String) : Fragment(),ReportAdapter.OnEmailClick {

    lateinit var activityLocal: Activity
    private lateinit var rootView: View
    private val nameWithImageList = ArrayList<NameWithImage>()
    var id_main : Int = 0
    lateinit var recyclerView: RecyclerView
    lateinit var cardBack_report: CardView
    lateinit var tvTitle: TextView
    lateinit var linearTop_report: CardView
    lateinit var fragment_container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_report, container, false)
        activityLocal = activity as MainActivity

        initViews()

        return rootView
    }

    private fun initViews() {


//        nameWithImageList.add(NameWithImage("User Activity", R.drawable.ic_report_user_activity,1))
//        nameWithImageList.add(NameWithImage("Attendance", R.drawable.ic_report_attendance,2))
//        nameWithImageList.add(NameWithImage("Tour Plan", R.drawable.ic_tour_icon,3))
//        nameWithImageList.add(NameWithImage("Expense", R.drawable.ic_expense_img,4))
//        nameWithImageList.add(NameWithImage("Order History", R.drawable.ic_order2,5))
//        nameWithImageList.add(NameWithImage("CD Approval(Pump)", R.drawable.ic_cluster_icon,6))
//        nameWithImageList.add(NameWithImage("Expense Approval", R.drawable.ic_expense_img,7))
//        nameWithImageList.add(NameWithImage("New Dealer Approval", R.drawable.ic_report_user_activity,8))
//        nameWithImageList.add(NameWithImage("Dealer CY vs LY Sales", R.drawable.ic_order2,9))
//        nameWithImageList.add(NameWithImage("Special Discount Approval", R.drawable.ic_cluster_icon,10))

        getcount()



        linearTop.visibility=View.GONE
        linearTop_report = rootView.findViewById(R.id.linearTop_report)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        cardBack_report = rootView.findViewById(R.id.cardBack_report)
        tvTitle = rootView.findViewById(R.id.tvTitle)
        fragment_container = rootView.findViewById(R.id.fragment_container)



        linearTop_report.visibility=View.VISIBLE
        recyclerView.visibility = View.VISIBLE

        cardBack_report.setOnClickListener {
            handleBackPressed()
        }

        println("customerVisitFlagcustomerVisitFlag=="+customerVisitFlag)

        if (customerVisitFlag == "Tour Plan"){
            tvTitle.text = "Tour Plan"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_tour(cardBack_report,linearTop_report,0,tvTitle)
        }else   if (customerVisitFlag == "Expense"){
            tvTitle.text = "Expense"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_expense(cardBack_report,linearTop_report,0,tvTitle)
        }else  if (customerVisitFlag == "User Activity"){
            tvTitle.text = "User Activity"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragmentB(cardBack_report,linearTop_report,0,tvTitle)
        }else  if (customerVisitFlag == "MSP Activity"){
            tvTitle.text = "MSP Activity"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_msp(cardBack_report,linearTop_report,2,tvTitle)
        }


    }

    private fun getcount() {
        if (!Utilities.isOnline(activityLocal)) {
            return
        }
        var dialog = DialogClass.progressDialog(activityLocal)
        val queryParams = HashMap<String, String>()
        queryParams["id"] = StaticSharedpreference.getInfo(Constant.USERID, activityLocal).toString()
        ApiClient.getreportcount(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activityLocal).toString(),
            queryParams,
            object : APIResultLitener<ReportcountModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<ReportcountModel>?, errorMessage: String?) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {


                            var roll_id = StaticSharedpreference.getInfo(Constant.ROLL_ID, activityLocal).toString()
                            val gson = Gson()
                            val rollIdListType = object : TypeToken<List<Int>>() {}.type
                            val rollIdList: List<Int> = gson.fromJson(roll_id, rollIdListType)
                            var compare_ids = listOf(1,6)
                            var compare_ids_specail = listOf(1,19)
                            val containsSimilarValue = rollIdList.any { it in compare_ids }
                            val containsSimilarspecial = rollIdList.any { it in compare_ids_specail }

                            val nameWithImageList = mutableListOf(
                                NameWithImage("User Activity", R.drawable.ic_report_user_activity, 1),
                                NameWithImage("Attendance", R.drawable.ic_report_attendance, 2),
                                NameWithImage("Tour Plan", R.drawable.ic_tour_icon, 3),
                                NameWithImage("Expense", R.drawable.ic_expense_img, 4),
                                NameWithImage("Order History", R.drawable.ic_order2, 5),
                                NameWithImage("Expense Approval", R.drawable.ic_expense_img, 7),
                                NameWithImage("New Dealer Approval", R.drawable.ic_report_user_activity, 8),
                                NameWithImage("Dealer CY vs LY Sales", R.drawable.ic_order2, 9),
                                NameWithImage("Dealer Growth", R.drawable.ic_order2, 11),
                                NameWithImage("Primary Scheme Report", R.drawable.ic_order2, 12),
                                NameWithImage("Dispatch Report", R.drawable.ic_order2, 13),
                                NameWithImage("MSP Report", R.drawable.ic_order2, 14),
                            )

                            println("roll_idsroll_ids=="+roll_id+"<<"+compare_ids)

                            if (containsSimilarValue) {
                                nameWithImageList.add(5, NameWithImage("CH Approval(Pump)", R.drawable.ic_cluster_icon, 6))
                            }
                            if (containsSimilarspecial) {
                                nameWithImageList.add(NameWithImage("Special Discount Approval", R.drawable.ic_cluster_icon, 10))
                            }

                            var mLayoutManager = LinearLayoutManager(activityLocal)
                            recyclerView.layoutManager = mLayoutManager
                            val taskAdapter = ReportAdapter(activityLocal, nameWithImageList,this@ReportFragment,response.body()!!.data)
                            recyclerView.adapter = taskAdapter

                        } else {

                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())
                                if (response.code() == 401){
                                    Toast.makeText(activityLocal,jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                                    StaticSharedpreference.deleteSharedPreference(activityLocal)
                                    startActivity(Intent(activityLocal, LoginActivity::class.java))
                                    activityLocal.finishAffinity()
                                    println("Erroorr=="+jsonObject.getString("message"))
                                }else{
                                    DialogClass.alertDialog(
                                        jsonObject.getString("status"),
                                        jsonObject.getString("message"),
                                        activityLocal, false
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        Toast.makeText(activityLocal,"No Record Found", Toast.LENGTH_LONG).show()
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
        println("IDDD+=" + id_main)
        if (id_main == 0) {
            startActivity(Intent(activityLocal, MainActivity::class.java))
            tabPosition = 0
            StaticSharedpreference.saveInfo(Constant.TabPosition, tabPosition.toString(), activityLocal)
        } else {
            id_main = 0
            tvTitle.text = "Report"
            recyclerView.visibility = View.VISIBLE
            fragment_container.visibility = View.GONE
        }
    }



    override fun onClickEmail(id: Int) {
        id_main = id
        if (id.equals(1)){
            tvTitle.text = "User Activity"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragmentB(cardBack_report,linearTop_report,0,tvTitle)
        }
        else if (id.equals(3)){
            tvTitle.text = "Tour Plan"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_tour(cardBack_report,linearTop_report,0,tvTitle)
        }
        else if (id.equals(4)){
            tvTitle.text = "Expense"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_expense(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(5)){
            startActivity(Intent(activityLocal, MainActivity::class.java))
            tabPosition = 3
            StaticSharedpreference.saveInfo(Constant.TabPosition, tabPosition.toString(), activityLocal)
        }else if (id.equals(6)){
            tvTitle.text = "CD Approval(Pump)"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_cluster(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(7)){
            tvTitle.text = "Expense Approval"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_expense_approval(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(8)){
            tvTitle.text = "Dealer Approval"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_dealer_approval(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(9)){
            tvTitle.text = "Dealer CY vs LY Sales"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_dealer_sales(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(10)){
            tvTitle.text = "Special Approval"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_special_dis(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(11)){
            tvTitle.text = "Dealer Growth"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_growth(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(12)){
//            Toast.makeText(activityLocal, "Working on it !!", Toast.LENGTH_LONG).show()
            tvTitle.text = "Primary Scheme Report"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_primary(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(13)){
//            Toast.makeText(activityLocal, "Working on it !!", Toast.LENGTH_LONG).show()
            tvTitle.text = "Dispatch Report"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_sales(cardBack_report,linearTop_report,0,tvTitle)
        }else if (id.equals(14)){
//            Toast.makeText(activityLocal, "Working on it !!", Toast.LENGTH_LONG).show()
            tvTitle.text = "MSP Report"
            recyclerView.visibility =View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_msp_report(cardBack_report,linearTop_report,0,tvTitle)
        }
        else if (id.equals(2)){
            tvTitle.text = "Attendance"
            recyclerView.visibility = View.GONE
            fragment_container.visibility =View.VISIBLE
            navigateToFragment_atten(cardBack_report,linearTop_report,0,tvTitle)
        }
    }

    private fun navigateToFragment_growth(cardBack: CardView, linearTopreport: CardView, i: Int, tvTitle: TextView) {
        val fragmentB = DealerGrowthFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }
    private fun navigateToFragment_primary(cardBack: CardView, linearTopreport: CardView, i: Int, tvTitle: TextView) {
        val fragmentB = PrimarySchemeFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }
    private fun navigateToFragment_sales(cardBack: CardView, linearTopreport: CardView, i: Int, tvTitle: TextView) {
        val fragmentB = NewSalesReportFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }

    private fun navigateToFragment_msp_report(cardBack: CardView, linearTopreport: CardView, i: Int, tvTitle: TextView) {
        val fragmentB = MSPReportFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }

    private fun navigateToFragment_cluster(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = ClusterDiscountFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun navigateToFragment_atten(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = ApproveAttendancefragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }

    private fun navigateToFragment_tour(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = TourFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }
    private fun navigateToFragment_expense(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = ExpenseFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }
    private fun navigateToFragment_expense_approval(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = ExpenseApprovalFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }
    private fun navigateToFragment_dealer_approval(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = DealerApproalFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }
    private fun navigateToFragment_dealer_sales(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = DealerCYvsLYSalesFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }
    private fun navigateToFragment_special_dis(cardBack: CardView, linearTopreport: CardView, tabPosition: Int, tvTitle: TextView) {
        val fragmentB = SpecialDiscountFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()
    }

    private fun navigateToFragmentB(
        cardBack: CardView,
        linearTopreport: CardView,
        tabPosition: Int,
        tvTitle: TextView
    ) {
        val fragmentB = UserActivityFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()

    }
    private fun navigateToFragment_msp(
        cardBack: CardView,
        linearTopreport: CardView,
        tabPosition: Int,
        tvTitle: TextView
    ) {
        val fragmentB = MSPFragment(cardBack,linearTopreport,tabPosition,tvTitle)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.addToBackStack(null)  // Optional: Add to back stack for navigation history
        transaction.commit()

    }

}
//class NameWithImage(val name: String, @DrawableRes val imageRes: Int, val Id :Int)
