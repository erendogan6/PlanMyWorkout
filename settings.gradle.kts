pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PlanMyWorkout"

// App module
include(":app")

// Core modules
include(":core")
include(":core-ui")

// Feature modules
include(":feature:auth")
include(":feature:onboarding")
include(":feature:home")
include(":feature:workout")
include(":feature:progress")
include(":feature:profile")
