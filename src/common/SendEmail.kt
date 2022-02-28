package hs.aalen.infona.common

import hs.aalen.infona.data.insertResetCode
import hs.aalen.infona.data.insertValidationCode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

fun sendEmail(toEmail:String, subject:String, message:String){

    val email = SimpleEmail()
    email.hostName = "smtp.googlemail.com"
    email.setSmtpPort(587)
    email.setAuthenticator(DefaultAuthenticator("matthiaskerat1996@gmail.com", "MatthiasGoogleApp12091996"))
    email.setFrom("Matthiaskerat1996@gmail.com")
    email.isStartTLSEnabled = true
    email.subject = subject
    email.setMsg(message)
    email.addTo(toEmail)
    email.send()

}

fun generateResetPasswordCode(length:Int, email: String):String{
    val code = generateRandomCode(length)
    GlobalScope.launch {
        insertResetCode(code, email)
    }
    return code
}

fun generateValidateEmailCode(length: Int, email:String):String{
    val code = generateRandomCode(length)
    GlobalScope.launch {
        insertValidationCode(code,email)
    }
    return code
}

fun generateRandomCode(length: Int):String{
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}