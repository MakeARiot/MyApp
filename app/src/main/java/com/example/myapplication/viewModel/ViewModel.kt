package com.example.myapplication.viewModel

import com.example.myapplication.model.IPv4

class ViewModel constructor(ipString: String, maskString: String) {

    val ip = IPv4(maskString)

    val mask = ip.convertMaskToUByteList()
    val wildcard = ip.getWildcard(mask).removeSuffix(".")
    val bitmask = maskString.split(" - ")[0]
    val netmask = maskString.split(" - ")[1]
    val network = ip.getNetwork(netmask, ipString).removeSuffix(".")
    val sub_hos = ip.getNumberOfSubnetsHosts(netmask)
    val broadcast = ip.getBroadcast(netmask, network)
    val firsthost = ip.getFirstHost(network, sub_hos!![1])
    val lasthost = ip.getLastHost(broadcast, sub_hos!![1])

    val maskHex = ip.getHex(netmask)
    val addrHex = ip.getHex(ipString)
    val wildcardHex = ip.getHex(wildcard)
    val broadcasthex = ip.getHex(broadcast)
    val firsthostHex = ip.getHex(firsthost)
    val lasthostHex = ip.getHex(lasthost)
    val networkhex = ip.getHex(network)
    val netmaskBin = ip.getNetmaskBin(netmask)
    val networkBin = ip.getNetworkBin(network)
}