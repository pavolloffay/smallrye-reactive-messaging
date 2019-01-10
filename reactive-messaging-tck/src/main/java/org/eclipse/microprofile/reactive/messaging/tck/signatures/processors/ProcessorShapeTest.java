package org.eclipse.microprofile.reactive.messaging.tck.signatures.processors;

import org.eclipse.microprofile.reactive.messaging.tck.TckBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import javax.inject.Inject;

public class ProcessorShapeTest extends TckBase {


  @Deployment
  public static Archive<JavaArchive> deployment() {
    return getBaseArchive()
      .addClasses(ProcessorBean.class, PublisherBean.class, DirectProcessorBean.class, TransformerBean.class);
  }

  @Inject
  ProcessorBean beanContainingMethodReturningProcessors;

  @Inject
  PublisherBean beanContainingMethodReturningPublishers;

  @Inject
  DirectProcessorBean beanManipulatingSingleElements;

  @Inject
  TransformerBean beanTransformingStreams;

  @Test
  public void verifySignaturesReturningProcessors() {
    beanContainingMethodReturningProcessors.verify();
  }

  @Test
  public void verifySignaturesReturningPublishers() {
    beanContainingMethodReturningPublishers.verify();
  }

  @Test
  public void verifySignaturesConsumingSingleElement() {
    beanManipulatingSingleElements.verify();
  }

  @Test
  public void verifySignatureConsumingAndProducingStreams() {
    beanTransformingStreams.verify();
  }
}
