package org.mavriksc.publishtests

import aws.sdk.kotlin.services.s3.S3Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageService {

    @Bean
    fun s3Client() = S3Client.builder().apply { config.region ="us-east-1" }.build()


}
