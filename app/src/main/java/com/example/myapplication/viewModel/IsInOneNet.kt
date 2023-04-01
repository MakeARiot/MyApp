package com.example.myapplication.viewModel

import com.google.android.material.textfield.TextInputEditText

class IsInOneNet {
    fun isInOneNet(ip1tv: TextInputEditText, ip2tv: TextInputEditText,
                   masktv: TextInputEditText): String {
        val mask = masktv.text.toString().split(".").map { it.toUByte() }
        val ip1 = ip1tv.text.toString().split(".").map { it.toUByte() }
        val ip2 = ip2tv.text.toString().split(".").map { it.toUByte() }

        val listres1 = mask.zip(ip1) { m, i -> m and i }
        val listres2 = mask.zip(ip2) { m, i -> m and i }

        return if (listres1 == listres2) {
            "Узлы находятся в одной сети"
        } else {
            "Узлы находятся в разных сетях"
        }
    }
}