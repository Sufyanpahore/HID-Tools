package com.sufyan.hidtools.scripts

import com.sufyan.hidtools.hid.HidKeyCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScriptRunnerTest {

    @Test
    fun `parseLine correctly parses KEY command`() {
        val (action, _) = ScriptRunner.parseLine("KEY A", 0)
        assertTrue(action is ScriptAction.KeyPress)
        assertEquals(HidKeyCode.KEY_A, (action as ScriptAction.KeyPress).keyCode)
        assertEquals(HidKeyCode.MOD_L_SHIFT, action.modifier)
    }

    @Test
    fun `parseLine correctly parses MOUSE MOVE command`() {
        val (action, _) = ScriptRunner.parseLine("MOUSE MOVE 10 -5", 0)
        assertTrue(action is ScriptAction.MouseMove)
        assertEquals(10.toByte(), (action as ScriptAction.MouseMove).dx)
        assertEquals((-5).toByte(), action.dy)
    }

    @Test
    fun `parseLine correctly parses DELAY command`() {
        val (action, _) = ScriptRunner.parseLine("DELAY 500", 0)
        assertTrue(action is ScriptAction.Delay)
        assertEquals(500, (action as ScriptAction.Delay).ms)
    }
}
