plugins {
	id("java-library")
	alias(libs.plugins.jetbrainsKotlinJvm)
	kotlin("plugin.serialization") version "1.9.21"
	id("maven-publish")
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
	implementation(libs.okHttp)
	implementation(libs.kotlinx.serialization.json)
}

publishing {

	publications {
		create<MavenPublication>("release") {
			groupId = "com.trendyol.mockingstar"
			artifactId = "mockingstar"
			version = "1.0.1"
			afterEvaluate {
				from(components["java"])
			}
		}
	}
}
