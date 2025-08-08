package com.exp.clonefieldkonnect.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.PartiallyDispatchActivity
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import java.math.RoundingMode
import java.text.DecimalFormat

class OrderDetailsAdapterPartially(
    val context: Activity,
    val orderDetails: List<OrderDetailsModel.Data.Orderdetail>?,
    var buyerType: String?,
    var productCatId: Int?,
    var Schme_Dis: String?,
    var Ebd_Dis: String?,
    var Mou_Dis: String?,
    var Special_Dis: String?,
    var Frieght_Dis: String?,
    var Cluster_Dis: String?,
    var Cash_Dis: String?,
    var Delear_Dis: String?,
    var onClickEmail: OnEmailClick,
    var linear_5gst: LinearLayout,
    var linear_12gst: LinearLayout,
    var linear_18gst: LinearLayout,
    var linear_28gst: LinearLayout,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var price_cust = 0.0
    var updated_qty = ""
    var price_cust_new = 0.0
    var totalLineSum  = 0.0
    var remainingAmount  = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.adapter_order_details, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        if (Delear_Dis!!.isEmpty()){
            Delear_Dis = ""
        }
        println("DISCOINT VALUE ${Schme_Dis} ,${Ebd_Dis} ,${Mou_Dis}, ${Special_Dis},${Frieght_Dis},${Cluster_Dis} ,${Cash_Dis} ,${Delear_Dis}")

        if(orderDetails!!.size== (position+1)){
            statementHandler.view.visibility = View.GONE

        }else{
            statementHandler.view.visibility = View.VISIBLE
        }
        statementHandler.tvQuantityedit.visibility = View.VISIBLE
        if (productCatId!!.equals(1)){
            statementHandler.tvName.text = orderDetails!![position]!!.productName
            statementHandler.tvQuantity.text = ( orderDetails!![position]!!.quantity!!.toInt() - orderDetails!![position]!!.shippedQty!!.toInt()).toString()
            statementHandler.tvDetails.text ="Model :-"+ orderDetails[position]!!.model_no.toString()
            statementHandler.tvGSTNew.text = "HP :- "+ orderDetails[position]!!.hp.toString()
            statementHandler.tvPrice.text = "Stage :- "+ orderDetails[position]!!.product_no.toString()
            statementHandler.tvspecification.text = "Phase :- "+ orderDetails[position]!!.phase.toString()
            println("buyerTypebuyerType=="+buyerType)
            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                price_cust = (statementHandler.tvQuantity.text.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.ebd_amount.toString()
                orderDetails!![position]!!.lineTotal=  price_cust.toString()
            } else {
                updated_qty = (orderDetails!![position]!!.quantity!!.toInt() - orderDetails!![position]!!.shippedQty!!.toInt()).toString()
                orderDetails!![position]!!.quantity =  updated_qty.toString()
                println("qqqqqq=="+orderDetails!![position]!!.quantity)

                price_cust = (statementHandler.tvQuantity.text.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(price_cust.toString().toFloat()).toString()

                particallyupdate(orderDetails!![position]!!.quantity!!.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)

            }
            statementHandler.tvQuantityedit.setOnClickListener {
                showPupupDialog(orderDetails!![position]!!.quantity!!.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)
            }

        }
        else if (productCatId!!.equals(2)){
            statementHandler.tvName.text = orderDetails!![position]!!.productName
            statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
            statementHandler.tvDetails.text = "Model :-"+orderDetails!![position]!!.model_no.toString()
            statementHandler.tvGSTNew.text = "Brand Name :-"+orderDetails!![position]!!.brand_name.toString()
            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.ebd_amount.toString()
                orderDetails!![position]!!.lineTotal=  price_cust.toString()
            } else {
                updated_qty = (orderDetails!![position]!!.quantity!!.toInt() - orderDetails!![position]!!.shippedQty!!.toInt()).toString()
                orderDetails!![position]!!.quantity =  updated_qty.toString()
                println("qqqqqq=="+orderDetails!![position]!!.quantity)

                price_cust = (statementHandler.tvQuantity.text.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(price_cust.toString().toFloat()).toString()

                particallyupdate(orderDetails!![position]!!.quantity!!.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)

            }
            statementHandler.tvQuantityedit.setOnClickListener {
                showPupupDialog(orderDetails!![position]!!.quantity.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)
            }

        }
        else if (productCatId!!.equals(4)){
            statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
            statementHandler.tvDetails.text = "Stage :- "+ orderDetails[position]!!.product_no.toString()
            statementHandler.tvGSTNew.text = "kW :- "+ orderDetails[position]!!.part_no.toString()
            statementHandler.tvPrice.text = "HP :- "+ orderDetails[position]!!.hp.toString()
            statementHandler.tvspecification.text = "Suc*Del :- "+ orderDetails[position]!!.specification.toString()
            if (buyerType.equals("Distributor")) {
                price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.price.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                orderDetails!![position]!!.lineTotal=  price_cust.toString()
            } else {
                updated_qty = (orderDetails!![position]!!.quantity!!.toInt() - orderDetails!![position]!!.shippedQty!!.toInt()).toString()
                orderDetails!![position]!!.quantity =  updated_qty.toString()
                println("qqqqqq=="+orderDetails!![position]!!.quantity)

                price_cust = (statementHandler.tvQuantity.text.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(price_cust.toString().toFloat()).toString()

                particallyupdate(orderDetails!![position]!!.quantity!!.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)

            }
            statementHandler.tvName.text = orderDetails!![position]!!.productName
            statementHandler.tvQuantityedit.setOnClickListener {
                showPupupDialog(orderDetails!![position]!!.quantity.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)
            }

        }
        else{
            statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
            statementHandler.tvDetails.text = "Stage :- "+ orderDetails[position]!!.product_no.toString()
            statementHandler.tvGSTNew.text = "kW :- "+ orderDetails[position]!!.part_no.toString()
            statementHandler.tvPrice.text = "HP :- "+ orderDetails[position]!!.hp.toString()
            statementHandler.tvspecification.text = "Suc*Del :- "+ orderDetails[position]!!.specification.toString()

            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                price_cust = (orderDetails?.get(position)?.quantity.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvAmount.text = price_cust.toString()
                statementHandler.tvRate.text = orderDetails!![position]!!.ebd_amount.toString()
            } else {

                updated_qty = (orderDetails!![position]!!.quantity!!.toInt() - orderDetails!![position]!!.shippedQty!!.toInt()).toString()
                orderDetails!![position]!!.quantity =  updated_qty.toString()
                println("qqqqqq=="+orderDetails!![position]!!.quantity)
                price_cust = (statementHandler.tvQuantity.text.toString().toDouble() * orderDetails?.get(position)?.ebd_amount.toString().toDouble())
                statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
                statementHandler.tvAmount.text = DecimalFormat("##.#").format(price_cust.toString().toFloat()).toString()

                particallyupdate(orderDetails!![position]!!.quantity!!.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)
            }
            statementHandler.tvName.text = orderDetails!![position]!!.productName
            statementHandler.tvQuantityedit.setOnClickListener {
                showPupupDialog(orderDetails!![position]!!.quantity.toString(), statementHandler.tvQuantity,
                    statementHandler.tvAmount,statementHandler.tvRate,orderDetails!![position]!!.ebd_amount.toString(),
                    buyerType!!,orderDetails!![position]!!.price.toString(),productCatId!!.toInt(),orderDetails,position)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showPupupDialog(Quty : String ,tvQuantity : TextView,tvAmount : TextView,tvRate: TextView,
                                ebd_amount : String ,buyerType :String,price: String,productCatId:Int,
                                orderDetails:List<OrderDetailsModel.Data.Orderdetail>?,position: Int) {

        lateinit var alertDialog2: AlertDialog
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_submit_layout_edit_qty, null)
        builder.setCancelable(false)

        val cardbtn_submit: CardView = view.findViewById(R.id.cardbtn_submit)
        val edtt_remark = view.findViewById<EditText>(R.id.edtt_remark)
        val img_close: ImageView = view.findViewById(R.id.img_close)

        cardbtn_submit.setOnClickListener {
            val remarkText = edtt_remark.text.toString() // Get the EditText input

            if (remarkText.isEmpty()) {
                Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show()
            } else if (remarkText == "0") {
                edtt_remark.setText("")
                Toast.makeText(context, "Please enter correct quantity", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    println("QutyQuty=="+Quty)
                    val remarkQuantity = remarkText.toInt() // Convert the input to Int
                    val productQuantity = Quty.toInt() // Convert Quty to Int for comparison

                    if (remarkQuantity > productQuantity) {
                        edtt_remark.setText("")
                        Toast.makeText(context, "Updated quantity more than product quantity", Toast.LENGTH_SHORT).show()
                    } else {
                        println("productCatId=="+productCatId)
                        if (productCatId!!.equals(1)){
                            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * ebd_amount.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()

                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }
                        else if (productCatId!!.equals(2)){
                            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * ebd_amount.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }
                        else if (productCatId!!.equals(3)){
                            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * ebd_amount.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }
                        else if (productCatId!!.equals(4)){
                            if (buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }

                        alertDialog2.dismiss()
                    }
                } catch (e: NumberFormatException) {
                    edtt_remark.setText("")
                    Toast.makeText(context, "Invalid quantity entered", Toast.LENGTH_SHORT).show()
                }
            }
        }

        img_close.setOnClickListener {
            alertDialog2.dismiss()
        }

        builder.setView(view)

        alertDialog2 = builder.create()
        alertDialog2.show()


    }


    private fun particallyupdate(Quty : String ,tvQuantity : TextView,tvAmount : TextView,tvRate: TextView,
                                ebd_amount : String ,buyerType :String,price: String,productCatId:Int,
                                orderDetails:List<OrderDetailsModel.Data.Orderdetail>?,position: Int) {

            val remarkText = Quty// Get the EditText input

                try {
                    val remarkQuantity = remarkText.toInt() // Convert the input to Int

                        println("productCatId=="+productCatId)
                        if (productCatId!!.equals(1)){
                            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * ebd_amount.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()

                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }
                        else if (productCatId!!.equals(2)){
                            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * ebd_amount.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }
                        else if (productCatId!!.equals(3)){
                            if (buyerType.equals("Dealer") || buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * ebd_amount.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }
                        else if (productCatId!!.equals(4)){
                            if (buyerType.equals("Distributor")) {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                tvQuantity.text = remarkQuantity.toString()

                            } else {
                                price_cust = (remarkQuantity.toString().toDouble() * price.toString().toDouble())
                                tvAmount.text = DecimalFormat("##.##").format(price_cust.toString().toFloat()).toString()
                                price_cust_new = tvAmount.text.toString().toDouble()
                                orderDetails?.get(position)?.lineTotal = price_cust_new.toString()
                                orderDetails?.get(position)?.quantity = remarkQuantity.toString()
                                tvQuantity.text = remarkQuantity.toString()
                                retailercalculation(position,price,remarkQuantity,orderDetails)
                            }
                        }
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Invalid quantity entered", Toast.LENGTH_SHORT).show()
                }

    }

    private fun retailercalculation(
        position: Int,
        price: String,
        remarkQuantity: Int,
        orderDetails: List<OrderDetailsModel.Data.Orderdetail>?
    ) {
        var quantity = 0
        var amount = 0.0
        var Gst5amount = 0.0
        var Gst12amount = 0.0
        var Gst18amount = 0.0
        var Gst28amount = 0.0
        var Gst0amount = 0.0
        var gst = 0.0

        println("CCCCC=="+position+"<<"+price+"<<"+remarkQuantity+"<<"+ orderDetails?.get(position)!!.lineTotal.toString()+"<<"+orderDetails.size)

        for ((index, value) in orderDetails!!.withIndex()) {

            quantity = value!!.quantity!!.toInt()
            println("CCCCC=quantity="+quantity)

            var total = 0.0
            var price = "1"

            price = value.lineTotal.toString()
            amount += (value.lineTotal!!.toDouble())

            println("CCCCC=amount="+amount)


//            var gsttt = orderDetails?.get(position)!!.gst.toString()

//            amount += (price!!.toDouble() * remarkQuantity!!.toDouble())


            if (value.gst!!.toDouble().equals(5.00)){
                linear_5gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst5amount += gstamount
                PartiallyDispatchActivity.tv_5gstamt.text = String.format("%.2f", Gst5amount)
            }
            if (value.gst!!.toDouble().equals(12.00)){
                linear_12gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst12amount += gstamount
                PartiallyDispatchActivity.tv_12gstamt.text = String.format("%.2f", Gst12amount)
            }
            if (value.gst!!.toDouble().equals(18.00)){
                linear_18gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst18amount += gstamount
                PartiallyDispatchActivity.tv_18gstamt.text = String.format("%.2f", Gst18amount)
            }
            if (value.gst!!.toDouble().equals(28.00)){
                linear_28gst.visibility = View.VISIBLE
                var gstMul = (price.toFloat() * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst28amount += gstamount
                PartiallyDispatchActivity.tv_28gstamt.text = String.format("%.2f", Gst28amount)
            }
            if (value.gst!!.toDouble().equals(0.00)){
                var gstMul = (price.toFloat() * value.gst!!.toDouble()).toDouble()
                var gstamount = gstMul / 100
                Gst0amount += gstamount
            }
            gst = Gst12amount+Gst5amount+Gst18amount+Gst28amount+Gst0amount
            println("gstgst=="+gst+"<<"+amount)


        }
        PartiallyDispatchActivity.tvAmount.text = DecimalFormat("##.#").format(amount.toFloat()).toString()

        val decimalFormat = DecimalFormat("##.##")
        decimalFormat.roundingMode = RoundingMode.DOWN
        val number2digits: Double = decimalFormat.format(gst.toFloat()).toDouble()
        PartiallyDispatchActivity.tvGSTTotal.text = number2digits.toString()

        var totalAmount = number2digits + amount
        PartiallyDispatchActivity.tvTotal.text = DecimalFormat("##.#").format(totalAmount.toFloat()).toString()

    }


    fun applyDiscount(amount: Double, discount: String?): Double {
        return discount?.toDoubleOrNull()?.let { percentage ->
            amount - (amount * (percentage / 100))
        } ?: amount
    }
    fun getPercentageValue(amount: Double, discount: String?): Double {
        return discount?.toDoubleOrNull()?.let { percentage ->
            amount * (percentage / 100)
        } ?: amount
    }
    override fun getItemCount(): Int {
        return orderDetails!!.size
    }

    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        var tvQuantityedit: ImageView = itemView.findViewById(R.id.tvQuantityedit)
        var view: View = itemView.findViewById(R.id.view)
    }
    interface OnEmailClick {
        fun onClickEmail(remainingAmount: Double,Schme_Dis_amt: Double,Ebd_Dis_amt: Double,Mou_Dis_amt: Double,Cluster_Dis_amt: Double
                         ,Frieght_Dis: Double,Cash_Dis: Double,Delear_Dis: Double,Special_Dis: Double,totalAmount: Double) {

        }
    }
}