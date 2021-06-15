package com.android.vlmespresso

import android.view.View
import org.junit.Assert.assertEquals

fun assertViewVisible(view: View) {
    assertEquals(View.VISIBLE, view.visibility)
}

fun assertViewInvisible(view: View) {
    assertEquals(View.INVISIBLE, view.visibility)
}

fun assertViewGone(view: View) {
    assertEquals(View.GONE, view.visibility)
}