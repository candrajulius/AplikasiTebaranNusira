package com.candra.aplikasitebarannusira.helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val FORMAT_DATE_AND_TIME = "EEEE,dd-MMMM-yyyy HH:mm:ss a"

const val POSITION = "position"

val timeStampt: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

val formatDate: String = SimpleDateFormat(
    FORMAT_DATE_AND_TIME,
    Locale.getDefault()
).format(Date())

fun createCustomTemptFile(context: Context): File{
    val storage: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStampt,".jpg",storage)
}



fun uriToFile(selectImg: Uri,context: Context): File{
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTemptFile(context)

    val inputStream = contentResolver.openInputStream(selectImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while(inputStream.read(buf).also { len = it } > 0)outputStream.write(buf,0,len)
    outputStream.close()
    inputStream.close()

    return myFile
}