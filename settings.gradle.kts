val kotlinVersion: String by settings

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if(requested.id.id.startsWith("org.jetbrains.kotlin"))
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        }
    }
}

rootProject.name = "cheapcsv"