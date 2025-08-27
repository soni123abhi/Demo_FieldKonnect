package com.exp.clonefieldkonnect.connection

import FillterModel
import android.content.Context
import com.exp.clonefieldkonnect.model.AttendanceDetailModel
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.BeatScheduleModel
import com.exp.clonefieldkonnect.model.CityModel
import com.exp.clonefieldkonnect.model.ClusterOrderListModel
import com.exp.clonefieldkonnect.model.CustomerParentModel
import com.exp.clonefieldkonnect.model.DealerApprovalListModel
import com.exp.clonefieldkonnect.model.DealerMonthlySalesReport
import com.exp.clonefieldkonnect.model.DealerSalesReportModel
import com.exp.clonefieldkonnect.model.DealergrowthModel
import com.exp.clonefieldkonnect.model.DistrictModel
import com.exp.clonefieldkonnect.model.DistriutorModel
import com.exp.clonefieldkonnect.model.DraftReportModel
import com.exp.clonefieldkonnect.model.EnquiryModel
import com.exp.clonefieldkonnect.model.ExpenseApprovalModel
import com.exp.clonefieldkonnect.model.ExpenseApprovalSubmitModel
import com.exp.clonefieldkonnect.model.ExpenseTypeModel
import com.exp.clonefieldkonnect.model.GetDiscountLimitModel
import com.exp.clonefieldkonnect.model.InsertOrderRequestModel
import com.exp.clonefieldkonnect.model.LeadContactModel
import com.exp.clonefieldkonnect.model.LeadDetailModel
import com.exp.clonefieldkonnect.model.LeadModel
import com.exp.clonefieldkonnect.model.LeadStatusSourceModel
import com.exp.clonefieldkonnect.model.LeadTaskModel
import com.exp.clonefieldkonnect.model.LeaveBalanceModel
import com.exp.clonefieldkonnect.model.LocationRequestModel
import com.exp.clonefieldkonnect.model.MSPFilterDataModel
import com.exp.clonefieldkonnect.model.MspActivityTypeModel
import com.exp.clonefieldkonnect.model.MspTabledataModel
import com.exp.clonefieldkonnect.model.NewDealerViewDetailMOdel
import com.exp.clonefieldkonnect.model.NotificationModel
import com.exp.clonefieldkonnect.model.OportunityDetailModel
import com.exp.clonefieldkonnect.model.OrderDetailsModel
import com.exp.clonefieldkonnect.model.OrderListModel
import com.exp.clonefieldkonnect.model.OrderPdfModel
import com.exp.clonefieldkonnect.model.OrderUpdateModel
import com.exp.clonefieldkonnect.model.OutstandingModel
import com.exp.clonefieldkonnect.model.ParticallyorderDetailsRequestModel
import com.exp.clonefieldkonnect.model.PinCodeModel
import com.exp.clonefieldkonnect.model.PointCollectionRequest
import com.exp.clonefieldkonnect.model.PrimaryFilterListModel
import com.exp.clonefieldkonnect.model.PrimarySchemeModel
import com.exp.clonefieldkonnect.model.PrimarySchemeTableModel
import com.exp.clonefieldkonnect.model.ProductDetailModel
import com.exp.clonefieldkonnect.model.ReportcountModel
import com.exp.clonefieldkonnect.model.SalesDetailModel
import com.exp.clonefieldkonnect.model.SalesModel
import com.exp.clonefieldkonnect.model.SalessListModel
import com.exp.clonefieldkonnect.model.SarthiPointsModel
import com.exp.clonefieldkonnect.model.SpecialDiscountModel
import com.exp.clonefieldkonnect.model.StateModel
import com.exp.clonefieldkonnect.model.TaskDropdownModel
import com.exp.clonefieldkonnect.model.TaskManagemnetModel
import com.exp.clonefieldkonnect.model.UnpaidInvoiceModel
import com.exp.clonefieldkonnect.model.UserActiveModel
import com.exp.clonefieldkonnect.model.UserActivityListModel
import com.exp.clonefieldkonnect.model.UserAttendanceListModel
import com.exp.clonefieldkonnect.model.UserDataModel
import com.exp.clonefieldkonnect.model.UserExpenseDetailModel
import com.exp.clonefieldkonnect.model.UserExpenseListModel
import com.exp.clonefieldkonnect.model.UserTourListModel
import com.exp.clonefieldkonnect.model.VersionModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        /*val BASE_API_URL = "http://abridgemspl.com/abridge/api/"
        val BASE_API_URL = "https://abridgemspl.com/gripit/api/"
        val BASE_API_URL = "http://gajragears.greymetre.io/api/"*/

        /*val BASE_API_URL = "http://gajragears.greymetre.io/api/"
        val BASE_IMAGE_URL = "http://gajragears.greymetre.io/public/uploads/"*/

        //old URL
        /*val BASE_API_URL = "http://15.207.254.162/api/"
        val BASE_IMAGE_URL = "https://gajragears.s3.ap-south-1.amazonaws.com/"*/

        //new URL
        // new URL local
//        val BASE_API_URL = "http://192.168.0.250/bediya-/api/"

        // new URL live
//        val BASE_API_URL = "https://expertfromindia.in/bediya/api/"
//        val BASE_IMAGE_URL = "https://expertfromindia.in/bediya/public/uploads/"
        //val BASE_IMAGE_URL = "https://expertfromindia.in/bediya/public/uploads/customers/customer231027082836.jpg"

        // Current new URL live
        val BASE_API_URL = "https://demo.fieldkonnect.io/api/"
//        val BASE_API_URL = "https://silver.fieldkonnect.io/api/"
        val BASE_IMAGE_URL = "https://silver.fieldkonnect.io/public/uploads/"

        private val httpClient = OkHttpClient.Builder()
        private val logging = HttpLoggingInterceptor()
        fun configClient1() {
            httpClient.connectTimeout(30, TimeUnit.MINUTES)
            httpClient.readTimeout(30, TimeUnit.MINUTES)
        }

        fun addLoggingIfNeeded() {
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.interceptors().add(logging)
        }

        var retrofit: Retrofit? = null
        fun getBaseApiServiceInstance1(): BaseApiService {

            if (retrofit == null) {
                addLoggingIfNeeded()
                configClient1()
                val gson = GsonBuilder().setLenient().create()
                retrofit = Retrofit.Builder().baseUrl(BASE_API_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient.build()).build()
            }
            return retrofit!!.create(BaseApiService::class.java)
        }

        fun login(queryParams: Map<String, String>, resultLitener: APIResultLitener<JsonObject>, mContext: Context) {
            val apiResponseCall = getBaseApiServiceInstance1().login(queryParams)
            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun updateCustomerLocation(token: String, queryParams: Map<String, String>, resultLitener: APIResultLitener<JsonObject>) {
            val apiResponseCall = getBaseApiServiceInstance1().updateCustomerLocation(token, queryParams)
            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun leadToCustomer(token: String, queryParams: Map<String, String>, resultLitener: APIResultLitener<JsonObject>) {
            val apiResponseCall = getBaseApiServiceInstance1().leadToCustomer(token, queryParams)
            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun createNewTask(token: String, queryParams: Map<String, String>, resultLitener: APIResultLitener<JsonObject>) {
            val apiResponseCall = getBaseApiServiceInstance1().createNewTask(token, queryParams)
            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun requestReport(token: String, queryParams: Map<String, String>, resultLitener: APIResultLitener<JsonObject>) {
            val apiResponseCall = getBaseApiServiceInstance1().requestReport(token, queryParams)
            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitCheckin(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitCheckin(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitCheckout(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitCheckout(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {
                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        /* fun storeCustomer(
             token: String,
             queryParams: StoreCustomerRequestModel,
             resultLitener: APIResultLitener<JsonObject>
         ) {
             val apiResponseCall = getBaseApiServiceInstance1().storeCustomer(token, queryParams)

             apiResponseCall.enqueue(object : Callback<JsonObject> {
                 override fun onResponse(
                     call: Call<JsonObject>,
                     response: retrofit2.Response<JsonObject>
                 ) {

                     resultLitener.onAPIResult(response, null)
                 }

                 override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                     resultLitener.onAPIResult(null, t.message)
                 }
             })
         }*/
        fun updateLiveLocation(
            token: String,
            queryParams: LocationRequestModel,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().updateLiveLocation(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun pointsCollection(
            token: String,
            queryParams: PointCollectionRequest,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().pointsCollection(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun updateCustomerProfile(
            token: String, name: RequestBody?, fullName: RequestBody?, mobile: RequestBody?, alternate_no: RequestBody?, parent_id: RequestBody?, email: RequestBody?,
            address1: RequestBody?, lat: RequestBody?, longi: RequestBody?, beatId: RequestBody?, gstin_no: RequestBody?, panNo: RequestBody?,
            adharNo: RequestBody?, otherNo: RequestBody?, zipcode: RequestBody?, landmark: RequestBody?, customertype: RequestBody?,
            statusType: RequestBody?, grade: RequestBody?, customerId: RequestBody?, addressId: RequestBody?, pincode: RequestBody?,
            city: RequestBody?, district: RequestBody?, state: RequestBody?, country: RequestBody?, survey: RequestBody?, dealing: RequestBody?,
            gstImg: MultipartBody.Part, panImg: MultipartBody.Part, adharImg: MultipartBody.Part, otherImg: MultipartBody.Part, cardImg: MultipartBody.Part,
            image: MultipartBody.Part,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().updateCustomerProfile(
                token, name, fullName, mobile, alternate_no, parent_id, email, address1, lat, longi, beatId, gstin_no, panNo, adharNo, otherNo, zipcode,
                landmark, customertype, statusType, grade, customerId, customertype, addressId, pincode, city, district, state, country, survey, dealing,
                gstImg, panImg, adharImg, otherImg, cardImg, image)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getTaskInfo(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getTaskInfo(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {
                    resultLitener.onAPIResult(response, null)
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun taskMarkComplite(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().taskMarkComplite(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun emailExists(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().emailExists(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun mobileNumberExists(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().mobileNumberExists(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun gstNumberExists(token: String, queryParams: Map<String, String>, resultLitener: APIResultLitener<JsonObject>) {
            val apiResponseCall =
                getBaseApiServiceInstance1().gstNumberExists(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getCheckin(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getCheckin(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getPunchin(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getPunchin(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getUserSataus(
            token: String,
            resultLitener: APIResultLitener<UserActiveModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getUserSataus(token)

            apiResponseCall.enqueue(object : Callback<UserActiveModel> {
                override fun onResponse(
                    call: Call<UserActiveModel>, response: retrofit2.Response<UserActiveModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UserActiveModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun dashboard(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().dashboard(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun logout(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().logout(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getBeatList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getBeatList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getBeatDropdownList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().getBeatDropdownList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getcustomertype(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getcustomertype(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getCategoryData(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getCategoryData(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getBeatCustomers(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getBeatCustomers(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun userPunchin(
            token: String, punchin_longitude: RequestBody?, punchin_latitude: RequestBody?, punchin_address: RequestBody?, summary: RequestBody?, tourid: RequestBody?,
            beatArr: RequestBody?, cityArr: RequestBody?, type: RequestBody?, resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().userPunchin(
                token, punchin_longitude, punchin_latitude, punchin_address, summary, tourid, beatArr, cityArr, type,)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun storeCustomer(
            token: String,
            name: RequestBody?,
            fullName: RequestBody?,
            mobile: RequestBody?,
            alternatemobile: RequestBody?,
            parent_id: RequestBody?,
            email: RequestBody?,
            address1: RequestBody?,
            lat: RequestBody?,
            longi: RequestBody?,
            beatId: RequestBody?,
            gstin_no: RequestBody?,
            panNo: RequestBody?,
            adharNo: RequestBody?,
            otherNo: RequestBody?,
            zipcode: RequestBody?,
            landmark: RequestBody?,
            customertype: RequestBody?,
            statusType: RequestBody?,
            grade: RequestBody?,
            survey: RequestBody?,
            dealing: RequestBody?,
            gstImg: MultipartBody.Part,
            panImg: MultipartBody.Part,
            adharImg: MultipartBody.Part,
            otherImg: MultipartBody.Part,
            cardImg: MultipartBody.Part,
            image: MultipartBody.Part,
            passbook: MultipartBody.Part,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().storeCustomer(
                token, name, fullName, mobile, alternatemobile, parent_id, email, address1, lat, longi, beatId, gstin_no, panNo, adharNo, otherNo,
                zipcode, landmark, customertype, statusType, grade, survey, dealing, gstImg, panImg, adharImg, otherImg, cardImg, image, passbook)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun userPunchout(
            token: String,
            punchin_id: RequestBody?,
            punchout_latitude: RequestBody?,
            punchout_longitude: RequestBody?,
            punchout_address: RequestBody?,
            summary: RequestBody?,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().userPunchout(
                token, punchin_id, punchout_longitude, punchout_latitude, punchout_address, summary
            )

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getProductList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getProductList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getProductDetails(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getProductDetails(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getPaymentInfo(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<ProductDetailModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getPaymentInfo(token, queryParams)

            apiResponseCall.enqueue(object : Callback<ProductDetailModel> {
                override fun onResponse(
                    call: Call<ProductDetailModel>, response: retrofit2.Response<ProductDetailModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<ProductDetailModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getUpcomingTasks(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getUpcomingTasks(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getCollectedPoints(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().getCollectedPoints(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun upcommingTourProgramme(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().upcommingTourProgramme(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun beatschedule(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<BeatScheduleModel>
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().getbeatschedule(token, queryParams)

            apiResponseCall.enqueue(object : Callback<BeatScheduleModel> {
                override fun onResponse(
                    call: Call<BeatScheduleModel>, response: retrofit2.Response<BeatScheduleModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<BeatScheduleModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getReportType(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getReportType(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getUsertourlist(
            token: String,
            queryParams: Map<String, String>,
            searchbranch: ArrayList<String>,
            resultLitener: APIResultLitener<UserTourListModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getusertourlist(token, queryParams,searchbranch)

            apiResponseCall.enqueue(object : Callback<UserTourListModel> {
                override fun onResponse(
                    call: Call<UserTourListModel>, response: retrofit2.Response<UserTourListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UserTourListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getexpenselist(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<UserExpenseListModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getexpenselist(token, queryParams)

            apiResponseCall.enqueue(object : Callback<UserExpenseListModel> {
                override fun onResponse(
                    call: Call<UserExpenseListModel>, response: retrofit2.Response<UserExpenseListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UserExpenseListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getreportcount(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<ReportcountModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getreportcount(token, queryParams)

            apiResponseCall.enqueue(object : Callback<ReportcountModel> {
                override fun onResponse(
                    call: Call<ReportcountModel>, response: retrofit2.Response<ReportcountModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<ReportcountModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getlogout(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().calllogout(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getexpensetypelist(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<ExpenseTypeModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getexpensetypelist(token, queryParams)

            apiResponseCall.enqueue(object : Callback<ExpenseTypeModel> {
                override fun onResponse(
                    call: Call<ExpenseTypeModel>, response: retrofit2.Response<ExpenseTypeModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<ExpenseTypeModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun getUserActivity(
            token: String,
            queryParams: Map<String, String>,
            searchbranch: ArrayList<String>,
            resultLitener: APIResultLitener<UserActivityListModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getuseractivitylist(token, queryParams,searchbranch)

            apiResponseCall.enqueue(object : Callback<UserActivityListModel> {
                override fun onResponse(
                    call: Call<UserActivityListModel>, response: retrofit2.Response<UserActivityListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UserActivityListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getlead(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<LeadModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getlead(token, queryParams)

            apiResponseCall.enqueue(object : Callback<LeadModel> {
                override fun onResponse(
                    call: Call<LeadModel>, response: retrofit2.Response<LeadModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<LeadModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getleadtask(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<LeadTaskModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getleadtaskk(token, queryParams)

            apiResponseCall.enqueue(object : Callback<LeadTaskModel> {
                override fun onResponse(
                    call: Call<LeadTaskModel>, response: retrofit2.Response<LeadTaskModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<LeadTaskModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun gettaskmanagemnet(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<TaskManagemnetModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().gettaskmanagementt(token, queryParams)

            apiResponseCall.enqueue(object : Callback<TaskManagemnetModel> {
                override fun onResponse(
                    call: Call<TaskManagemnetModel>, response: retrofit2.Response<TaskManagemnetModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<TaskManagemnetModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getnotification(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<NotificationModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getnotificationn(token, queryParams)

            apiResponseCall.enqueue(object : Callback<NotificationModel> {
                override fun onResponse(
                    call: Call<NotificationModel>, response: retrofit2.Response<NotificationModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<NotificationModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getopportunity(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<OportunityDetailModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getopportunity(token, queryParams)

            apiResponseCall.enqueue(object : Callback<OportunityDetailModel> {
                override fun onResponse(
                    call: Call<OportunityDetailModel>, response: retrofit2.Response<OportunityDetailModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<OportunityDetailModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getleaddetail(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<LeadDetailModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getleaddetailsss(token, queryParams)

            apiResponseCall.enqueue(object : Callback<LeadDetailModel> {
                override fun onResponse(
                    call: Call<LeadDetailModel>, response: retrofit2.Response<LeadDetailModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<LeadDetailModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getleadtaskdropdown(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<TaskDropdownModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getleadtaskdropdownn(token, queryParams)

            apiResponseCall.enqueue(object : Callback<TaskDropdownModel> {
                override fun onResponse(
                    call: Call<TaskDropdownModel>, response: retrofit2.Response<TaskDropdownModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<TaskDropdownModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getleadcontact(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<LeadContactModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getleadcontacttt(token, queryParams)

            apiResponseCall.enqueue(object : Callback<LeadContactModel> {
                override fun onResponse(
                    call: Call<LeadContactModel>, response: retrofit2.Response<LeadContactModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<LeadContactModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getleadstatussource(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<LeadStatusSourceModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getleadstatussource(token, queryParams)

            apiResponseCall.enqueue(object : Callback<LeadStatusSourceModel> {
                override fun onResponse(
                    call: Call<LeadStatusSourceModel>, response: retrofit2.Response<LeadStatusSourceModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<LeadStatusSourceModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getdealersaleseport(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<DealerSalesReportModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getdealersales(token, queryParams)

            apiResponseCall.enqueue(object : Callback<DealerSalesReportModel> {
                override fun onResponse(
                    call: Call<DealerSalesReportModel>, response: retrofit2.Response<DealerSalesReportModel>
                ) {
                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DealerSalesReportModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getdealergrowthreport(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<DealergrowthModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getdealergrowth(token, queryParams)

            apiResponseCall.enqueue(object : Callback<DealergrowthModel> {
                override fun onResponse(
                    call: Call<DealergrowthModel>, response: retrofit2.Response<DealergrowthModel>
                ) {
                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DealergrowthModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getprimaryfilterdata(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<PrimaryFilterListModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getprimaryfilterlist(token, queryParams)

            apiResponseCall.enqueue(object : Callback<PrimaryFilterListModel> {
                override fun onResponse(
                    call: Call<PrimaryFilterListModel>, response: retrofit2.Response<PrimaryFilterListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<PrimaryFilterListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getmspfilterdata(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<MSPFilterDataModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getmspfilterlist(token, queryParams)

            apiResponseCall.enqueue(object : Callback<MSPFilterDataModel> {
                override fun onResponse(
                    call: Call<MSPFilterDataModel>, response: retrofit2.Response<MSPFilterDataModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<MSPFilterDataModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getmsptabledata(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<MspTabledataModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getmsptablelist(token, queryParams)

            apiResponseCall.enqueue(object : Callback<MspTabledataModel> {
                override fun onResponse(
                    call: Call<MspTabledataModel>, response: retrofit2.Response<MspTabledataModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<MspTabledataModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getprimaryschemedata(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<PrimarySchemeModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getprimaryschemelist(token, queryParams)

            apiResponseCall.enqueue(object : Callback<PrimarySchemeModel> {
                override fun onResponse(
                    call: Call<PrimarySchemeModel>, response: retrofit2.Response<PrimarySchemeModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<PrimarySchemeModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getprimaryschemeshowdata(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<PrimarySchemeTableModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getprimaryschemeshowdata(token, queryParams)

            apiResponseCall.enqueue(object : Callback<PrimarySchemeTableModel> {
                override fun onResponse(
                    call: Call<PrimarySchemeTableModel>, response: retrofit2.Response<PrimarySchemeTableModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<PrimarySchemeTableModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun getmonthlysaleseport(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<DealerMonthlySalesReport>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getmonthlysales(token, queryParams)

            apiResponseCall.enqueue(object : Callback<DealerMonthlySalesReport> {
                override fun onResponse(
                    call: Call<DealerMonthlySalesReport>, response: retrofit2.Response<DealerMonthlySalesReport>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DealerMonthlySalesReport>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getUserData(
            token: String,
            queryParams: Map<String, String>,
            selectedmonth: ArrayList<String>,
            resultLitener: APIResultLitener<UserDataModel>
        ){
            val apiResponseCall = getBaseApiServiceInstance1().getUserData(token, queryParams,selectedmonth)

            apiResponseCall.enqueue(object : Callback<UserDataModel> {
                override fun onResponse(
                    call: Call<UserDataModel>, response: retrofit2.Response<UserDataModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UserDataModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getexpenseaprovelist(
            token: String,
            queryParams: Map<String, String>,
            searchbranch: ArrayList<String>,
            resultLitener: APIResultLitener<ExpenseApprovalModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getexpenseaprovelist(token, queryParams,searchbranch)

            apiResponseCall.enqueue(object : Callback<ExpenseApprovalModel> {
                override fun onResponse(
                    call: Call<ExpenseApprovalModel>, response: retrofit2.Response<ExpenseApprovalModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<ExpenseApprovalModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getdealerapprovelist(
            token: String,
            queryParams: Map<String, String>,
            searchbranch: ArrayList<String>,
            resultLitener: APIResultLitener<DealerApprovalListModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getdealerapprovallist(token, queryParams,searchbranch)

            apiResponseCall.enqueue(object : Callback<DealerApprovalListModel> {
                override fun onResponse(
                    call: Call<DealerApprovalListModel>, response: retrofit2.Response<DealerApprovalListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DealerApprovalListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getdealerviewdetail(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<NewDealerViewDetailMOdel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getdealerviewdetail(token, queryParams)

            apiResponseCall.enqueue(object : Callback<NewDealerViewDetailMOdel> {
                override fun onResponse(
                    call: Call<NewDealerViewDetailMOdel>, response: retrofit2.Response<NewDealerViewDetailMOdel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<NewDealerViewDetailMOdel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun submitdraftreport(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitdraftreport(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getdraftreport(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<DraftReportModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getdraftreport(token, queryParams)

            apiResponseCall.enqueue(object : Callback<DraftReportModel> {
                override fun onResponse(
                    call: Call<DraftReportModel>, response: retrofit2.Response<DraftReportModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DraftReportModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun approverdealearappointment(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().approverdealearappointment(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun saveappointmentremark(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().saveappointmentremark(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }



        fun getUserAttendance(
            token: String,
            queryParams: Map<String, String>,
            searchbranch: ArrayList<String>,
            resultLitener: APIResultLitener<UserAttendanceListModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getuserattendancelist(token, queryParams,searchbranch)

            apiResponseCall.enqueue(object : Callback<UserAttendanceListModel> {
                override fun onResponse(
                    call: Call<UserAttendanceListModel>, response: retrofit2.Response<UserAttendanceListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UserAttendanceListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getsarthipoint(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<SarthiPointsModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getsarthipoint(token, queryParams)

            apiResponseCall.enqueue(object : Callback<SarthiPointsModel> {
                override fun onResponse(
                    call: Call<SarthiPointsModel>, response: retrofit2.Response<SarthiPointsModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<SarthiPointsModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getuserattendancedetail(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceDetailModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getuserattendancedetail(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceDetailModel> {
                override fun onResponse(
                    call: Call<AttendanceDetailModel>, response: retrofit2.Response<AttendanceDetailModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceDetailModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun userattendancesubmit(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().userattendancesubmit(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun callcancelorder(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().cancelorder(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun fullydispatchorder(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().fullydispatchorder(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun expenseapprovalsubmit(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<ExpenseApprovalSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().expenseapprovalsubmit(token, queryParams)

            apiResponseCall.enqueue(object : Callback<ExpenseApprovalSubmitModel> {
                override fun onResponse(
                    call: Call<ExpenseApprovalSubmitModel>, response: retrofit2.Response<ExpenseApprovalSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<ExpenseApprovalSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun expenserejectionsubmit(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<ExpenseApprovalSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().expenserejectionsubmit(token, queryParams)

            apiResponseCall.enqueue(object : Callback<ExpenseApprovalSubmitModel> {
                override fun onResponse(
                    call: Call<ExpenseApprovalSubmitModel>, response: retrofit2.Response<ExpenseApprovalSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<ExpenseApprovalSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun userleavesubmit(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().userleavesubmit(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun userleavebal(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<LeaveBalanceModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().userleavebal(token, queryParams)

            apiResponseCall.enqueue(object : Callback<LeaveBalanceModel> {
                override fun onResponse(
                    call: Call<LeaveBalanceModel>, response: retrofit2.Response<LeaveBalanceModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<LeaveBalanceModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun createtoursubmit(
            token: String,
            queryParams: Map<String, String>,
            tourCreateDate: ArrayList<String>,
            tourCreateCity: ArrayList<String>,
            tourCreateObjective: ArrayList<String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().createtoursubmit(token, queryParams,tourCreateDate,tourCreateCity,
                tourCreateObjective)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun expensesubmit(

            token: String,
            queryParams: Map<String, String>,
            multipartprofile: List<MultipartBody.Part>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().Expensesubmit(token, queryParams,multipartprofile)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun expenseupdate(

            token: String,
            queryParams: Map<String, String>,
            multipartprofile: List<MultipartBody.Part>,
            edit_select_imagepath_remove: ArrayList<String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().Expenseupdate(token, queryParams,multipartprofile,edit_select_imagepath_remove)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        } 
        fun marketingintelligence(

            token: String,
            queryParams: Map<String, String>,
            jsonFormData: RequestBody,
            fileToUploadOther: MultipartBody.Part,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().marketinteeee(token, queryParams,jsonFormData,fileToUploadOther)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun expensedetail(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<UserExpenseDetailModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getexpensedetail(token, queryParams)

            apiResponseCall.enqueue(object : Callback<UserExpenseDetailModel> {
                override fun onResponse(
                    call: Call<UserExpenseDetailModel>, response: retrofit2.Response<UserExpenseDetailModel>) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UserExpenseDetailModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun submittourapproval(
            token: String,
            queryParams: Map<String, String>,
            touridarray: ArrayList<String>,
            datearray: ArrayList<String>,
            townarray: ArrayList<String>,
            objectivearray: ArrayList<String>,
            statusArray: ArrayList<Int>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submittourapproval(token, queryParams,touridarray,datearray,
                townarray,objectivearray,statusArray)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun submitcreatelead(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitcreatelead(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submittaskstatus(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submittaskstatus(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {
                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submittaskmanagementstatus(
            token: String,
            queryParams: Map<String, String>,
            multipartParts: List<MultipartBody.Part>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submittaskmanagementstatus(token, queryParams,multipartParts)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {
                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun callreadnotification(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().callreadnotificationn(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitleadnote(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitleadnote(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun updateleadtype(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().updateleadtypeee(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitleadopportunity(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitleadopportunityyy(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitleadcheckin(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitleadcheckin(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitleadcheckout(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitleadcheckout(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun deleteleadopportunity(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().deleteleadopportunityyy(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitleadtask(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitleadtask(token, queryParams)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getuseractivitydetail(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getuseractivitydetail(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {
                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getNotification(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getNotification(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun userCityList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().userCityList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun usermsptypeList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<MspActivityTypeModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().usermsptypeList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<MspActivityTypeModel> {
                override fun onResponse(
                    call: Call<MspActivityTypeModel>, response: retrofit2.Response<MspActivityTypeModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<MspActivityTypeModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun Tourviewdetails(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().tourviewdetails(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getdivisionlist(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().customerdivisionList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getParentlist(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<CustomerParentModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().customerparentList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<CustomerParentModel> {
                override fun onResponse(
                    call: Call<CustomerParentModel>, response: retrofit2.Response<CustomerParentModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<CustomerParentModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getWorkType(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getWorkType(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getDiscountLimit(
            token: String,
            resultLitener: APIResultLitener<GetDiscountLimitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getDiscountLimit(token)

            apiResponseCall.enqueue(object : Callback<GetDiscountLimitModel> {
                override fun onResponse(
                    call: Call<GetDiscountLimitModel>, response: retrofit2.Response<GetDiscountLimitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<GetDiscountLimitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun getSubCategoryData(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>,
            mContext: Context
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().getSubCategoryData(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getSales(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<SalesModel>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getSales(token, queryParams)

            apiResponseCall.enqueue(object : Callback<SalesModel> {
                override fun onResponse(
                    call: Call<SalesModel>, response: retrofit2.Response<SalesModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<SalesModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getOrderList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<OrderListModel>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getOrderList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<OrderListModel> {
                override fun onResponse(
                    call: Call<OrderListModel>, response: retrofit2.Response<OrderListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<OrderListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getsalessList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<SalessListModel>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getsalesssList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<SalessListModel> {
                override fun onResponse(
                    call: Call<SalessListModel>, response: retrofit2.Response<SalessListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<SalessListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getclusterOrderList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<ClusterOrderListModel>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getclusterOrderList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<ClusterOrderListModel> {
                override fun onResponse(
                    call: Call<ClusterOrderListModel>, response: retrofit2.Response<ClusterOrderListModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<ClusterOrderListModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getspecialOrderList(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<SpecialDiscountModel>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getspecialOrderList(token, queryParams)

            apiResponseCall.enqueue(object : Callback<SpecialDiscountModel> {
                override fun onResponse(
                    call: Call<SpecialDiscountModel>, response: retrofit2.Response<SpecialDiscountModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<SpecialDiscountModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getOrderDetails(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<OrderDetailsModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getOrderDetails(header, queryParams)

            apiResponseCall.enqueue(object : Callback<OrderDetailsModel> {
                override fun onResponse(
                    call: Call<OrderDetailsModel>, response: retrofit2.Response<OrderDetailsModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<OrderDetailsModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getsalesDetails(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<SalesDetailModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getsalesDetails(header, queryParams)

            apiResponseCall.enqueue(object : Callback<SalesDetailModel> {
                override fun onResponse(
                    call: Call<SalesDetailModel>, response: retrofit2.Response<SalesDetailModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<SalesDetailModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun updateorder(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<OrderUpdateModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().updateorder(header, queryParams)

            apiResponseCall.enqueue(object : Callback<OrderUpdateModel> {
                override fun onResponse(
                    call: Call<OrderUpdateModel>, response: retrofit2.Response<OrderUpdateModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<OrderUpdateModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getorderpdf(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<OrderPdfModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getorderpdf(header, queryParams)

            apiResponseCall.enqueue(object : Callback<OrderPdfModel> {
                override fun onResponse(
                    call: Call<OrderPdfModel>, response: retrofit2.Response<OrderPdfModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<OrderPdfModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }
        fun getversion(
            resultLitener: APIResultLitener<VersionModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getversion()

            apiResponseCall.enqueue(object : Callback<VersionModel> {
                override fun onResponse(
                    call: Call<VersionModel>, response: retrofit2.Response<VersionModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<VersionModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getSalesDetails(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getSalesDetails(header, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getSurveyQuestions(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<EnquiryModel>
        ) {
            val apiResponseCall =
                getBaseApiServiceInstance1().getSurveyQuestions(header, queryParams)

            apiResponseCall.enqueue(object : Callback<EnquiryModel> {
                override fun onResponse(
                    call: Call<EnquiryModel>, response: retrofit2.Response<EnquiryModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<EnquiryModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getCustomerInfo(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getCustomerInfo(header, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getUnpaidInvoice(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<UnpaidInvoiceModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getUnpaidInvoice(header, queryParams)

            apiResponseCall.enqueue(object : Callback<UnpaidInvoiceModel> {
                override fun onResponse(
                    call: Call<UnpaidInvoiceModel>, response: retrofit2.Response<UnpaidInvoiceModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<UnpaidInvoiceModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getPaymentList(
            header: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<OutstandingModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getPaymentList(header, queryParams)

            apiResponseCall.enqueue(object : Callback<OutstandingModel> {
                override fun onResponse(
                    call: Call<OutstandingModel>, response: retrofit2.Response<OutstandingModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<OutstandingModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun getDistributors(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<DistriutorModel>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getDistributors(token, queryParams)

            apiResponseCall.enqueue(object : Callback<DistriutorModel> {
                override fun onResponse(
                    call: Call<DistriutorModel>, response: retrofit2.Response<DistriutorModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DistriutorModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getVisitTypes(
            token: String,
            queryParams: Map<String, String>,
            resultLitener: APIResultLitener<JsonObject>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getVisitTypes(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun insertOrder(
            token: String,
            queryParams: InsertOrderRequestModel,
            resultLitener: APIResultLitener<JsonObject>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().insertOrder(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun particallydispatchorder(
            token: String,
            queryParams: ParticallyorderDetailsRequestModel,
            resultLitener: APIResultLitener<JsonObject>,
            mContext: Context
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().particallydispatchOrder(token, queryParams)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun paymentReceived(
            token: String,
            jsonArray: RequestBody?,
            paymentDate: RequestBody?,
            paymentTypeRB: RequestBody?,
            customerId: RequestBody?,
            paymentMode: RequestBody?,
            amount: RequestBody?,
            reference: RequestBody?,
            bankName: RequestBody?,
            description: RequestBody?,
            files: MultipartBody.Part,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().paymentReceived(
                token,
                jsonArray,
                paymentDate,
                paymentTypeRB,
                customerId,
                paymentMode,
                amount,
                reference,
                bankName,
                description,
                files
            )

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitVisitReports(
            token: String,
            customerId: RequestBody,
            checkin_id: RequestBody,
            visit_type_id: RequestBody,
            report_title: RequestBody,
            description: RequestBody,
            lead_id: RequestBody,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitVisitReports(
                token, customerId, checkin_id, visit_type_id, report_title, description, lead_id
            )

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun insertSales(
            token: String,
            buyer_id: RequestBody?,
            seller_id: RequestBody?,
            invoice_no: RequestBody?,
            invoice_date: RequestBody?,
            grand_total: RequestBody?,
            files: List<MultipartBody.Part?>?,

            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().insertSales(
                token, buyer_id, seller_id, invoice_no, invoice_date, grand_total, files
            )

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun updateProfile(
            token: String,

            files: MultipartBody.Part,

            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().updateProfile(
                token, files
            )

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getRetailers(
            token: String,

            queryParams: Map<String, String>,

            resultLitener: APIResultLitener<DistriutorModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getRetailers(
                token, queryParams
            )

            apiResponseCall.enqueue(object : Callback<DistriutorModel> {
                override fun onResponse(
                    call: Call<DistriutorModel>, response: retrofit2.Response<DistriutorModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DistriutorModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getRetailerSearch(
            token: String,

            queryParams: Map<String, String>,

            searchcity: ArrayList<String>,
            searchbranch: ArrayList<String>,
            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getRetailersSearch(token, queryParams,searchcity,searchbranch)

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun submitmspactivity(
            token: String,
            queryParams: Map<String, String>,
            searchcity: ArrayList<String>,
            searchbranch: ArrayList<String>,
            resultLitener: APIResultLitener<AttendanceSubmitModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().submitmsp(token, queryParams,searchcity,searchbranch)

            apiResponseCall.enqueue(object : Callback<AttendanceSubmitModel> {
                override fun onResponse(
                    call: Call<AttendanceSubmitModel>, response: retrofit2.Response<AttendanceSubmitModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<AttendanceSubmitModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getfillterList(
            token: String,

            queryParams: Map<String, String>,

            resultLitener: APIResultLitener<FillterModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getfillterList(
                token, queryParams
            )

            apiResponseCall.enqueue(object : Callback<FillterModel> {
                override fun onResponse(
                    call: Call<FillterModel>, response: retrofit2.Response<FillterModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<FillterModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


        fun getStateList(
            token: String,

            queryParams: Map<String, String>,

            resultLitener: APIResultLitener<StateModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getStateList(
                token, queryParams
            )

            apiResponseCall.enqueue(object : Callback<StateModel> {
                override fun onResponse(
                    call: Call<StateModel>, response: retrofit2.Response<StateModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<StateModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getDistrictList(
            token: String,

            queryParams: Map<String, String>,

            resultLitener: APIResultLitener<DistrictModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getDistrictList(
                token, queryParams
            )

            apiResponseCall.enqueue(object : Callback<DistrictModel> {
                override fun onResponse(
                    call: Call<DistrictModel>, response: retrofit2.Response<DistrictModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<DistrictModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getPincodeInfo(
            token: String,

            queryParams: Map<String, String>,

            resultLitener: APIResultLitener<JsonObject>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getPincodeInfo(
                token, queryParams
            )

            apiResponseCall.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response: retrofit2.Response<JsonObject>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getCityList(
            token: String,

            queryParams: Map<String, String>,

            resultLitener: APIResultLitener<CityModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getCityList(
                token, queryParams
            )

            apiResponseCall.enqueue(object : Callback<CityModel> {
                override fun onResponse(
                    call: Call<CityModel>, response: retrofit2.Response<CityModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<CityModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }

        fun getPincodeList(
            token: String,

            queryParams: Map<String, String>,

            resultLitener: APIResultLitener<PinCodeModel>
        ) {
            val apiResponseCall = getBaseApiServiceInstance1().getPincodeList(
                token, queryParams)

            apiResponseCall.enqueue(object : Callback<PinCodeModel> {
                override fun onResponse(
                    call: Call<PinCodeModel>, response: retrofit2.Response<PinCodeModel>
                ) {

                    resultLitener.onAPIResult(response, null)
                }

                override fun onFailure(call: Call<PinCodeModel>, t: Throwable) {
                    resultLitener.onAPIResult(null, t.message)
                }
            })
        }


    }
}