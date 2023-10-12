# ParserMob

![badge][badge-android]
![badge][badge-ios]
![badge][badge-js]
![badge][badge-jvm]

[![ParserMob CI](https://github.com/SubMob/ParserMob/actions/workflows/main.yml/badge.svg)](https://github.com/SubMob/ParserMob/actions/workflows/main.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.submob/parsermob/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.submob/parsermob)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/35c32a0221ab44e18400834c35b8f402)](https://www.codacy.com/gh/SubMob/ParserMob?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SubMob/ParserMob&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://app.codacy.com/project/badge/Coverage/9a20b6c5bb574e6fa8b3c8fb8c729378)](https://app.codacy.com/gh/SubMob/ParserMob/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Codecov Badge](https://codecov.io/gh/SubMob/ParserMob/graph/badge.svg?token=8BXIYNG758)](https://codecov.io/gh/SubMob/ParserMob)

## Install

ParserMob is currently published to Maven Central, so add that to repositories.

```groovy
repositories {
    mavenCentral()
}
```

Then, simply add the dependency to your common source-set dependencies

```groovy
commonMain {
    dependencies {
        implementation("com.github.submob:parsermob:LATEST_VERSION")
    }
}
```

### License

```markdown
Copyright 2020 Mustafa Ozhan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[badge-android]: https://img.shields.io/badge/platform-android-green

[badge-ios]: https://img.shields.io/badge/platform-ios-orange

[badge-js]: https://img.shields.io/badge/platform-js-yellow

[badge-jvm]: https://img.shields.io/badge/platform-jvm-red