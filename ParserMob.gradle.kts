/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.IOException
import java.util.Properties

plugins {
    libs.plugins.apply {
        alias(libs.plugins.kotlinMultiplatform).apply(false)
        alias(libs.plugins.kover)
    }
    `maven-publish`
}

allprojects {

    apply(plugin = rootProject.libs.plugins.kover.get().pluginId).also {
        rootProject.dependencies.add("kover", project(path))
    }

    Library.apply {

        group = GROUP
        version = ProjectSettings.getVersionName(project)

        repositories {
            google()
            mavenCentral()
        }

        val emptyJavadocJar by tasks.registering(Jar::class) {
            archiveClassifier.set("javadoc")
        }

        afterEvaluate {
            extensions.findByType<PublishingExtension>()?.apply {
                repositories {
                    maven {
                        url = uri(if (isReleaseBuild) RELEASE_URL else SNAPSHOT_URL)
                        credentials {
                            username = getSecret("MAVEN_USERNAME")
                            password = getSecret("MAVEN_PASSWORD")
                        }
                    }
                }

                publications.withType<MavenPublication>().configureEach {
                    artifact(emptyJavadocJar.get())

                    pom {
                        name.set(NAME)
                        description.set(DESCRIPTION)
                        url.set(URL)

                        licenses {
                            license {
                                name.set(LICENSE_NAME)
                                url.set(LICENSE_URL)
                                distribution.set(LICENSE_DISTRIBUTION)
                            }
                        }
                        developers {
                            developer {
                                id.set(DEVELOPER_ID)
                                name.set(DEVELOPER_NAME)
                                email.set(DEVELOPER_EMAIL)
                            }
                        }
                        scm { url.set(URL) }
                    }
                }
            }

            extensions.findByType<PublishingExtension>()?.let { publishing ->
                val key = getSecret("GPG_KEY").replace("\\n", "\n")
                val password = getSecret("GPG_PASSWORD")

                extensions.findByType<SigningExtension>()?.apply {
                    useInMemoryPgpKeys(key, password)
                    sign(publishing.publications)
                }
            }

            tasks.withType<Sign>().configureEach {
                onlyIf { isReleaseBuild }
            }
        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            allWarningsAsErrors = true
        }
    }
}

val isReleaseBuild: Boolean
    get() = System.getenv("GPG_KEY") != null

fun getSecret(
    key: String,
    default: String = "secret" // these values can not be public
): String = System.getenv(key).let {
    if (it.isNullOrEmpty()) {
        getSecretProperties()?.get(key)?.toString() ?: default
    } else {
        it
    }
}

fun getSecretProperties() = try {
    Properties().apply { load(file("key.properties").inputStream()) }
} catch (e: IOException) {
    logger.debug(e.message, e)
    null
}

object Library {
    const val GROUP = "com.github.submob"
    const val URL = "https://github.com/SubMob/ParserMob"
    const val NAME = "ParserMob"
    const val DESCRIPTION = "Multiplatform expression parser library"
    const val DEVELOPER_NAME = "Mustafa Ozhan"
    const val DEVELOPER_ID = "mustafaozhan"
    const val DEVELOPER_EMAIL = "mr.mustafa.ozhan@gmail.com"
    const val LICENSE_NAME = "The Apache Software License, Version 2.0"
    const val LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    const val LICENSE_DISTRIBUTION = "repo"
    const val RELEASE_URL = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"
    const val SNAPSHOT_URL = "https://s01.oss.sonatype.org/content/repositories/snapshots"
}
