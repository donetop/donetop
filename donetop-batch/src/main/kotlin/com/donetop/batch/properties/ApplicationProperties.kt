package com.donetop.batch.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@ConfigurationProperties(prefix = "application.properties")
@ConstructorBinding
data class ApplicationProperties(
	val storage: Storage,
	val donetopPHP: DonetopPHP,
	val collectPassword: String
) {
	init {
	    require(collectPassword.isNotEmpty()) { "collect password shouldn't be empty." }
	}
}

data class Storage(
	val root: String,
	val tmp: String
) {
	init {
		require(root.isNotEmpty()) { "root shouldn't be empty." }
	    require(tmp.isNotEmpty()) { "tmp shouldn't be empty." }
		Files.createDirectories(Path.of(tmp))
	}

	fun clearTmpDir() {
		Files.walk(Paths.get(tmp))
			.filter { Files.isRegularFile(it) }
			.map { it.toFile() }
			.forEach { it.delete() }
	}
}

data class DonetopPHP(
	val url: Url,
	val admin: Admin
) {}

data class Url (
	val base: String,
	val board: String,
	val login: String
) {
	init {
		require(base.isNotEmpty()) { "base shouldn't be empty." }
		require(board.isNotEmpty()) { "board shouldn't be empty." }
		require(login.isNotEmpty()) { "login shouldn't be empty." }
	}
}

data class Admin(
	val id: String,
	val password: String
) {
	init {
	    require(id.isNotEmpty()) { "id shouldn't be empty." }
		require(password.isNotEmpty()) { "password shouldn't be empty." }
	}
}
