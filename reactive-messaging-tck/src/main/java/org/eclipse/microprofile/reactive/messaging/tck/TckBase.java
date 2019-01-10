package org.eclipse.microprofile.reactive.messaging.tck;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import java.util.ServiceLoader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RunWith(Arquillian.class)
public abstract class TckBase {

  public static Executor EXECUTOR = Executors.newSingleThreadExecutor();

  public static JavaArchive getBaseArchive() {
    JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
      .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

    ServiceLoader.load(ArchiveExtender.class).iterator().forEachRemaining(ext -> ext.extend(archive));

    return archive;
  }

}
