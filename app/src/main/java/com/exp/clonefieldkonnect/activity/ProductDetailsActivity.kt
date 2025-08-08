package com.exp.clonefieldkonnect.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.exp.import.Utilities
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.ProductActivity.Companion.productArrAddToCart
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.helper.DialogClass
import com.exp.clonefieldkonnect.helper.StaticSharedpreference
import com.exp.clonefieldkonnect.model.ProductNewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.text.DecimalFormat
import java.util.HashMap

class ProductDetailsActivity : AppCompatActivity() {

    lateinit var cardBack: CardView
    lateinit var cardAddToCart: CardView
    lateinit var cardcontinueorder: CardView

    lateinit var linearMinus: LinearLayout
    lateinit var linearAdd: LinearLayout

    lateinit var tvUnit: TextView
    lateinit var tvDescription: TextView
    lateinit var tvBrand: TextView
    lateinit var tvDisplay: TextView
    lateinit var tvCount: TextView
    lateinit var tvGG: TextView
    lateinit var tvOEPart: TextView
    lateinit var tvSpecification: TextView
    lateinit var tvCategory: TextView
    lateinit var tvSubCat: TextView
    lateinit var edtCount: EditText
    lateinit var edtDetails: AutoCompleteTextView
    lateinit var imgProduct: ImageView
    lateinit var badge_layout1: RelativeLayout
    lateinit var linearAmount: LinearLayout
    lateinit var edtPrice: EditText
    lateinit var edtTax: EditText
    lateinit var edtAmount: EditText
    lateinit var edtDiscount: EditText
    lateinit var edtTotalDiscount: EditText
    lateinit var cbGST: CheckBox
    var isGST: Boolean = true

    var productDetail: ProductNewModel = ProductNewModel()
    private var quantityCount = 1
    private var productId = 0
    private var productDetailId = -1
    private var productDetailSelectedPos = 0

    private var disablePrice: Boolean = false
    private var disableDiscount: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        initViews()
    }

    private fun initViews() {
        edtTotalDiscount = findViewById(R.id.edtTotalDiscount)
        cbGST = findViewById(R.id.cbGST)
        linearAmount = findViewById(R.id.linearAmount)
        edtPrice = findViewById(R.id.edtPrice)
        edtTax = findViewById(R.id.edtTax)
        tvSubCat = findViewById(R.id.tvSubCat)
        tvCategory = findViewById(R.id.tvCategory)
        tvSpecification = findViewById(R.id.tvSpecification)
        tvGG = findViewById(R.id.tvGG)
        tvOEPart = findViewById(R.id.tvOEPart)

        edtDiscount = findViewById(R.id.edtDiscount)
        edtAmount = findViewById(R.id.edtAmount)

        tvCount = findViewById(R.id.tvCount)
        linearMinus = findViewById(R.id.linearMinus)
        linearAdd = findViewById(R.id.linearAdd)
        imgProduct = findViewById(R.id.imgProduct)
        cardAddToCart = findViewById(R.id.cardAddToCart)
        badge_layout1 = findViewById(R.id.badge_layout1)

        tvUnit = findViewById(R.id.tvUnit)
        tvDescription = findViewById(R.id.tvDescription)
        tvBrand = findViewById(R.id.tvBrand)
        tvDisplay = findViewById(R.id.tvDisplay)
        edtCount = findViewById(R.id.edtCount)

        edtDetails = findViewById(R.id.edtDetails)
        cardBack = findViewById(R.id.cardBack)
        cardcontinueorder = findViewById(R.id.cardcontinueorder)

        cardcontinueorder.setOnClickListener {
            onBackPressed()
        }


        cardBack.setOnClickListener {
            onBackPressed()
        }

        cbGST.setOnCheckedChangeListener { compoundButton, b ->

            isGST = !b
            calculateAmount(
                edtPrice.text.toString(),
                edtDiscount.text.toString(),
                edtCount.text.toString()
            )
        }

        edtDetails.setOnClickListener {

            edtDetails.showDropDown()
        }

        linearAdd.setOnClickListener {

            /*  if (productDetailId == -1) {
                  Toast.makeText(this@ProductDetailsActivity,"Please select product detail",Toast.LENGTH_LONG).show()
                  return@setOnClickListener
              }*/
            quantityCount++

            edtCount.setText(quantityCount.toString())
            calculateAmount(
                edtPrice.text.toString(),
                edtDiscount.text.toString(),
                edtCount.text.toString()
            )
        }

        linearMinus.setOnClickListener {
            /* if (productDetailId == -1) {
                 Toast.makeText(this@ProductDetailsActivity,"Please select product detail",Toast.LENGTH_LONG).show()
                 return@setOnClickListener
             }
 */
            if (quantityCount > 1)
                quantityCount--

            edtCount.setText(quantityCount.toString())
            calculateAmount(
                edtPrice.text.toString(),
                edtDiscount.text.toString(),
                edtCount.text.toString()
            )
        }

        edtCount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (edtCount.text.toString() != "")
                    quantityCount = edtCount.text.toString().toInt()

                if (edtCount.text.toString() == "0") {
                    edtCount.setText("")
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        "Enter at least one quantity",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (edtCount.text.toString() != "" && edtCount.text.toString() != "0")
                    calculateAmount(
                        edtPrice.text.toString(),
                        edtDiscount.text.toString(),
                        edtCount.text.toString()
                    )
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        edtPrice.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (!disablePrice) {
                    if (edtPrice.text.toString() != "") {

                        /*   calculateAmount(
                               edtPrice.text.toString(),
                               edtDiscount.text.toString(),
                               edtCount.text.toString()
                           )*/
                    } else {
                        edtTax.setText("0")
                        edtAmount.setText("0")
                        edtTotalDiscount.setText("0")
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        edtDiscount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (!disableDiscount) {
                    if (edtDiscount.text.toString() != "") {
                        calculateAmount(
                            edtPrice.text.toString(),
                            edtDiscount.text.toString(),
                            edtCount.text.toString()
                        )
                    } else {
                        calculateAmount(edtPrice.text.toString(), "0", edtCount.text.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        cardAddToCart.setOnClickListener {

            var oldQuantity = -1
            var oldProductPos = -1
            /*  if (productDetailId == -1) {
                  Toast.makeText(this@ProductDetailsActivity,"Please select product detail",Toast.LENGTH_LONG).show()
              } else {*/

            if (edtCount.text.toString() == "") {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Please enter Quantity",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            var price = edtPrice.text.toString()
            var priceInt = 0
            if (price.contains(".")) {
                priceInt = price.split(".")[0].toInt()
            } else {
                priceInt = price.toInt()
            }

            var singleDiscount = 0.0
            if (edtDiscount.text.toString() != "" && edtDiscount.text.toString().toDouble()
                    .toInt() > 0
            ) {

                var count = edtCount.text.toString().toInt()
                singleDiscount = edtTotalDiscount.text.toString().toDouble() / count
            }

            var singlePriceWithDis = priceInt - singleDiscount

/*
            if (productDetail!!.details!![productDetailSelectedPos].min_price!!.toFloat() > singlePriceWithDis) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Discount is too high or price is low",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }
*/

            for ((index, value) in productArrAddToCart.withIndex()) {

                if (value!!.id == productId) {
                    oldQuantity = value.quantity
                    oldProductPos = index
                    break
                }
            }

            if (oldQuantity == -1) {

                productDetail.quantity= edtCount.text.toString().toInt()
                productDetail.amount = edtAmount.text.toString()
                productArrAddToCart.add(productDetail)
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Product Added into Cart",
                    Toast.LENGTH_LONG
                ).show()

                tvCount.text = productArrAddToCart.size.toString()

            } else {

                productArrAddToCart[oldProductPos]!!.quantity  = edtCount.text.toString().toInt()
                productArrAddToCart[oldProductPos]!!.amount  = edtAmount.text.toString()

                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Product Quantity updated",
                    Toast.LENGTH_LONG
                ).show()
            }
            //    }
        }

        badge_layout1.setOnClickListener {
            if (productArrAddToCart.size == 0)
                Toast.makeText(this, "Please add product", Toast.LENGTH_LONG).show()
            else {
//                val intent1 = Intent(this@ProductDetailsActivity, AddToCartActivity::class.java)
//                intent1.putExtra("checkin", intent.getStringExtra("checkin"))
//                startActivity(intent1)
            }
        }

        getProductDetails()
    }

    private fun calculateAmount(price: String, discount1: String, qty: String) {

        var discount = discount1
        if (isGST) {

            var discountInt = 0
            var priceInt = 0
            var discountAmount = 0.0

            if (discount == "")
                discount = "0"

            if (discount.contains(".")) {
                discountInt = discount.split(".")[0].toInt()
            } else {
                discountInt = discount.toInt()
            }

            if (price.contains(".")) {
                priceInt = price.split(".")[0].toInt()
            } else {
                priceInt = price.toInt()
            }

            if (discountInt > 0) {
                var discountAmountTemp = (priceInt * qty.toInt() * discountInt).toDouble()
                discountAmount = discountAmountTemp / 100
            } else {
                discountAmount = 0.0
            }

            var finalAmount = (priceInt * qty.toInt()) - discountAmount

            if (discountAmount < 0)
                discountAmount = -discountAmount
            edtTotalDiscount.setText(
                DecimalFormat("##.##").format(discountAmount.toFloat()).toString()
            )


          /*  var gstStr = productDetail!!.details!![productDetailSelectedPos].gst!!
            var gstInt = 0
            if (gstStr.contains(".")) {
                gstInt = gstStr.split(".")[0].toInt()
            } else {
                gstInt = gstStr.toInt()
            }

            var tax = finalAmount * gstInt
            var taxFinal = tax / 100*/

         //   edtTax.setText(DecimalFormat("##.##").format(taxFinal.toFloat()).toString())
            edtAmount.setText(
                DecimalFormat("##.##").format( finalAmount).toString()
            )


        } else {

            edtTax.setText("0")

            var discountInt = 0
            var priceInt = 0
            var discountAmount = 0.0

            if (discount.contains(".")) {
                discountInt = discount.split(".")[0].toInt()
            } else {
                discountInt = discount.toInt()
            }

            if (price.contains(".")) {
                priceInt = price.split(".")[0].toInt()
            } else {
                priceInt = price.toInt()
            }
            if (discountInt > 0) {
                var discountAmountTemp = (priceInt * qty.toInt() * discountInt).toDouble()
                discountAmount = discountAmountTemp / 100
            } else {
                discountAmount = 0.0
            }

            var finalAmount = (priceInt * qty.toInt()) - discountAmount

            if (discountAmount < 0)
                discountAmount = -discountAmount
            edtTotalDiscount.setText(
                DecimalFormat("##.##").format(discountAmount.toFloat()).toString()
            )
            edtAmount.setText(DecimalFormat("##.##").format(finalAmount.toFloat()).toString())

        }
    }

    private fun getProductDetails() {

        if (!Utilities.isOnline(this)) {
            return
        }

        var dialog = DialogClass.progressDialog(this)
        println("ABHI=="+intent.getIntExtra("id", 0).toString())

        val queryParams = HashMap<String, String>()
        queryParams["product_id"] = intent.getIntExtra("id", 0).toString()
        ApiClient.getProductDetails(
            StaticSharedpreference.getInfo(
                Constant.ACCESS_TOKEN,
                this@ProductDetailsActivity
            ).toString(), queryParams, object : APIResultLitener<JsonObject> {
                override fun onAPIResult(
                    response: Response<JsonObject>?,
                    errorMessage: String?
                ) {
                    dialog.dismiss()
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {

                            val gson = Gson()
                            val listType = object :
                                TypeToken<ProductNewModel>() {}.type

                            productDetail = gson.fromJson<ProductNewModel>(
                                response.body()!!.get("data").asJsonObject,
                                listType
                            )

                            
                            Glide.with(this@ProductDetailsActivity)
                                .load(ApiClient.BASE_IMAGE_URL+productDetail.productImage)
                                .into(imgProduct)

                            productId = productDetail.id!!
                            tvDisplay.text = productDetail.productName
                            tvBrand.text = productDetail.brandName
                            tvDescription.text = productDetail.description
                            tvGG.text = productDetail.product_no
                            tvOEPart.text = productDetail.part_no
                            tvSpecification.text = productDetail.specification
                            edtPrice.setText(productDetail.mrp)
                            tvCategory.setText(productDetail.categoryName)
                            tvSubCat.setText(productDetail.subcategoryName)
                            tvUnit.text = "Unit : " + productDetail.unitCode

                            println("ABHIIprrooo=="+productId+"<<"+productDetail.productName+"<<"+productDetail.brandName+"<<"+productDetail.description+"<<"+productDetail.product_no+"<<"+
                                    productDetail.part_no+"<<"+productDetail.specification+"<<"+productDetail.price+"<<"+productDetail.categoryName+"<<"+productDetail.subcategoryName+"<<"+
                                    productDetail.unitCode+"<<"+productDetail.product_ebd_amount)

                            calculateAmount(edtPrice.text.toString(), "0", edtCount.text.toString())
                            
                        }
                    } else {
                        Toast.makeText(
                            this@ProductDetailsActivity,
                            resources.getString(R.string.poor_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()

        if (ProductActivity.isBack) {
            ProductActivity.isBack = false
            finish()
        }

        tvCount.text = productArrAddToCart!!.size.toString()
    }

}