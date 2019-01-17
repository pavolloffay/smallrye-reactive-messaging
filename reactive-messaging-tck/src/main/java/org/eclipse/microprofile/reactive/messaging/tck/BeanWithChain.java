package org.eclipse.microprofile.reactive.messaging.tck;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class BeanWithChain {

  private List<String> list = new CopyOnWriteArrayList<>();

  @Outgoing("source")
  public PublisherBuilder<String> source() {
    return ReactiveStreams.of("hello", "with","MicroProfile", "reactive", "messaging");
  }

  @Incoming("source")
  @Outgoing("processed-a")
  public String toUpperCase(String payload) {
    return payload.toUpperCase();
  }

  @Incoming("processed-a")
  @Outgoing("processed-b")
  public PublisherBuilder<String> filter(PublisherBuilder<String> input) {
    return input.filter(item -> item.length() > 4);
  }

  @Incoming("processed-b")
  public void sink(String word) {
    list.add(word);
  }

  public List<String> list() {
    return list;
  }


}
