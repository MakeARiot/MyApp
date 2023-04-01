package com.example.myapplication.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMain2Binding
import com.example.myapplication.viewModel.ViewModel

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onResume() {
        super.onResume()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val ipString = intent.extras?.get("ip").toString()
        val maskString = intent.extras?.get("mask").toString()

        val vm = ViewModel(ipString, maskString)
        vm.apply {
            binding.address.text = ipString
            binding.Bitmask.text = bitmask
            binding.netmask.text = netmask
            binding.wildcard.text = wildcard
            binding.broadcast.text = broadcast
            binding.network.text = network
            binding.subnetsnumber.text = sub_hos!![0]
            binding.hostsnumber.text = sub_hos[1]
            binding.firsthost.text = firsthost
            binding.lasthost.text = lasthost

            binding.addrHex.text = addrHex
            binding.netmaskHex.text = maskHex
            binding.wildcardHex.text = wildcardHex
            binding.broadcastHex.text = broadcasthex
            binding.networkHex.text = networkhex
            binding.firsthostHex.text = firsthostHex
            binding.lasthostHex.text = lasthostHex

            binding.addrBin.text = ip.getBin(ipString)
            binding.netmaskBin.text = netmaskBin
            binding.networkBin.text = networkBin
            binding.wildcardBin.text = ip.getBin(wildcard)
            binding.broadcastBin.text = ip.getBin(broadcast)
            binding.firsthostBin.text = ip.getBin(firsthost)
            binding.lasthostBin.text = ip.getBin(lasthost)
        }
    }
}

