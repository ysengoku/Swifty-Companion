# Swifty Companion

<!-- Add image or use below -->
<img src="https://capsule-render.vercel.app/api?type=venom&height=300&color=0:61baf8,100:256082&text=Swifty%20Companion&fontColor=ae3855&animation=fadeIn&textBg=false&stroke=e995a4&strokeWidth=1&desc=Short%20description&descSize=18&descAlignY=66" width="100%" />
<!-- <img src="" width="100%" /> -->

<div align="center">
  <!-- <img src="https://img.shields.io/badge/validated-125/100-brightgreen?style=for-the-badge&logo=cachet" /> -->
  <br />
  <em>  
    This project was created as part of the 42 curriculum by <a href="https://github.com/ysengoku">yusengok</a>.
  </em>
  <br /><br /><br />
  <img src="https://img.shields.io/github/commit-activity/t/ysengoku/swifty-companion?style=flat-square&color=9D9E0A" />
  <img src="https://img.shields.io/github/created-at/ysengoku/swifty-companion?style=flat-square&color=9D9E0A" />
  <img src="https://img.shields.io/github/issues/ysengoku/swifty-companion?style=flat-square&color=9D9E0A" />
</div>

## Table of Contents

<details>
<summary>Click to Show / Hide</summary>

- [About](#about)
- [Objectives](#objectives)
- [Features](#features)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [Technical Restrictions](#technical-restrictions)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Usage](#usage)
- [Development](#development)
  - [Workflow](#workflow)
  - [Linting & Formatting](#linting--formatting)
  - [Testing](#testing)
- [Notes](#notes)
- [Resources](#resources)
- [AI Usage](#ai-usage)
- [Authors](#authors)
- [License](#license)

</details>

## About

## Objectives

## Features

## Architecture

## Project Structure

## Tech Stack

<!-- Use utils/TECH_STACK_LIST.md -->

## Tech Stack

**Languages:**

<div>   
  <img src="https://img.shields.io/badge/Kotlin-333333?style=for-the-badge&logo=kotlin&logoColor=37F52FF" />
</div>

Google's recommended language for Android since 2019.
Null safety and coroutines make asynchronous code shorter and safer
than the Java equivalent.
<br />


**Frameworks & Libraries:**

<div>
  <img src="https://img.shields.io/badge/Jetpack_Compose-333333?style=for-the-badge&logo=jetpackcompose&logoColor=4285F4" />
  <img src="https://img.shields.io/badge/Retrofit-333333?style=for-the-badge&logo=square&logoColor=FFFFFF" />
  <img src="https://img.shields.io/badge/OkHttp-333333?style=for-the-badge&logo=square&logoColor=FFFFFF" />
  <img src="https://img.shields.io/badge/Coil-333333?style=for-the-badge&logo=kotlin&logoColor=7F52FF" />
</div>

- **Jetpack Compose** is a declarative UI toolkit and part of AndroidX. It satisfies
the subject's requirement for a flexible layout technique: layouts adapt to screen
size without separate XML files per configuration. **Navigation Compose** handles
the two required views and the back stack.

- **Retrofit** is the HTTP client. Android has no usable built-in one, so writing
against `HttpURLConnection` would mean handling threading, error mapping and JSON
parsing by hand. Its Gson converter maps the 42 API's responses onto Kotlin data
classes. **OkHttp** is Retrofit's underlying engine, declared explicitly here
because the bearer token is attached through a custom interceptor.

- **Coil** loads profile pictures, handling download, caching and lifecycle-aware
cancellation.
<br />

**API:**

<div>
  <img src="https://img.shields.io/badge/42_Intra_API-333333?style=for-the-badge&logo=42&logoColor=FFFFFF" />
  <img src="https://img.shields.io/badge/OAuth_2.0-333333?style=for-the-badge&logo=auth0&logoColor=EB5424" />
</div>
<br />

**Tools:**
<div>
  <img src="https://img.shields.io/badge/Android_Studio-333333?style=for-the-badge&logo=androidstudio&logoColor=3DDC84" />
  <img src="https://img.shields.io/badge/Gradle-333333?style=for-the-badge&logo=gradle&logoColor=02303A" />
</div>
<br />

**Development Environment:**
<div>
  <img src="https://img.shields.io/badge/Android_Emulator_API_37-333333?style=for-the-badge&logo=android&logoColor=3DDC84" />
</div>

## Technical Restrictions

### Credentials

The Android convention would be `local.properties`, which Gradle reads natively and which is git-ignored by default.
In this project, the subject requires a `.env` file instead, so `.env` is parsed at build time in `app/build.gradle.kts` and exposed through `BuildConfig`. The runtime path stays the standard one.

## Getting Started

### Prerequisites

### Installation

### Usage

## Development

### Workflow

### Linting & Formatting

### Testing

## Notes

## Resources

## AI Usage

## Authors

## License
