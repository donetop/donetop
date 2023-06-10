package com.donetop

import com.donetop.collect.properties.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class DonetopBatchApplication

fun main(args: Array<String>) {
	runApplication<DonetopBatchApplication>(*args)
}
