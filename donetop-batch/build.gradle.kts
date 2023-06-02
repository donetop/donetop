val profile = rootProject.ext.get("profile") as String
val projectName = "batch"

plugins {
	kotlin("jvm") version "1.8.21"
	kotlin("plugin.spring") version "1.8.21"
}

dependencies {
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("com.mysql:mysql-connector-j:8.0.33")
	implementation("org.jsoup:jsoup:1.16.1")

	runtimeOnly("com.h2database:h2")

	implementation(project(":donetop-domain"))
	implementation(project(":donetop-dto"))
	implementation(project(":donetop-enum"))
	implementation(project(":donetop-common"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
	jvmToolchain(11)
	extra["kotlin.version"] = "1.8.21"
}

sourceSets {
	main {
		resources {
			srcDir("src/main/resources")
			srcDir("src/main/resources-$profile")
		}
	}
}

tasks.register<Copy>("copySubmoduleFiles") {
	from("../donetop-secret/$projectName/resources-$profile")
	include("*")
	into("src/main/resources-$profile")
}

tasks {
	processResources {
		dependsOn("copySubmoduleFiles")
	}
}