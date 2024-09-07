package org.mavriksc.publishtests

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.ListObjectsRequest
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Publisher(val s3Client: S3Client) {

    @Value("\${github.ref_name}")
    val branch: String? = null

    @Value("\${github.repository}")
    val repository: String? = null

    @Value("\${path.tests}")
    val testsPath: String? = null

    @Value("\${bucket.name}")
    val bucketName: String? = null

    @Value("\${publisher.setup:false}")
    val setup: Boolean? = null

    fun publish() {
        if (setup!!) {
            println("setting up ...")
        }
        println("pushing tests on $repository/$branch  from Dir: $testsPath")
        println("fetching app/branch names")
        println("push app.json")
        updateAppsDotJSON()
    }

    private fun updateAppsDotJSON() {
        val request = ListObjectsRequest {
            bucket = bucketName
            prefix = "reports"
        }
        //val appMap =
        runBlocking {
            val response = s3Client.listObjects(request)
            response.contents?.forEach { myObject ->
                println("The name of the key is ${myObject.key}")
                println("The owner is ${myObject.owner}")
            }
        }
    }
}
