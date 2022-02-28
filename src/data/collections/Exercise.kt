package hs.aalen.infona.data.collections

import hs.aalen.infona.data.collections.exercise_datatypes.ExerciseInfo
import hs.aalen.infona.data.collections.exercise_datatypes.Loesung
import hs.aalen.infona.data.collections.exercise_datatypes.Tipp

data class Exercise(
    val _id:String,
    val name:String,
    val description:String,
    val picture_url:String,
    val musterLoesung:Loesung,
    val exerciseInfo:ExerciseInfo,
    val tipps:List<Tipp>,
    val coin_reward:Int,
    val belongs_to_topic:String
)
