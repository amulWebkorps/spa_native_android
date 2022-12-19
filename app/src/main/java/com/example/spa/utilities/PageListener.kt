package com.example.spa.utilities

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PageListener(val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var VISIBLE_THRESHOLD = 1
    private var lastVisibleItem = 0
    private var totalItemCount = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        onScrolledItems(recyclerView,dx)


        totalItemCount = layoutManager.itemCount
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()



        //Log.v("PageListener","onScrolled ${!isLoading()} && $totalItemCount <= $lastVisibleItem + $VISIBLE_THRESHOLD")


        if (!isLoading() && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
            if (callWs())
                loadMoreItems()
        }
    }

    abstract fun onScrolledItems(recyclerView: RecyclerView, newState: Int)
    abstract fun loadMoreItems()
    abstract fun isLoading(): Boolean
    abstract fun callWs(): Boolean

}