package org.eclipse.microprofile.reactive.messaging.tck.signatures.processors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationScoped
public class TransformerBean {

  private Map<String, List<String>> collector = new ConcurrentHashMap<>();

  private static final List<String> EXPECTED = Arrays.asList(
    "1", "1",
    "2", "2",
    "3", "3",
    "4", "4",
    "5", "5",
    "6", "6",
    "7", "7",
    "8", "8",
    "9", "9",
    "10", "10"
  );

  @Outgoing("publisher-for-publisher-message")
  public PublisherBuilder<Integer> streamForProcessorOfMessages() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Outgoing("publisher-for-publisher-payload")
  public PublisherBuilder<Integer> streamForProcessorOfPayloads() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Outgoing("publisher-for-publisher-builder-message")
  public PublisherBuilder<Integer> streamForProcessorBuilderOfMessages() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Outgoing("publisher-for-publisher-builder-payload")
  public PublisherBuilder<Integer> streamForProcessorBuilderOfPayloads() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Incoming("publisher-message")
  public void getMessgesFromProcessorOfMessages(String value) {
    add("message", value);
  }

  @Incoming("publisher-payload")
  public void getMessgesFromProcessorOfPayloads(String value) {
    add("payload", value);
  }

  @Incoming("publisher-builder-message")
  public void getMessgesFromProcessorBuilderOfMessages(String value) {
    add("builder-message", value);
  }

  @Incoming("publisher-builder-payload")
  public void getMessgesFromProcessorBuilderOfPayloads(String value) {
    add("builder-payload", value);
  }

  @Incoming("publisher-for-publisher-message")
  @Outgoing("publisher-message")
  public Publisher<Message<String>> processorOfMessages(Publisher<Message<Integer>> stream) {
    return ReactiveStreams.fromPublisher(stream)
      .map(Message::getPayload)
      .map(i -> i + 1)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .map(Message::of)
      .buildRs();
  }

  @Incoming("publisher-for-publisher-payload")
  @Outgoing("publisher-payload")
  public Publisher<String> processorOfPayloads(Publisher<Integer> stream) {
    return ReactiveStreams.fromPublisher(stream)
      .map(i -> i + 1)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .buildRs();
  }

  @Incoming("publisher-for-publisher-builder-message")
  @Outgoing("publisher-builder-message")
  public PublisherBuilder<Message<String>> processorBuilderOfMessages(PublisherBuilder<Message<Integer>> stream) {
    return stream
      .map(Message::getPayload)
      .map(i -> i + 1)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .map(Message::of);
  }

  @Incoming("publisher-for-publisher-builder-payload")
  @Outgoing("publisher-builder-payload")
  public PublisherBuilder<String> processorBuilderOfPayloads(PublisherBuilder<Integer> stream) {
    return stream
      .map(i -> i + 1)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i));
  }

  private void add(String key, String value) {
    collector.computeIfAbsent(key, x -> new CopyOnWriteArrayList<>()).add(value);
  }

  void verify() {
    assertThat(collector).hasSize(4).allSatisfy((k, v) -> assertThat(v).containsExactlyElementsOf(EXPECTED));
  }

}
