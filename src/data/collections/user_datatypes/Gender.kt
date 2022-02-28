package hs.aalen.infona.data.collections.user_datatypes

sealed class Gender(val description:String){
    object Male: Gender("Male")
    object Female: Gender("Female")
}
