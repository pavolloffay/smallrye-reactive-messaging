package org.eclipse.microprofile.reactive.messaging.tck.invalid;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

public class BeanWithBadOutgoingSignature {

  @Outgoing("foo")
  public void producer() {
    // Invalid
  }
}
