package com.candra.aplikasitebarannusira.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import coil.load
import com.candra.aplikasitebarannusira.R
import com.candra.aplikasitebarannusira.`object`.Pdf
import com.candra.aplikasitebarannusira.databinding.InputLayoutBinding
import com.candra.aplikasitebarannusira.helper.POSITION
import com.candra.aplikasitebarannusira.helper.createCustomTemptFile
import com.candra.aplikasitebarannusira.helper.formatDate
import com.candra.aplikasitebarannusira.helper.uriToFile
import java.io.File


class InputData: AppCompatActivity()
{

    private lateinit var binding: InputLayoutBinding

    private var getFile: File? = null
    private lateinit var currentPhoto: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InputLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnKirim.isEnabled = true



        binding.apply {
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
                onBackPressed()
            }
            btnKonfirmasi.setOnClickListener {
                setClikcConfirmation()
            }
            tanggalSkrng.text = formatDate
        }


        setToolbar()

    }

    private fun setActivateCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            resolveActivity(packageManager)
        }

        createCustomTemptFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this@InputData,
                "$packageName.provider",
                it
            )

            currentPhoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun setActivateGallery(){
       val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
       launcherIntentGallery.launch(intent)
    }

    private fun setClikcConfirmation(){
        with(binding){
            val inputLokasi = inputLokasi.text.toString()
            val inputTemuan = inputTemuan.text.toString()
            val inputNama = namaPenemu.text.toString()
            val nikPenemu = nikPenemu.text.toString()
            val bagian = inputBagianPenemu.text.toString()
            val isianTemuanCategory = inputIsianKategori.text.toString()

            val path = getFile?.absolutePath

            val bitmap = BitmapFactory.decodeFile(path)

            if (inputLokasi.isEmpty() || inputTemuan.isEmpty() || inputNama.isEmpty() || nikPenemu.isEmpty() ||
                bagian.isEmpty() || getFile == null || isianTemuanCategory.isEmpty())
            {
                Toast.makeText(this@InputData,getString(R.string.mohon_isi_inputan_dengan_benar),Toast.LENGTH_SHORT).show()
            }else{
                intent.getIntExtra(POSITION,0).let{
                    when (it) {
                        1 -> {
                            Pdf.cetakPdf(binding.titleInput.text.toString(),inputLokasi,bitmap,inputTemuan,inputNama,nikPenemu,bagian,this@InputData,
                                getString(R.string.isian_kategori),isianTemuanCategory)
                            binding.btnKirim.isEnabled = true
                        }
                        2 -> {
                            Pdf.cetakPdf(binding.titleInput.text.toString(),inputLokasi,bitmap,inputTemuan,inputNama,nikPenemu,bagian,this@InputData,
                                getString(R.string.isian_kategori),isianTemuanCategory)
                            binding.btnKirim.isEnabled = true
                        }
                        3 -> {
                            Pdf.cetakPdf(binding.titleInput.text.toString(),inputLokasi,bitmap,inputTemuan,inputNama,nikPenemu,bagian,this@InputData,
                                getString(R.string.isian_kategori),isianTemuanCategory)
                            binding.btnKirim.isEnabled = true
                        }
                    }
                }
            }
        }

    }

    private fun setToolbar(){
        val position = intent.getIntExtra(POSITION,0)
        binding.apply {
            when (position) {
                1 -> {
                    imgTitle.setImageResource(R.drawable.tindakan_bahaya)
                    titleInput.text = resources.getString(R.string.tindakan_bahaya)
                }
                2 -> {
                    imgTitle.setImageResource(R.drawable.kondisi_bahaya)
                    titleInput.text = resources.getString(R.string.kondisi_bahaya)
                }
                3 -> {
                    imgTitle.setImageResource(R.drawable.pencemaran)
                    titleInput.text = resources.getString(R.string.pencemaran)
                }
                4 -> {
                    imgTitle.setImageResource(R.drawable.menu_5_r)
                    titleInput.text = getString(R.string.temu_5_r)
                }
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhoto)

            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)

            binding.imagePenemu.load(result){
                crossfade(true)
                crossfade(600)
                placeholder(R.drawable.ic_baseline_broken_image_24)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImg = it.data?.data as Uri

            val myFile = uriToFile(selectedImg,this@InputData)

            getFile = myFile

            binding.imagePenemu.load(selectedImg){
                crossfade(true)
                crossfade(600)
                placeholder(R.drawable.ic_baseline_broken_image_24)
            }
        }
    }

}