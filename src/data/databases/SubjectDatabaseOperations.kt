package hs.aalen.infona.data.databases

import hs.aalen.infona.data.collections.Subject
import hs.aalen.infona.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("InfonaDatabase")
private val subjects = database.getCollection<Subject>()

suspend fun getSubjects():List<Subject>{
    return subjects.collection.find().toList()
}