
object Dependencies {

    const val MaterialDesign = "com.google.android.material:material:1.2.0-beta01"
    const val ColorPicker = "com.jaredrummler:colorpicker:1.1.0"
    const val FB = "com.facebook.android:facebook-android-sdk:[4,5)"

    object Kotlin {
        const val StandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib:${ProjectConfig.Kotlin.Version}"
        const val CoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6"
    }

    object AndroidX {

        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val CoordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:1.1.0"

        const val AppCompat = "androidx.appcompat:appcompat:1.1.0"
        const val CoreKtx = "androidx.core:core-ktx:1.3.0"
        const val ActivityKtx = "androidx.activity:activity-ktx:1.2.0-alpha05"

        object Lifecycle {
            const val ViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha03"
            const val LiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.3.0-alpha03"
        }
    }

    object Test {

        const val JUnit = "junit:junit:4.13"

        object Android {
            const val JUnit = "androidx.test.ext:junit:1.1.1"
            const val Espresso = "androidx.test.espresso:espresso-core:3.2.0"
        }
    }
}