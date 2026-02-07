# domain-logger

`domain-logger` is a small Java utility that **taps into your domain layer and emits structured logs** for every event you care about.  
It’s meant for debugging real-world systems where the domain model is doing… weird things — and you want to see *when* and *why* without adding printlns all over the codebase.

This project is intentionally simple, lightweight, and framework-agnostic.

---

## What it does

- Logs **domain events** with timestamp + metadata.
- Lets you track **state transitions** across aggregates/entities.
- Helps identify inconsistent flows, re-entrant logic, and invalid call sequences.
- Can be plugged into **tests** or **runtime** to expose domain behavior.
- Zero dependencies on Spring or any DI framework.

---

## Why it exists

Because domain code can silently misbehave:  
- unexpected transitions,  
- dirty writes,  
- mis-ordered event flows,  
- logic that’s “correct until it isn’t”.

`domain-logger` gives you a simple way to **observe** without rewriting your domain model.

---

## Install

```xml
<dependency>
  <groupId>org.waabox</groupId>
  <artifactId>domain-logger</artifactId>
  <version>1.0</version>
</dependency>
