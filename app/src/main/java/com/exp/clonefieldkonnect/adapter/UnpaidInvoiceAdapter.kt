package com.exp.clonefieldkonnect.adapter


import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.fragment.PaymentReceivedFragment
import com.exp.clonefieldkonnect.model.UnpaidInvoiceModel
import java.util.regex.Pattern


class UnpaidInvoiceAdapter(val arr: ArrayList<UnpaidInvoiceModel.Datum>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context
    val arrInt: ArrayList<Int> = arrayListOf()

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvNumber: TextView = itemView.findViewById(R.id.tvNumber)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        var tvAmountDue: TextView = itemView.findViewById(R.id.tvAmountDue)
        var edtAmount: TextView = itemView.findViewById(R.id.edtAmount)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mcontext = parent.context

        val v =
            LayoutInflater.from(mcontext).inflate(R.layout.adapter_unpaid_invoice, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

        statementHandler.tvNumber.text = arr[position].invoiceNo
        statementHandler.tvAmount.text = arr[position].grandTotal
        statementHandler.tvAmountDue.text = arr[position].amountUnpaid.toString()
        statementHandler.tvDate.text = arr[position].invoiceDate

        statementHandler.edtAmount.setFilters(
            arrayOf<InputFilter>(
                filter,
                LengthFilter(arr[position].amountUnpaid.toString().length)
            )
        )

        statementHandler.edtAmount.tag = position
        statementHandler.edtAmount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {


                    arr!![statementHandler.adapterPosition].enterAmount =
                        statementHandler.edtAmount.text.toString()

                    var sum= 0
                    for (value in arr) {

                        if(value.enterAmount!="")
                        sum += value.enterAmount.toInt()

                    }
                    PaymentReceivedFragment.edtAmount.setText(sum.toString())


            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {


            }
        })

        statementHandler.edtAmount.setText(arr[position].enterAmount)

    }


    override fun getItemCount(): Int {
        return arr.size
    }

    var filter = InputFilter { source, start, end, dest, dstart, dend ->
        for (i in start until end) {
            if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*")
                    .matcher(source[i].toString()).matches()
            ) {
                return@InputFilter ""
            }
        }
        null
    }
}