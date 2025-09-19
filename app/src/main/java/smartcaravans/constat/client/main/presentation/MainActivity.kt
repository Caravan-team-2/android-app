package smartcaravans.constat.client.main.presentation

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ReaderCallback
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.android.ext.android.get
import org.koin.compose.viewmodel.koinViewModel
import smartcaravans.constat.client.core.presentation.util.SetSystemBarColors
import smartcaravans.constat.client.main.presentation.viewmodel.MainUiAction
import smartcaravans.constat.client.main.presentation.viewmodel.MainViewModel
import smartcaravans.constat.client.ui.theme.AppTheme
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

class MainActivity : ComponentActivity(), ReaderCallback {
    lateinit var viewModel: MainViewModel

    private var nfcAdapter: NfcAdapter? = null
    private var readRequested: Boolean = false
    private val pendingWriteText = AtomicReference<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize NFC adapter
        nfcAdapter = (getSystemService(NFC_SERVICE) as NfcManager).defaultAdapter

        setContent {
            viewModel = koinViewModel<MainViewModel>()
            AppTheme {
                SetSystemBarColors(get())
                AppScreen(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        enableReaderMode()
    }

    override fun onPause() {
        super.onPause()
        disableReaderMode()
    }

    private fun enableReaderMode() {
        nfcAdapter?.enableReaderMode(
            this,
            this,
            NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V or
                NfcAdapter.FLAG_READER_NFC_BARCODE or
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )
    }

    private fun disableReaderMode() {
        nfcAdapter?.disableReaderMode(this)
    }

    /** Public API: start listening for a text NDEF and handle it */
    fun readTextWithNfc() {
        if (nfcAdapter == null) {
            toast("NFC n'est pas disponible sur cet appareil")
            return
        }
        if (nfcAdapter?.isEnabled != true) {
            toast("Veuillez activer le NFC dans les paramètres")
            return
        }
        readRequested = true
        pendingWriteText.set(null)
        toast("Approchez une balise NFC pour lire les données")
    }

    /** Public API: prepare to write the provided text to the next tag tapped */
    fun shareTextWithNfc(text: String) {
        if (nfcAdapter == null) {
            toast("NFC n'est pas disponible sur cet appareil")
            return
        }
        if (nfcAdapter?.isEnabled != true) {
            toast("Veuillez activer le NFC dans les paramètres")
            return
        }
        readRequested = false
        pendingWriteText.set(text)
        toast("Approchez une balise NFC pour écrire les données")
    }

    override fun onTagDiscovered(tag: Tag) {
        try {
            val textToWrite = pendingWriteText.get()
            if (textToWrite != null) {
                // Try writing
                val success = writeTextToTag(tag, textToWrite)
                runOnUiThread {
                    if (success) {
                        toast("Écriture NFC réussie")
                        pendingWriteText.set(null)
                    } else {
                        toast("Échec de l'écriture sur la balise NFC")
                    }
                }
                return
            }

            if (!readRequested) return // Reading not requested

            // Try reading
            val readText = readTextFromTag(tag)
            if (readText != null) {
                runOnUiThread {
                    viewModel.onAction(MainUiAction.Scanned(readText))
                    toast("Lecture NFC réussie")
                    readRequested = false
                }
            } else {
                runOnUiThread { toast("Aucune donnée texte trouvée sur la balise NFC") }
            }
        } catch (e: Exception) {
            runOnUiThread { toast("Erreur NFC: ${e.message}") }
        }
    }

    private fun readTextFromTag(tag: Tag): String? {
        val ndef = Ndef.get(tag) ?: return null
        return try {
            ndef.connect()
            val message = ndef.cachedNdefMessage ?: ndef.ndefMessage
            message?.let { parseFirstTextRecord(it) }
        } finally {
            try { ndef.close() } catch (_: Exception) {}
        }
    }

    private fun writeTextToTag(tag: Tag, text: String): Boolean {
        val record = createTextRecord(text, Locale.getDefault(), true)
        val message = NdefMessage(arrayOf(record))

        // If tag already formatted as NDEF
        Ndef.get(tag)?.let { ndef ->
            return try {
                ndef.connect()
                if (!ndef.isWritable) return false
                if (ndef.maxSize < message.toByteArray().size) return false
                ndef.writeNdefMessage(message)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                try { ndef.close() } catch (_: Exception) {}
            }
        }

        // Try to format as NDEF
        NdefFormatable.get(tag)?.let { formatable ->
            return try {
                formatable.connect()
                formatable.format(message)
                true
            } catch (_: Exception) {
                false
            } finally {
                try { formatable.close() } catch (_: Exception) {}
            }
        }

        return false
    }

    private fun parseFirstTextRecord(message: NdefMessage): String? {
        for (record in message.records) {
            if (record.tnf == NdefRecord.TNF_WELL_KNOWN && record.type.contentEquals(NdefRecord.RTD_TEXT)) {
                val payload = record.payload
                if (payload.isEmpty()) continue
                val status = payload[0].toInt()
                val isUtf16 = (status and 0x80) != 0
                val langLength = status and 0x3F
                val textBytes = payload.copyOfRange(1 + langLength, payload.size)
                val charset = if (isUtf16) Charsets.UTF_16 else Charsets.UTF_8
                return String(textBytes, charset)
            }
        }
        return null
    }

    private fun createTextRecord(text: String, locale: Locale, encodeInUtf8: Boolean): NdefRecord {
        val langBytes = locale.language.toByteArray(Charsets.US_ASCII)
        val textBytes = text.toByteArray(if (encodeInUtf8) Charsets.UTF_8 else Charsets.UTF_16)
        val utfBit = if (encodeInUtf8) 0 else 0x80
        val status = (utfBit + langBytes.size).toByte()
        val payload = ByteArray(1 + langBytes.size + textBytes.size)
        payload[0] = status
        System.arraycopy(langBytes, 0, payload, 1, langBytes.size)
        System.arraycopy(textBytes, 0, payload, 1 + langBytes.size, textBytes.size)
        return NdefRecord(
            NdefRecord.TNF_WELL_KNOWN,
            NdefRecord.RTD_TEXT,
            ByteArray(0),
            payload
        )
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
