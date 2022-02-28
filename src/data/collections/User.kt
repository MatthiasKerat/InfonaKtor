package hs.aalen.infona.data.collections

import hs.aalen.infona.data.collections.user_datatypes.Birthday
import hs.aalen.infona.data.collections.user_datatypes.BoughtTip
import hs.aalen.infona.data.collections.user_datatypes.Gender
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val _id:String,
    val email:String = "",
    val password: String = "",
    val name: String = "",
    val profile_picture_url:String = "",
    val gender:String = "Female",
    val birthday:Birthday = Birthday(12,6,1992),
    val averageGrade:Float = 3.0f,
    val university:String = "",
    val privacy:List<Boolean> = listOf(true,true,true),
    val coins:Int = 1500,
    val doneExercises:List<String> = listOf(),
    val boughtTips:List<BoughtTip> = listOf(),
    val validated: Boolean = false,
)
