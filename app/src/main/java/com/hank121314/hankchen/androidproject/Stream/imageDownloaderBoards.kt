package com.hank121314.hankchen.androidproject.Stream

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import com.github.nkzawa.global.Global.encodeURIComponent
import com.hank121314.hankchen.androidproject.services.socket
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import java.io.*
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Created by hankchen on 2017/12/26.
 */
class imageDownloaderBoards(activity:AppCompatActivity, fileName:String):ObservableOnSubscribe<String> {
    val str =fileName.split("-")
    val self=activity
    var attachmentFileName =encodeURIComponent(str[0])
    var boundary = "*****"
    val downloadServer= URL("${socket.server_siete}/download/boardsPhoto")
    val connection=downloadServer.openConnection() as HttpURLConnection
    lateinit var bmp:Bitmap
    override fun subscribe(e: ObservableEmitter<String>) {
        connection.setUseCaches(false)
        connection.requestMethod="GET"
        connection.setRequestProperty("Connection", "Keep-Alive")
        connection.setRequestProperty("Cache-Control", "no-cache")
        connection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + this.boundary)
        connection.setRequestProperty("boards",attachmentFileName)
        doAsync{
            connection.setRequestProperty("touch",false.toString())
            val serverFile = BufferedInputStream(connection.inputStream)
            if (connection.responseCode >= HTTP_BAD_REQUEST) {
                e.onError(throw FileNotFoundException(downloadServer.toString()));
            }
            val responseStreamReader = BufferedReader(InputStreamReader(serverFile))
            val stringBuilder = StringBuilder()
            responseStreamReader.lines().forEach {s-> stringBuilder.append(s).append("\n") }
            responseStreamReader.close()
            val serverFileName = stringBuilder.toString()
            serverFile.close()
            val error =  connection.errorStream
            connection.disconnect()
            val localFile = File("${self.filesDir.absoluteFile}/boardsImage/${serverFileName}.jpeg").exists()
            if(localFile==true) {
                onComplete {
                    e.onNext("${self.filesDir.absoluteFile}/boardsImage/${serverFileName}.jpeg")
                }
            }
            else {
                val connection2=downloadServer.openConnection() as HttpURLConnection
                connection2.setUseCaches(false)
                connection2.requestMethod="GET"
                connection2.setRequestProperty("Connection", "Keep-Alive")
                connection2.setRequestProperty("Cache-Control", "no-cache")
                connection2.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary)
                connection2.setRequestProperty("boards",attachmentFileName)
                connection2.setRequestProperty("touch",true.toString())
                val responseStream = BufferedInputStream(connection2.inputStream)
                val response = responseStream.readBytes()
                responseStream.close()
                connection.disconnect()
                val bmp = BitmapFactory.decodeByteArray(response, 0, response.size)
                val dir = File("${self.filesDir.absolutePath}/boardsImage/")
                if (!dir.exists()) {
                    dir.mkdir()
                }
                val fileOut = FileOutputStream("${self.filesDir.absoluteFile}/boardsImage/${serverFileName}.jpeg")
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOut)
                connection2.disconnect()
                onComplete {
                    e.onNext("${self.filesDir.absoluteFile}/boardsImage/${serverFileName}.jpeg")
                }
            }
        }
    }
}