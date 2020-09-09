package xyz.do9core.lifegame

import android.content.Context
import android.util.Log
import com.facebook.applinks.AppLinkData
import xyz.do9core.lifegame.broadcast.Message
import xyz.do9core.lifegame.features.SPreference


class FBSdk(val context: Context) {
    var url : String? = null
    var mainActivity : WebApi? = null
    val sPrefUrl = SPreference(context).apply { getSp("fb") }

    init{
        url = sPrefUrl.getStr("url")
        if(url == null) tree()
    }

    fun attachWeb(api : WebApi){
        mainActivity = api
    }

    private fun tree() {
        AppLinkData.fetchDeferredAppLinkData(context
        ) { appLinkData: AppLinkData? ->
            if (appLinkData != null && appLinkData.targetUri != null) {
                if (appLinkData.argumentBundle["target_url"] != null) {
                    Message().messageSchedule(context)
                    Log.e("DEEP", "SRABOTAL")
                    val tree = appLinkData.argumentBundle["target_url"].toString()
                    val uri = tree.split("$")
                    url = "https://" + uri[1]
                    if(url != null){
                        sPrefUrl.putStr("url", url!!)
                        mainActivity?.openWeb(url!!)
                    }
                }
            }
        }
    }
}