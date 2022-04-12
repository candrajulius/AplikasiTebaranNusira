package com.candra.aplikasitebarannusira.`object`

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.candra.aplikasitebarannusira.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

object Animation {

    fun playAnimationSplashScreen(image: ImageView){
        ObjectAnimator.ofFloat(image, View.TRANSLATION_X,-30f,30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    fun playAnimationContentMain(cardView: MaterialCardView,cardView2: MaterialCardView,cardView3: MaterialCardView,title: MaterialTextView){
        val tindakanBahaya = ObjectAnimator.ofFloat(cardView,View.ALPHA,1f).setDuration(500)
        val kondisiBahaya = ObjectAnimator.ofFloat(cardView2,View.ALPHA,1f).setDuration(500)
        val pencemaran = ObjectAnimator.ofFloat(cardView3,View.ALPHA,1f).setDuration(500)
        val title_menu = ObjectAnimator.ofFloat(title,View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title_menu,tindakanBahaya,kondisiBahaya,pencemaran)
            start()
        }
    }

    @SuppressLint("SetTextI18n")
    fun showDialog(context: Context, file: String){
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)

        val view = LayoutInflater.from(context).inflate(
            R.layout.thank_you_layout,
            null
        )
        builder.setView(view)

        val alertDialog = builder.create()

        with(view){
            findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
                alertDialog.dismiss()
            }

            findViewById<MaterialTextView>(R.id.textFolder).text = "File anda berada di: $file"
        }

        alertDialog.window?.let {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        alertDialog.show()

    }

}