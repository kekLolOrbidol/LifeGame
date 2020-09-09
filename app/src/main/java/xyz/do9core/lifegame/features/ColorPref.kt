package xyz.do9core.lifegame.features

import android.content.Context

class ColorPref(context: Context) {
    private val ACTION_NAME = "color"
    private val colorPref = SPreference(context).apply { getSp("color") }

    fun getBackgroundColor() : Int? {
        return colorPref.getInt("background")
    }

    fun setBackgroundColor(color : Int){
        colorPref.putInt("background", color)
    }

    fun getCellColor() : Int? {
        return colorPref.getInt("cell")
    }

    fun setCellColor(color : Int){
        colorPref.putInt("cell", color)
    }
}