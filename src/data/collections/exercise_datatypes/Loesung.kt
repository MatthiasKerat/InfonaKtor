package hs.aalen.infona.data.collections.exercise_datatypes

data class Loesung(
    val loesungsTyp: String,
    val loesungString:String = "",
    val loesungArray:List<String> = listOf(),
    val loesungMultipleChoice:List<Boolean> = listOf(),
    val loesungSingleChoice:Int = 0
)