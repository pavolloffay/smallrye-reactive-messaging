package org.eclipse.microprofile.reactive.messaging.tck;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ServiceLoader;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class SimpleIncomingTest {
  @Deployment(testable = false)
  public static Archive<JavaArchive> deployment() {
    JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
      .addClasses(SimpleIncomingBean.class, ValueCollector.class, StringSource.class)
      .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

    ServiceLoader.load(ArchiveExtender.class).iterator().forEachRemaining(ext -> ext.extend(archive));
    return archive;
  }

  @Inject
  private SimpleIncomingBean simple;

  @Test
  public void testReceptionWithValues() {
    assertThat(simple.getValues()).containsExactlyElementsOf(StringSource.VALUES);
  }

  @Test
  public void testReceptionWithEmpty() {

  }


}
