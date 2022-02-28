package hs.aalen.infona.data.databases

import hs.aalen.infona.data.collections.Topic
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("InfonaDatabase")
private val topics = database.getCollection<Topic>()

suspend fun getTopics(subject_id:String):List<Topic>{
    return topics.collection.find(Topic::belongs_to_subject eq subject_id).toList()
}