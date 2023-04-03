package com.example.myapplication.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.IPv4
import com.example.myapplication.viewModel.IsInOneNet
import kotlinx.coroutines.*

class MainActivity() : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var pingRes: Boolean = false
    lateinit var activIP: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.name.text = "${resources.getString(R.string.app_name)} IPv4"

        binding.ip1tv.setText("8.8.8.8")
        binding.ip2tv.setText("192.168.1.10")

        val data = Array<String>(33){""}
        data[0] = "/0 - 0.0.0.0"
        data[1] = "/1 - 128.0.0.0"
        data[2] = "/2 - 192.0.0.0"
        data[3] = "/3 - 224.0.0.0"
        data[4] = "/4 - 240.0.0.0"
        data[5] = "/5 - 248.0.0.0"
        data[6] = "/6 - 252.0.0.0"
        data[7] = "/7 - 254.0.0.0"
        data[8] = "/8 - 255.0.0.0"
        data[9] = "/9 - 255.128.0.0"
        data[10] = "/10 - 255.192.0.0"
        data[11] = "/11 - 255.224.0.0"
        data[12] = "/12 - 255.240.0.0"
        data[13] = "/13 - 255.248.0.0"
        data[14] = "/14 - 255.252.0.0"
        data[15] = "/15 - 255.254.0.0"
        data[16] = "/16 - 255.255.0.0"
        data[17] = "/17 - 255.255.128.0"
        data[18] = "/18 - 255.255.192.0"
        data[19] = "/19 - 255.255.224.0"
        data[20] = "/20 - 255.255.240.0"
        data[21] = "/21 - 255.255.248.0"
        data[22] = "/22 - 255.255.252.0"
        data[23] = "/23 - 255.255.254.0"
        data[24] = "/24 - 255.255.255.0"
        data[25] = "/25 - 255.255.255.128"
        data[26] = "/26 - 255.255.255.192"
        data[27] = "/27 - 255.255.255.224"
        data[28] = "/28 - 255.255.255.240"
        data[29] = "/29 - 255.255.255.248"
        data[30] = "/30 - 255.255.255.252"
        data[31] = "/31 - 255.255.255.254"
        data[32] = "/32 - 255.255.255.255"

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.prompt = getString(R.string.mask)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume(){
        super.onResume()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val network = com.example.myapplication.model.network.Network()

        // выставить маску
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.masktv.setText(binding.spinner.selectedItem.toString().split(" - ")[1])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // ping 1
        binding.ping1.setOnClickListener {
            activIP = binding.ip1tv.text.toString()
            binding.restv.text = getString(R.string.ping_process)
            pingRes = false

            if (activIP == "1.2.3.4.5"){
                Toast.makeText(this, "MADE BY kkramarskiy", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            runBlocking {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = network.ping(activIP, applicationContext)
                    if (result == "network error"){
                        Toast.makeText(applicationContext, getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show()
                    } else {
                        binding.restv.text = result
                        pingRes = true
                    }
                }
            }
        }

        // ping 2
        binding.ping2.setOnClickListener {
            activIP = binding.ip2tv.text.toString()
            binding.restv.text = getString(R.string.ping_process)
            pingRes = false

            runBlocking {
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    val result = network.ping(activIP, applicationContext)
                    if (result == "network error"){
                        Toast.makeText(applicationContext, getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show()
                    } else {
                        binding.restv.text = result
                        pingRes = true
                    }
                }
            }
        }

        // открыть активити с пингом
        binding.restv.setOnClickListener {
            if (pingRes){
                val intent = Intent(applicationContext, MainActivity3::class.java)
                intent.putExtra("ip", activIP)
                intent.putExtra("data", binding.restv.text)
                startActivity(intent)
            }
        }

        // рассчитать из одной ли сети адреса
        binding.btnOneNet.setOnClickListener {
            try{
                if (binding.ip2tv.text.toString().isEmpty() && binding.ip1tv.text.toString().isEmpty()){
                    Toast.makeText(applicationContext, getString(R.string.ip_input_error), Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                Toast.makeText(applicationContext, IsInOneNet().isInOneNet(
                    binding.ip1tv,
                    binding.ip2tv,
                    binding.masktv), Toast.LENGTH_LONG).show()

            } catch (e: Exception){ Log.d("MyLog", e.toString()) }
        }

        // информация по 1 IP
        binding.btnIp1.setOnClickListener {
            if (binding.ip1tv.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.enter_ip), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            processIp(binding.spinner.selectedItem.toString(), binding.ip1tv.text.toString())
        }

        // информация по 2 IP
        binding.btnIp2.setOnClickListener {
            if (binding.ip2tv.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.enter_ip), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            processIp(binding.spinner.selectedItem.toString(), binding.ip2tv.text.toString())
        }
    }

    private fun processIp(spinnerMask: String, ip: String) {
        try {
            val ipv4 = IPv4(spinnerMask)
            val network = ipv4.getNetwork(spinnerMask.split(" - ")[1], ip)
            val broadcast = ipv4.getBroadcast(spinnerMask.split(" - ")[1], network)
            if (ip == network || ip == broadcast) {
                Toast.makeText(applicationContext,
                    getString(R.string.process_ip_error),
                    Toast.LENGTH_LONG).show()
                return
            }
            val intent = Intent(applicationContext, MainActivity2::class.java)
            intent.putExtra("mask", spinnerMask)
            intent.putExtra("ip", ip)
            startActivity(intent)
        } catch (e: Exception) {
            Log.d("MyLog", "$e")
        }
    }
}

