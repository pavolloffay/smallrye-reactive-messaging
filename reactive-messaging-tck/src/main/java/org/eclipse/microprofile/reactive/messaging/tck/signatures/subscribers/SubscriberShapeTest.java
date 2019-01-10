package org.eclipse.microprofile.reactive.messaging.tck.signatures.subscribers;

import org.eclipse.microprofile.reactive.messaging.tck.TckBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import javax.inject.Inject;

public class SubscriberShapeTest extends TckBase {

  @Deployment
  public static Archive<JavaArchive> deployment() {
    return getBaseArchive()
      .addClasses(SubscriberBean.class);
  }

  @Inject
  SubscriberBean verifier;

  @Test
  public void verifySubscriberSignatures() {
    verifier.verify();
  }
}
