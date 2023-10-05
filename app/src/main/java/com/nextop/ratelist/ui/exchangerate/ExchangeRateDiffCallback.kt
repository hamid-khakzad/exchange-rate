package com.nextop.ratelist.ui.exchangerate

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.nextop.ratelist.data.local.ExchangeRate

class ExchangeRateDiffCallback : DiffUtil.ItemCallback<ExchangeRate>() {
    companion object {
        const val PRICE_CHANGE_INDICATOR_KEY: String = "price_change_indicator"
        const val PRICE_KEY: String = "price_change"
    }

    override fun areItemsTheSame(oldItem: ExchangeRate, newItem: ExchangeRate): Boolean {
        return oldItem.symbol == newItem.symbol;
    }

    override fun areContentsTheSame(oldItem: ExchangeRate, newItem: ExchangeRate): Boolean {
        return oldItem.price == newItem.price
    }

    @Synchronized
    override fun getChangePayload(oldItem: ExchangeRate, newItem: ExchangeRate): Any? {

        if (oldItem.symbol != newItem.symbol) return null

        // Use Bundle to pass the diff information to the UI layer for partial update
        return Bundle().also { bundle ->
            // Compare the price and put the change type and new price in the bundle
            when {
                oldItem.price != newItem.price -> {
                    bundle.putInt(PRICE_CHANGE_INDICATOR_KEY, oldItem.price.compareTo(newItem.price))
                    bundle.putDouble(PRICE_KEY, newItem.price)
                }
            }
        }.takeIf { !it.isEmpty() } // Return the bundle only if it contains changes
    }
}
