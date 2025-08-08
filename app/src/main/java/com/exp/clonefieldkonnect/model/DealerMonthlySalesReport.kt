package com.exp.clonefieldkonnect.model

import com.google.gson.annotations.SerializedName

class DealerMonthlySalesReport {

     @SerializedName("status")
     var status: String? = null

     @SerializedName("message")
     var message: String? = null

     @SerializedName("data")
     var data: Data? = Data()

     class Data {

         @SerializedName("April")
         var April: April? = April()

         @SerializedName("May")
         var May: May? = May()

         @SerializedName("June")
         var June: June? = June()

         @SerializedName("July")
         var July: July? = July()

         @SerializedName("August")
         var August: August? = August()

         @SerializedName("September")
         var September: September? = September()

         @SerializedName("October")
         var October: October? = October()

         @SerializedName("November")
         var November: November? = November()

         @SerializedName("December")
         var December: December? = December()

         @SerializedName("January")
         var January: January? = January()

         @SerializedName("February")
         var February: February? = February()

         @SerializedName("March")
         var March: March? = March()

     }
     class April {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }

     class May {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }

     class June {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class July {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class August {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }

     class September {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class October {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class November {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class December {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class January {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class February {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
     class March {

         @SerializedName("achiv")
         var achiv: String? = null

         @SerializedName("terg")
         var terg: String? = null

         @SerializedName("achivper")
         var achivper: String? = null

     }
 }