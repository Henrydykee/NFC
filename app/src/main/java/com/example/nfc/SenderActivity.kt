package com.example.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var tvOutcomingMessage: TextView
    private lateinit var etOutcomingMessage: TextView
    private lateinit var btnSetoutMessage: Button
    private val nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //To begin with we check that the device supports NFC feature
        val isNfcSupported: Boolean =
            this.nfcAdapter != null
        if (!isNfcSupported) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
        }
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled!!) {
                Toast.makeText(this, "Nfc is disabled", Toast.LENGTH_SHORT).show()
            }
        }
        initViews()
        // encapsulate sending logic in a separate class
        this.outcomingNfcCallback = OutcomingNfcManager(this)
        this.nfcAdapter?.setOnNdefPushCompleteCallback(outcomingNfcCallback, this)
        this.nfcAdapter?.setNdefPushMessageCallback(outcomingNfcCallback, this)
    }

    private fun initViews() {
        this.tvOutcomingMessage = findViewById(R.id.tv_out_message)
        this.etOutcomingMessage = findViewById(R.id.et_message)
        this.btnSetoutMessage = findViewById(R.id.btn_set_out_message)
        this.btnSetoutMessage.setOnClickListener {
            setOutGoingMessage()
        }
    }

    override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }


    private fun setOutGoingMessage() {
        val outMessage = this.etOutcomingMessage.text.toString()
        this.tvOutcomingMessage.text = outMessage
    }

    override fun getOutcomingMessage(): String =
        this.tvOutcomingMessage.text.toString()


    override fun signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
        runOnUiThread {
            Toast.makeText(this, "beaming complete", Toast.LENGTH_SHORT).show()
        }

    }
}
