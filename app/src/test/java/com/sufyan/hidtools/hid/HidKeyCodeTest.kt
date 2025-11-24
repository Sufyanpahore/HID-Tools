package com.sufyan.hidtools.hid

import org.junit.Assert.assertEquals
import org.junit.Test

class HidKeyCodeTest {

    @Test
    fun `charToHid maps lowercase a correctly`() {
        val (modifier, keyCode) = HidKeyCode.charToHid('a')
        assertEquals(HidKeyCode.MOD_NONE, modifier)
        assertEquals(HidKeyCode.KEY_A, keyCode)
    }

    @Test
    fun `charToHid maps uppercase A correctly`() {
        val (modifier, keyCode) = HidKeyCode.charToHid('A')
        assertEquals(HidKeyCode.MOD_L_SHIFT, modifier)
        assertEquals(HidKeyCode.KEY_A, keyCode)
    }

    @Test
    fun `charToHid maps digit 1 correctly`() {
        val (modifier, keyCode) = HidKeyCode.charToHid('1')
        assertEquals(HidKeyCode.MOD_NONE, modifier)
        assertEquals(HidKeyCode.KEY_1, keyCode)
    }

    @Test
    fun `charToHid maps special character correctly`() {
        val (modifier, keyCode) = HidKeyCode.charToHid('!')
        assertEquals(HidKeyCode.MOD_L_SHIFT, modifier)
        assertEquals(HidKeyCode.KEY_1, keyCode)
    }
}
