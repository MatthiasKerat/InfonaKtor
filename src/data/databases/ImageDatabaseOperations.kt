package hs.aalen.infona.data.databases

import com.mongodb.client.gridfs.GridFSBuckets
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.KMongo
import org.litote.kmongo.bson
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.json
import java.io.InputStream
import java.nio.file.Files

private val client = KMongo.createClient()
private val database = client.getDatabase("InfonaDatabase")
private val bucket = GridFSBuckets.create(database,"file")


suspend fun insertImage(fileName:String,inputStream: InputStream):String{
    return bucket.uploadFromStream(fileName,inputStream).toString()
}

suspend fun getImage(object_id:String):String{
    val downloadStream = bucket.openDownloadStream(ObjectId(object_id))
    val length = downloadStream.gridFSFile.length
    val byteArray = byteArrayOf(elements = ByteArray(length.toInt()))
    downloadStream.read(byteArray)
    return byteArray.json
}

suspend fun deleteImage(object_id:String){
    try{
        bucket.delete(ObjectId(object_id))
    }catch(e:Exception){
        //
    }
}