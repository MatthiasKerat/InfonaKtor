package hs.aalen.infona.data

import hs.aalen.infona.common.generateValidateEmailCode
import hs.aalen.infona.data.collections.ResetCode
import hs.aalen.infona.data.collections.User
import hs.aalen.infona.data.collections.VerificationCode
import hs.aalen.infona.data.user_database.checkIfUserExists
import hs.aalen.infona.data.user_database.setUserValidationState
import hs.aalen.infona.security.checkHashForPassword
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("InfonaDatabase")
private val users = database.getCollection<User>()
private val resetCodes = database.getCollection<ResetCode>()
private val verificationCodes = database.getCollection<VerificationCode>()

suspend fun checkPasswordForEmail(email:String, passwordToCheck:String):Boolean{
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return checkHashForPassword(passwordToCheck,actualPassword)
}

suspend fun registerUser(user:User):Boolean{
    return users.insertOne(user).wasAcknowledged()
}

suspend fun resetPasswordForUser(email:String, password:String):Boolean{
    if(!checkIfUserExists(email)){
        return false
    }
    return users.updateOne(User::email eq email, setValue(User::password,password)).wasAcknowledged()
}

suspend fun insertResetCode(code:String, email:String):Boolean{
    return resetCodes.insertOne(ResetCode(code = code, email = email)).wasAcknowledged()
}

suspend fun insertValidationCode(code:String, email:String):Boolean{
    return verificationCodes.insertOne(VerificationCode(code = code, email = email)).wasAcknowledged()
}

suspend fun getVerificationCode(email:String):String{
    //Check if there is a code
    val code = verificationCodes.findOne(VerificationCode::email eq email)
    code?.let { validationCode ->
        return validationCode.code
    }
    return generateValidateEmailCode(6,email)
}

suspend fun verifyResetCode(code:String, email:String):Boolean{
    val resetCode = resetCodes.findOne(ResetCode::code eq code)
    resetCode?.let {
        if(resetCode.email == email){
            deleteResetCode(code)
            return true
        }
    }
    return false
}

suspend fun validateCode(code:String, email:String):Boolean{
    val verificationCode = verificationCodes.findOne(VerificationCode::code eq code)
    verificationCode?.let {
        if(verificationCode.email == email){
            setUserValidationState(email,true)
            deleteValidationCode(code)
            return true
        }
    }
    return false
}

suspend fun deleteResetCode(code:String):Boolean{
    return resetCodes.deleteOne(ResetCode::code eq code).wasAcknowledged()
}

suspend fun deleteValidationCode(code:String):Boolean{
    return verificationCodes.deleteOne(VerificationCode::code eq code).wasAcknowledged()
}