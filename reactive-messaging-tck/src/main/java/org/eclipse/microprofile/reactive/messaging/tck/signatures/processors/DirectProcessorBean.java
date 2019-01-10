package org.eclipse.microprofile.reactive.messaging.tck.signatures.processors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.microprofile.reactive.messaging.tck.TckBase.EXECUTOR;

@ApplicationScoped
public class DirectProcessorBean {

  private Map<String, List<String>> collector = new ConcurrentHashMap<>();

  private static final List<String> EXPECTED = Arrays.asList(
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "9",
    "10"
  );

  @Outgoing("publisher-synchronous-message")
  public PublisherBuilder<Integer> streamForProcessorOfMessages() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Outgoing("publisher-synchronous-payload")
  public PublisherBuilder<Integer> streamForProcessorOfPayloads() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Outgoing("publisher-asynchronous-message")
  public PublisherBuilder<Integer> streamForProcessorBuilderOfMessages() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Outgoing("publisher-asynchronous-payload")
  public PublisherBuilder<Integer> streamForProcessorBuilderOfPayloads() {
    return ReactiveStreams.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
  }

  @Incoming("synchronous-message")
  public void getMessgesFromProcessorOfMessages(String value) {
    add("processor-message", value);
  }

  @Incoming("synchronous-payload")
  public void getMessgesFromProcessorOfPayloads(String value) {
    add("processor-payload", value);
  }

  @Incoming("asynchronous-message")
  public void getMessgesFromProcessorBuilderOfMessages(String value) {
    add("processor-builder-message", value);
  }

  @Incoming("asynchronous-payload")
  public void getMessgesFromProcessorBuilderOfPayloads(String value) {
    add("processor-builder-payload", value);
  }

  @Incoming("publisher-synchronous-message")
  @Outgoing("synchronous-message")
  public Message<String> messageSynchronous(Message<Integer> message) {
    return Message.of(Integer.toString(message.getPayload() + 1));
  }

  @Incoming("publisher-synchronous-payload")
  @Outgoing("synchronous-payload")
  public String payloadSynchronous(int value) {
    return Integer.toString(value + 1);
  }

  @Incoming("publisher-asynchronous-message")
  @Outgoing("asynchronous-message")
  public CompletionStage<Message<String>> messageAsynchronous(Message<Integer> message) {
    return CompletableFuture.supplyAsync(() -> Message.of(Integer.toString(message.getPayload() + 1)), EXECUTOR);
  }

  @Incoming("publisher-asynchronous-payload")
  @Outgoing("asynchronous-payload")
  public CompletionStage<String> payloadAsynchronous(int value) {
    return CompletableFuture.supplyAsync(() -> Integer.toString(value + 1), EXECUTOR);
  }

  private void add(String key, String value) {
    collector.computeIfAbsent(key, x -> new CopyOnWriteArrayList<>()).add(value);
  }

  void verify() {
    assertThat(collector).hasSize(4).allSatisfy((k, v) -> assertThat(v).containsExactlyElementsOf(EXPECTED));
  }

}
