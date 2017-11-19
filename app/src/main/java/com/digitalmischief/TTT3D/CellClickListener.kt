package com.digitalmischief.TTT3D

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by andy on 11/5/17.
 */
class CellClickListener(
        context: Context,
        val recyclerView: RecyclerView,
        var listener: OnItemClickListener)
    : RecyclerView.OnItemTouchListener {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onLongItemClick(view: View, position: Int)
    }

    var gestureDetector: GestureDetector = GestureDetector(context,
            object: GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean{
            return true
        }
        override fun onLongPress(e: MotionEvent) {
            recyclerView.findChildViewUnder(e.x, e.y)?.let{
                listener.onLongItemClick(it, recyclerView.getChildAdapterPosition(it))
            }
        }
    })

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        view.findChildViewUnder(e.x, e.y)?.let{
            if (gestureDetector.onTouchEvent(e)) {
                listener.onItemClick(it, view.getChildAdapterPosition(it))
                return true
            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}