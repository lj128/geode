/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.internal.cache.wan.serial;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import org.apache.geode.cache.client.internal.Connection;
import org.apache.geode.cache.client.internal.pooling.ConnectionDestroyedException;
import org.apache.geode.cache.wan.GatewaySender;
import org.apache.geode.internal.Version;
import org.apache.geode.internal.cache.wan.AbstractGatewaySender;
import org.apache.geode.internal.cache.wan.GatewaySenderConfigurationException;
import org.apache.geode.internal.cache.wan.GatewaySenderEventCallbackDispatcher;
import org.apache.geode.internal.cache.wan.GatewaySenderEventDispatcher;
import org.apache.geode.internal.cache.wan.GatewaySenderEventRemoteDispatcher;
import org.apache.geode.internal.cache.wan.GatewaySenderException;
import org.apache.geode.internal.logging.LogService;

public class RemoteSerialGatewaySenderEventProcessor extends SerialGatewaySenderEventProcessor {

  private static final Logger logger = LogService.getLogger();

  public RemoteSerialGatewaySenderEventProcessor(AbstractGatewaySender sender, String id) {
    super(sender, id);
  }

  public void initializeEventDispatcher() {
    if (logger.isDebugEnabled()) {
      logger.debug(" Creating the GatewayEventRemoteDispatcher");
    }
    // In case of serial there is a way to create gatewaySender and attach
    // asyncEventListener. Not sure of the use-case but there are dunit tests
    // To make them pass uncommenting the below condition
    if (this.sender.getRemoteDSId() != GatewaySender.DEFAULT_DISTRIBUTED_SYSTEM_ID) {
      this.dispatcher = new GatewaySenderEventRemoteDispatcher(this);
    } else {
      this.dispatcher = new GatewaySenderEventCallbackDispatcher(this);
    }
  }

  /**
   * Returns if corresponding receiver WAN site of this GatewaySender has GemfireVersion > 7.0.1
   *
   * @param disp
   * @return true if remote site Gemfire Version is >= 7.0.1
   */
  protected boolean shouldSendVersionEvents(GatewaySenderEventDispatcher disp)
      throws GatewaySenderException {
    return true;
  }

}
