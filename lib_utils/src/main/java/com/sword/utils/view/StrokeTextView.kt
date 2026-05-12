package com.sword.utils.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.sword.utils.R

class StrokeTextView : AppCompatTextView {
    private var borderText: TextView? = null // 用于描边的TextView

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        borderText = TextView(context, attrs, defStyleAttr)
        // 读取自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView, defStyleAttr, 0)
        val strokeColor = a.getColor(
            R.styleable.StrokeTextView_st_strokeColor,
            resources.getColor(R.color.color_white)
        )
        val strokeSize = a.getInt(R.styleable.StrokeTextView_st_strokeSize, 15)
        a.recycle()
        val tp1 = borderText!!.paint
        tp1.strokeWidth = strokeSize.toFloat() // 设置描边宽度
        tp1.setStyle(Paint.Style.STROKE) // 对文字只描边
        borderText!!.setTextColor(strokeColor) // 设置描边颜色
        borderText!!.setGravity(getGravity())
    }

    override fun setText(text: CharSequence, type: BufferType?) {
        super.setText(text, type)
        try {
            if (borderText != null && text != borderText!!.getText()) {
                borderText!!.setText(text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        borderText!!.setLayoutParams(params)
    }

    override fun setBackgroundResource(resid: Int) {
        super.setBackgroundResource(resid)
        borderText?.setBackgroundResource(resid)
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        borderText?.background = background
    }

    public fun setStrokeColor(strokeColor: Int) {
        borderText!!.setTextColor(strokeColor) // 设置描边颜色
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val tt = borderText!!.getText()

        // 两个TextView上的文字必须一致
        if (tt == null || tt != this.getText()) {
            borderText!!.setText(getText())
            // 移除 postInvalidate() 避免循环刷新
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        borderText!!.measure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        borderText!!.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        borderText!!.draw(canvas)
        super.onDraw(canvas)
    }
}
