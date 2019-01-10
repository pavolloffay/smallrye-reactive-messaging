package org.eclipse.microprofile.reactive.messaging.tck.signatures.publishers;

import org.eclipse.microprofile.reactive.messaging.tck.TckBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import javax.inject.Inject;


public class PublisherShapeTest extends TckBase {

  @Deployment
  public static Archive<JavaArchive> deployment() {
    return getBaseArchive()
      .addClasses(PublisherBean.class, VerifierForPublisherBean.class);
  }

  @Inject
  VerifierForPublisherBean verifier;

  @Test
  public void verifyPublisherSignatures() {
    verifier.verify();
  }

}
