# Selenium + Java + TestNG Automation Framework

[![Selenium Tests](https://github.com/amr-medhat-qa/selenium-java-testng/actions/workflows/tests.yml/badge.svg)](https://github.com/amr-medhat-qa/selenium-java-testng/actions/workflows/tests.yml)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4-43B02A?logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7-FF7F00)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)

UI test automation framework built with **Selenium WebDriver**, **Java 21** and **TestNG**, against
[the-internet.herokuapp.com](https://the-internet.herokuapp.com) — a practice site covering real-world UI challenges.

> 🚧 Work in progress — Allure reporting is next.

Runs on every push / PR and on a weekday schedule in **GitHub Actions** on **Chrome and Firefox** (headless).

## Design

```
src/test/java/com/amrmedhat/qa/
├── config/    Config         — base URL, browser, headless, wait time (overridable with -D)
├── driver/    DriverFactory  — Chrome / Firefox / Edge, headless, Selenium Manager (no driver binaries)
├── base/      BaseTest       — ThreadLocal driver per test → safe parallel runs; screenshot on failure
├── pages/     BasePage + page objects — explicit waits only (no Thread.sleep, no implicit waits)
├── data/      test data
└── tests/     TestNG tests
src/test/resources/suites/    testng.xml (regression, parallel) · smoke.xml (smoke group)
```

- **Page Object Model** with fluent navigation (`loginPage.open().loginAs(...).logout()`)
- **Page objects wait for their own readiness** (e.g. `SecureAreaPage` waits for the `/secure` URL) — no race conditions in tests
- **Data-driven** negative tests with a TestNG `@DataProvider`
- **Parallel execution** (methods, 4 threads) with a thread-safe driver

## What's covered

| Area | Scenarios |
|------|-----------|
| Login | Valid login, logout, protected page redirect, 5 data-driven invalid cases (unknown user, wrong password, empty fields, case sensitivity) |
| JavaScript alerts | Alert accept, confirm OK / Cancel, prompt with typed text, prompt dismissed (`null`) — using `ExpectedConditions.alertIsPresent()` |
| Frames | Nested frames (frame inside frame), data-driven per frame, always switching back with `defaultContent()` in a `finally` block |
| Dropdown | Default placeholder, selectable options, select by visible text and by value, single-select check — with Selenium's `Select` |
| Dynamic loading | Element hidden in the DOM vs. element rendered later — both handled with explicit waits (no `Thread.sleep`) |

**23 tests** in total.

## Run

Requires Java 21 and Maven. Browsers drivers are downloaded automatically.

```bash
mvn test
```
```bash
mvn test -Dsuite=smoke.xml
```
```bash
mvn test -Dbrowser=firefox -Dheadless=false
```

| Property | Default |
|----------|---------|
| `browser` | `chrome` (`firefox`, `edge`) |
| `headless` | `true` |
| `baseUrl` | `https://the-internet.herokuapp.com` |
| `waitSeconds` | `10` |

## Author

**Amr Medhat** — Senior QA Engineer · [GitHub](https://github.com/amr-medhat-qa) · [LinkedIn](https://www.linkedin.com/in/amr-medhat-wafeek)
