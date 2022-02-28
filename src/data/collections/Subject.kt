package hs.aalen.infona.data.collections

data class Subject(
    val _id:String,
    val name:String,
    val description:String,
    val suitable_for:String,
    val related_subjects:List<String>,
    val required_basics:String
)
