package org.eclipse.microprofile.reactive.messaging.tck;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimpleIncomingBean extends ValueCollector {

  
  @Incoming("strings")
  public void incoming(String s) {
    values.add(s);
  }

}
