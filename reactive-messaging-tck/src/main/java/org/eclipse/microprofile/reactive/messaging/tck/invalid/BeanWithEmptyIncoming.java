package org.eclipse.microprofile.reactive.messaging.tck.invalid;


import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

public class BeanWithEmptyIncoming {

  @Incoming
  public void consumer(String data) {

  }

}
