package com.github.rtyvZ.kitties.common.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvZ.kitties.R

class DragItemHelper(val function: (Int, Int) -> Unit, context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val likeItem = ContextCompat.getDrawable(context, R.drawable.thumb_up_green)
    private val dislikeItem = ContextCompat.getDrawable(context, R.drawable.thumb_down_64)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        function(position, direction)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val item = viewHolder.itemView

        drawIcon(item, dX, c)
    }

    private fun drawIcon(item: View, dX: Float, canvas: Canvas) {

        when {
            dX > 0 -> { // swipe to right
                likeItem?.let {
                    val iconTop = item.top + (item.height - it.intrinsicHeight) / 2
                    val iconBottom = iconTop + it.intrinsicHeight
                    val iconLeft = (item.right - it.intrinsicWidth) / 2
                    val iconRight = iconLeft + it.intrinsicWidth
                    it.bounds = Rect(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(canvas)
                }
            }
            dX < 0 -> { // swipe to left
                dislikeItem?.let {
                    val iconTop = item.top + (item.height - it.intrinsicHeight) / 2
                    val iconBottom = iconTop + it.intrinsicHeight
                    val iconLeft = (item.right - it.intrinsicWidth) / 2
                    val iconRight = iconLeft + it.intrinsicWidth
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(canvas)
                }
            }
        }
    }
}
