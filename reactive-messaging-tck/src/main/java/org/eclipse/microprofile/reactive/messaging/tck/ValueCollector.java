package org.eclipse.microprofile.reactive.messaging.tck;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ValueCollector {

  protected List<String> values = new CopyOnWriteArrayList<>();

  public List<String> getValues() {
    return values;
  }
}
