# kotlin-android-library-template 🤖

![Android CI](https://github.com/lcdsmao/kotlin-android-template/workflows/Android%20CI/badge.svg) ![License](https://img.shields.io/github/license/cortinico/kotlin-android-template.svg) ![Language](https://img.shields.io/github/languages/top/cortinico/kotlin-android-template?color=blue&logo=kotlin)

A simple Github template that lets you create an **Android/Kotlin** library.

Forked from [cortinico/kotlin-android-template](https://github.com/cortinico/kotlin-android-template).

## Features

- **100% Kotlin-only template**.
- 3 Sample modules (Android app, Android library, Kotlin library).
- 100% Gradle Kotlin DSL setup.
- Dependency versions managed via `versions.properties`.
- CI Setup with GitHub Actions.
- Deploy website and docs.
- Kotlin Static Analysis via `detekt`.
- Publishing Ready.
- Issues Template (bug report + feature request)
- Pull Request Template.

## Gradle Setup

This template is using [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) as well as the [Plugin DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block) to setup the build.

Dependencies are centralized inside the [versions.properties](versions.properties) managed by [RefreshVersions](https://github.com/jmfayard/refreshVersions).

To check the newest version of dependencies you can run `./gradlew refreshVersions`.

## Static Analysis

This template is using [detekt](https://github.com/arturbosch/detekt) to analyze the source code, with the configuration that is stored in the [detekt.yml](config/detekt/detekt.yml) file.

To reformat all the source code as well as the buildscript you can run `./gradlew detektFormat`.

## CI

This template is using [GitHub Actions](https://github.com/lcdsmao/kotlin-android-template/actions) as CI.

## Docs

This template deploy the website to the Github Pages via [Material for MkDocs](https://github.com/squidfunk/mkdocs-material) and [Dokka](https://github.com/Kotlin/dokka).

## Publishing

The template is setup to be ready to publish a library/artifact on a Maven Repository.
See publish configuration in [gradle.properties](gradle.properties).

## Contributing

Feel free to open a issue or submit a pull request for any bugs/improvements.
