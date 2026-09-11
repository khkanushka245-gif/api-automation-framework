# 🔗 API Automation Framework

> A **REST/SOAP API automation suite** demonstrating data-driven testing, schema/contract validation, and CI/CD execution — with examples in both **Python (pytest)** and **Java (Rest Assured + TestNG)**.

![Python](https://img.shields.io/badge/Python-3776AB?style=flat&logo=python&logoColor=white)
![pytest](https://img.shields.io/badge/pytest-0A9EDC?style=flat&logo=pytest&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Rest Assured](https://img.shields.io/badge/Rest%20Assured-4B8BBE?style=flat)
![GitHub Actions](https://img.shields.io/badge/CI-GitHub%20Actions-2088FF?style=flat&logo=githubactions&logoColor=white)

---

## ✨ Overview

This framework shows how to build robust, maintainable **API test automation**:

- ✅ CRUD coverage against a public demo API
- 📦 **Data-driven** tests using external fixtures
- 🔍 **Schema / contract validation** of responses
- 🔁 Reusable request clients and assertions
- ⚡ **CI/CD** execution with reports

This is a **personal, sanitized demo** running against public sample APIs (e.g., `jsonplaceholder`, `reqres`) — no company data or confidential information.

---

## 📂 Project Structure

```
api-automation-framework/
├── python/
│   ├── tests/
│   │   ├── test_users.py
│   │   └── test_posts.py
│   ├── clients/
│   │   └── api_client.py
│   ├── data/
│   │   └── users.json
│   ├── requirements.txt
│   └── pytest.ini
├── java/
│   ├── src/test/java/com/example/api/
│   │   └── UsersApiTest.java
│   └── pom.xml
└── .github/workflows/
    └── api-tests.yml
```

---

## 🚀 Getting Started

### Python (pytest)

```bash
cd python
pip install -r requirements.txt
pytest -v
```

### Java (Rest Assured + TestNG)

```bash
cd java
mvn clean test                     # runs testng.xml
mvn test -DbaseUri=https://your-api # point at a different target
```

**Structure (POM-style for APIs):**
- `config/EnvConfig` — base URI, auth, and finite timeouts (system-property / env overridable)
- `config/RestAssuredConfigFactory` — explicit connect + socket timeouts (fail fast, no `Thread.sleep`)
- `base/BaseApiTest` — `@BeforeSuite` fixture with reusable, immutable request/response specs
- `services/UserService` — service object (API equivalent of a Page Object); no assertions inside
- `retry/RetryAnalyzer` + `RetryListener` — retries **only** transient failures (network/timeout/5xx), never assertion failures
- `api/UsersApiTest` — data-driven (`@DataProvider`), order-independent, contract + field assertions
- `testng.xml` — suite + retry listener registration

**Anti-flakiness by design:** finite HTTP timeouts, transient-only bounded retry,
no shared mutable state (safe in any order / parallel), and assertions on the
contract and stable fields rather than volatile values.

> The canonical green run is CI (`.github/workflows/api-tests.yml`), which runs
> `mvn -B clean test` on a clean runner. Behind a TLS-intercepting corporate
> proxy, local runs may fail SSL handshake to public APIs — that is an
> environment trust issue, not a test defect; run in CI or add your proxy CA to
> the JVM truststore.

---

## 🧪 Example (Python)

```python
def test_get_user_returns_expected_fields(api_client):
    resp = api_client.get("/users/1")
    assert resp.status_code == 200
    body = resp.json()
    assert set(["id", "name", "email"]).issubset(body.keys())
```

## 🧪 Example (Java — Rest Assured)

```java
@Test
public void getUserReturnsExpectedFields() {
    given()
        .baseUri("https://jsonplaceholder.typicode.com")
    .when()
        .get("/users/1")
    .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("email", notNullValue());
}
```

---

## 🎯 Highlights

- Proves **API testing depth** across two ecosystems (Python & Java).
- Demonstrates **data-driven** and **contract validation** techniques.
- Shows **CI/CD** integration and clean framework structure.

---

## 📌 Notes

- Demonstration project for portfolio purposes. Runs against public sample APIs only.

---

<p align="center">
  Built by <strong>Kanushka Herath</strong> · QA Automation Lead — AI &amp; Playwright<br>
  <a href="mailto:khkanushka245@gmail.com">khkanushka245@gmail.com</a> ·
  <a href="https://www.linkedin.com/in/kanushka-mayomi-69201345">LinkedIn</a>
</p>
