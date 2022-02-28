package hs.aalen.infona.data.databases

import hs.aalen.infona.data.collections.Exercise
import hs.aalen.infona.data.collections.Topic
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("InfonaDatabase")
private val exercises = database.getCollection<Exercise>()

suspend fun getExercises(topic_id:String):List<Exercise>{
    return exercises.collection.find(Exercise::belongs_to_topic eq topic_id).toList()
}

suspend fun getAllExercises():List<Exercise>{
    return exercises.collection.find().toList()
}