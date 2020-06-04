/*
 * Copyright © 2019 camunda services GmbH (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zeebe.config;

import java.time.Duration;

public class TTStarterCfg {

  private String processId;
  private int rate;
  private int threads;

  private WorkerCfg worker;
  private int numTasks;
  private Duration instanceTimeout;
  private String payloadPath;

  public String getPayloadPath() {
    return payloadPath;
  }

  public void setPayloadPath(final String payloadPath) {
    this.payloadPath = payloadPath;
  }

  public String getProcessId() {
    return processId;
  }

  public void setProcessId(final String processId) {
    this.processId = processId;
  }

  public int getRate() {
    return rate;
  }

  public void setRate(final int rate) {
    this.rate = rate;
  }

  public int getThreads() {
    return threads;
  }

  public void setThreads(final int threads) {
    this.threads = threads;
  }

  public WorkerCfg getWorker() {
    return this.worker;
  }

  public void setWorker(final WorkerCfg worker) {
    this.worker = worker;
  }

  public int getNumTasks() {
    return this.numTasks;
  }

  public void setNumTasks(final int numTasks) {
    this.numTasks = numTasks;
  }

  public Duration getInstanceTimeout() {
    return this.instanceTimeout;
  }

  public void setInstanceTimeout(final Duration instanceTimeout) {
    this.instanceTimeout = instanceTimeout;
  }
}
