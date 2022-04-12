package com.candra.aplikasitebarannusira.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Explode
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.candra.aplikasitebarannusira.R
import com.candra.aplikasitebarannusira.`object`.Pdf
import com.candra.aplikasitebarannusira.databinding.InputLayoutBinding

@Suppress("DEPRECATION")
class InputData: AppCompatActivity()
{

    companion object{
        const val REQUEST_CODE_CAMERA = 1
        const val REQUEST_CODE_GALLERY = 2
    }

    private lateinit var binding: InputLayoutBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InputLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnKirim.isEnabled = true

        binding.apply {
            if (!btnKirim.isEnabled){
                btnKirim.setBackgroundColor(R.color.colorDisable)
            }else if(btnKirim.isEnabled) {
                btnKirim.setBackgroundColor(R.color.colorPrimary)
            }
            btnCamera.setOnClickListener {
                setActivateCamera()
            }
            btnGallery.setOnClickListener {
                setActivateGallery()
            }
            btnKirim.setOnClickListener {
                Pdf.sharePdf(binding.titleInput.text.toString(),this@InputData)
            }
            backArrow.setOnClickListener {
                finish()
            }
        }
        setToolbar()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_CAMERA ->{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.apply {
                        imagePenemu.load(bitmap){
                            crossfade(true)
                            crossfade(1000)
                            placeholder(R.drawable.ic_baseline_image_24)
                            error(R.drawable.ic_baseline_broken_image_24)
                        }
                        btnKonfirmasi.setOnClickListener {
                            setClikcConfirmation(bitmap)
                        }
                    }
                }
                REQUEST_CODE_GALLERY -> {
                    val uri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
                    binding.apply {
                        imagePenemu.load(bitmap){
                            crossfade(true)
                            crossfade(1000)
                            placeholder(R.drawable.ic_baseline_image_24)
                            error(R.drawable.ic_baseline_broken_image_24)
                        }
                        btnKonfirmasi.setOnClickListener {
                            setClikcConfirmation(bitmap)
                        }
                    }
                }
            }
        }
    }

    private fun setActivateCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun setActivateGallery(){
       val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
       startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun setClikcConfirmation(gambar: Bitmap){

        with(binding){
            val inputLokasi = inputLokasi.text.toString()
            val inputTemuan = inputTemuan.text.toString()
            val inputNama = namaPenemu.text.toString()
            val nikPenemu = nikPenemu.text.toString()
            val bagian = inputBagianPenemu.text.toString()

            if (inputLokasi.isEmpty() || inputTemuan.isEmpty() || inputNama.isEmpty() || nikPenemu.isEmpty() ||
                bagian.isEmpty())
            {
                Toast.makeText(this@InputData,"Mohon isi inputan data dengna benar!!",Toast.LENGTH_SHORT).show()
            }else{
                Pdf.cetakPdf(binding.titleInput.text.toString(),inputLokasi,gambar,inputTemuan,inputNama,nikPenemu,bagian,binding.btnKirim,this@InputData)
                binding.btnKirim.isEnabled = true
            }
        }

    }

    private fun setToolbar(){
        val position = intent.getIntExtra("position",0)
        binding.apply {
            if (position == 1){
                titleInput.text = resources.getString(R.string.tindakan_bahaya)
            }else if (position == 2){
                titleInput.text = resources.getString(R.string.kondisi_bahaya)
            }else if (position == 3){
                titleInput.text = resources.getString(R.string.pencemaran)
            }
        }
    }

}