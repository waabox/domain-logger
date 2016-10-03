package org.waabox.log;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.*;

public class LogTest {

  @Before public void setUp() {
    DomainLogger.cleanup();
  }

  @After public void after() {
    DomainLogger.cleanup();
  }

  @Test public void test() {
    DomainLogger log = DomainLogger.get("test");

    assertThat(log.generate().getNodes().isEmpty(), is(true));

    log.log("waabox.formulas.sum", "10");
    log.log("waabox.formulas.pow", "20");
    log.log("waabox_2.formulas.sum", "20");
    log.log("waabox_2.formulas.pow", "30");

    DomainLog logEntry = log.generate();

    DomainLog waaboxSumEntry = logEntry.getNodes().get(0).getNodes().get(0)
        .getNodes().get(0).getNodes().get(0);
    assertThat(waaboxSumEntry.getValue(), is("10"));

    DomainLog waaboxPowEntry = logEntry.getNodes().get(0).getNodes().get(0)
        .getNodes().get(1).getNodes().get(0);
    assertThat(waaboxPowEntry.getValue(), is("20"));

    DomainLog waabox_2SumEntry = logEntry.getNodes().get(1).getNodes().get(0)
        .getNodes().get(0).getNodes().get(0);
    assertThat(waabox_2SumEntry.getValue(), is("20"));

    DomainLog waabox_2PowEntry = logEntry.getNodes().get(1).getNodes().get(0)
        .getNodes().get(1).getNodes().get(0);
    assertThat(waabox_2PowEntry.getValue(), is("30"));
  }

  @Test public void test_withStructure() {
    DomainLogger log = DomainLogger.get("test");
    assertThat(log.generate().getNodes().isEmpty(), is(true));

    Structure s = Structure.named("testExample")
        .with("name", "waabox").with("formulaName", "sum");
    log.log(s, "10");

    Structure s1 = Structure.named("testExample")
        .with("name", "waabox_2").with("formulaName", "pow");
    log.log(s1, "20");
    DomainLog logEntry = log.generate();

    DomainLog waaboxSumEntry = logEntry.getNodes().get(0).getNodes().get(0)
        .getNodes().get(0).getNodes().get(0);
    assertThat(waaboxSumEntry.getValue(), is("10"));

    DomainLog waabox_2PowmEntry = logEntry.getNodes().get(1).getNodes().get(0)
        .getNodes().get(0).getNodes().get(0);
    assertThat(waabox_2PowmEntry.getValue(), is("20"));
  }

  @Test public void test_globalVars() {
    DomainLogger log = DomainLogger.get("test");
    log.registerGlobal("name", "waabox");

    Structure s = Structure.named("testExample")
        .with("name", "${name}").with("formulaName", "sum");
    log.log(s, "10");

    Structure s1 = Structure.named("testExample")
        .with("name", "${name}").with("formulaName", "pow");
    log.log(s1, "20");

    DomainLog logEntry = log.generate();

    DomainLog waaboxSumEntry = logEntry.getNodes().get(0).getNodes().get(0)
        .getNodes().get(0).getNodes().get(0);
    assertThat(waaboxSumEntry.getValue(), is("10"));

    DomainLog waaboxPowEntry = logEntry.getNodes().get(0).getNodes().get(0)
        .getNodes().get(1).getNodes().get(0);
    assertThat(waaboxPowEntry.getValue(), is("20"));
  }

}
