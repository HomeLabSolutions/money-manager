# Подготовка к Kotlin Multiplatform

## Текущее состояние

`:shared` — общий модуль без UI. Он собирается для Android, JVM,
реального iPhone (`iosArm64`) и симулятора на Apple Silicon (`iosSimulatorArm64`).
JVM target нужен существующим доменным модулям с Kotlin/JVM.
Android target использует `com.android.kotlin.multiplatform.library`,
который поддерживает AGP 9 без устаревшей интеграции `androidTarget`.

В `commonMain` перенесены `TransactionType`, `isIncome` и `Int.toType`.
Пакет и значения 0/1 сохранены, поэтому формат хранения операций не меняется.
Потребители подключают `:shared` напрямую через `implementation`, без `api`.
Тесты в `commonTest` проверяют значения хранения, обратное преобразование,
классификацию доходов и обработку неизвестных значений.

Общий модуль явно подключает stdlib: в проекте отключена автоматическая
зависимость через `kotlin.stdlib.default.dependency=false`.

## Проверки

На Linux:

```sh
./gradlew :shared:jvmTest :shared:testAndroidHostTest :app:assembleDebug
./gradlew projectHealth detektCheck ktlintCheck tomlCheck noApiCheck
```

Полный `projectHealth` также анализирует release-вариант и требует production
`google-services.json`. В CI этот файл подставляется из секрета; без него
локальная задача останавливается на `:app:processReleaseGoogleServices`.

На macOS с Xcode и установленными command line tools:

```sh
./gradlew :shared:iosSimulatorArm64Test
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
./gradlew :shared:linkDebugFrameworkIosArm64
```

Фреймворки `MoneyManagerShared.framework` появляются в
`shared/build/bin/<target>/debugFramework/`. Модуль можно импортировать
в Swift как `MoneyManagerShared`. Xcode-проект пока не создан; для интеграции
с будущим iOS-приложением нужно настроить сборку фреймворка из Xcode
или выпуск XCFramework. Выбор SwiftUI/Compose Multiplatform остаётся открытым.
Linux не проверяет компиляцию и запуск Apple targets.

## Следующие этапы

1. Отделить денежные значения и арифметику от `java.math.BigDecimal`.
   Нужны KMP-совместимая десятичная арифметика либо целые минимальные единицы
   с явно заданным масштабом. Сохранить точность, округление и формат хранения;
   не заменять денежные значения на `Double`.
2. Разделить `DateTimeUtil`: календарные вычисления на `kotlinx.datetime`
   можно переносить отдельно от `java.text.SimpleDateFormat`, `Date` и `Locale`.
3. Переносить модели, контракты и interactor-логику вертикальными участками,
   с общими тестами. Не подключать Android-модули к `commonMain`.
4. Подготовить KMP-хранилище. Текущие Room и DataStore связаны с Android;
   спроектировать платформенное создание базы/файлов и проверить миграции
   существующей Android-базы до изменения реализации.
5. Отделить Retrofit/OkHttp и Android Firebase от общих контрактов.
   Выбрать общую сеть либо платформенные реализации после аудита API.
6. Оставить Hilt в Android composition root; общий код получать зависимости
   через конструкторы. Для iOS создать отдельную сборку графа зависимостей.
7. Реализовать iOS-адаптеры авторизации, покупок, уведомлений, фоновых задач,
   геолокации и резервного копирования. Google Play Billing и WorkManager
   требуют отдельных реализаций на Apple-платформе.
8. Создать iOS-приложение, выбрать UI и добавить macOS CI с тестами симулятора.

Версии Kotlin и AGP берутся из существующего version catalog через `buildSrc`.
В `:shared` плагины применяются по ID без повторного указания версии: они уже
находятся в classpath сборки. Массового обновления зависимостей этот этап не требует.
