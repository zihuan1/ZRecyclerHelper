package com.zihuan.view.crvlibrary

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * RecyclerView 扩展类
 * @author Zihuan
 */

val RECYCLER_VERTICAL = LinearLayoutManager.VERTICAL
val RECYCLER_HORIZONTAL = LinearLayoutManager.HORIZONTAL

//横向布局

fun RecyclerView.initHorizontal(adapter: RecyclerView.Adapter<*>) = apply { initHorizontal2(adapter) }

fun RecyclerView.initHorizontal2(adapter: RecyclerView.Adapter<*>) =
    getLinearLayoutManager(RECYCLER_HORIZONTAL, this, adapter)

//竖向布局
fun RecyclerView.initVertical2(adapter: RecyclerView.Adapter<*>) =
    getLinearLayoutManager(RECYCLER_VERTICAL, this, adapter)

fun RecyclerView.initVertical(adapter: RecyclerView.Adapter<*>) = apply { initVertical2(adapter) }

fun RecyclerView.initGrid2(count: Int, adapter: RecyclerView.Adapter<*>) = getGridLayoutManager(count, this, adapter)

fun RecyclerView.initGrid(count: Int, adapter: RecyclerView.Adapter<*>) = apply { initGrid2(count, adapter) }

private fun getLinearLayoutManager(direction: Int, view: RecyclerView, adapter: RecyclerView.Adapter<*>) =
    LinearLayoutManager(view.context).apply {
        orientation = direction
        view.layoutManager = this
        view.isNestedScrollingEnabled = false
        view.adapter = adapter
    }

private fun getGridLayoutManager(count: Int, view: RecyclerView, adapter: RecyclerView.Adapter<*>) =
    GridLayoutManager(view.context, count).apply {
        view.layoutManager = this
        view.isNestedScrollingEnabled = false
        view.adapter = adapter
    }
