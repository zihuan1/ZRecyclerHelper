package com.zihuan.view.crvlibrary

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class RecycleViewDivider : ItemDecoration {
    private var mPaint: Paint? = null
    private var mDivider: Drawable
    var mDividerHeight = 2 //分割线高度，默认为1px
    //列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    var mOrientation = LinearLayoutManager.VERTICAL
    private val ATTRS = intArrayOf(R.attr.listDivider)

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    constructor(context: Context, orientation: Int = LinearLayoutManager.VERTICAL) {
        require(!(orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL)) { "请输入正确的参数！" }
        mOrientation = orientation
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    constructor(context: Context, drawableId: Int, orientation: Int = LinearLayoutManager.VERTICAL) {
        mDivider = context.resources.getDrawable(drawableId)
        mOrientation = orientation
        if (mDivider.intrinsicHeight > 0)
            mDividerHeight = mDivider.intrinsicHeight
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, dividerColor: Int, dividerHeight: Int, orientation: Int = LinearLayoutManager.VERTICAL) {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        mDividerHeight = dividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = dividerColor
        mPaint!!.style = Paint.Style.FILL
    }

    var mDTop = 0
    var mDBottom = 0
    fun setDividerTopAndBottom(top: Int, bottom: Int) {
        mDTop = top
        mDBottom = bottom
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     *
     * @param canvas
     * @param parent
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        mPaint
                )
            }
        }
    }

    /**
     * 绘制横向列表时的分隔线  这时分隔线是竖着的
     * l、r 变化； t、b 不变
     *
     * @param canvas
     * @param parent
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        mPaint
                )
            }
        }
    }

    var isDrawBottom = false
    fun setDrawBotton(isDraw: Boolean) {
        isDrawBottom = isDraw
    }

    //获取分割线尺寸
    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        val spanCount = getSpanCount(parent)
        val childCount = parent.adapter!!.itemCount
        //        // 如果是最后一行，则不需要绘制底部
//        if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
//            // 最后一行且为最后一列则不绘制
//            if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
//                outRect.set(0, 0, 0, 0);
//            } else {
//                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
//            }
//        } else if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
//            // 如果是最后一列，则不需要绘制右边
//            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//        } else {
//            outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
//        }
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (!isDrawBottom && (itemPosition + 1) % spanCount == 0) { // 如果是最后一列，默认不需要绘制底部分割线
                outRect[0, 0, 0] = 0
            } else {
                outRect[0, 0, 0] = mDividerHeight
            }
        } else {
            outRect[0, 0, mDividerHeight] = 0
        }
    }

    private fun isLastColumn(
            parent: RecyclerView,
            pos: Int,
            spanCount: Int,
            childCount: Int
    ): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) { // 如果是最后一列，则不需要绘制右边
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager.orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) { // 如果是最后一列，则不需要绘制右边
                    return true
                }
            } else {
                childCount -= childCount % spanCount
                if (pos >= childCount) // 如果是最后一列，则不需要绘制右边
                    return true
            }
        }
        return false
    }

    private fun isLastRaw(
            parent: RecyclerView,
            pos: Int,
            spanCount: Int,
            childCount: Int
    ): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) { // 如果是最后一行，则不需要绘制底部
            if (pos + spanCount >= childCount) return true
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                    .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) { // 如果是最后一行，则不需要绘制底部
                if (pos + spanCount >= childCount) return true
            } else  // StaggeredGridLayoutManager 且横向滚动
            { // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun getSpanCount(parent: RecyclerView): Int { // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

}