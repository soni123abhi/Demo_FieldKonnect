package com.exp.clonefieldkonnect.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.MainActivity
import com.exp.clonefieldkonnect.activity.OrderDetailEditActivity
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import com.exp.clonefieldkonnect.model.OrderUpdateModel
import com.exp.import.Utilities
import retrofit2.Response
import java.text.DecimalFormat

class OrderEditDetailAdapter(
    val context: Activity,
    val orderDetails: List<OrderDetailsModel.Data.Orderdetail>?,
    var buyerType: String?,
    var cardapprove: CardView,
    var cardreject: CardView,
    var id: Int?,
    var createdBy: String?,
    var type: String,
    var flag_ebd: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var number2digitsss = 0.0
    var status = 0
    var special_status = 0
    var price_cust = 0.0
    var flag = false
    var enteredText = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.adapter_order_details, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

        if (orderDetails!!.size == (position + 1)) {
            statementHandler.view.visibility = View.GONE
        } else {
            statementHandler.view.visibility = View.VISIBLE
        }

        println("Conditiiiiii=="+flag_ebd)



        setupcalculation(position,statementHandler.tvQuantity, statementHandler.tvRate,
            statementHandler.tvDetails,statementHandler.tvGSTNew,statementHandler.tvPrice,
            statementHandler.tvspecification,statementHandler.tvAmount,statementHandler.tvName,flag_ebd)




//        println("UPdateeeeee=="+id+"<<"+status+"<<"+OrderDetailEditActivity.tvAmount.text.toString()+"<<"+
//                OrderDetailEditActivity.tvTotal.text.toString()+"<<"+OrderDetailEditActivity.tvGSTTotal.text.toString()+"<<"+
//                OrderDetailEditActivity.tv_cluster_amt.text.toString()+"<<"+OrderDetailEditActivity.tv_clusterfilter.text.toString()+"<<"+
//                OrderDetailEditActivity.edt_dealerfilter.text.toString()+"<<"+OrderDetailEditActivity.tv_dealer_amt.text.toString()
//        )

    }

    @SuppressLint("SetTextI18n")
    private fun setupcalculation(
        position: Int,
        tvQuantity: TextView,
        tvRate: TextView,
        tvDetails: TextView,
        tvGSTNew: TextView,
        tvPrice: TextView,
        tvspecification: TextView,
        tvAmount: TextView,
        tvName: TextView,
        flag_ebd: Boolean
    ) {
        tvName.text = orderDetails?.get(position)?.productName.toString()
        tvQuantity.text = orderDetails?.get(position)?.quantity.toString()
        tvRate.text = orderDetails?.get(position)?.ebd_amount.toString()
        tvDetails.text = "Product stage :- " + orderDetails?.get(position)?.product_no.toString()
        tvGSTNew.text = "kW :- " + orderDetails?.get(position)?.part_no.toString()
        tvPrice.text = "HP :- " + orderDetails?.get(position)?.hp.toString()
        tvspecification.text = "Suc*Del :- " + orderDetails?.get(position)?.specification.toString()

        if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
            price_cust = (orderDetails?.get(position)?.quantity.toString()
                .toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
            tvAmount.text = price_cust.toString()
        } else {
            tvAmount.text = DecimalFormat("##.#").format(
                orderDetails?.get(position)?.lineTotal.toString().toFloat()
            ).toString()
        }

        cardapprove.setOnClickListener {
            if (type.equals("special")){
                special_status = 1
            }else{
                status = 1
            }
            println("Satatusus==aaa="+status+"<<"+special_status)
            updateorder(status, id,special_status)
        }
        cardreject.setOnClickListener {
            if (type.equals("special")){
                special_status = 2
            }else{
                status = 2
            }
            println("Satatusus==rrr="+status+"<<"+special_status)
            updateorder(status, id, special_status)
        }


        var product_schme_amt: ArrayList<Double> = ArrayList()
        var product_gst: ArrayList<Double> = ArrayList()
        var grand_total = 0.0
        var amount_custt = 0.0
        var calculate_cluster = 0.0
        var calculate_dealer = 0.0
        var calculate_distributor = 0.0
        var calculate_frieght = 0.0
        var calculate_ebd = 0.0
        var calculate_specail = 0.0
        var calculate_cash = 0.0

        var tot_dis_value = 0.0
        var tot_dis_per = 0.0


        if (orderDetails != null) {
            for (item in orderDetails) {
                var schme_amt = item.ebd_amount.toString().toDouble() * item.quantity.toString().toDouble()
                product_schme_amt.add(schme_amt)
                product_gst.add(item.gst.toString().toDouble())
                amount_custt += schme_amt
            }
        }
        grand_total = amount_custt
        println("calculate_clustercalculate_cluster==" + amount_custt)

        OrderDetailEditActivity.tv_ebdfilter.setOnClickListener {
            if (!calculate_ebd.equals(0.0)){
                flag = false
                setupcalculation(position, tvQuantity, tvRate, tvDetails, tvGSTNew, tvPrice, tvspecification, tvAmount, tvName, this.flag_ebd)
            }else{
                val percentages = arrayOf("0%","1%", "2%", "3%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                    val subtotal_ebd = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                    calculate_ebd = subtotal_ebd * (selectedPercentageValue / 100)
                    println("PERRRRRRRRRR=2=$calculate_ebd")
                    OrderDetailEditActivity.tv_ebd_dis_amt.text = String.format("%.2f", calculate_ebd)
                    OrderDetailEditActivity.tv_ebdfilter.text = selectedPercentage
                    println("flag=ebd==" + flag)
                    if (flag.equals(false)) {
                        OrderDetailEditActivity.tv_distributorfilter.text = ""
                        OrderDetailEditActivity.tv_distributor_amt.text = ""
                        OrderDetailEditActivity.tv_specialfilter.text = ""
                        OrderDetailEditActivity.tv_special_amt.text = ""
                        OrderDetailEditActivity.tv_frieghtfilter.text = ""
                        OrderDetailEditActivity.tv_Frieght_amt.text = ""
                        OrderDetailEditActivity.tv_clusterfilter.text = ""
                        OrderDetailEditActivity.tv_cluster_amt.text = ""
                        OrderDetailEditActivity.tv_dealerfilter.text = ""
                        OrderDetailEditActivity.tv_dealer_amt.text = ""
                        OrderDetailEditActivity.tv_cashfilter.text = ""
                        OrderDetailEditActivity.tv_cash_amt.text = ""
                        OrderDetailEditActivity.tv_totalfilter.text = ""
                        OrderDetailEditActivity.tv_total_amt.text = ""
                        calculate_cash = 0.0
                        calculate_distributor = 0.0
                        calculate_specail = 0.0
                        calculate_frieght = 0.0
                        calculate_cluster = 0.0
                        calculate_dealer = 0.0
                        flag = true
                        println("flag=ebd=11=" + flag)
                    }

                    grand_total = subtotal_ebd - calculate_ebd
                    OrderDetailEditActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    OrderDetailEditActivity.tvTotal.text =
                        DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                    tot_dis_value = (amount_custt - grand_total)
                    tot_dis_per = (tot_dis_value / amount_custt)*100

                    OrderDetailEditActivity.tv_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                    OrderDetailEditActivity.tv_totalfilter.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }
        OrderDetailEditActivity.tv_distributorfilter.setOnClickListener {
            if (flag_ebd.equals(true) && calculate_ebd.equals(0.0)){
                response_message("Please select EBD discount")
            }
            else if (!calculate_distributor.equals(0.0)){
                flag = false
                setupcalculation(position, tvQuantity, tvRate, tvDetails, tvGSTNew, tvPrice, tvspecification, tvAmount, tvName, this.flag_ebd)
            }else{
                showCustomDialog(context, OrderDetailEditActivity.tv_distributorfilter) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        val subtototal3 = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                        println("enteredTextenteredText==" + enteredText)
                        OrderDetailEditActivity.tv_distributorfilter.text = enteredText
                        val inputValue =
                            OrderDetailEditActivity.tv_distributorfilter.text.toString().toDouble()
                        calculate_distributor = subtototal3 * inputValue / 100
                        OrderDetailEditActivity.tv_distributor_amt.text =
                            String.format("%.2f", calculate_distributor)
                        println("flagflag=" + flag)
                        if (flag.equals(false)) {
                            OrderDetailEditActivity.tv_ebdfilter.text = ""
                            OrderDetailEditActivity.tv_ebd_dis_amt.text = ""
                            OrderDetailEditActivity.tv_specialfilter.text = ""
                            OrderDetailEditActivity.tv_special_amt.text = ""
                            OrderDetailEditActivity.tv_frieghtfilter.text = ""
                            OrderDetailEditActivity.tv_Frieght_amt.text = ""
                            OrderDetailEditActivity.tv_clusterfilter.text = ""
                            OrderDetailEditActivity.tv_cluster_amt.text = ""
                            OrderDetailEditActivity.tv_dealerfilter.text = ""
                            OrderDetailEditActivity.tv_dealer_amt.text = ""
                            OrderDetailEditActivity.tv_cashfilter.text = ""
                            OrderDetailEditActivity.tv_cash_amt.text = ""
                            OrderDetailEditActivity.tv_totalfilter.text = ""
                            OrderDetailEditActivity.tv_total_amt.text = ""
                            calculate_cash = 0.0
                            calculate_ebd = 0.0
                            calculate_specail = 0.0
                            calculate_frieght = 0.0
                            calculate_cluster = 0.0
                            calculate_dealer = 0.0
                            flag = true
                        }
                        grand_total = subtototal3 - calculate_distributor
                        OrderDetailEditActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        OrderDetailEditActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                        tot_dis_value = (amount_custt - grand_total)
                        tot_dis_per = (tot_dis_value / amount_custt)*100

                        OrderDetailEditActivity.tv_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                        OrderDetailEditActivity.tv_totalfilter.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        OrderDetailEditActivity.tv_specialfilter.setOnClickListener {
            if (OrderDetailEditActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }
            else if (!calculate_specail.equals(0.0)) {
                flag = false
                setupcalculation(position, tvQuantity, tvRate, tvDetails, tvGSTNew, tvPrice, tvspecification, tvAmount, tvName, this.flag_ebd)
            } else{
                showCustomDialog(context, OrderDetailEditActivity.tv_specialfilter) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        val subtototal3 = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                        val inputValue = OrderDetailEditActivity.tv_specialfilter.text.toString().toDouble()
                        calculate_specail = subtototal3 * inputValue / 100
                        OrderDetailEditActivity.tv_special_amt.text = String.format("%.2f", calculate_specail)
                        if (flag.equals(false)) {
                            OrderDetailEditActivity.tv_ebdfilter.text = ""
                            OrderDetailEditActivity.tv_ebd_dis_amt.text = ""
                            OrderDetailEditActivity.tv_distributorfilter.text = ""
                            OrderDetailEditActivity.tv_distributor_amt.text = ""
                            OrderDetailEditActivity.tv_frieghtfilter.text = ""
                            OrderDetailEditActivity.tv_Frieght_amt.text = ""
                            OrderDetailEditActivity.tv_clusterfilter.text = ""
                            OrderDetailEditActivity.tv_cluster_amt.text = ""
                            OrderDetailEditActivity.tv_dealerfilter.text = ""
                            OrderDetailEditActivity.tv_dealer_amt.text = ""
                            OrderDetailEditActivity.tv_cashfilter.text = ""
                            OrderDetailEditActivity.tv_cash_amt.text = ""
                            OrderDetailEditActivity.tv_totalfilter.text = ""
                            OrderDetailEditActivity.tv_total_amt.text = ""
                            calculate_cash = 0.0
                            calculate_ebd = 0.0
                            calculate_distributor = 0.0
                            calculate_frieght = 0.0
                            calculate_cluster = 0.0
                            calculate_dealer = 0.0
                            flag = true
                        }

                        grand_total = subtototal3 - calculate_specail
                        OrderDetailEditActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        OrderDetailEditActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                        tot_dis_value = (amount_custt - grand_total)
                        tot_dis_per = (tot_dis_value / amount_custt)*100

                        OrderDetailEditActivity.tv_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                        OrderDetailEditActivity.tv_totalfilter.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        OrderDetailEditActivity.tv_frieghtfilter.setOnClickListener {
           if (OrderDetailEditActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (OrderDetailEditActivity.tv_specialfilter.text.isNullOrEmpty()){
                response_message("Please select Special discount")
            } else if (!calculate_frieght.equals(0.0)) {
                flag = false
                setupcalculation(
                    position,
                    tvQuantity,
                    tvRate,
                    tvDetails,
                    tvGSTNew,
                    tvPrice,
                    tvspecification,
                    tvAmount,
                    tvName,
                    this.flag_ebd
                )
            } else{
                val percentages = arrayOf("0%","1%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                    val subtototal4 = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                    calculate_frieght = subtototal4 * (selectedPercentageValue / 100)

                    println("PERRRRRRRRRR=2=$calculate_frieght")
                    OrderDetailEditActivity.tv_Frieght_amt.text =
                        String.format("%.2f", calculate_frieght)
                    OrderDetailEditActivity.tv_frieghtfilter.text = selectedPercentage
                    if (flag.equals(false)) {
                        OrderDetailEditActivity.tv_ebdfilter.text = ""
                        OrderDetailEditActivity.tv_ebd_dis_amt.text = ""
                        OrderDetailEditActivity.tv_distributorfilter.text = ""
                        OrderDetailEditActivity.tv_distributor_amt.text = ""
                        OrderDetailEditActivity.tv_specialfilter.text = ""
                        OrderDetailEditActivity.tv_special_amt.text = ""
                        OrderDetailEditActivity.tv_clusterfilter.text = ""
                        OrderDetailEditActivity.tv_cluster_amt.text = ""
                        OrderDetailEditActivity.tv_dealerfilter.text = ""
                        OrderDetailEditActivity.tv_dealer_amt.text = ""
                        OrderDetailEditActivity.tv_cashfilter.text = ""
                        OrderDetailEditActivity.tv_cash_amt.text = ""
                        OrderDetailEditActivity.tv_totalfilter.text = ""
                        OrderDetailEditActivity.tv_total_amt.text = ""
                        calculate_cash = 0.0
                        calculate_ebd = 0.0
                        calculate_distributor = 0.0
                        calculate_specail = 0.0
                        calculate_cluster = 0.0
                        calculate_dealer = 0.0
                        flag = true
                    }
                    grand_total = subtototal4 - calculate_frieght
                    OrderDetailEditActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    OrderDetailEditActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)
                    tot_dis_value = (amount_custt - grand_total)
                    tot_dis_per = (tot_dis_value / amount_custt)*100

                    OrderDetailEditActivity.tv_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                    OrderDetailEditActivity.tv_totalfilter.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }
        OrderDetailEditActivity.tv_clusterfilter.setOnClickListener {
            if (OrderDetailEditActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (OrderDetailEditActivity.tv_specialfilter.text.isNullOrEmpty()){
                response_message("Please select Special discount")
            }else if (OrderDetailEditActivity.tv_frieghtfilter.text.isNullOrEmpty()){
                response_message("Please select Frieght discount")
            }else if (!calculate_cluster.equals(0.0)) {
                flag = false
                setupcalculation(
                    position,
                    tvQuantity,
                    tvRate,
                    tvDetails,
                    tvGSTNew,
                    tvPrice,
                    tvspecification,
                    tvAmount,
                    tvName,
                    this.flag_ebd
                )
            } else{
                val percentages = arrayOf("0%","1%", "2%", "3%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                    val subtotal = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer

                    println("PERRRRRRRRRR==$subtotal")
                    println("PERRRRRRRRRR=1=$amount_custt")
                    println("PERRRRRRRRRR=3=$selectedPercentageValue")

                    calculate_cluster = subtotal * (selectedPercentageValue / 100)
                    println("PERRRRRRRRRR=2=$calculate_cluster")
                    OrderDetailEditActivity.tv_cluster_amt.text = String.format("%.2f", calculate_cluster)
                    OrderDetailEditActivity.tv_clusterfilter.text = selectedPercentage

                    if (flag.equals(false)) {
                        OrderDetailEditActivity.tv_ebdfilter.text = ""
                        OrderDetailEditActivity.tv_ebd_dis_amt.text = ""
                        OrderDetailEditActivity.tv_distributorfilter.text = ""
                        OrderDetailEditActivity.tv_distributor_amt.text = ""
                        OrderDetailEditActivity.tv_specialfilter.text = ""
                        OrderDetailEditActivity.tv_special_amt.text = ""
                        OrderDetailEditActivity.tv_frieghtfilter.text = ""
                        OrderDetailEditActivity.tv_Frieght_amt.text = ""
                        OrderDetailEditActivity.tv_dealerfilter.text = ""
                        OrderDetailEditActivity.tv_dealer_amt.text = ""
                        OrderDetailEditActivity.tv_cashfilter.text = ""
                        OrderDetailEditActivity.tv_cash_amt.text = ""
                        OrderDetailEditActivity.tv_totalfilter.text = ""
                        OrderDetailEditActivity.tv_total_amt.text = ""
                        calculate_cash = 0.0
                        calculate_ebd = 0.0
                        calculate_distributor = 0.0
                        calculate_specail = 0.0
                        calculate_frieght = 0.0
                        calculate_dealer = 0.0
                        flag = true
                    }
                    grand_total = subtotal - calculate_cluster
                    OrderDetailEditActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    OrderDetailEditActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                    tot_dis_value = (amount_custt - grand_total)
                    tot_dis_per = (tot_dis_value / amount_custt)*100

                    OrderDetailEditActivity.tv_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                    OrderDetailEditActivity.tv_totalfilter.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }
        OrderDetailEditActivity.tv_cashfilter.setOnClickListener {
            if (OrderDetailEditActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (OrderDetailEditActivity.tv_specialfilter.text.isNullOrEmpty()){
                response_message("Please select Special discount")
            }else if (OrderDetailEditActivity.tv_frieghtfilter.text.isNullOrEmpty()){
                response_message("Please select Frieght discount")
            }else if (OrderDetailEditActivity.tv_clusterfilter.text.isNullOrEmpty()) {
                response_message("Please select Cluster discount")
            }else if (!calculate_cash.equals(0.0)) {
                flag = false
                setupcalculation(position, tvQuantity, tvRate, tvDetails, tvGSTNew, tvPrice, tvspecification, tvAmount, tvName, this.flag_ebd)
            }else{
                showCustomDialog(context, OrderDetailEditActivity.tv_cashfilter) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        val subtototal2 = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                        val inputValue = OrderDetailEditActivity.tv_cashfilter.text.toString().toDouble()
                        calculate_cash = subtototal2 * inputValue / 100
                        OrderDetailEditActivity.tv_cash_amt.text = String.format("%.2f", calculate_cash)
                        if (flag.equals(false)) {
                            OrderDetailEditActivity.tv_ebdfilter.text = ""
                            OrderDetailEditActivity.tv_ebd_dis_amt.text = ""
                            OrderDetailEditActivity.tv_distributorfilter.text = ""
                            OrderDetailEditActivity.tv_distributor_amt.text = ""
                            OrderDetailEditActivity.tv_specialfilter.text = ""
                            OrderDetailEditActivity.tv_special_amt.text = ""
                            OrderDetailEditActivity.tv_frieghtfilter.text = ""
                            OrderDetailEditActivity.tv_Frieght_amt.text = ""
                            OrderDetailEditActivity.tv_clusterfilter.text = ""
                            OrderDetailEditActivity.tv_cluster_amt.text = ""
                            OrderDetailEditActivity.tv_dealerfilter.text = ""
                            OrderDetailEditActivity.tv_dealer_amt.text = ""
                            OrderDetailEditActivity.tv_totalfilter.text = ""
                            OrderDetailEditActivity.tv_total_amt.text = ""
                            calculate_ebd = 0.0
                            calculate_distributor = 0.0
                            calculate_specail = 0.0
                            calculate_frieght = 0.0
                            calculate_cluster = 0.0
                            calculate_dealer = 0.0
                            flag = true
                        }
                        grand_total = subtototal2 - calculate_cash
                        OrderDetailEditActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        OrderDetailEditActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        println("Updated product_ebd_amt=22=: $product_schme_amt")
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)
                        tot_dis_value = (amount_custt - grand_total)
                        tot_dis_per = (tot_dis_value / amount_custt)*100

                        OrderDetailEditActivity.tv_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                        OrderDetailEditActivity.tv_totalfilter.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        OrderDetailEditActivity.tv_dealerfilter.setOnClickListener {
             if (OrderDetailEditActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (OrderDetailEditActivity.tv_specialfilter.text.isNullOrEmpty()){
                response_message("Please select Special discount")
            }else if (OrderDetailEditActivity.tv_frieghtfilter.text.isNullOrEmpty()){
                response_message("Please select Frieght discount")
            }else if (OrderDetailEditActivity.tv_clusterfilter.text.isNullOrEmpty()){
                response_message("Please select Cluster discount")
            }else if (OrderDetailEditActivity.tv_cashfilter.text.isNullOrEmpty()){
                response_message("Please select Cash discount")
            }else if (!calculate_dealer.equals(0.0)) {
                flag = false
                setupcalculation(position,
                    tvQuantity, tvRate, tvDetails, tvGSTNew, tvPrice, tvspecification, tvAmount, tvName, this.flag_ebd)
            }else{
                showCustomDialog(context, OrderDetailEditActivity.tv_dealerfilter) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        val subtototal2 = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                        val inputValue = OrderDetailEditActivity.tv_dealerfilter.text.toString().toDouble()
                        calculate_dealer = subtototal2 * inputValue / 100
                        OrderDetailEditActivity.tv_dealer_amt.text =
                            String.format("%.2f", calculate_dealer)
                        if (flag.equals(false)) {
                            OrderDetailEditActivity.tv_ebdfilter.text = ""
                            OrderDetailEditActivity.tv_ebd_dis_amt.text = ""
                            OrderDetailEditActivity.tv_distributorfilter.text = ""
                            OrderDetailEditActivity.tv_distributor_amt.text = ""
                            OrderDetailEditActivity.tv_specialfilter.text = ""
                            OrderDetailEditActivity.tv_special_amt.text = ""
                            OrderDetailEditActivity.tv_frieghtfilter.text = ""
                            OrderDetailEditActivity.tv_Frieght_amt.text = ""
                            OrderDetailEditActivity.tv_clusterfilter.text = ""
                            OrderDetailEditActivity.tv_cluster_amt.text = ""
                            OrderDetailEditActivity.tv_cashfilter.text = ""
                            OrderDetailEditActivity.tv_cash_amt.text = ""
                            OrderDetailEditActivity.tv_totalfilter.text = ""
                            OrderDetailEditActivity.tv_total_amt.text = ""
                            calculate_cash = 0.0
                            calculate_ebd = 0.0
                            calculate_distributor = 0.0
                            calculate_specail = 0.0
                            calculate_frieght = 0.0
                            calculate_cluster = 0.0
                            flag = true
                        }
                        grand_total = subtototal2 - calculate_dealer
                        OrderDetailEditActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        OrderDetailEditActivity.tvTotal.text =
                            DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        println("Updated product_ebd_amt=22=: $product_schme_amt")
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                        tot_dis_value = (amount_custt - grand_total)
                        tot_dis_per = (tot_dis_value / amount_custt)*100

                        OrderDetailEditActivity.tv_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                        OrderDetailEditActivity.tv_totalfilter.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog(
        context: Activity,
        tvDistributorfilter: TextView,
        callback: (String) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.edittext_dialog_layout, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Enter Value")
            .create()

        submitBtn.setOnClickListener {
            val enteredText = editText.text.toString()
            tvDistributorfilter.text = enteredText
            callback(enteredText)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun response_message(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateorder(status: Int, id: Int?, special_status: Int) {

        if (!Utilities.isOnline(context)) {
            return
        }
        var dialog = DialogClass.progressDialog(context)


        val queryParams = HashMap<String, String>()
        queryParams["order_id"] = id.toString()
        queryParams["discount_status"] = status.toString()
        queryParams["sp_discount_status"] = special_status.toString()
        queryParams["sub_total"] = OrderDetailEditActivity.tvAmount.text.toString()
        queryParams["grand_total"] = OrderDetailEditActivity.tvTotal.text.toString()
        queryParams["gst_amount"] = OrderDetailEditActivity.tvGSTTotal.text.toString()
        queryParams["ebd_discount"] = OrderDetailEditActivity.tv_ebdfilter.text.toString()
        queryParams["ebd_amount"] = OrderDetailEditActivity.tv_ebd_dis_amt.text.toString()
        queryParams["special_discount"] = OrderDetailEditActivity.tv_specialfilter.text.toString()
        queryParams["special_amount"] = OrderDetailEditActivity.tv_special_amt.text.toString()
        queryParams["cluster_discount"] = OrderDetailEditActivity.tv_clusterfilter.text.toString()
        queryParams["cluster_amount"] = OrderDetailEditActivity.tv_cluster_amt.text.toString()
        queryParams["cash_discount"] = OrderDetailEditActivity.tv_cashfilter.text.toString()
        queryParams["cash_amount"] = OrderDetailEditActivity.tv_cash_amt.text.toString()
        queryParams["total_discount"] = OrderDetailEditActivity.tv_totalfilter.text.toString()
        queryParams["total_amount"] = OrderDetailEditActivity.tv_total_amt.text.toString()
        queryParams["deal_discount"] = OrderDetailEditActivity.tv_dealerfilter.text.toString()
        queryParams["deal_amount"] = OrderDetailEditActivity.tv_dealer_amt.text.toString()
        queryParams["distributor_discount"] = OrderDetailEditActivity.tv_distributorfilter.text.toString()
        queryParams["distributor_amount"] = OrderDetailEditActivity.tv_distributor_amt.text.toString()
        queryParams["frieght_discount"] = OrderDetailEditActivity.tv_frieghtfilter.text.toString()
        queryParams["frieght_amount"] = OrderDetailEditActivity.tv_Frieght_amt.text.toString()
        queryParams["gst5_amt"] = OrderDetailEditActivity.tv_5gstamt.text.toString()
        queryParams["gst12_amt"] = OrderDetailEditActivity.tv_12gstamt.text.toString()
        queryParams["gst18_amt"] = OrderDetailEditActivity.tv_18gstamt.text.toString()
        queryParams["remark"] == OrderDetailEditActivity.tv_remark.text.toString()

        ApiClient.updateorder(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                context
            ).toString(), queryParams, object : APIResultLitener<OrderUpdateModel> {
                override fun onAPIResult(
                    response: Response<OrderUpdateModel>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG)
                                .show()
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Poor Connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }


    private fun calcultionn(
        product_ebd_amt: ArrayList<Double>,
        product_gst: ArrayList<Double>,
        selectedPercentageValue: Float,
        grand_total: Double,
    ) {

        println("Initial product_ebd_amt: $product_ebd_amt")
        println("Initial product_gst: $product_gst")
        println("Initial Percentage Value: $selectedPercentageValue")
        println("Initial grand_total : $grand_total")

        var Gst5amount = 0.0
        var Gst12amount = 0.0
        var Gst18amount = 0.0
        var Gst28amount = 0.0
        var Gst0amount = 0.0
        var gst = 0.0

        for (i in product_ebd_amt.indices) {
            val originalPrice = product_ebd_amt[i]
            val originalgst = product_gst[i]
            val calculatedValue = subtractPercentage(originalPrice, selectedPercentageValue)
            val calculatedgst = gstcalll(calculatedValue, originalgst)
            product_ebd_amt[i] = String.format("%.2f", calculatedValue).toDouble()
            println(
                "Price after subtracting $selectedPercentageValue% from $originalPrice: ${
                    String.format(
                        "%.2f",
                        calculatedValue
                    )
                }"
            )
            println(
                "Price after gstt $selectedPercentageValue% from $calculatedValue: ${
                    String.format(
                        "%.2f",
                        calculatedgst
                    )
                }"
            )
            when (originalgst) {
                5.00 -> {
                    Gst5amount += calculatedgst.toString().toDouble()
                    OrderDetailEditActivity.tv_5gstamt.text = String.format("%.2f", Gst5amount)
                }
                12.00 -> {
                    Gst12amount += calculatedgst.toString().toDouble()
                    OrderDetailEditActivity.tv_12gstamt.text = String.format("%.2f", Gst12amount)
                }
                18.00 -> {
                    Gst18amount += calculatedgst.toString().toDouble()
                    OrderDetailEditActivity.tv_18gstamt.text = String.format("%.2f", Gst18amount)
                }
                28.00 -> {
                    Gst28amount += calculatedgst.toString().toDouble()
                    OrderDetailEditActivity.tv_28gstamt.text = String.format("%.2f", Gst28amount)
                }
                else -> {
                    Gst0amount += calculatedgst.toString().toDouble()
                }
            }
        }

        gst = Gst12amount + Gst5amount + Gst18amount + Gst28amount + Gst0amount
        println("gstgst==$gst")
        OrderDetailEditActivity.tvGSTTotal.text = String.format("%.2f", gst)
        println("gstgst=1=$grand_total")
        val totalAmount = gst + grand_total
        println("gstgst=2=$totalAmount")

        OrderDetailEditActivity.tvTotal.text =
            DecimalFormat("##.#").format(totalAmount.toFloat()).toString()

        println("Updated product_ebd_amt: $product_ebd_amt")
    }

    private fun gstcalll(calculatedValue: Double, originalgst: Double): Any {
        val gstamount = calculatedValue * (originalgst / 100)
        return gstamount
    }

    fun subtractPercentage(price: Double, percentage: Float): Double {
        val subtractionAmount = price * (percentage / 100)
        return price - subtractionAmount
    }


    override fun getItemCount(): Int {
        return orderDetails!!.size
    }

    private inner class StatementHandler(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvEAN: TextView = itemView.findViewById(R.id.tvEAN)
        var tvGSTNew: TextView = itemView.findViewById(R.id.tvGSTNew)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvspecification: TextView = itemView.findViewById(R.id.tvspecification)
        var tvGST: TextView = itemView.findViewById(R.id.tvGST)
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        var tvRate: TextView = itemView.findViewById(R.id.tvRate)
        var tvTax: TextView = itemView.findViewById(R.id.tvTax)
        var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        var imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        var view: View = itemView.findViewById(R.id.view)
    }
}