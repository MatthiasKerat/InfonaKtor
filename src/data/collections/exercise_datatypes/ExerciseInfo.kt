package hs.aalen.infona.data.collections.exercise_datatypes

data class ExerciseInfo (
    val exerciseTyp: String,
    val selectionPossibilities:List<String> = listOf()
)