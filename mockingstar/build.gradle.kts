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

	repositories {
		maven {
			val releasesRepoUrl = layout.buildDirectory.dir("repos/releases")
			val snapshotsRepoUrl = layout.buildDirectory.dir("repos/snapshots")
			url = uri(
				if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
			)
		}
	}
	publications {
		create<MavenPublication>("release") {
			groupId = "com.trendyol.mockingstar"
			artifactId = "mockingstar"
			version = "1.0.0"
			afterEvaluate {
				from(components["java"])
			}
		}
	}
}
