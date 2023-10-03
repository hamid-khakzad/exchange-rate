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
        if (oldItem.symbol == newItem.symbol) {
            val diffBundle = Bundle()
            diffBundle.putInt(PRICE_CHANGE_INDICATOR_KEY, oldItem.price.compareTo(newItem.price))
            diffBundle.putDouble(PRICE_KEY, newItem.price)
            if (diffBundle.size() == 0) return null
            return diffBundle
        }
        return super.getChangePayload(oldItem, newItem)

    }
}
