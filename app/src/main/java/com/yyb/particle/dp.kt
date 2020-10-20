package com.yyb.particle

import android.content.Context

/**
 *     author : 闫裕波
 *     e-mail : yyb@zlhopesun.com
 *     time   : 2020/10/20
 *     desc   : 类说明
 */
fun Context.dp2Px(dp: Float ):Float{
    val density = this.resources.displayMetrics.density
    return dp * density + 0.5f
}