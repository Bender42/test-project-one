package com.vmatdev.testprojectone.utils

import android.widget.TextView
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.slots.Slot
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

class CustomMaskImpl(private val mask: String) {

    var maskFormatWatcher: MaskFormatWatcher

    init {
        maskFormatWatcher = MaskFormatWatcher(createTerminated(mask))
    }

    fun installOn(textView: TextView) {
        maskFormatWatcher.installOn(textView)
    }

    fun getValue(maskedValue: String, strip: Boolean): String {
        val maskImpl = createTerminated(mask)
        maskImpl.insertFront(maskedValue)
        if (strip) {
            return maskImpl.toUnformattedString()
        } else {
            return maskImpl.toString()
        }
    }

    private fun createTerminated(mask: String): MaskImpl {
        val resultSlots = ArrayList<Slot>()
        for (char in mask) {
            when {
                char.isDigit() -> resultSlots.add(PredefinedSlots.hardcodedSlot(char))
                char == 'Х' -> resultSlots.add(PredefinedSlots.digit())
                else -> resultSlots.add(PredefinedSlots.hardcodedSlot(char).withTags(Slot.TAG_DECORATION))
            }
        }
        return MaskImpl.createTerminated(resultSlots.toTypedArray())
    }
}