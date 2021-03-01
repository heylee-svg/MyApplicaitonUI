package com.cylan.smart.base

import android.content.pm.PackageManager
import com.cylan.smart.base.utils.AppContext

/**
 * @author Lupy
 * @since 19-7-12
 * @desc $
 */

class OEM {


    var oem:String
    var host:String
    var port:Int

    companion object{
        private val oem:OEM by lazy {
            OEM()
        }

        fun getInstance():OEM{
            return oem
        }
    }

    constructor(){
//        var applicationInfo = AppContext.getContext().packageManager.getApplicationInfo( AppContext.getContext().packageName, PackageManager.GET_META_DATA)
//        val metaData = applicationInfo.metaData

//        oem = metaData.getString("OEM")
//        host = metaData.getString("HOST")
//        port = metaData.getInt("PORT")
        oem = "smart";
        host ="http://yun.smartdoby.com";
        port = 80;
    }




}