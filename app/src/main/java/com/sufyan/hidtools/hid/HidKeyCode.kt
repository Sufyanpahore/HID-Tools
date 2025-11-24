package com.sufyan.hidtools.hid

object HidKeyCode {

    const val MOD_NONE: Byte = 0x00
    const val MOD_L_CTRL: Byte = 0x01
    const val MOD_L_SHIFT: Byte = 0x02
    const val MOD_L_ALT: Byte = 0x04
    const val MOD_L_META: Byte = 0x08
    const val MOD_R_CTRL: Byte = 0x10
    const val MOD_R_SHIFT: Byte = 0x20
    const val MOD_R_ALT: Byte = 0x40
    const val MOD_R_META: Byte = -128

    val KEY_A = 0x04.toByte()
    val KEY_B = 0x05.toByte()
    val KEY_C = 0x06.toByte()
    val KEY_D = 0x07.toByte()
    val KEY_E = 0x08.toByte()
    val KEY_F = 0x09.toByte()
    val KEY_G = 0x0A.toByte()
    val KEY_H = 0x0B.toByte()
    val KEY_I = 0x0C.toByte()
    val KEY_J = 0x0D.toByte()
    val KEY_K = 0x0E.toByte()
    val KEY_L = 0x0F.toByte()
    val KEY_M = 0x10.toByte()
    val KEY_N = 0x11.toByte()
    val KEY_O = 0x12.toByte()
    val KEY_P = 0x13.toByte()
    val KEY_Q = 0x14.toByte()
    val KEY_R = 0x15.toByte()
    val KEY_S = 0x16.toByte()
    val KEY_T = 0x17.toByte()
    val KEY_U = 0x18.toByte()
    val KEY_V = 0x19.toByte()
    val KEY_W = 0x1A.toByte()
    val KEY_X = 0x1B.toByte()
    val KEY_Y = 0x1C.toByte()
    val KEY_Z = 0x1D.toByte()

    val KEY_1 = 0x1E.toByte()
    val KEY_2 = 0x1F.toByte()
    val KEY_3 = 0x20.toByte()
    val KEY_4 = 0x21.toByte()
    val KEY_5 = 0x22.toByte()
    val KEY_6 = 0x23.toByte()
    val KEY_7 = 0x24.toByte()
    val KEY_8 = 0x25.toByte()
    val KEY_9 = 0x26.toByte()
    val KEY_0 = 0x27.toByte()

    val KEY_ENTER = 0x28.toByte()
    val KEY_BACKSPACE = 0x2A.toByte()
    val KEY_TAB = 0x2B.toByte()
    val KEY_SPACE = 0x2C.toByte()

    val KEY_ARROW_RIGHT = 0x4F.toByte()
    val KEY_ARROW_LEFT = 0x50.toByte()
    val KEY_ARROW_DOWN = 0x51.toByte()
    val KEY_ARROW_UP = 0x52.toByte()

    val KEY_F1 = 0x3A.toByte()
    val KEY_F2 = 0x3B.toByte()
    val KEY_F3 = 0x3C.toByte()
    val KEY_F4 = 0x3D.toByte()
    val KEY_F5 = 0x3E.toByte()
    val KEY_F6 = 0x3F.toByte()
    val KEY_F7 = 0x40.toByte()
    val KEY_F8 = 0x41.toByte()
    val KEY_F9 = 0x42.toByte()
    val KEY_F10 = 0x43.toByte()
    val KEY_F11 = 0x44.toByte()
    val KEY_F12 = 0x45.toByte()

    fun charToHid(char: Char): Pair<Byte, Byte> {
        return when (char) {
            in 'a'..'z' -> Pair(MOD_NONE, (char.code - 'a'.code + KEY_A).toByte())
            in 'A'..'Z' -> Pair(MOD_L_SHIFT, (char.code - 'A'.code + KEY_A).toByte())
            in '0'..'9' -> Pair(MOD_NONE, (char.code - '0'.code + KEY_1).toByte())
            '\n' -> Pair(MOD_NONE, KEY_ENTER)
            '\b' -> Pair(MOD_NONE, KEY_BACKSPACE)
            '\t' -> Pair(MOD_NONE, KEY_TAB)
            ' ' -> Pair(MOD_NONE, KEY_SPACE)

            '!' -> Pair(MOD_L_SHIFT, KEY_1)
            '@' -> Pair(MOD_L_SHIFT, KEY_2)
            '#' -> Pair(MOD_L_SHIFT, KEY_3)

            else -> Pair(MOD_NONE, 0x00.toByte())
        }
    }
}
