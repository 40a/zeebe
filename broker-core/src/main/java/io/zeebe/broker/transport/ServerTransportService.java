/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.0. You may not use this file
 * except in compliance with the Zeebe Community License 1.0.
 */
package io.zeebe.broker.transport;

import io.zeebe.broker.transport.commandapi.CommandApiMessageHandler;
import io.zeebe.servicecontainer.Service;
import io.zeebe.servicecontainer.ServiceStartContext;
import io.zeebe.servicecontainer.ServiceStopContext;
import io.zeebe.transport.Loggers;
import io.zeebe.transport.ServerTransport;
import io.zeebe.transport.Transports;
import io.zeebe.transport.impl.memory.NonBlockingMemoryPool;
import io.zeebe.util.ByteValue;
import io.zeebe.util.sched.ActorScheduler;
import java.io.Closeable;
import java.net.InetSocketAddress;
import org.slf4j.Logger;

public class ServerTransportService implements Service<ServerTransport> {
  public static final Logger LOG = Loggers.TRANSPORT_LOGGER;

  private final String readableName;
  private final InetSocketAddress bindAddress;
  private final ByteValue sendBufferSize;

  private CommandApiMessageHandler commandApiMessageHandler;
  private ServerTransport serverTransport;

  public ServerTransportService(
      final String readableName,
      final InetSocketAddress bindAddress,
      final ByteValue sendBufferSize,
      final CommandApiMessageHandler commandApiMessageHandler) {
    this.readableName = readableName;
    this.bindAddress = bindAddress;
    this.sendBufferSize = sendBufferSize;
    this.commandApiMessageHandler = commandApiMessageHandler;
  }

  @Override
  public void start(final ServiceStartContext serviceContext) {
    final ActorScheduler scheduler = serviceContext.getScheduler();

    serverTransport =
        Transports.newServerTransport()
            .name(readableName)
            .bindAddress(bindAddress)
            .scheduler(scheduler)
            .messageMemoryPool(new NonBlockingMemoryPool(sendBufferSize))
            .build(commandApiMessageHandler, commandApiMessageHandler);

    LOG.info("Bound {} to {}", readableName, bindAddress);
  }

  @Override
  public void stop(final ServiceStopContext serviceStopContext) {
    serviceStopContext.async(serverTransport.closeAsync());
  }

  @Override
  public ServerTransport get() {
    return serverTransport;
  }

  public Closeable getReleasingResourcesDelegate() {
    return () -> {
      if (serverTransport != null) {
        serverTransport.releaseResources();
      }
    };
  }
}
