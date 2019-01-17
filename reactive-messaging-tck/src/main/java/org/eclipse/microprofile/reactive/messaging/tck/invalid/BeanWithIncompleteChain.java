package org.eclipse.microprofile.reactive.messaging.tck.invalid;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

public class BeanWithIncompleteChain {


  @Incoming("missing")
  @Outgoing("data")
  public String process(String s) {
    return s;
  }

  @Incoming("data")
  public void sink(String s) {
    // Do nothing.
  }
}
