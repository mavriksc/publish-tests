package org.mavriksc.publishtests

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.ListObjectsRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectResponse
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.asByteStream
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files


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
            pushStaticFiles()
        }
        println("pushing tests on $repository/$branch  from Dir: $testsPath")
        pushTestResults()
        updateAppsDotJSON()
    }

    private fun pushTestResults() = pushDir(
        File(testsPath!!),
        "reports/${repository!!.split("/")[1]}/$branch/"
    )

    private fun pushStaticFiles() = pushDir(File("static-files"))

    private fun pushDir(dir: File, prefix: String = "") {
        runBlocking {
            dir.walk()
                .filter { it.isFile }
                .forEach { file ->
                    launch { putFile("$prefix${file.relativeTo(dir)}".replace("\\", "/"), file) }
                }
        }
    }

    private suspend fun putFile(destination: String, src: File): PutObjectResponse {
        val bs = src.asByteStream()
        val put = PutObjectRequest {
            bucket = bucketName
            key = destination
            body = bs
            contentType = Files.probeContentType(src.toPath())
            contentLength = bs.contentLength
        }
        return s3Client.putObject(put)
    }

    private fun updateAppsDotJSON() {
        println("fetching app/branch names")
        val request = ListObjectsRequest {
            bucket = bucketName
            prefix = "reports"
        }
        val appMap = runBlocking { s3Client.listObjects(request) }
            .contents!!.map {
                val pathSlugs = it.key?.split("/")!!
                Pair(pathSlugs[1], pathSlugs[2])
            }.groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .mapValues { it.value.toSet() }
        val json = Json.encodeToString(appMap)
        println("push apps.json")
        val put = PutObjectRequest {
            bucket = bucketName
            key = "apps.json"
            body = ByteStream.fromString(json)
        }
        runBlocking { s3Client.putObject(put) }
    }
}
