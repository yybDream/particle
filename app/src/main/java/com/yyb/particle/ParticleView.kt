package com.yyb.particle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import java.lang.Math.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 *     author : 闫裕波
 *     e-mail : yyb@zlhopesun.com
 *     time   : 2020/10/20
 *     desc   : 模仿网易云音乐唱片特效
 */
class ParticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //中心
    private var centerY: Float = 0f
    private var centerX: Float = 0f
    private var random = Random()
    private val path = Path()

    //圆的半径
    private var circleradius = 0f

    //星星的数量
    private var particleNum = 0
    private val pathMeasure = PathMeasure()//路径，用于测量扩散圆某一处的X,Y值
    private var pos = FloatArray(2) //扩散圆上某一点的x,y
    private val tan = FloatArray(2)//扩散圆上某一点切线

    //定义一个粒子的集合
    private var particleList = mutableListOf<Coordinate>()

    //定义画笔
    private var paint = Paint()

    //定义动画
    private var animator = ValueAnimator.ofFloat(0f, 1f)

    init {
        //初始化画笔
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        //初始化值动画
        animator.duration = 2000
        animator.repeatCount = 1
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            updateParticle(it.animatedValue as Float)
            invalidate()//重绘界面
        }
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.particleStyle)
        circleradius =
            obtainStyledAttributes.getDimension(
                R.styleable.particleStyle_circleradius,
                100f
            )
        particleNum = obtainStyledAttributes.getInteger(R.styleable.particleStyle_particleNum, 2000)
        obtainStyledAttributes.recycle()
    }

    private fun updateParticle(fl: Float) {
        particleList.forEach { particle ->
            //当偏移量大于最大的偏移量，重新归0
            if (particle.offset > particle.maxOffset) {
                particle.offset = 0
                particle.speed = (random.nextInt(3) + 1.5).toFloat()
            }
            particle.alpha = ((1f - particle.offset / particle.maxOffset) * 225f).toInt()
            //计算偏移的点X轴的坐标
            particle.x =
                (centerX + cos(particle.angle) * (circleradius + particle.offset)).toFloat()
            //计算Y轴偏移的点
            if (particle.y > centerY) {
                particle.y =
                    (sin(particle.angle) * (circleradius + particle.offset) + centerY).toFloat()
            } else {
                particle.y =
                    (centerY - sin(particle.angle) * (circleradius + particle.offset)).toFloat()
            }
            particle.offset += particle.speed.toInt()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()
        path.addCircle(centerX, centerY, circleradius, Path.Direction.CCW)
        //通过这个方法就可以获取到圆的路径上各个点的xy
        pathMeasure.setPath(path, false)
        //循环创建发散的星星各个坐标系统
        for (i in 0..2000) {
            pathMeasure.getPosTan(i / 2000f * pathMeasure.length, pos, tan)
            //取出xy的坐标
            val nextX = pos[0] + random.nextInt(6) - 3f
            val nextY = pos[1] + random.nextInt(6) - 3f
            //通过反余选函数获取角度
            val angle = acos(((pos[0] - centerX) / circleradius).toDouble())
            //设置星星的速度
            val speed = random.nextInt(2) + 2f
            val offSet = random.nextInt(200)
            val maxOffset = random.nextInt(200).toFloat()
            particleList.add(
                Coordinate(nextX, nextY, 2f, speed, 100, maxOffset, offSet, angle)
            )
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        particleList.forEachIndexed { index, particle ->
            if (particle.offset > 5f) {
                //根据偏移量和最大偏移量的比例计算渐变
                paint.alpha = ((1f - particle.offset / particle.maxOffset) * 0.8 * 225f).toInt()
                canvas?.drawCircle(particle.x, particle.y, particle.radius, paint)
            } else {
                paint.alpha = 225
            }
            canvas?.drawCircle(particle.x, particle.y, particle.radius, paint)

        }


    }
}