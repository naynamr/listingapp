package com.example.test.util

import android.content.Context
import android.graphics.drawable.Drawable
import java.io.InputStream

object CommonUtil {

    /**
     *  Get data from asset and convert into string
     */
    fun getDataFromAsset(context: Context, fileName: String): String? {
        return try {
            val data = context.assets.open(fileName).bufferedReader().use { it.readText() }
            data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     *  Load image from asset folder
     */
    fun getImageFromAsset(context: Context, fileName: String): Drawable? {

        return try {
            // get input stream
            val ins: InputStream = context.assets.open(fileName)
            // load image as Drawable
            val drawable = Drawable.createFromStream(ins, null)
            ins.close()
            drawable
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
}