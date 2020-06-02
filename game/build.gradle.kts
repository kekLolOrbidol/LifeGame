import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    val fileTree = fileTree(file("libs")) {
        include("*.jar")
    }
    implementation(fileTree)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${ProjectConfig.Kotlin.Version}")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
}
