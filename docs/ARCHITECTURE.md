# Architecture

## Platform

- Minecraft: 1.21.1
- Loader: NeoForge
- Java: 21
- Build system: Gradle
- Mappings: Parchment

## Mod

- Mod ID: the_unwritten
- Mod name: The Unwritten
- Base package: com.theunwritten

## General principles

1. Игровая логика не должна зависеть от клиентского рендера.
2. Данные игрока должны сохраняться независимо от текущей сессии.
3. Системы должны иметь минимальное количество прямых зависимостей друг от друга.
4. Игровые правила сначала проектируются, затем реализуются.
5. Каждая крупная система должна иметь собственную документацию.