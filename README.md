# domain-logger

**Lightweight Java domain-event logger for debugging messy domain flows.  
Attach it, instrument nothing, and watch your domain chatter in real time.  
Zero magic — just interception, timestamps, and signal.**

---

## What it is

`domain-logger` is a small Java utility that records **domain events + metadata** to help you inspect state transitions, detect weird flows, and understand what your domain layer is doing without scattering debug prints everywhere.

Designed to be minimal, framework-agnostic, and brutally straightforward.

---

## Why

Because real systems drift:

- unexpected transitions  
- misordered event flows  
- hidden side-effects  
- aggregates changing state for reasons nobody remembers  

This library gives you **temporal breadcrumbs** that make domain debugging tolerable.

---

## Installation

```xml
<dependency>
  <groupId>org.waabox</groupId>
  <artifactId>domain-logger</artifactId>
  <version>1.0</version>
</dependency>
```

---

## Usage

Basic logging:

```java
DomainLogger logger = new DomainLogger();

logger.log("cart.created", Map.of("cartId", "C123"));
logger.log("cart.item.added", Map.of("sku", "ABC-999", "qty", 2));

logger.dump(System.out);
```

Sample output:

```
[2026-02-07T12:22:14.120Z] cart.created        cartId=C123
[2026-02-07T12:22:14.145Z] cart.item.added     sku=ABC-999 qty=2
```

---

## API Surface

```java
new DomainLogger()
  .log(String event)
  .log(String event, Map<String, Object> data)
  .entries()           // returns the collected events
  .dump(PrintStream)   // prints them
```

No async.  
No sinks.  
No configuration DSL.  
Just logs in memory.

---

## When to Use

- Debugging DDD aggregates  
- Inspecting state machines  
- Verifying event ordering  
- Capturing domain traces in integration tests  
- Revealing accidental transitions during refactors  

It's a **surgical tool**, not a framework.

---

## Philosophy

- **Small** → keep it obvious  
- **Deterministic** → no invisible state  
- **Replaceable** → bolt it on, rip it out anytime  
- **Observable** → everything is loggable and grep-friendly  

---

## Status

Legacy but still useful for teams working with complex domain models and wanting tighter observability during development or refactors.

PRs welcome.

---

## License

Apache 2.0
