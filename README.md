# Selenium + Java + TestNG Automation Framework

[![Selenium Tests](https://github.com/amr-medhat-qa/selenium-java-testng/actions/workflows/tests.yml/badge.svg)](https://github.com/amr-medhat-qa/selenium-java-testng/actions/workflows/tests.yml)
[![Allure Report](https://img.shields.io/badge/Allure-report-ff6a00)](https://amr-medhat-qa.github.io/selenium-java-testng/)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4-43B02A?logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7-FF7F00)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)

UI test automation framework built with **Selenium WebDriver**, **Java 21** and **TestNG**, against
[the-internet.herokuapp.com](https://the-internet.herokuapp.com) — a practice site covering real-world UI challenges.

📊 **[Live Allure report →](https://amr-medhat-qa.github.io/selenium-java-testng/)** (Chrome and Firefox in one report, published after every run on `main`)

Runs on every push / PR and on a weekday schedule in **GitHub Actions** on **Chrome and Firefox** (headless).

## Design

```
src/test/java/com/amrmedhat/qa/
├── config/     Config         — base URL, browser, headless, wait time (overridable with -D)
├── driver/     DriverFactory  — Chrome / Firefox / Edge, headless, Selenium Manager (no driver binaries)
├── base/       BaseTest       — ThreadLocal driver per test → safe parallel runs; screenshot on failure
├── listeners/  RetryAnalyzer  — one visible retry for tests hit by site/network noise
├── pages/      BasePage + page objects — explicit waits only (no Thread.sleep, no implicit waits)
├── data/       test data
└── tests/      TestNG tests
src/test/resources/suites/    testng.xml (regression, parallel) · smoke.xml (smoke group)
```

- **Page Object Model** with fluent navigation (`loginPage.open().loginAs(...).logout()`)
- **Page objects wait for their own readiness** (e.g. `SecureAreaPage` waits for the `/secure` URL) — no race conditions in tests
- **Data-driven** negative tests with a TestNG `@DataProvider`
- **Parallel execution** (methods, 4 threads) with a thread-safe driver

## Allure reporting

- Every page-object action is a **step** (open, click, type); password values are masked as `****`
- A **screenshot is attached** to every failed test
- Tests are grouped by **Epic / Feature** and carry **severity**; browser, URL and Java version appear in the **Environment** widget
- CI merges the Chrome and Firefox runs into **one report** and publishes it to GitHub Pages

## Flaky-test handling (evidence-based)

The practice site is a free public service and sometimes misbehaves. Instead of guessing, each flake was investigated:

| Symptom | Root cause (found with Chrome's network log) | Handling |
|---------|----------------------------------------------|----------|
| Logout click ignored on Chrome in CI | Chrome's **password-leak popup** covered the page after login | Password manager and leak detection disabled in test browsers |
| Login ends on `/login` ("You must login…") or the error message is missing, ~1 in 60 logins | The site **loses the session** right after a login: `POST /authenticate → 303`, then `GET /secure → 302`. Flash messages live in that session, so they vanish too | `LoginPage` repeats the login **once** and records a visible step in the report |
| `ERR_TIMED_OUT` while opening a page, ~1 in 300 requests under stress | Network timeout to the public site | `RetryAnalyzer`: a failed test is re-run **once** (`-DretryCount=0` turns it off); a real defect fails twice and is still reported |

Retried tests show up in the Allure **Retries** tab, so flakiness stays visible instead of being hidden.

### Deterministic CI: the application under test runs inside the job

Looking at the application's source explains the lost sessions: its session secret is generated **per process**
(`SESSION_SECRET = ENV.fetch('SESSION_SECRET') { SecureRandom.hex(64) }`), so a public deployment with several
instances can reject a session issued by another one.

To make CI independent of a free public service, the workflow builds the official
[saucelabs/the-internet](https://github.com/saucelabs/the-internet) source **at a pinned commit** with its own
`Dockerfile`, runs it on `localhost:7080` and points the tests at it (`-DbaseUrl=http://localhost:7080`).
Locally the tests still default to the public site.

| | CI | Local default |
|---|---|---|
| Application under test | pinned build on `localhost` | `https://the-internet.herokuapp.com` |
| Network / session noise | none | possible (see table above) |

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

Requires Java 21 and Maven. Browser drivers are downloaded automatically.

```bash
mvn test
```
```bash
mvn test -Dsuite=smoke.xml
```
```bash
mvn test -Dbrowser=firefox -Dheadless=false
```

Open the Allure report locally (needs Node.js; `clean` removes results of earlier runs):

```bash
mvn clean test
```
```bash
npx allure-commandline serve target/allure-results
```

| Property | Default |
|----------|---------|
| `browser` | `chrome` (`firefox`, `edge`) |
| `headless` | `true` |
| `baseUrl` | `https://the-internet.herokuapp.com` |
| `waitSeconds` | `10` |
| `retryCount` | `1` |

## Author

**Amr Medhat** — Senior QA Engineer · [GitHub](https://github.com/amr-medhat-qa) · [LinkedIn](https://www.linkedin.com/in/amr-medhat-wafeek)
