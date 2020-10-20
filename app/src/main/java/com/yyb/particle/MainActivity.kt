package com.yyb.particle

import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.ofFloat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initViewAndAnimation()

  }

  private fun initViewAndAnimation() {
    Glide.with(this)
      .load(R.mipmap.test)
      .circleCrop()
      .transition(DrawableTransitionOptions.withCrossFade(500))
      .into(iv_src)
    val animator = ObjectAnimator.ofFloat(iv_src, View.ROTATION, 0f, 360f)
    animator.apply {
      duration=6000
      repeatCount=-1
      interpolator= LinearInterpolator()
    }
    animator.start()
  }
}