package com.exp.clonefieldkonnect.helper

import android.content.Context
import com.exp.clonefieldkonnect.R
import java.lang.Exception

class StaticSharedpreference {

    companion object {
        fun saveInfo(key: String, value: String, context: Context): String {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.commit()

            return key
        }

        fun saveBoolean(key: String, value: Boolean, context: Context) {
            val PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        // Retrieve Boolean Value
        fun getBoolean(key: String, context: Context): Boolean {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)
            return try {
                val sharedpreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                sharedpreference.getBoolean(key, false)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun getInfo(key: String, context: Context): String? {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)

            var value: String? = ""
            try {
                var sharedpreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                // var default : String = ""
                value = sharedpreference.getString(key, "")
                return value
            } catch (e: Exception) {
                e.printStackTrace()
                return value
            }
        }

  fun saveInfoUnclear(key: String, value: String, context: Context): String {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)+"_unclear"
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.commit()

            return key
        }

        fun getInfoUnclear(key: String, context: Context): String? {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)+"_unclear"

            var value: String? = ""
            try {
                var sharedpreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                // var default : String = ""
                value = sharedpreference.getString(key, "")
                return value
            } catch (e: Exception) {
                e.printStackTrace()
                return value
            }
        }



        fun deleteSharedPreference(context: Context) {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)

            var sharedpreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedpreference.edit()
            editor.clear()
            editor.commit()
        }

        fun saveInt(key: String, valuInt: Int, context: Context) {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)

            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(key, valuInt)
            editor.commit()

        }

        fun getInt(key: String, context: Context): Int {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)

            var value = 0
            return try {
                var sharedpreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                value = sharedpreference.getInt(key, 0)
                value
            } catch (e: Exception) {
                e.printStackTrace()
                value
            }
        }


        fun removeKey(key: String, context: Context) {
            var PREF_NAME = context.packageName + "." + context.resources.getString(R.string.app_name)

            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove(key)
            editor.commit()

        }
    }
}