package com.example.myapplication.model

import android.util.Log
import java.util.*
import kotlin.math.pow

class IPv4(private val maskString: String){
    // принимает netMask: String, ipString: String, возвращает network: String в десятичном виде
    fun getNetwork(netMask: String, ipString: String): String {
        var network = ""
        try {
            val maskb = ArrayList<UByte>()
            netMask.split(".").forEach { b ->
                maskb.add(b.toUByte())
            }
            val ip1b = ArrayList<UByte>()
            ipString.split(".").forEach { b ->
                ip1b.add(b.toUByte())
            }
            for (i in 0..3) {
                network += "${(maskb[i] and ip1b[i])}."
            }
            return network.removeSuffix(".")

        } catch (e: Exception) { Log.d("MyLog", "network $e") }

        return network
    }

    // принимает netmask: String возвращает ArrayList<String> количество подсетей[0], количество хостов[1]
    fun getNumberOfSubnetsHosts(netmask: String): List<String>? {
        return try {
            val binNetmask = netmask
                .split(".")
                .map { it.toInt() }
                .joinToString("") { convertIntToBinString(it).padStart(8, '0') }

            val subnets = 2.0.pow(binNetmask.count { it == '1' }).toInt()
            val hosts = 2.0.pow(binNetmask.count { it == '0' } - 1).toInt() - 2

            listOf("$subnets", "$hosts")
        } catch (e: Exception) {
            null
        }
    }

    fun getBroadcast(netmask: String, network: String): String {
        val maskBin = convertIpToBinString(netmask)
        val ipBin = convertIpToBinString(network)
        val broadcastBin = ipBin.mapIndexed { index, c ->
            if (maskBin[index] == '0') '1' else c
        }.joinToString("")

        return convertBinStringToIp(broadcastBin)
    }

    fun getFirstHost(network: String, hosts: String): String {
        return if (hosts == "0") {
            "No hosts available"
        } else {
            val netList = network.split(".").map(String::toUByte).toMutableList()
            netList[3] = netList[3].plus(1u).toUByte()
            val firstHost = netList.joinToString(".")
            Log.d("MyLog", """getFirstHost: 
            |firstHost $firstHost""".trimMargin())

            firstHost
        }
    }

    fun getLastHost(broadcast: String, hosts: String): String {
        return if (hosts == "0") {
            "No hosts available"
        } else {
            val netList = broadcast.split(".").map(String::toUByte).toMutableList()
            netList[3] = netList[3].minus(1u).toUByte()
            val lastHost = netList.joinToString(".")
            Log.d("MyLog", """getLastHost: 
            |getLastHost $lastHost""".trimMargin())

            lastHost
        }
    }

    fun getBin(string: String): String {
        val list = string.split(".").map(String::toUByte)
        return convertUByteListToBinStringList(list).joinToString(".").removePrefix("[").removeSuffix("]")
    }

    fun getNetmaskBin(netmask: String): String =
        convertUByteListToBinStringList(convertIpToUByteList(netmask)).joinToString(".")
            .removeSuffix("]").removePrefix("[")


    fun getNetworkBin(network: String): String =
        convertUByteListToBinStringList(convertIpToUByteList(network)).joinToString(".")
            .removeSuffix("]").removePrefix("[")

    fun getWildcard(mask: List<UByte>): String = mask.map { it.inv() }.joinToString(".")
    fun getHex(string: String): String  = string.split(".").joinToString(":") { it.toLong().toString(16) }.uppercase(Locale.ROOT)
    fun convertMaskToUByteList(): List<UByte> = maskString.split(" - ")[1].split(".").map { it.toUByte() }
    private fun convertIntToBinString(value: Int) = Integer.toBinaryString(value)
    private fun convertUByteListToBinStringList(list: List<UByte>) = list.map { convertIntToBinString(it.toInt()) }
    private fun convertIpToUByteList(ip: String) = ip.split(".").map { it.toUByte() }
    private fun convertIpToBinString(ip: String) = convertIpToUByteList(ip).joinToString("") { convertIntToBinString(it.toInt()).padStart(8, '0') }
    private fun convertBinStringToIp(binString: String) = binString.chunked(8).map { Integer.parseInt(it, 2) }.joinToString(".")
}