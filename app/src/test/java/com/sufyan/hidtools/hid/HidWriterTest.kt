package com.sufyan.hidtools.hid

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class HidWriterTest {

    @Test
    fun `buildKeyboardReport creates correct report`() {
        val modifier = HidKeyCode.MOD_L_SHIFT
        val keyCodes = byteArrayOf(HidKeyCode.KEY_A, HidKeyCode.KEY_B, 0, 0, 0, 0)

        val report = HidWriter.buildKeyboardReport(modifier, keyCodes)

        val expectedReport = byteArrayOf(
            modifier,       // Modifier
            0,              // Reserved
            HidKeyCode.KEY_A, // Key 1
            HidKeyCode.KEY_B, // Key 2
            0,              // Key 3
            0,              // Key 4
            0,              // Key 5
            0               // Key 6
        )

        assertArrayEquals(expectedReport, report)
    }
}
