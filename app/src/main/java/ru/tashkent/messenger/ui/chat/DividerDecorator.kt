package ru.tashkent.messenger.ui.chat

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DividerDecorator(
    private val verticalDivider: Int,
    private val horizontalDivider: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val halfVertical = verticalDivider / 2
        val halfHorizontal = horizontalDivider / 2

        with(outRect) {
            top = halfVertical
            bottom = halfVertical

            left = halfHorizontal
            right = halfHorizontal
        }
    }
}