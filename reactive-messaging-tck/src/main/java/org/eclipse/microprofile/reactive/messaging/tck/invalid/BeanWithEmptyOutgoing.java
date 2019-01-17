package org.eclipse.microprofile.reactive.messaging.tck.invalid;


import org.eclipse.microprofile.reactive.messaging.Outgoing;

public class BeanWithEmptyOutgoing {

  @Outgoing
  public String producer() {
    return "hello";
  }

}
