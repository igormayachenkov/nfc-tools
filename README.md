#  :sdk
1. API: Contains jar-file required in build time. Linked by many modules as a gradle file dependency.
2. IMPLEMENTATION: Contains compiled binary code (jni-libs) implemented the SmartEngines SDK logic. Used by :app to load library in runtime (System.loadLibrary())

## :sdk:bundles
Contains SDK-bundles. Included in the app as an asset pack.
Used by engine loaders.

# :utils (core)
Полезные утилиты, которые могут пригодиться в разных местах

# :engine (core), :targets (feature)
Обёртка движка и реализация процесса распознавания.
### Основные классы
Engine - engine wrapper, выполняет загрузку движка, предоставляет engine как поле данных
Session - session wrapper, создаёт сессию и колбэки, предоставляет данные колбэков, как поля данных 
Target - то, что мы хотим распознать. Создаёт и настраивает нужную сессию с учётом специфичных настроек, потому :feature
ImageProcessor - реализует процесс распознавания, скармливает картинки сессии и проверяет результат

# :camera (feature)
Код для работы с камерой, включая экран

# :visualization
Специализированнный инструментарий для работы с данными сессий, которые отображаются на экране в процессе распознавания (на экране камеры)

