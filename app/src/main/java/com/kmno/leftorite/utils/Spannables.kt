/*
 * Creator: Kamran Noorinejad on 7/15/20 10:28 AM
 * Last modified: 7/15/20 10:28 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.*

/**
 * Created by Kamran Noorinejad on 7/15/2020 AD 10:28.
 * Edited by Kamran Noorinejad on 7/15/2020 AD 10:28.
 */

private const val EMPTY_STRING = ""
private const val FIRST_SYMBOL = 0

fun spannable(func: () -> SpannableString) = func()

private fun span(s: CharSequence, o: Any) = getNewSpannableString(s).apply {
    setSpan(o, FIRST_SYMBOL, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

private fun getNewSpannableString(charSequence: CharSequence): SpannableString {
    return if (charSequence is String) {
        SpannableString(charSequence)
    } else {
        charSequence as? SpannableString ?: SpannableString(EMPTY_STRING)
    }
}

operator fun SpannableString.plus(s: CharSequence) = SpannableString(TextUtils.concat(this, "", s))

fun CharSequence.makeSpannableString() = span(this, Spanned.SPAN_COMPOSING)
fun CharSequence.makeBold() = span(this, StyleSpan(BOLD))
fun CharSequence.makeItalic() = span(this, StyleSpan(ITALIC))
fun CharSequence.makeUnderline() = span(this, UnderlineSpan())
fun CharSequence.makeStrike() = span(this, StrikethroughSpan())
fun CharSequence.makeSuperscript() = span(this, SuperscriptSpan())
fun CharSequence.makeSubscript() = span(this, SubscriptSpan())
fun CharSequence.makeAnotherSize(size: Float) = span(this, RelativeSizeSpan(size))
fun CharSequence.makeAnotherColor(color: Int) = span(this, ForegroundColorSpan(color))
fun CharSequence.makeAnotherBackground(color: Int) = span(this, BackgroundColorSpan(color))
fun CharSequence.makeUrl(url: String) = span(this, URLSpan(url))