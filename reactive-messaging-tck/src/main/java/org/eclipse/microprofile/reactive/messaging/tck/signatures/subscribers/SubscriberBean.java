package org.eclipse.microprofile.reactive.messaging.tck.signatures.subscribers;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

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
public class SubscriberBean {

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

  @Outgoing("subscriber-message")
  public Publisher<Message<String>> sourceForSubscriberMessage() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("subscriber-message")
  public Subscriber<Message<String>> subscriberOfMessages() {
    return ReactiveStreams.<Message<String>>builder().forEach(m -> add("subscriber-message", m.getPayload())).build();
  }

  @Outgoing("subscriber-payload")
  public Publisher<Message<String>> sourceForSubscribePayload() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("subscriber-payload")
  public Subscriber<String> subscriberOfPayloads() {
    return ReactiveStreams.<String>builder().forEach(p -> add("subscriber-payload", p)).build();
  }


  @Outgoing("void-payload")
  public Publisher<Message<String>> sourceForVoidPayload() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("void-payload")
  public void consumePayload(String payload) {
    add("void-payload", payload);
  }

  @Outgoing("string-payload")
  public Publisher<Message<String>> sourceForStringPayload() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("string-payload")
  public String consumePayloadsAndReturnSomething(String payload) {
    add("string-payload", payload);
    return payload;
  }

  @Outgoing("cs-void-message")
  public Publisher<Message<String>> sourceForCsVoidMessage() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("cs-void-message")
  public CompletionStage<Void> consumeMessageAndReturnCompletionStageOfVoid(Message<String> message) {
    return CompletableFuture.runAsync(() -> add("cs-void-message", message.getPayload()), EXECUTOR);
  }

  @Outgoing("cs-void-payload")
  public Publisher<Message<String>> sourceForCsVoidPayload() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("cs-void-payload")
  public CompletionStage<Void> consumePayloadAndReturnCompletionStageOfVoid(String payload) {
    return CompletableFuture.runAsync(() -> add("cs-void-payload", payload), EXECUTOR);
  }

  @Outgoing("cs-string-message")
  public Publisher<Message<String>> sourceForCsStringMessage() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("cs-string-message")
  public CompletionStage<String> consumeMessageAndReturnCompletionStageOfString(Message<String> message) {
    return CompletableFuture.supplyAsync(() -> {
      add("cs-string-message", message.getPayload());
      return "something";
    }, EXECUTOR);
  }

  @Outgoing("cs-string-payload")
  public Publisher<Message<String>> sourceForCsStringPayload() {
    return ReactiveStreams.fromIterable(EXPECTED).map(Message::of).buildRs();
  }

  @Incoming("cs-string-payload")
  public CompletionStage<String> consumePayloadAndReturnCompletionStageOfString(String payload) {
    return CompletableFuture.supplyAsync(() -> {
      add("cs-string-payload", payload);
      return "something";
    }, EXECUTOR);
  }

  private void add(String key, String value) {
    collector.computeIfAbsent(key, x -> new CopyOnWriteArrayList<>()).add(value);
  }

  void verify() {
    assertThat(collector).hasSize(8).allSatisfy((k, v) -> assertThat(v).containsExactlyElementsOf(EXPECTED));
  }


}
