package hs.aalen.infona.data.user_database

import hs.aalen.infona.data.collections.User
import org.litote.kmongo.SetTo
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.set
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("InfonaDatabase")
private val users = database.getCollection<User>()

suspend fun setUserValidationState(email:String,status:Boolean):Boolean{
    return users.updateOne(User::email eq email, setValue(User::validated,status)).wasAcknowledged()
}

suspend fun setUserProfilePictureUrl(userId:String, pictureUrl:String):Boolean{
    return users.updateOne(User::_id eq userId, setValue(User::profile_picture_url,pictureUrl)).wasAcknowledged()
}

suspend fun checkIfUserExists(email:String):Boolean{
    return users.findOne(
        User::email eq email
    ) != null
}

suspend fun getUserByEmail(email:String):User?{
    return users.findOne(User::email eq email)
}

suspend fun getUserById(id:String):User?{
    return users.findOne(User::_id eq id)
}

suspend fun getUsers():List<User>{
    return users.collection.find().toList()
}

suspend fun updateUserData(user:User):Boolean{

    val userToUpdate = getUserByEmail(user.email)

    return if(userToUpdate!=null){
        val setToName = SetTo(User::name,user.name)
        val setToProfilPicutreUrl = SetTo(User::profile_picture_url,user.profile_picture_url)
        val setToGender = SetTo(User::gender, user.gender)
        val setToBirthday = SetTo(User::birthday, user.birthday)
        val setToAverageGrade = SetTo(User::averageGrade,user.averageGrade)
        val setToUniversity = SetTo(User::university,user.university)
        val setToPrivacy = SetTo(User::privacy,user.privacy)
        val setToCoins = SetTo(User::coins,user.coins)
        val setToDoneExercises = SetTo(User::doneExercises,user.doneExercises)
        val setToBoughtTips = SetTo(User::boughtTips,user.boughtTips)
        users.updateOneById(userToUpdate._id,set(setToName, setToProfilPicutreUrl,setToGender,setToBirthday,setToAverageGrade,setToUniversity,setToPrivacy,setToCoins,setToDoneExercises,setToBoughtTips)).wasAcknowledged()
    }else{
        return users.insertOne(user).wasAcknowledged()
    }
}