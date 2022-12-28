package allianceone.prog.leanmanagementsystem.core

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.vectorToBitmap(drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun TextInputLayout.markRequiredInRed() {
    hint = buildSpannedString {
        append(hint)
        color(Color.RED) { append(" *") }
    }
}

fun TextInputLayout.disableErrorMessage() {
    this.error = null
    this.isErrorEnabled = false
}

fun EditText.disableErrorMessageChanged() {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (parent is TextInputLayout) {
                (parent as TextInputLayout).disableErrorMessage()
            }
        }

        override fun afterTextChanged(editable: Editable?) {
        }
    })
}

fun EditText.hasText(): Boolean {
    if (this.text.isNotEmpty()) {
        return true
    }
    return false
}

fun EditText.getValue(): String {
    return this.text.toString()
}

fun View.reveal() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun String.longToTime(time: Long, format: String): String {
    val date = Date(time)
    val formatter = SimpleDateFormat(format, Locale.ROOT)
    return formatter.format(date)
}

fun convertLongToTime(time: Long, format: String = "dd/MM/yyyy"): String {
    val date = Date(time)
    val formatter = SimpleDateFormat(format, Locale.ROOT)
    return formatter.format(date)
}

