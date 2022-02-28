package hs.aalen.infona.security

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

fun getHasWithSalt(stringToHash: String, saltLength: Int = 32): String{
    val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32)
    val saltAsHex = Hex.encodeHex(salt)
    val hash = DigestUtils.sha256Hex("$saltAsHex$stringToHash")
    return "$saltAsHex:$hash"
}

fun checkHashForPassword(password:String, hashWithSalt: String):Boolean{
    val hashAndSalt = hashWithSalt.split(":")
    val salt = hashAndSalt[0]
    val hash = hashAndSalt[1]
    val passwordHash = DigestUtils.sha256Hex("$salt$password")
    return hash == passwordHash
}