
package com.hank121314.hankchen.androidproject.Stream

import java.net.HttpURLConnection
import java.net.URL
import android.graphics.Bitmap
import com.hank121314.hankchen.androidproject.services.socket
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import org.jetbrains.anko.doAsync
import java.io.*




/**
 * Created by hankchen on 2017/12/26.
 */
class imageUploaderUser(img: Bitmap, filename:String): ObservableOnSubscribe<String> {
    var attachmentName = "file"
    var attachmentFileName = "${filename}.jpeg"
    var crlf = "\r\n"
    var twoHyphens = "--"
    var boundary = "*****"
    val file= img
    val updateServer= URL("${socket.server_siete}/upload/user")
    val connection=updateServer.openConnection() as HttpURLConnection

    override fun subscribe(e: ObservableEmitter<String>) {
        connection.setUseCaches(false)
        connection.setDoOutput(true)
        connection.setRequestMethod("POST")
        connection.setRequestProperty("Connection", "Keep-Alive")
        connection.setRequestProperty("Cache-Control", "no-cache")
        connection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + this.boundary)
        doAsync{
            val request = DataOutputStream(
                    connection.getOutputStream())
            request.writeBytes(twoHyphens + boundary + crlf)
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" +crlf)
            request.writeBytes(crlf)
            val byteArrayOutputStream = ByteArrayOutputStream()
            file.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            request.write(byteArray)
            request.writeBytes(crlf)
            request.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf)
            val totalSize = byteArray.size
            var bytesTransferred = 0
            val chunkSize = 2000
            while (bytesTransferred < totalSize) {
                var nextChunkSize = totalSize - bytesTransferred
                if (nextChunkSize > chunkSize) {
                    nextChunkSize = chunkSize
                }
                bytesTransferred += nextChunkSize
                // Here you can call the method which updates progress
                // be sure to wrap it so UI-updates are done on the main thread!
                e.onNext((100 * bytesTransferred / totalSize).toString())
            }
            request.flush()
            request.close()
            val responseStream = BufferedInputStream(connection.inputStream)
            val responseStreamReader = BufferedReader(InputStreamReader(responseStream))
            val stringBuilder = StringBuilder()
            responseStreamReader.lines().forEach {s-> stringBuilder.append(s).append("\n") }
            responseStreamReader.close()
            val response = stringBuilder.toString()
            responseStream.close()
            connection.disconnect()
            e.onNext(response)
        }
    }

}