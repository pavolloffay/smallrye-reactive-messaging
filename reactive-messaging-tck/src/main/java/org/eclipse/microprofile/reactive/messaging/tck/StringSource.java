package org.eclipse.microprofile.reactive.messaging.tck;

import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import javax.enterprise.context.Dependent;
import java.util.Arrays;
import java.util.List;

@Dependent
public class StringSource {

  public static final List<String> VALUES = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "e", "j");

  @Outgoing("strings")
  public Publisher<String> strings() {
    return Flowable.fromIterable(VALUES);
  }

}
