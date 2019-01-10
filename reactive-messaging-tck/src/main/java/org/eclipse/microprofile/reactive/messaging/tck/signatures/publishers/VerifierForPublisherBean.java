package org.eclipse.microprofile.reactive.messaging.tck.signatures.publishers;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationScoped
public class VerifierForPublisherBean {

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

  @Incoming("publisher-flowable-message")
  public void getMessageFromASubclassOfPublisher(String value) {
    add("publisher-flowable-message", value);
  }

  @Incoming("publisher-flowable-payload")
  public void getPayloadFromASubclassOfPublisher(String value) {
    add("publisher-flowable-payload", value);
  }

  @Incoming("publisher-builder-message")
  public void getMessageFromAPublisherBuilder(String value) {
    add("publisher-builder-message", value);
  }

  @Incoming("publisher-builder-payload")
  public void getPayloadFromPublisherBuilder(String value) {
    add("publisher-builder-payload", value);
  }

  @Incoming("publisher-payload")
  public void getPayloadFromPublisher(String value) {
    add("publisher-payload", value);
  }

  @Incoming("publisher-message")
  public void getMessageFromPublisher(String value) {
    add("publisher-message", value);
  }

  @Incoming("generator-payload")
  public Subscriber<Integer> getFromInfinitePayloadGenerator() {
    return ReactiveStreams.<Integer>builder()
      .limit(10)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .forEach(s -> add("generator-payload", s))
      .build();
  }

  @Incoming("generator-message")
  public Subscriber<Message<Integer>> getFromInfiniteMessageGenerator() {
    return ReactiveStreams.<Message<Integer>>builder()
      .limit(10)
      .map(Message::getPayload)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .forEach(s -> add("generator-message", s))
      .build();
  }

  @Incoming("generator-payload-async")
  public Subscriber<Integer> getFromInfiniteAsyncPayloadGenerator() {
    return ReactiveStreams.<Integer>builder()
      .limit(10)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .forEach(s -> add("generator-payload-async", s))
      .build();
  }

  @Incoming("generator-message-async")
  public Subscriber<Message<Integer>> getFromInfiniteAsyncMessageGenerator() {
    return ReactiveStreams.<Message<Integer>>builder()
      .limit(10)
      .map(Message::getPayload)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .forEach(s -> add("generator-message-async", s))
      .build();
  }

  private void add(String key, String value) {
    collector.computeIfAbsent(key, x -> new CopyOnWriteArrayList<>()).add(value);
  }

  void verify() {
    assertThat(collector).hasSize(10).allSatisfy((k, v) -> assertThat(v).containsExactlyElementsOf(EXPECTED));
  }

}
