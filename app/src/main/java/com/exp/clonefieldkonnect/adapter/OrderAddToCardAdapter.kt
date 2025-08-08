package com.exp.clonefieldkonnect.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.AddToCartActivity
import com.exp.clonefieldkonnect.activity.ProductActivity
import com.exp.clonefieldkonnect.activity.ProductActivity.Companion.productArrAddToCart
import java.math.RoundingMode
import java.text.DecimalFormat

class OrderAddToCardAdapter(
    val context: Activity,
    var linear_5gst: LinearLayout,
    var linear_12gst: LinearLayout,
    var linear_18gst: LinearLayout,
    var linear_28gst: LinearLayout,
    var customertype: String,
    var linear_ebd: LinearLayout,
    var linear_cluster: LinearLayout,
    var linear_dealer: LinearLayout,
    var linear_distributor: LinearLayout,
    var linear_Frieght: LinearLayout,
    var linear_special: LinearLayout,
    var linear_ebd_dis: LinearLayout,
    var linear_Doddiscount: LinearLayout,
    var linear_Specialdiscount: LinearLayout,
    var linear_distributionmargindiscount: LinearLayout,
    var linear_cashdiscount: LinearLayout,
    var linear_total_dis: LinearLayout,
    var linear_extradiscount: LinearLayout,
    var linear_cashdiscount_pump: LinearLayout,
    var linear_total_dis_pump: LinearLayout,
    var linear_agri_std_discount: LinearLayout,
    var linear_agri_total_dis: LinearLayout,
    var Discount_Limit  :Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var price_cust = 0.0
    var number2digitsss = 0.0

    companion object {
        var pos: Int = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.adapter_order_add_to_cart, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

        println("Discount_Limit ${Discount_Limit}")
        if(productArrAddToCart.size== (position+1)){
            statementHandler.view.visibility = View.GONE
        }else{
            statementHandler.view.visibility = View.VISIBLE
        }
        val categoryName = productArrAddToCart.get(position)?.categoryName
        val categoryId = productArrAddToCart.get(position)?.categoryId

        println("divisionnn=="+categoryName+"<<"+categoryId+"<<"+customertype)

        statementHandler.tvQuantity.text = productArrAddToCart!![position]!!.quantity.toString()

        if (categoryId!!.equals(1)){
            statementHandler.tvName.text = productArrAddToCart!![position]!!.productName
            statementHandler.tvDetails.text = "Model :- "+productArrAddToCart[position]!!.model_no.toString()
            statementHandler.tvPrice.text = "Hp :-"+ productArrAddToCart[position]!!.hp.toString()
            statementHandler.tvhp.text ="Stage :- "+productArrAddToCart[position]!!.product_no.toString()
            statementHandler.tvsuc.text ="Phase :- "+productArrAddToCart[position]!!.phase.toString()

        }else if (categoryId!!.equals(2)){
            statementHandler.tvName.text = productArrAddToCart[position]!!.productName.toString()
            statementHandler.tvDetails.text = "Brand Name :- "+productArrAddToCart[position]!!.brandName.toString()
            statementHandler.tvPrice.text = "Model :- "+productArrAddToCart[position]!!.model_no.toString()
        }else{
            statementHandler.tvName.text = productArrAddToCart!![position]!!.productName
            statementHandler.tvDetails.text = "Stage :-"+ productArrAddToCart[position]!!.product_no.toString()
            statementHandler.tvPrice.text = "Kw :- "+productArrAddToCart[position]!!.part_no.toString()
            statementHandler.tvhp.text = "hp :- "+productArrAddToCart[position]!!.hp.toString()
            statementHandler.tvsuc.text = "Suc*Del :- "+productArrAddToCart[position]!!.specification.toString()
        }

        if ((customertype.equals("Dealer") || customertype.equals("Distributor")) && categoryId.equals(1)) {

            statementHandler.tvRate.text =productArrAddToCart!![position]!!.mrp.toString()
            price_cust = (productArrAddToCart!![position]!!.quantity * productArrAddToCart!![position]!!.mrp.toString().toDouble()).toDouble()
            statementHandler.tvAmount.text = String.format("%.2f", price_cust)

            delearcalculation(position,statementHandler.tvName,statementHandler.tvDetails,
                statementHandler.tvGSTNew,statementHandler.imgDelete,statementHandler.tvAmount)

        } else if ((customertype.equals("Dealer") || customertype.equals("Distributor")) && categoryId.equals(2)) {
            statementHandler.tvRate.text =productArrAddToCart!![position]!!.mrp.toString()
            price_cust = (productArrAddToCart!![position]!!.quantity * productArrAddToCart!![position]!!.mrp.toString().toDouble()).toDouble()
            statementHandler.tvAmount.text = String.format("%.2f", price_cust)

            FanAndApplincesCalculation(position,statementHandler.tvName,statementHandler.tvDetails,
                statementHandler.tvGSTNew,statementHandler.imgDelete,statementHandler.tvAmount)

        } else if (customertype.equals("Distributor") && categoryId.equals(4)){
            var rate = productArrAddToCart!![position]!!.mrp!!.toDouble() / 1.06
            rate = String.format("%.2f", rate.toDouble()).toDouble()
            statementHandler.tvRate.text = rate.toString()


            price_cust = (productArrAddToCart!![position]!!.quantity * rate.toDouble()).toDouble()
            statementHandler.tvAmount.text = String.format("%.2f", price_cust)

            AgridealerCalculation(position,statementHandler.tvName,statementHandler.tvDetails,
                statementHandler.tvGSTNew,statementHandler.imgDelete,statementHandler.tvAmount)

        }
        else if (customertype.equals("Dealer") && categoryId.equals(4)){
            statementHandler.tvRate.text =productArrAddToCart!![position]!!.mrp.toString()
            price_cust = (productArrAddToCart!![position]!!.quantity * productArrAddToCart!![position]!!.mrp.toString().toDouble()).toDouble()
            statementHandler.tvAmount.text = String.format("%.2f", price_cust)

            AgridealerCalculation(position,statementHandler.tvName,statementHandler.tvDetails,
                statementHandler.tvGSTNew,statementHandler.imgDelete,statementHandler.tvAmount)
        }
        else{
            statementHandler.tvRate.text = productArrAddToCart!![position]!!.mrp.toString()
            price_cust = (productArrAddToCart!![position]!!.quantity * productArrAddToCart!![position]!!.mrp.toString().toDouble()).toDouble()
            statementHandler.tvAmount.text = String.format("%.2f", price_cust)

            linear_ebd.visibility = View.GONE
            linear_cluster.visibility = View.GONE
            linear_dealer.visibility = View.GONE
            linear_distributor.visibility = View.GONE
            linear_Frieght.visibility = View.GONE
            linear_special.visibility = View.GONE
            linear_ebd_dis.visibility = View.GONE
            linear_cashdiscount_pump.visibility = View.GONE
            linear_total_dis_pump.visibility = View.GONE

            linear_Doddiscount.visibility = View.GONE
            linear_Specialdiscount.visibility = View.GONE
            linear_distributionmargindiscount.visibility = View.GONE
            linear_cashdiscount.visibility = View.GONE
            linear_total_dis.visibility = View.GONE
            linear_extradiscount.visibility = View.GONE

            retailercalculation(position,statementHandler.tvName,statementHandler.tvDetails,
                statementHandler.tvGSTNew,statementHandler.imgDelete)
        }

    }



    private fun AgridealerCalculation(position: Int, tvName: TextView, tvDetails: TextView, tvGSTNew: TextView, imgDelete: ImageView, tvAmount: TextView) {
        linear_agri_std_discount.visibility = View.VISIBLE
        linear_agri_total_dis.visibility = View.VISIBLE
        var product_gst : ArrayList<Double> = ArrayList()
        var product_schme_amt : ArrayList<Double> = ArrayList()

        AddToCartActivity.tv_agri_dis_amt.text = ""
        AddToCartActivity.tv_std_agrivalue.text = ""
        AddToCartActivity.tv_agri_total_amt.text = ""
        AddToCartActivity.tv_agri_totalvalue.text = ""

        AddToCartActivity.edt_advance_pay.text.clear()
        AddToCartActivity.tv_remaining_pay.text = ""

        var calculate_dis = 0.0

        var grand_total = 0.0
        var sub_total = 0.0
        var tot_dis_value = 0.0
        var tot_dis_per = 0.0

        if (customertype.equals("Distributor")){

            for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {
                var schme_amt = value!!.mrp.toString().toDouble()/ 1.06 * value!!.quantity.toString().toDouble()
                product_schme_amt.add(schme_amt)
                product_gst.add(value!!.gst.toString().toDouble())
                sub_total += schme_amt
            }
            println("Final Sub Total: $sub_total")

        }else{
            for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {
                var schme_amt = value!!.mrp.toString().toDouble() * value!!.quantity.toString().toDouble()
                product_schme_amt.add(schme_amt)
                product_gst.add(value!!.gst.toString().toDouble())
                sub_total += value!!.mrp!!.toDouble() * value!!.quantity.toString().toDouble()
            }
        }


        grand_total = sub_total
        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total) // Pass grand_total as Double


        println("AAAAA=="+grand_total)

        gstcalculation(number2digitsss,grand_total,imgDelete,position)

        AddToCartActivity.tv_std_agrivalue.setOnClickListener {
            AddToCartActivity.edt_advance_pay.text.clear()
            AddToCartActivity.tv_remaining_pay.text = ""

            if (!calculate_dis.equals(0.0)){
                AgridealerCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else{
                showCustomDialog(context, AddToCartActivity.tv_std_agrivalue) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        var subtototal = sub_total-calculate_dis
                        val inputValue = AddToCartActivity.tv_std_agrivalue.text.toString().toDouble()
                        calculate_dis = subtototal * inputValue / 100
                        AddToCartActivity.tv_agri_dis_amt.text = String.format("%.2f", calculate_dis)

                        grand_total = subtototal - calculate_dis
                        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                        tot_dis_value = (sub_total - grand_total)
                        tot_dis_per = (tot_dis_value / sub_total)*100

                        AddToCartActivity.tv_agri_total_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                        AddToCartActivity.tv_agri_totalvalue.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun FanAndApplincesCalculation(position: Int, tvName: TextView, tvDetails: TextView, tvGSTNew: TextView, imgDelete: ImageView, tvAmount: TextView) {
        linear_Doddiscount.visibility = View.VISIBLE
        linear_Specialdiscount.visibility = View.VISIBLE
        linear_distributionmargindiscount.visibility = View.VISIBLE
        linear_extradiscount.visibility = View.VISIBLE
        linear_cashdiscount.visibility = View.VISIBLE
        linear_total_dis.visibility = View.VISIBLE
        var product_gst : ArrayList<Double> = ArrayList()
        var product_schme_amt : ArrayList<Double> = ArrayList()

        AddToCartActivity.tv_dodvalue.text = ""
        AddToCartActivity.tv_dod_amt.text = ""
        AddToCartActivity.tv_specialvalue.text = ""
        AddToCartActivity.tv_special_amt.text = ""
        AddToCartActivity.tv_marginvalue.text = ""
        AddToCartActivity.tv_margin_amt.text = ""
        AddToCartActivity.tv_cashvalue.text = ""
        AddToCartActivity.tv_cash_amt.text = ""
        AddToCartActivity.tv_disvalue.text = ""
        AddToCartActivity.tv_dis_amt.text = ""
        AddToCartActivity.tv_extravalue.text = ""
        AddToCartActivity.tv_extra_amt.text = ""

        var calculate_dob = 0.0
        var calculate_special = 0.0
        var calculate_dis = 0.0
        var calculate_extra = 0.0
        var calculate_cash = 0.0

        var tot_dis_value = 0.0
        var tot_dis_per = 0.0


        var grand_total = 0.0
        var sub_total = 0.0

        for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {
            var schme_amt = value!!.mrp.toString().toDouble() * value!!.quantity.toString().toDouble()
            product_schme_amt.add(schme_amt)
            product_gst.add(value!!.gst.toString().toDouble())
            sub_total += value!!.mrp!!.toDouble() * value!!.quantity.toString().toDouble()
        }

        grand_total = sub_total
        AddToCartActivity.tvAmount.text = grand_total.toString()

        AddToCartActivity.tv_dodvalue.setOnClickListener {
            if (calculate_dob.equals(0.0) && AddToCartActivity.tv_dodvalue.text.equals("0%")){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else if (!calculate_dob.equals(0.0)){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else{
                val percentages = arrayOf("0%","2.5%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()

                    var subtototal = sub_total-calculate_dob-calculate_special-calculate_dis-calculate_extra-calculate_cash
                    calculate_dob = subtototal * (selectedPercentageValue / 100)

                    AddToCartActivity.tv_dod_amt.text = String.format("%.2f", calculate_dob)
                    AddToCartActivity.tv_dodvalue.text = selectedPercentage

                    grand_total = subtototal-calculate_dob
                    AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt,product_gst,selectedPercentageValue,grand_total)

                    println("fannnncalll=dob="+sub_total+"<<"+grand_total)

                    tot_dis_value = (sub_total - grand_total)
                    tot_dis_per = (tot_dis_value / sub_total)*100

                    AddToCartActivity.tv_dis_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                    AddToCartActivity.tv_disvalue.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }
        AddToCartActivity.tv_specialvalue.setOnClickListener {
            if (AddToCartActivity.tv_dodvalue.text.isNullOrEmpty()){
                response_message("Please select DOD discount")
            }else if (calculate_special.equals(0.0) && AddToCartActivity.tv_specialvalue.text.equals("0%")){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else if (!calculate_special.equals(0.0)){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else{
                val percentages = arrayOf("0%","2%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()

                    var subtototal = sub_total-calculate_dob-calculate_special-calculate_dis-calculate_extra-calculate_cash
                    calculate_special = subtototal * (selectedPercentageValue / 100)

                    AddToCartActivity.tv_special_amt.text = String.format("%.2f", calculate_special)
                    AddToCartActivity.tv_specialvalue.text = selectedPercentage

                    grand_total = subtototal-calculate_special
                    AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt,product_gst,selectedPercentageValue,grand_total)

                    println("fannnncalll=spe="+sub_total+"<<"+grand_total)

                    tot_dis_value = (sub_total - grand_total)
                    tot_dis_per = (tot_dis_value / sub_total)*100

                    AddToCartActivity.tv_dis_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                    AddToCartActivity.tv_disvalue.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }
        AddToCartActivity.tv_marginvalue.setOnClickListener {
            if (AddToCartActivity.tv_dodvalue.text.isNullOrEmpty()){
                response_message("Please select DOD discount")
            }else if (AddToCartActivity.tv_specialvalue.text.isNullOrEmpty()){
                response_message("Please select Special discount")
            }else if (calculate_dis.equals(0.0) && AddToCartActivity.tv_marginvalue.text.equals("0%")){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else if (!calculate_dis.equals(0.0)){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else{
                val percentages = arrayOf("0%","5%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()

                    var subtototal = sub_total-calculate_dob-calculate_special-calculate_dis-calculate_extra-calculate_cash
                    calculate_dis = subtototal * (selectedPercentageValue / 100)

                    AddToCartActivity.tv_margin_amt.text = String.format("%.2f", calculate_dis)
                    AddToCartActivity.tv_marginvalue.text = selectedPercentage

                    grand_total = subtototal-calculate_dis
                    AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt,product_gst,selectedPercentageValue,grand_total)


                    tot_dis_value = (sub_total - grand_total)
                    tot_dis_per = (tot_dis_value / sub_total)*100

                    AddToCartActivity.tv_dis_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                    AddToCartActivity.tv_disvalue.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }
        AddToCartActivity.tv_extravalue.setOnClickListener {
            if (AddToCartActivity.tv_dodvalue.text.isNullOrEmpty()){
                response_message("Please select DOD discount")
            }else if (AddToCartActivity.tv_specialvalue.text.isNullOrEmpty()){
                response_message("Please select Special discount")
            }else if (AddToCartActivity.tv_marginvalue.text.isNullOrEmpty()){
                response_message("Please select Distributor margin discount")
            }else if (calculate_extra.equals(0.0) && AddToCartActivity.tv_extravalue.text.equals("0%")){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else if (!calculate_extra.equals(0.0)){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else{
                showCustomDialog(context, AddToCartActivity.tv_extravalue) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        var subtototal = sub_total-calculate_dob-calculate_special-calculate_dis-calculate_extra-calculate_cash
                        val inputValue = AddToCartActivity.tv_extravalue.text.toString().toDouble()
                        calculate_extra = subtototal * inputValue / 100
                        AddToCartActivity.tv_extra_amt.text = String.format("%.2f", calculate_extra)

                        grand_total = subtototal - calculate_extra
                        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)


                        tot_dis_value = (sub_total - grand_total)
                        tot_dis_per = (tot_dis_value / sub_total)*100

                        AddToCartActivity.tv_dis_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                        AddToCartActivity.tv_disvalue.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        AddToCartActivity.tv_cashvalue.setOnClickListener {
            if (AddToCartActivity.tv_dodvalue.text.isNullOrEmpty()){
                response_message("Please select DOD discount")
            }else if (AddToCartActivity.tv_specialvalue.text.isNullOrEmpty()){
                response_message("Please select Special discount")
            }else if (AddToCartActivity.tv_marginvalue.text.isNullOrEmpty()){
                response_message("Please select Distributor margin discount")
            }else if (AddToCartActivity.tv_extravalue.text.isNullOrEmpty()){
                response_message("Please Enter Extra discount")
            }else if (calculate_cash.equals(0.0) && AddToCartActivity.tv_cashvalue.text.equals("0%")){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else if (!calculate_cash.equals(0.0)){
                FanAndApplincesCalculation(position,tvName,tvDetails, tvGSTNew,imgDelete,tvAmount)
            }else{
                val percentages = arrayOf("0%","3%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()

                    var subtototal = sub_total-calculate_dob-calculate_special-calculate_dis-calculate_extra-calculate_cash
                    calculate_cash = subtototal * (selectedPercentageValue / 100)

                    AddToCartActivity.tv_cash_amt.text = String.format("%.2f", calculate_cash)
                    AddToCartActivity.tv_cashvalue.text = selectedPercentage

                    grand_total = subtototal-calculate_cash
                    AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt,product_gst,selectedPercentageValue,grand_total)


                    tot_dis_value = (sub_total - grand_total)
                    tot_dis_per = (tot_dis_value / sub_total)*100

                    AddToCartActivity.tv_dis_amt.text = DecimalFormat("##.#").format(tot_dis_value.toFloat()).toString()
                    AddToCartActivity.tv_disvalue.text = DecimalFormat("##.#").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }


        gstcalculation(number2digitsss,grand_total,imgDelete,position)

    }



    private fun delearcalculation(position: Int, tvName: TextView, tvDetails: TextView, tvGSTNew: TextView, imgDelete: ImageView, tvAmount: TextView) {

        linear_ebd.visibility = View.VISIBLE
        linear_cluster.visibility = View.VISIBLE
        linear_dealer.visibility = View.VISIBLE
        linear_distributor.visibility = View.VISIBLE
        linear_Frieght.visibility = View.VISIBLE
        linear_special.visibility = View.VISIBLE
        linear_ebd_dis.visibility = View.VISIBLE

        linear_cashdiscount_pump.visibility = View.VISIBLE
        linear_total_dis_pump.visibility = View.VISIBLE

        AddToCartActivity.tv_ebdfilter.text = ""
        AddToCartActivity.tv_ebd_dis_amt.text = ""
        AddToCartActivity.tv_distributorfilter.text = ""
        AddToCartActivity.tv_distributor_amt.text = ""
        AddToCartActivity.tv_Specialfilter.text = ""
        AddToCartActivity.tv_Special_amt.text = ""
        AddToCartActivity.tv_Frieghtfilter.text = ""
        AddToCartActivity.tv_Frieght_amt.text = ""
        AddToCartActivity.tv_clusterfilter.text = ""
        AddToCartActivity.tv_cluster_amt.text = ""
        AddToCartActivity.tv_dealerfilter.text = ""
        AddToCartActivity.tv_dealer_amt.text = ""

        AddToCartActivity.tv_cashvalue_pump.text = ""
        AddToCartActivity.tv_cash_amt_pump.text = ""
        AddToCartActivity.tv_disvalue_pump.text = ""
        AddToCartActivity.tv_dis_amt_pump.text = ""

        var product_schme_amt : ArrayList<Double> = ArrayList()
        var product_gst : ArrayList<Double> = ArrayList()
        var grand_total = 0.0
        var amount_custt = 0.0
        var amount_custt2 = 0.0
        var schme_amt = 0.0
        var schme_dis = 0.0
        var calculate_ebd = 0.0
        var calculate_cluster = 0.0
        var calculate_cash = 0.0
        var calculate_dealer = 0.0
        var calculate_distributor = 0.0
        var calculate_frieght = 0.0
        var calculate_specail = 0.0

        var tot_dis_value = 0.0
        var tot_dis_per = 0.0


        for (item in productArrAddToCart){
            var schme_amt = item!!.product_ebd_amount.toString().toDouble() * item!!.quantity.toString().toDouble()
            product_schme_amt.add(schme_amt)
            product_gst.add(item!!.gst.toString().toDouble())
        }

        for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {
            amount_custt += value!!.mrp!!.toDouble() * value!!.quantity.toString().toDouble()
            amount_custt2 += value!!.mrp!!.toDouble() * value!!.quantity.toString().toDouble()
//            schme_amt += value!!.scheme_amount!!.toDouble()
            schme_amt += value!!.amount_diff!!.toDouble() * value!!.quantity.toString().toDouble()
            schme_dis = value!!.discount!!.toDouble()
        }
        amount_custt -= schme_amt
        grand_total = amount_custt
        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
        AddToCartActivity.tv_ebd_amt.text = String.format("%.2f", schme_amt)
        AddToCartActivity.tv_stdfilter.text = String.format("%.2f", schme_dis)


        AddToCartActivity.tv_ebdfilter.setOnClickListener {
            if (calculate_ebd.equals(0.0) && AddToCartActivity.tv_ebdfilter.text.equals("0%")){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            }else if (!calculate_ebd.equals(0.0)){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            }else {
                if (Discount_Limit.equals(0) || Discount_Limit.equals(null) || Discount_Limit.equals("")){
                    val percentages = arrayOf("0%","1%","2%","3%")
                    val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Select Percentage")
                    builder.setAdapter(adapter) { _, which ->
                        val selectedPercentage = percentages[which]
                        val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                        var subtotal_ebd = amount_custt-calculate_ebd-calculate_distributor-calculate_specail-calculate_frieght-calculate_cluster-calculate_cash-calculate_dealer
                        calculate_ebd = subtotal_ebd * (selectedPercentageValue / 100)
                        AddToCartActivity.tv_ebd_dis_amt.text = String.format("%.2f", calculate_ebd)
                        AddToCartActivity.tv_ebdfilter.text = selectedPercentage

                        grand_total = subtotal_ebd-calculate_ebd
                        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        calcultionn(product_schme_amt,product_gst,selectedPercentageValue,grand_total)

                        tot_dis_value = ((amount_custt2 - grand_total))

                        tot_dis_per = (tot_dis_value / amount_custt2)*100



                        AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                        AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                        if (selectedPercentage.equals("0%")){
                            linear_dealer.visibility = View.VISIBLE
                        }else{
                            linear_dealer.visibility = View.GONE
                            AddToCartActivity.tv_dealerfilter.text = ""
                            AddToCartActivity.tv_dealer_amt.text = ""
                            calculate_dealer = 0.0
                        }
                    }
                    builder.show()
                }else{

                    val percentages = arrayOf("0%", "1%", "2%", "3%")
                    val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Select Percentage")
                    builder.setAdapter(adapter) { _, which ->
                        val selectedPercentage = percentages[which]
                        val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                        val subtotal_ebd = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                        val new_ebd = subtotal_ebd * (selectedPercentageValue / 100)

                        val new_grand_total = subtotal_ebd - new_ebd
                        val new_tot_dis_value = amount_custt2 - new_grand_total
                        val new_tot_dis_per = (new_tot_dis_value / amount_custt2) * 100

                        println("Discount_Limit 21 ${Discount_Limit}")
                        if (new_tot_dis_per > Discount_Limit) {
                            Toast.makeText(context, "Discount percentage exceeds "+Discount_Limit+ "%,please choose a lower value.", Toast.LENGTH_SHORT).show()
                        } else {
                            calculate_ebd = new_ebd
                            AddToCartActivity.tv_ebd_dis_amt.text = String.format("%.2f", calculate_ebd)
                            AddToCartActivity.tv_ebdfilter.text = selectedPercentage

                            grand_total = new_grand_total
                            AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                            val totalAmount = number2digitsss + grand_total
                            AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                            calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                            tot_dis_value = new_tot_dis_value
                            tot_dis_per = new_tot_dis_per

                            AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                            AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()

                            if (selectedPercentage == "0%") {
                                linear_dealer.visibility = View.VISIBLE
                            } else {
                                linear_dealer.visibility = View.GONE
                                AddToCartActivity.tv_dealerfilter.text = ""
                                AddToCartActivity.tv_dealer_amt.text = ""
                                calculate_dealer = 0.0
                            }
                        }
                    }
                    builder.show()
                }

            }
        }

        AddToCartActivity.tv_distributorfilter.setOnClickListener {
            if (AddToCartActivity.tv_ebdfilter.text.isNullOrEmpty()){
                response_message("Please select EBD discount")
            }else if (calculate_distributor.equals(0.0) && AddToCartActivity.tv_distributorfilter.text.equals("0")){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            }else if (!calculate_distributor.equals(0.0)){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            } else {
                if (Discount_Limit.equals(0) || Discount_Limit.equals(null) || Discount_Limit.equals("")){
                    showCustomDialog(context, AddToCartActivity.tv_distributorfilter) { enteredText ->
                        if (enteredText.isNotEmpty()) {
                            var subtototal3 = amount_custt-calculate_ebd-calculate_distributor-calculate_specail-calculate_frieght-calculate_cluster-calculate_cash-calculate_dealer
                            AddToCartActivity.tv_distributorfilter.text = enteredText
                            val inputValue = AddToCartActivity.tv_distributorfilter.text.toString().toDouble()
                            calculate_distributor = subtototal3 * inputValue / 100
                            AddToCartActivity.tv_distributor_amt.text = String.format("%.2f", calculate_distributor)

                            grand_total = subtototal3 - calculate_distributor
                            AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                            var totalAmount = number2digitsss + grand_total
                            AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                            var selectedPercentageValue = inputValue.toFloat()
                            calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)


                            tot_dis_value = (amount_custt2 - grand_total)
                            tot_dis_per = (tot_dis_value / amount_custt2)*100

                            AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                            AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                        } else {
                            Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    showCustomDialog(context, AddToCartActivity.tv_distributorfilter) { enteredText ->
                        if (enteredText.isNotEmpty()) {
                            val subtototal3 = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                            AddToCartActivity.tv_distributorfilter.text = enteredText
                            val inputValue = AddToCartActivity.tv_distributorfilter.text.toString().toDouble()
                            val new_distributor = subtototal3 * inputValue / 100
                            val new_grand_total = subtototal3 - new_distributor
                            val new_tot_dis_value = amount_custt2 - new_grand_total
                            val new_tot_dis_per = (new_tot_dis_value / amount_custt2) * 100


                            if (new_tot_dis_per > Discount_Limit) {
                                Toast.makeText(context, "Discount percentage exceeds "+Discount_Limit+ "%,please choose a lower value.", Toast.LENGTH_SHORT).show()
                                AddToCartActivity.tv_distributorfilter.text = "Select"
                            } else {
                                calculate_distributor = new_distributor
                                AddToCartActivity.tv_distributor_amt.text = String.format("%.2f", calculate_distributor)

                                grand_total = new_grand_total
                                AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                                val totalAmount = number2digitsss + grand_total
                                AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()

                                val selectedPercentageValue = inputValue.toFloat()
                                calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                                tot_dis_value = new_tot_dis_value
                                tot_dis_per = new_tot_dis_per

                                AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                                AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                            }
                        } else {
                            Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }
        }

        AddToCartActivity.tv_clusterfilter.setOnClickListener {
            if (AddToCartActivity.tv_ebdfilter.text.isNullOrEmpty()){
                response_message("Please select EBD discount")
            }else if (AddToCartActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (!calculate_cluster.equals(0.0)){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            } else {
                if (Discount_Limit.equals(0) || Discount_Limit.equals(null) || Discount_Limit.equals("")){
                    val percentages = arrayOf("0%","1%", "2%", "3%")
                    val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Select Percentage")
                    builder.setAdapter(adapter) { _, which ->
                        val selectedPercentage = percentages[which]
                        val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                        var subtotal = amount_custt-calculate_ebd-calculate_distributor-calculate_specail-calculate_frieght-calculate_cluster-calculate_cash-calculate_dealer

                        calculate_cluster = subtotal * (selectedPercentageValue / 100)

                        AddToCartActivity.tv_cluster_amt.text = String.format("%.2f", calculate_cluster)
                        AddToCartActivity.tv_clusterfilter.text = selectedPercentage

                        grand_total = subtotal-calculate_cluster
                        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        calcultionn(product_schme_amt,product_gst,selectedPercentageValue,grand_total)

                        tot_dis_value = (amount_custt2 - grand_total)
                        tot_dis_per = (tot_dis_value / amount_custt2)*100

                        AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                        AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                    }
                    builder.show()
                }else{
                    val percentages = arrayOf("0%", "1%", "2%", "3%")
                    val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Select Percentage")
                    builder.setAdapter(adapter) { _, which ->
                        val selectedPercentage = percentages[which]
                        val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                        val subtotal = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                        val new_cluster = subtotal * (selectedPercentageValue / 100)
                        val new_grand_total = subtotal - new_cluster
                        val new_tot_dis_value = amount_custt2 - new_grand_total
                        val new_tot_dis_per = (new_tot_dis_value / amount_custt2) * 100

                        if (new_tot_dis_per > Discount_Limit) {
                            Toast.makeText(context, " Discount percentage exceeds "+Discount_Limit+ "%,please choose a lower value.", Toast.LENGTH_SHORT).show()
                            AddToCartActivity.tv_clusterfilter.text = "Select"
                        } else {
                            calculate_cluster = new_cluster
                            AddToCartActivity.tv_cluster_amt.text = String.format("%.2f", calculate_cluster)
                            AddToCartActivity.tv_clusterfilter.text = selectedPercentage

                            grand_total = new_grand_total
                            AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                            val totalAmount = number2digitsss + grand_total
                            AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                            calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                            tot_dis_value = new_tot_dis_value
                            tot_dis_per = new_tot_dis_per

                            AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                            AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                        }
                    }
                    builder.show()
                }

            }
        }



        AddToCartActivity.tv_Frieghtfilter.setOnClickListener {
            if (AddToCartActivity.tv_ebdfilter.text.isNullOrEmpty()){
                response_message("Please select EBD discount")
            }else if (AddToCartActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (AddToCartActivity.tv_clusterfilter.text.isNullOrEmpty()){
                response_message("Please select Cluster discount")
            }else if (!calculate_frieght.equals(0.0)){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            } else {
                val percentages = arrayOf("0%","1%")
                val adapter = PercentageAdapter(context, android.R.layout.simple_list_item_1, percentages)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Percentage")
                builder.setAdapter(adapter) { _, which ->
                    val selectedPercentage = percentages[which]
                    val selectedPercentageValue = selectedPercentage.replace("%", "").toFloat()
                    var subtototal4 = amount_custt-calculate_ebd-calculate_distributor-calculate_specail-calculate_frieght-calculate_cluster-calculate_cash-calculate_dealer
                    calculate_frieght = subtototal4 * (selectedPercentageValue / 100)

                    AddToCartActivity.tv_Frieght_amt.text = String.format("%.2f", calculate_frieght)
                    AddToCartActivity.tv_Frieghtfilter.text = selectedPercentage

                    grand_total = subtototal4-calculate_frieght
                    AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                    var totalAmount = number2digitsss + grand_total
                    AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                    calcultionn(product_schme_amt,product_gst,selectedPercentageValue,grand_total)

                    tot_dis_value = (amount_custt2 - grand_total)
                    tot_dis_per = (tot_dis_value / amount_custt2)*100

                    AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                    AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                }
                builder.show()
            }
        }


        AddToCartActivity.tv_cashvalue_pump.setOnClickListener {
            if (AddToCartActivity.tv_ebdfilter.text.isNullOrEmpty()){
                response_message("Please select EBD discount")
            }else if (AddToCartActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (AddToCartActivity.tv_clusterfilter.text.isNullOrEmpty()){
                response_message("Please select Cluster discount")
            } else if (AddToCartActivity.tv_Frieghtfilter.text.isNullOrEmpty()){
                response_message("Please select Frieght discount")
            }else if (!calculate_cash.equals(0.0)){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            }else {
                showCustomDialog(context, AddToCartActivity.tv_cashvalue_pump) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        var subtototal2 = amount_custt-calculate_ebd-calculate_distributor-calculate_specail-calculate_frieght-calculate_cluster-calculate_cash-calculate_dealer
                        val inputValue = AddToCartActivity.tv_cashvalue_pump.text.toString().toDouble()
                        calculate_cash = subtototal2 * inputValue / 100
                        AddToCartActivity.tv_cash_amt_pump.text = String.format("%.2f", calculate_cash)
                        grand_total = subtototal2 - calculate_cash
                        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                        tot_dis_value = (amount_custt2 - grand_total)
                        tot_dis_per = (tot_dis_value / amount_custt2)*100


                        AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                        AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        AddToCartActivity.tv_dealerfilter.setOnClickListener {
            if (AddToCartActivity.tv_distributorfilter.text.isNullOrEmpty()){
                response_message("Please select MOU discount")
            }else if (AddToCartActivity.tv_clusterfilter.text.isNullOrEmpty()){
                response_message("Please select Cluster discount")
            }else if (AddToCartActivity.tv_Frieghtfilter.text.isNullOrEmpty()){
                response_message("Please select Frieght discount")
            }else if (AddToCartActivity.tv_cashvalue_pump.text.isNullOrEmpty()){
                response_message("Please select Cash discount")
            }else if (calculate_dealer.equals(0.0) && AddToCartActivity.tv_dealerfilter.text.equals("0")){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            }else if (!calculate_dealer.equals(0.0)){
                delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
            }else {
                showCustomDialog(context, AddToCartActivity.tv_dealerfilter) { enteredText ->
                    if (enteredText.isNotEmpty()) {
                        var subtototal2 = amount_custt-calculate_ebd-calculate_distributor-calculate_specail-calculate_frieght-calculate_cluster-calculate_cash-calculate_dealer
                        val inputValue = AddToCartActivity.tv_dealerfilter.text.toString().toDouble()
                        calculate_dealer = subtototal2 * inputValue / 100
                        AddToCartActivity.tv_dealer_amt.text = String.format("%.2f", calculate_dealer)
                        grand_total = subtototal2 - calculate_dealer
                        AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                        var totalAmount = number2digitsss + grand_total
                        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()
                        var selectedPercentageValue = inputValue.toFloat()
                        calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                        tot_dis_value = (amount_custt2 - grand_total)
                        tot_dis_per = (tot_dis_value / amount_custt2)*100

                        AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                        AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()

                        if (inputValue.equals("0")){
                            linear_ebd_dis.visibility = View.VISIBLE
                        }else{
                            linear_ebd_dis.visibility = View.GONE
                            AddToCartActivity.tv_ebdfilter.text = ""
                            AddToCartActivity.tv_ebd_dis_amt.text = ""
                            calculate_ebd = 0.0
                        }

                    } else {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        AddToCartActivity.tv_Specialfilter.setOnClickListener {
            /*    if (AddToCartActivity.tv_distributorfilter.text.isNullOrEmpty()){
                      response_message("Please select MOU discount")
                  } else if (AddToCartActivity.tv_Frieghtfilter.text.isNullOrEmpty()){
                      response_message("Please select Frieght discount")
                  }else if (AddToCartActivity.tv_clusterfilter.text.isNullOrEmpty()){
                      response_message("Please select Cluster discount")
                  }else if (AddToCartActivity.tv_cashvalue_pump.text.isNullOrEmpty()){
                      response_message("Please select Cash discount")
                  }else if (AddToCartActivity.tv_dealerfilter.text.isNullOrEmpty()){
                      response_message("Please select Deal discount")
                  } else */if (calculate_specail.equals(0.0) && AddToCartActivity.tv_Specialfilter.text.equals("0")){
            delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
        }else if (!calculate_specail.equals(0.0)){
            delearcalculation(position, tvName, tvDetails, tvGSTNew, imgDelete, tvAmount)
        } else {
            showCustomDialog(context, AddToCartActivity.tv_Specialfilter) { enteredText ->
                if (enteredText.isNotEmpty()) {
                    val subtototal5 = amount_custt - calculate_ebd - calculate_distributor - calculate_specail - calculate_frieght - calculate_cluster - calculate_cash - calculate_dealer
                    val inputValue = enteredText.toDouble()
                    val new_special = subtototal5 * inputValue / 100
                    val new_grand_total = subtototal5 - new_special
                    val new_tot_dis_value = amount_custt2 - new_grand_total
                    val new_tot_dis_per = (new_tot_dis_value / amount_custt2) * 100

                    /*if (new_tot_dis_per > 40) {
                        Toast.makeText(context, "Discount percentage exceeds 40%, please choose a lower value.", Toast.LENGTH_SHORT).show()
                    } else {*/
                    calculate_specail = new_special
                    AddToCartActivity.tv_Special_amt.text = String.format("%.2f", calculate_specail)

                    grand_total = new_grand_total
                    AddToCartActivity.tvAmount.text = String.format("%.2f", grand_total)
                    val totalAmount = number2digitsss + grand_total
                    AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()

                    val selectedPercentageValue = inputValue.toFloat()
                    calcultionn(product_schme_amt, product_gst, selectedPercentageValue, grand_total)

                    tot_dis_value = new_tot_dis_value
                    tot_dis_per = new_tot_dis_per

                    AddToCartActivity.tv_dis_amt_pump.text = DecimalFormat("##.##").format(tot_dis_value.toFloat()).toString()
                    AddToCartActivity.tv_disvalue_pump.text = DecimalFormat("##.##").format(tot_dis_per.toFloat()).toString()
                    // }
                } else {
                    Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                }
            }
        }
        }


        tvGSTNew.text = "GST: " + productArrAddToCart!![position]!!.gst + "%"

        gstcalculation(number2digitsss,grand_total,imgDelete,position)

    }


    private fun response_message(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    private fun calcultionn(
        product_schme_amt: ArrayList<Double>,
        product_gst: ArrayList<Double>,
        selectedPercentageValue: Float,
        grand_total: Double,
    ) {
        var Gst5amount = 0.0
        var Gst12amount = 0.0
        var Gst18amount = 0.0
        var Gst28amount = 0.0
        var Gst0amount = 0.0
        var gst = 0.0

        println("grand_total=="+grand_total)

        for (i in product_schme_amt.indices) {
            val originalPrice = product_schme_amt[i]
            val originalgst = product_gst[i]
            val calculatedValue = subtractPercentage(originalPrice, selectedPercentageValue)
            val calculatedgst = gstcalll(calculatedValue, originalgst)
            product_schme_amt[i] = String.format("%.2f", calculatedValue).toDouble()
            println("Price after originalgst $originalgst}")
            println("Price after subtracting $selectedPercentageValue% from $originalPrice: ${String.format("%.2f", calculatedValue)}")
            println("Price after gstt $selectedPercentageValue% from $calculatedValue: ${String.format("%.2f", calculatedgst)}")
            when (originalgst) {
                5.00 -> {
                    linear_5gst.visibility = View.VISIBLE
                    Gst5amount += calculatedgst.toString().toDouble()
                    AddToCartActivity.tv_5gstamt.text = String.format("%.2f", Gst5amount)
                }
                12.00 -> {
                    linear_12gst.visibility = View.VISIBLE
                    Gst12amount += calculatedgst.toString().toDouble()
                    println("Price after Gst12amount $Gst12amount}")
                    AddToCartActivity.tv_12gstamt.text = String.format("%.2f", Gst12amount)
                }
                18.00 -> {
                    linear_18gst.visibility = View.VISIBLE
                    Gst18amount += calculatedgst.toString().toDouble()
                    AddToCartActivity.tv_18gstamt.text = String.format("%.2f", Gst18amount)
                }
                28.00 -> {
                    linear_28gst.visibility = View.VISIBLE
                    Gst28amount += calculatedgst.toString().toDouble()
                    AddToCartActivity.tv_28gstamt.text = String.format("%.2f", Gst28amount)
                }
                else -> {
                    Gst0amount += calculatedgst.toString().toDouble()
                }
            }
        }

        gst = Gst12amount + Gst5amount + Gst18amount + Gst28amount + Gst0amount
        AddToCartActivity.tvGSTTotal.text = String.format("%.2f", gst)
        val totalAmount = gst + grand_total

        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()

    }

    private fun gstcalll(calculatedValue: Double, originalgst: Double): Any {
        val gstamount = calculatedValue *(originalgst/100)
        return gstamount
    }

    fun subtractPercentage(price: Double, percentage: Float): Double {
        val subtractionAmount = price * (percentage / 100)
        return price - subtractionAmount
    }

    private fun gstcalculation(
        number2digitsss: Double,
        grand_total: Double,
        imgDelete: ImageView,
        position: Int,
    ) {
        var quantity = 0
        var amount = 0.0
        var Gst5amount = 0.0
        var Gst12amount = 0.0
        var Gst18amount = 0.0
        var Gst28amount = 0.0
        var Gst0amount = 0.0
        var gst = 0.0

        if (customertype.equals("Distributor") && productArrAddToCart.get(position)?.categoryId!!.equals(4)) {
            for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {

                quantity += value!!.quantity

                var total = 0.0
                var price = "1"

                price = ((value.product_ebd_amount?.toDouble() ?: 0.0) / 1.06).toString()
                amount += value.amount.toDouble()


                if (value.gst!!.toDouble().equals(5.00)){
                    linear_5gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    Gst5amount = gstMul / 100
                    AddToCartActivity.tv_5gstamt.text = String.format("%.2f", Gst5amount)
                }
                if (value.gst!!.toDouble().equals(12.00)){
                    linear_12gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst12amount += gstamount
                    AddToCartActivity.tv_12gstamt.text = String.format("%.2f", Gst12amount)
                }
                if (value.gst!!.toDouble().equals(18.00)){
                    linear_18gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst18amount += gstamount
                    AddToCartActivity.tv_18gstamt.text = String.format("%.2f", Gst18amount)
                }
                if (value.gst!!.toDouble().equals(28.00)){
                    linear_28gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst28amount += gstamount
                    AddToCartActivity.tv_28gstamt.text = String.format("%.2f", Gst28amount)
                }
                if (value.gst!!.toDouble().equals(0.00)){
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst0amount += gstamount
                }
                gst = Gst12amount+Gst5amount+Gst18amount+Gst28amount+Gst0amount
            }

            val decimalFormat = DecimalFormat("##.##")
            decimalFormat.roundingMode = RoundingMode.DOWN

            this.number2digitsss = decimalFormat.format(gst.toFloat()).toDouble()

            AddToCartActivity.tvGSTTotal.text = this.number2digitsss.toString()

            var totalAmount = this.number2digitsss + grand_total
            AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()

            imgDelete.tag = position
            imgDelete.setOnClickListener { view ->
                AddToCartActivity.tv_cluster_amt.text = ""
                AddToCartActivity.tv_clusterfilter.text = ""
                AddToCartActivity.tv_dealerfilter.text = ""
                AddToCartActivity.tv_dealer_amt.text = ""
                AddToCartActivity.tv_distributorfilter.text = ""
                AddToCartActivity.tv_distributor_amt.text = ""
                AddToCartActivity.tv_Frieghtfilter.text = ""
                AddToCartActivity.tv_Frieght_amt.text = ""
                AddToCartActivity.tv_ebdfilter.text = ""
                AddToCartActivity.tv_ebd_dis_amt.text = ""
                AddToCartActivity.tv_Specialfilter.text = ""
                AddToCartActivity.tv_Special_amt.text = ""
                AddToCartActivity.tv_dodvalue.text = ""
                AddToCartActivity.tv_cashvalue_pump.text = ""
                AddToCartActivity.tv_cash_amt_pump.text = ""
                AddToCartActivity.tv_disvalue_pump.text = ""
                AddToCartActivity.tv_dis_amt_pump.text = ""
                AddToCartActivity.tv_stdfilter.text = ""
                AddToCartActivity.tv_ebd_amt.text = ""


                AddToCartActivity.tv_dodvalue.text = ""
                AddToCartActivity.tv_dod_amt.text = ""
                AddToCartActivity.tv_specialvalue.text = ""
                AddToCartActivity.tv_special_amt.text = ""
                AddToCartActivity.tv_marginvalue.text = ""
                AddToCartActivity.tv_margin_amt.text = ""
                AddToCartActivity.tv_cashvalue.text = ""
                AddToCartActivity.tv_cash_amt.text = ""
                AddToCartActivity.tv_disvalue.text = ""
                AddToCartActivity.tv_dis_amt.text = ""
                AddToCartActivity.tv_extravalue.text = ""
                AddToCartActivity.tv_extra_amt.text = ""

                AddToCartActivity.tv_agri_dis_amt.text = ""
                AddToCartActivity.tv_std_agrivalue.text = ""
                AddToCartActivity.tv_agri_total_amt.text = ""
                AddToCartActivity.tv_agri_totalvalue.text = ""

                var pos = view.tag as Int

                for ((index, value) in productArrAddToCart!!.withIndex()) {
                    if (value!!.id == productArrAddToCart[pos]!!.id) {
                        productArrAddToCart[index]!!.isAddToCart = false
                        productArrAddToCart[index]!!.quantity = 0
                        break
                    }
                }

                productArrAddToCart.removeAt(pos)

                var quantity = 0
                var amount = 0.0
                var Gst5amount = 0.0
                var Gst12amount = 0.0
                var Gst18amount = 0.0
                var Gst28amount = 0.0
                var Gst0amount = 0.0
                var gst = 0.0

                for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {

                    quantity += value!!.quantity

                    var total = 0.0
                    var price = "1"

                    price = ((value.product_ebd_amount?.toDouble() ?: 0.0) / 1.06).toString()
                    amount += value.product_ebd_amount!!.toDouble()


                    if (value.gst!!.toDouble().equals(5.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        Gst5amount = gstMul / 100
                        AddToCartActivity.tv_5gstamt.text = Gst5amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(12.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst12amount += gstamount
                        AddToCartActivity.tv_12gstamt.text = Gst12amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(18.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst18amount += gstamount
                        AddToCartActivity.tv_18gstamt.text = Gst18amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(28.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst28amount += gstamount
                        AddToCartActivity.tv_28gstamt.text = Gst28amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(0.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst0amount += gstamount
                    }

                }
                if (Gst5amount.equals(0.0)){
                    linear_5gst.visibility = View.GONE
                }
                if (Gst12amount.equals(0.0)){
                    linear_12gst.visibility = View.GONE
                }
                if (Gst18amount.equals(0.0)){
                    linear_18gst.visibility = View.GONE
                }
                if (Gst28amount.equals(0.0)){
                    linear_28gst.visibility = View.GONE
                }
                gst = Gst12amount+Gst5amount+Gst18amount+Gst28amount+Gst0amount


                AddToCartActivity.tvAmount.text = amount.toString()

                val number2digits: Double = String.format("%.2f", gst).toDouble()
                AddToCartActivity.tvGSTTotal.text = number2digits.toString()

                var totalAmount = number2digits + amount
                AddToCartActivity.tvTotal.text = (totalAmount).toString()

                notifyDataSetChanged()
            }


        }

        else{
            for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {

                quantity += value!!.quantity

                var total = 0.0
                var price = "1"

                price = value.product_ebd_amount.toString()
                amount += value.amount.toDouble()


                if (value.gst!!.toDouble().equals(5.00)){
                    linear_5gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    Gst5amount = gstMul / 100
                    AddToCartActivity.tv_5gstamt.text = String.format("%.2f", Gst5amount)
                }
                if (value.gst!!.toDouble().equals(12.00)){
                    linear_12gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst12amount += gstamount
                    AddToCartActivity.tv_12gstamt.text = String.format("%.2f", Gst12amount)
                }
                if (value.gst!!.toDouble().equals(18.00)){
                    linear_18gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst18amount += gstamount
                    AddToCartActivity.tv_18gstamt.text = String.format("%.2f", Gst18amount)
                }
                if (value.gst!!.toDouble().equals(28.00)){
                    linear_28gst.visibility = View.VISIBLE
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst28amount += gstamount
                    AddToCartActivity.tv_28gstamt.text = String.format("%.2f", Gst28amount)
                }
                if (value.gst!!.toDouble().equals(0.00)){
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst0amount += gstamount
                }
                gst = Gst12amount+Gst5amount+Gst18amount+Gst28amount+Gst0amount
            }


            val decimalFormat = DecimalFormat("##.##")
            decimalFormat.roundingMode = RoundingMode.DOWN

            this.number2digitsss = decimalFormat.format(gst.toFloat()).toDouble()

            AddToCartActivity.tvGSTTotal.text = this.number2digitsss.toString()

            var totalAmount = this.number2digitsss + grand_total
            AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()

            imgDelete.tag = position

            imgDelete.setOnClickListener { view ->
                AddToCartActivity.tv_cluster_amt.text = ""
                AddToCartActivity.tv_clusterfilter.text = ""
                AddToCartActivity.tv_dealerfilter.text = ""
                AddToCartActivity.tv_dealer_amt.text = ""
                AddToCartActivity.tv_distributorfilter.text = ""
                AddToCartActivity.tv_distributor_amt.text = ""
                AddToCartActivity.tv_Frieghtfilter.text = ""
                AddToCartActivity.tv_Frieght_amt.text = ""
                AddToCartActivity.tv_ebdfilter.text = ""
                AddToCartActivity.tv_ebd_dis_amt.text = ""
                AddToCartActivity.tv_Specialfilter.text = ""
                AddToCartActivity.tv_Special_amt.text = ""
                AddToCartActivity.tv_dodvalue.text = ""
                AddToCartActivity.tv_cashvalue_pump.text = ""
                AddToCartActivity.tv_cash_amt_pump.text = ""
                AddToCartActivity.tv_disvalue_pump.text = ""
                AddToCartActivity.tv_dis_amt_pump.text = ""
                AddToCartActivity.tv_stdfilter.text = ""
                AddToCartActivity.tv_ebd_amt.text = ""


                AddToCartActivity.tv_dodvalue.text = ""
                AddToCartActivity.tv_dod_amt.text = ""
                AddToCartActivity.tv_specialvalue.text = ""
                AddToCartActivity.tv_special_amt.text = ""
                AddToCartActivity.tv_marginvalue.text = ""
                AddToCartActivity.tv_margin_amt.text = ""
                AddToCartActivity.tv_cashvalue.text = ""
                AddToCartActivity.tv_cash_amt.text = ""
                AddToCartActivity.tv_disvalue.text = ""
                AddToCartActivity.tv_dis_amt.text = ""
                AddToCartActivity.tv_extravalue.text = ""
                AddToCartActivity.tv_extra_amt.text = ""

                AddToCartActivity.tv_agri_dis_amt.text = ""
                AddToCartActivity.tv_std_agrivalue.text = ""
                AddToCartActivity.tv_agri_total_amt.text = ""
                AddToCartActivity.tv_agri_totalvalue.text = ""

                var pos = view.tag as Int

                for ((index, value) in productArrAddToCart!!.withIndex()) {
                    if (value!!.id == productArrAddToCart[pos]!!.id) {
                        productArrAddToCart[index]!!.isAddToCart = false
                        productArrAddToCart[index]!!.quantity = 0
                        break
                    }
                }

                productArrAddToCart.removeAt(pos)

                var quantity = 0
                var amount = 0.0
                var Gst5amount = 0.0
                var Gst12amount = 0.0
                var Gst18amount = 0.0
                var Gst28amount = 0.0
                var Gst0amount = 0.0
                var gst = 0.0

                for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {

                    quantity += value!!.quantity

                    var total = 0.0
                    var price = "1"

                    price = value.product_ebd_amount.toString()
                    amount += value.product_ebd_amount!!.toDouble()


                    if (value.gst!!.toDouble().equals(5.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        Gst5amount = gstMul / 100
                        AddToCartActivity.tv_5gstamt.text = Gst5amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(12.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst12amount += gstamount
                        AddToCartActivity.tv_12gstamt.text = Gst12amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(18.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst18amount += gstamount
                        AddToCartActivity.tv_18gstamt.text = Gst18amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(28.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst28amount += gstamount
                        AddToCartActivity.tv_28gstamt.text = Gst28amount.toString()
                    }
                    if (value.gst!!.toDouble().equals(0.00)){
                        var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                        var gstamount = gstMul / 100
                        Gst0amount += gstamount
                    }

                }
                if (Gst5amount.equals(0.0)){
                    linear_5gst.visibility = View.GONE
                }
                if (Gst12amount.equals(0.0)){
                    linear_12gst.visibility = View.GONE
                }
                if (Gst18amount.equals(0.0)){
                    linear_18gst.visibility = View.GONE
                }
                if (Gst28amount.equals(0.0)){
                    linear_28gst.visibility = View.GONE
                }
                gst = Gst12amount+Gst5amount+Gst18amount+Gst28amount+Gst0amount


                AddToCartActivity.tvAmount.text = amount.toString()

                val number2digits: Double = String.format("%.2f", gst).toDouble()
                AddToCartActivity.tvGSTTotal.text = number2digits.toString()

                var totalAmount = number2digits + amount
                AddToCartActivity.tvTotal.text = (totalAmount).toString()

                notifyDataSetChanged()
            }
        }


    }

    private fun retailercalculation(
        position: Int,
        tvName: TextView,
        tvDetails: TextView,
        tvGSTNew: TextView,
        imgDelete: ImageView
    ) {
        var gstTemp = 0
        if ( productArrAddToCart!![position]!!.gst.toString().contains(".")) {
            gstTemp =  productArrAddToCart!![position]!!.gst.toString().split(".")[0].toInt()
        } else {
            gstTemp =  productArrAddToCart!![position]!!.gst.toString().toInt()
        }

        var gstMul = ((price_cust).toFloat() * productArrAddToCart!![position]!!.quantity * gstTemp).toDouble()
        var finalGst = gstMul / 100

        var quan = (price_cust).toFloat() * productArrAddToCart!![position]!!.quantity

        var mrpValue = ""

        tvGSTNew.text = "GST: " + productArrAddToCart!![position]!!.gst + "%"

        var quantity = 0
        var amount = 0.0
        var Gst5amount = 0.0
        var Gst12amount = 0.0
        var Gst18amount = 0.0
        var Gst28amount = 0.0
        var Gst0amount = 0.0
        var gst = 0.0

        for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {

            quantity += value!!.quantity

            var total = 0.0
            var price = "1"

            price = value.mrp.toString()
            amount += (value.mrp!!.toDouble() * value.quantity!!.toDouble())


            if (value.gst!!.toDouble().equals(5.00)){
                linear_5gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                Gst5amount = gstMul / 100
                AddToCartActivity.tv_5gstamt.text = String.format("%.2f", Gst5amount)
            }
            if (value.gst!!.toDouble().equals(12.00)){
                linear_12gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst12amount += gstamount
                AddToCartActivity.tv_12gstamt.text = String.format("%.2f", Gst12amount)
            }
            if (value.gst!!.toDouble().equals(18.00)){
                linear_18gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst18amount += gstamount
                AddToCartActivity.tv_18gstamt.text = String.format("%.2f", Gst18amount)
            }
            if (value.gst!!.toDouble().equals(28.00)){
                linear_28gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst28amount += gstamount
                AddToCartActivity.tv_28gstamt.text = String.format("%.2f", Gst28amount)
            }
            if (value.gst!!.toDouble().equals(0.00)){
                var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst0amount += gstamount
            }
            gst = Gst12amount+Gst5amount+Gst18amount+Gst28amount+Gst0amount

        }

        AddToCartActivity.tvAmount.text = DecimalFormat("##.#").format(amount.toFloat()).toString()

        val decimalFormat = DecimalFormat("##.##")
        decimalFormat.roundingMode = RoundingMode.DOWN
        val number2digits: Double = decimalFormat.format(gst.toFloat()).toDouble()
        AddToCartActivity.tvGSTTotal.text = number2digits.toString()

        var totalAmount = number2digits + amount
        AddToCartActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()



        imgDelete.tag = position
        imgDelete.setOnClickListener { view ->

            var pos = view.tag as Int

            for ((index, value) in productArrAddToCart!!.withIndex()) {
                if (value!!.id == productArrAddToCart[pos]!!.id) {
                    productArrAddToCart[index]!!.isAddToCart = false
                    productArrAddToCart[index]!!.quantity = 0
                    break
                }
            }

            productArrAddToCart.removeAt(pos)

            var quantity = 0
            var amount = 0.0
            var Gst5amount = 0.0
            var Gst12amount = 0.0
            var Gst18amount = 0.0
            var Gst28amount = 0.0
            var Gst0amount = 0.0
            var gst = 0.0

            for ((index, value) in ProductActivity.productArrAddToCart!!.withIndex()) {

                quantity += value!!.quantity

                var total = 0.0
                var price = "1"

                price = value.price.toString()
                amount += value.amount.toDouble()


                if (value.gst!!.toDouble().equals(5.00)){
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    Gst5amount = gstMul / 100
                    AddToCartActivity.tv_5gstamt.text = String.format("%.2f", Gst5amount)
                }
                if (value.gst!!.toDouble().equals(12.00)){
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst12amount += gstamount
                    AddToCartActivity.tv_12gstamt.text = String.format("%.2f", Gst12amount)
                }
                if (value.gst!!.toDouble().equals(18.00)){
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst18amount += gstamount
                    AddToCartActivity.tv_18gstamt.text = String.format("%.2f", Gst18amount)
                }
                if (value.gst!!.toDouble().equals(28.00)){
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst28amount += gstamount
                    AddToCartActivity.tv_28gstamt.text = String.format("%.2f", Gst28amount)
                }
                if (value.gst!!.toDouble().equals(0.00)){
                    var gstMul = (price.toFloat() * value.quantity * value.gst!!.toDouble()).toDouble()
                    var gstamount = gstMul / 100
                    Gst0amount += gstamount
                }

            }
            if (Gst5amount.equals(0.0)){
                linear_5gst.visibility = View.GONE
            }
            if (Gst12amount.equals(0.0)){
                linear_12gst.visibility = View.GONE
            }
            if (Gst18amount.equals(0.0)){
                linear_18gst.visibility = View.GONE
            }
            if (Gst28amount.equals(0.0)){
                linear_28gst.visibility = View.GONE
            }
            gst = Gst12amount+Gst5amount+Gst18amount+Gst28amount+Gst0amount

            println("dataa=="+Gst5amount+"<<"+Gst12amount+"<<"+Gst18amount+"<<"+Gst28amount+"<<"+Gst0amount)
            println("dataa=gst="+gst)

            AddToCartActivity.tvAmount.text = amount.toString()

            val number2digits: Double = String.format("%.2f", gst).toDouble()
            AddToCartActivity.tvGSTTotal.text = String.format("%.2f", number2digits)

            var totalAmount = number2digits + amount
            AddToCartActivity.tvTotal.text = (totalAmount).toString()

            notifyDataSetChanged()
        }

    }


    override fun getItemCount(): Int {
        return productArrAddToCart!!.size
    }

    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvEAN: TextView = itemView.findViewById(R.id.tvEAN)
        var tvGSTNew: TextView = itemView.findViewById(R.id.tvGSTNew)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvhp: TextView = itemView.findViewById(R.id.tvhp)
        var tvsuc: TextView = itemView.findViewById(R.id.tvsuc)
        var tvGST: TextView = itemView.findViewById(R.id.tvGST)
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        var tvRate: TextView = itemView.findViewById(R.id.tvRate)
        var tvTax: TextView = itemView.findViewById(R.id.tvTax)
        var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        var imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        var view: View = itemView.findViewById(R.id.view)
    }
}