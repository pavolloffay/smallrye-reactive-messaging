package org.eclipse.microprofile.reactive.messaging.tck.signatures.publishers;

import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.eclipse.microprofile.reactive.messaging.tck.TckBase.EXECUTOR;

@ApplicationScoped
public class PublisherBean {

  @Outgoing("publisher-message")
  public Publisher<Message<String>> getAPublisherProducingMessage() {
    return ReactiveStreams.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .map(Message::of)
      .buildRs();
  }

  @Outgoing("publisher-payload")
  public Publisher<String> getAPublisherProducingPayload() {
    return ReactiveStreams.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .buildRs();
  }

  @Outgoing("publisher-builder-message")
  public PublisherBuilder<Message<String>> getAPublisherBuilderProducingMessage() {
    return ReactiveStreams.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i))
      .map(Message::of);
  }

  @Outgoing("publisher-builder-payload")
  public PublisherBuilder<String> getAPublisherBuilderProducingPayload() {
    return ReactiveStreams.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
      .flatMap(i -> ReactiveStreams.of(i, i))
      .map(i -> Integer.toString(i));
  }

  @Outgoing("publisher-flowable-message")
  public Flowable<Message<String>> getASubclassOfPublisherProducingMessage() {
    return getASubclassOfPublisherProducingPayload().map(Message::of);
  }

  @Outgoing("publisher-flowable-payload")
  public Flowable<String> getASubclassOfPublisherProducingPayload() {
    return Flowable.range(1, 10).flatMap(i -> Flowable.just(i, i)).map(i -> Integer.toString(i));
  }

  private AtomicInteger generatorPayload = new AtomicInteger();
  private AtomicInteger generatorMessage = new AtomicInteger();
  private AtomicInteger generatorAsyncMessage = new AtomicInteger();
  private AtomicInteger generatorAsyncPayload = new AtomicInteger();

  @Outgoing("generator-payload")
  public int getPayloads() {
    return generatorPayload.incrementAndGet();
  }


  @Outgoing("generator-message")
  public Message<Integer> getMessage() {
    return Message.of(generatorMessage.incrementAndGet());
  }


  @Outgoing("generator-message-async")
  public CompletionStage<Message<Integer>> getMessageAsync() {
    return CompletableFuture.supplyAsync(() -> Message.of(generatorAsyncMessage.incrementAndGet()), EXECUTOR);
  }

  @Outgoing("generator-payload-async")
  public CompletionStage<Integer> getPayloadAsync() {
    return CompletableFuture.supplyAsync(() -> generatorAsyncPayload.incrementAndGet(), EXECUTOR);
  }

}
