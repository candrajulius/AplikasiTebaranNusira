package com.candra.aplikasitebarannusira.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.candra.aplikasitebarannusira.R
import com.candra.aplikasitebarannusira.`object`.Animation
import com.candra.aplikasitebarannusira.databinding.ActivityMainBinding
import com.candra.aplikasitebarannusira.helper.POSITION
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlin.system.exitProcess

class MainUtama: AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickItemCardView()

        supportActionBar?.hide()

        checkSelfPermission()



        binding.apply {
            Animation.playAnimationContentMain(
                tindakanBahaya,
                kondisiBahaya,
                pencemaran,
                titleText,
                temu5R
            )
        }
    }

    private fun setClickItemCardView() {

        with(binding){
            temu5R.setOnClickListener {
                Intent(this@MainUtama,InputData::class.java).apply {
                    putExtra(POSITION,4)
                }.also { startActivity(it,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainUtama).toBundle()) }
            }
            tindakanBahaya.setOnClickListener {
                val intent = Intent(this@MainUtama, InputData::class.java)
                intent.putExtra(POSITION, 1)
                startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainUtama).toBundle())
            }
            kondisiBahaya.setOnClickListener {
                val intent = Intent(this@MainUtama, InputData::class.java)
                intent.putExtra(POSITION, 2)
                startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainUtama).toBundle())
            }
            pencemaran.setOnClickListener {
                val intent = Intent(this@MainUtama, InputData::class.java)
                intent.putExtra(POSITION, 3)
                startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainUtama).toBundle())
            }
        }

    }

    private fun checkSelfPermission(){
        Dexter.withContext(this@MainUtama)
            .withPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(
                object: MultiplePermissionsListener{
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        if (p0?.areAllPermissionsGranted() == true){
                            Toast.makeText(this@MainUtama,getString(R.string.permission_acccess),Toast.LENGTH_SHORT).show()
                        }else{
                            showDialogPermissionGranted()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }

                }
            ).onSameThread().check()
    }

    fun showDialogPermissionGranted() {
        AlertDialog.Builder(this@MainUtama)
            .setMessage("Aplikasi ini membutuhkan fitur perizinan dari sistem android anda" +
                    "Jika anda tidak mengaktifkan fiturnya maka aplikasi tidak dapat digunakan" +
                    "SIlahkan tekan tombol Peri Ke Setting untuk mengaktifkan perizinan")
            .setTitle("Peringatan")
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton(getString(R.string.go_setting)){_,_ ->

                try {
                    intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                    Log.d("Input Data", "showDialogPermissionGranted: " + e.message.toString())
                }

            }
            .setNegativeButton(getString(R.string.cancel)){dialog,_ ->
                dialog.dismiss()
                exitProcess(0)
            }.show()
    }

}