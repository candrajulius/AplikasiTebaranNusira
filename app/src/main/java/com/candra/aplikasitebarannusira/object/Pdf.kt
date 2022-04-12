package com.candra.aplikasitebarannusira.`object`

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
@Suppress("DEPRECATION")
@SuppressLint("ObsoleteSdkInt")
object Pdf{
    fun cetakPdf(namePath: String,lokasi: String,gambar: Bitmap,temuan: String,namaPenemu: String,nikPenemu: String,bagianPenemu: String,button: MaterialButton
    ,context: Context){
        val path: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()

        val simpleDateFormat = SimpleDateFormat("EEEE,dd-MMM-yyyy",Locale.getDefault()).format(
            Date()
        )

        val dateNow = simpleDateFormat

        val file = File(path,"$dateNow $namePath.pdf")

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(0f,0f,0f,0f)

        val waktu = SimpleDateFormat("HH:mm:ss a",Locale.getDefault()).format(Date())

        val paragraphTitle = Paragraph(namePath).setBold().setFontSize(24F).setTextAlignment(
            TextAlignment.LEFT
        )

       val dateAndTime = Paragraph("Dibuat pada tanggal :$dateNow dan waktu: $waktu").setFontSize(18F).setTextAlignment(
           TextAlignment.LEFT)

        val width = floatArrayOf(100F,100F,100F,100F,100F,100F)
        val tableData = Table(width)

        tableData.addCell(Cell().add(Paragraph("Lokasi")))
        tableData.addCell(Cell().add(Paragraph("Foto Temuan")))
        tableData.addCell(Cell().add(Paragraph("Temuan")))
        tableData.addCell(Cell().add(Paragraph("Nama Penemu")))
        tableData.addCell(Cell().add(Paragraph("Nik Penemu")))
        tableData.addCell(Cell().add(Paragraph("Bagian Penemu")))

        tableData.addCell(Cell().add(Paragraph(lokasi)))
        val stream = ByteArrayOutputStream()
        gambar.compress(Bitmap.CompressFormat.PNG,100,stream)
        val byte = stream.toByteArray()

        val imageData = ImageDataFactory.create(byte)
        val image = Image(imageData).setHorizontalAlignment(HorizontalAlignment.CENTER).setWidth(150F).setHeight(150F)
        tableData.addCell(Cell().add(image))
        tableData.addCell(Cell().add(Paragraph(temuan)))
        tableData.addCell(Cell().add(Paragraph(namaPenemu)))
        tableData.addCell(Cell().add(Paragraph(nikPenemu)))
        tableData.addCell(Cell().add(Paragraph(bagianPenemu)))

        document.add(paragraphTitle)
        document.add(dateAndTime)
        document.add(tableData)

        document.close()

        Animation.showDialog(context = context,file.toString())
    }

    fun sharePdf(namePath: String, context: Context){
        val simpelDateFormat = SimpleDateFormat("EEEE,dd-MMM-yyyy", Locale.getDefault()).format(
            Date()
        )
        val dateNow: String = simpelDateFormat
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path,"$dateNow $namePath.pdf")

       val pdfUri: Uri?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            pdfUri = FileProvider.getUriForFile(context,context.packageName + ".provider",file)
        }else{
            pdfUri = Uri.fromFile(file)
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM,pdfUri)
            setPackage("com.whatsapp")
        }
        context.startActivity(shareIntent)
    }
}