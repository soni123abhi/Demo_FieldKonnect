package com.exp.clonefieldkonnect.adapter


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.CounterDashboardActivity
import com.exp.clonefieldkonnect.databinding.AdapterBeatCustomerBinding
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.BeatCustomerModel


class BeatCustomerAdapter(
    val arr: ArrayList<BeatCustomerModel?>,
    val isLead: Boolean,
    var isToday: Boolean,
    var beatScheduleId: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ItemViewHolder(val binding: AdapterBeatCustomerBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun addData(dataViews: List<BeatCustomerModel?>) {
        this.arr.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun clear() {
        this.arr.clear()
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            arr.add(null)
            notifyItemInserted(arr.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (arr.size != 0) {
            arr.removeAt(arr.size - 1)
            notifyItemRemoved(arr.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context

        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val binding = AdapterBeatCustomerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ItemViewHolder(binding)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val customer = arr[position]!!

            holder.binding.tvName.text = customer.customerName
            holder.binding.tvAddress.text = customer.address
            holder.binding.tvGrade.text = customer.grade
            holder.binding.tvStatus.text = customer.visitStatus

            if (customer.latitude.isNullOrEmpty() && customer.longitude.isNullOrEmpty()) {
                holder.binding.cardloc.setBackgroundResource(R.color.bg_loc_red)
            } else {
                holder.binding.cardloc.setBackgroundResource(R.color.bg_loc_green)
            }

            val drawableResource =
                if (customer.latitude.isNullOrEmpty() && customer.longitude.isNullOrEmpty()) {
                    R.drawable.ic_location_customer
                } else {
                    R.drawable.ic_location_customer2
                }
            holder.binding.txtLoc.setCompoundDrawablesWithIntrinsicBounds(drawableResource, 0, 0, 0)

            // Grade logic
            if (!customer.grade.isNullOrEmpty()) {
                holder.binding.cardGrade.visibility = View.VISIBLE
                when (customer.grade) {
                    "Grade A" -> {
                        holder.binding.cardGrade.setCardBackgroundColor(Color.parseColor("#FFF4B9"))
                        holder.binding.tvGrade.setTextColor(Color.parseColor("#CFA200"))
                    }
                    "Grade B" -> {
                        holder.binding.cardGrade.setCardBackgroundColor(Color.parseColor("#E3E3E3"))
                        holder.binding.tvGrade.setTextColor(Color.parseColor("#7E7E7E"))
                    }
                    "Grade C" -> {
                        holder.binding.cardGrade.setCardBackgroundColor(Color.parseColor("#FFC398"))
                        holder.binding.tvGrade.setTextColor(Color.parseColor("#B27244"))
                    }
                    "Grade D" -> {
                        holder.binding.cardGrade.setCardBackgroundColor(Color.parseColor("#B8B8B8"))
                        holder.binding.tvGrade.setTextColor(Color.parseColor("#494949"))
                    }
                }
            } else {
                holder.binding.cardGrade.visibility = View.GONE
            }

            // Visit status logic
            if (!customer.visitStatus.isNullOrEmpty()) {
                holder.binding.cardStatus.visibility = View.VISIBLE
                when (customer.visitStatus) {
                    "Hot" -> {
                        holder.binding.cardStatus.setCardBackgroundColor(Color.parseColor("#FFF2F2"))
                        holder.binding.tvStatus.setTextColor(Color.parseColor("#FF0000"))
                    }
                    "Cold" -> {
                        holder.binding.cardStatus.setCardBackgroundColor(Color.parseColor("#F2F5FF"))
                        holder.binding.tvStatus.setTextColor(Color.parseColor("#0057FF"))
                    }
                    "Warm", "Existing" -> {
                        holder.binding.cardStatus.setCardBackgroundColor(Color.parseColor("#FFFBD6"))
                        holder.binding.tvStatus.setTextColor(Color.parseColor("#EAC70F"))
                    }
                }
            } else {
                holder.binding.cardStatus.visibility = View.GONE
            }

            // Load image
            Glide.with(mcontext).load(customer.profile_image).into(holder.binding.img)

            // Clicks
            holder.binding.relativeMain.setOnClickListener {
                StaticSharedpreference.saveInfo(
                    Constant.CHECKIN_CUST_ID,
                    customer.customerId.toString(),
                    mcontext
                )

                val intent = Intent(mcontext, CounterDashboardActivity::class.java).apply {
                    putExtra("image", customer.profile_image.toString())
                    putExtra("customerName", customer.customerName.toString())
                    putExtra("isLead", isLead)
                    putExtra("checkInId", "")
                    putExtra("outstanding", customer.outstanding.toString())
                    putExtra("customerAddress", customer.address.toString())
                    putExtra("isToday", isToday)
                    putExtra("beat_schedule_id", beatScheduleId)
                }
                mcontext.startActivity(intent)
            }

            holder.binding.linearLocation.setOnClickListener {
                if (!customer.latitude.isNullOrEmpty() && !customer.longitude.isNullOrEmpty()) {
                    val lat = customer.latitude
                    val long = customer.longitude
                    val label = customer.address
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:$lat,$long?q=$lat,$long($label)")
                    )
                    mcontext.startActivity(intent)
                }
            }

            holder.binding.linearCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${customer.customerMobile}")
                }
                mcontext.startActivity(intent)
            }

            holder.binding.linearWhatsApp.setOnClickListener {
                openWhatsApp(customer.customerMobile ?: "", mcontext)
            }

            holder.binding.linearMail.setOnClickListener {
                if (!customer.email.isNullOrEmpty()) {
                    val to = customer.email
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
                        putExtra(Intent.EXTRA_SUBJECT, "")
                        putExtra(Intent.EXTRA_TEXT, "")
                    }
                    mcontext.startActivity(Intent.createChooser(intent, "Choose an Email client :"))
                }
            }
        }
    }

    private fun openWhatsApp(number: String, context: Context) {
        val installed = appInstalledOrNot("com.whatsapp",context)

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("http://api.whatsapp.com/send?phone=$number")
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Whats app not installed on your device",
                Toast.LENGTH_SHORT
            ).show()

        }

    }


    //Create method appInstalledOrNot
    private fun appInstalledOrNot(url: String,context: Context): Boolean {
        val packageManager: PackageManager = context.getPackageManager()
        val app_installed: Boolean
        app_installed = try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }
    override fun getItemViewType(position: Int): Int {
        return if (arr[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }

}