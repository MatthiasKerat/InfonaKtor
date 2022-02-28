package hs.aalen.infona.routes

import hs.aalen.infona.data.databases.deleteImage
import hs.aalen.infona.data.databases.getImage
import hs.aalen.infona.data.databases.insertImage
import hs.aalen.infona.data.response.ImageUploadResponse
import hs.aalen.infona.data.user_database.getUserById
import hs.aalen.infona.data.user_database.setUserProfilePictureUrl
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.json

fun Route.imageRoute(){
    route("image/{user_id}"){
        //authenticate("auth-jwt"){
        post{
            val userId= call.parameters["user_id"]
            if(userId != null){
                val multipartData = call.receiveMultipart()
                multipartData.forEachPart { part ->
                    if(part is PartData.FileItem){
                        val fileName = part.originalFileName as String
                        var objectId = ""
                        withContext(Dispatchers.IO){
                            objectId = insertImage(fileName, part.streamProvider())

                            //Altes Foto löschen
                            val user = getUserById(userId)
                            deleteImage(user?.profile_picture_url?:"")

                            //Http-Antwort
                            call.respond(HttpStatusCode.OK, ImageUploadResponse(objectId).json)

                        }
                    }
                }
            }
        }
        //}
    }

    route("image/{object_id}"){
        get{
            val objectId = call.parameters["object_id"]
            if(objectId!=null){
                val imageData = getImage(objectId)
                call.respond(HttpStatusCode.OK,imageData)
            }
        }
    }
}