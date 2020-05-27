/*
 * Copyright © 2020 camunda services GmbH (info@camunda.com)
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
/// *
// * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
// * one or more contributor license agreements. See the NOTICE file distributed
// * with this work for additional information regarding copyright ownership.
// * Licensed under the Zeebe Community License 1.0. You may not use this file
// * except in compliance with the Zeebe Community License 1.0.
// */
// package io.atomix.raft.impl.zeebe.snapshot;
//
// import static java.nio.file.StandardOpenOption.CREATE_NEW;
//
// import io.atomix.raft.snapshot.SnapshotStore;
// import io.zeebe.util.ChecksumUtil;
// import io.zeebe.util.FileUtil;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.StandardOpenOption;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;
// import org.slf4j.Logger;
//
// final class FileSnapshotConsumer implements SnapshotConsumer {
//
//  private final SnapshotStore store;
//  private final Logger logger;
//
//  FileSnapshotConsumer(final SnapshotStore store, final Logger logger) {
//    this.store = store;
//    this.logger = logger;
//  }
//
//  @Override
//  public boolean consumeSnapshotChunk(final SnapshotChunk chunk) {
//    try {
//      return writeChunkToDisk(chunk, store);
//    } catch (final IOException e) {
//      logger.error("Failed to write snapshot chunk {} to disk", chunk, e);
//      return false;
//    }
//  }
//
//  @Override
//  public boolean completeSnapshot(final String snapshotId, final long snapshotChecksum) {
//    return verifySnapshot(snapshotId, snapshotChecksum)
//        &&
// storage.getPendingDirectoryFor(snapshotId).flatMap(storage::commitSnapshot).isPresent();
//  }
//
//  @Override
//  public void invalidateSnapshot(final String snapshotId) {
//    storage.getPendingDirectoryFor(snapshotId).ifPresent(this::deletePendingSnapshot);
//  }
//
//  private boolean verifySnapshot(final String snapshotId, final long snapshotChecksum) {
//    final Optional<Path> snapshot = storage.getPendingDirectoryFor(snapshotId);
//    if (snapshot.isEmpty()) {
//      return false;
//    }
//
//    try (final Stream<Path> chunkPaths = Files.list(snapshot.get()).sorted()) {
//      final List<Path> paths = chunkPaths.collect(Collectors.toList());
//      return ChecksumUtil.createCombinedChecksum(paths) == snapshotChecksum;
//    } catch (final IOException e) {
//      logger.trace("Could not verify snapshot checksum. ", e);
//      return false;
//    }
//  }
//
//  private void deletePendingSnapshot(final Path pendingDirectory) {
//    try {
//      if (Files.exists(pendingDirectory)) {
//        FileUtil.deleteFolder(pendingDirectory);
//      }
//    } catch (final IOException e) {
//      logger.error("Could not delete temporary snapshot directory {}", pendingDirectory, e);
//    }
//  }
//
//  private boolean writeChunkToDisk(final SnapshotChunk snapshotChunk, final SnapshotStore store)
//      throws IOException {
//    final String snapshotId = snapshotChunk.getSnapshotId();
//    final String chunkName = snapshotChunk.getChunkName();
//
//    if (storage.exists(snapshotId)) {
//      logger.debug(
//          "Ignore snapshot chunk {}, because snapshot {} already exists.", chunkName, snapshotId);
//      return true;
//    }
//
//    final long expectedChecksum = snapshotChunk.getChecksum();
//    final long actualChecksum = SnapshotChunkUtil.createChecksum(snapshotChunk.getContent());
//
//    if (expectedChecksum != actualChecksum) {
//      logger.warn(
//          "Expected to have checksum {} for snapshot chunk file {} ({}), but calculated {}",
//          expectedChecksum,
//          chunkName,
//          snapshotId,
//          actualChecksum);
//      return false;
//    }
//
//    final var optionalPath = storage.getPendingDirectoryFor(snapshotId);
//    if (optionalPath.isEmpty()) {
//      logger.warn("Failed to obtain pending snapshot directory for snapshot ID {}", snapshotId);
//      return false;
//    }
//
//    final var tmpSnapshotDirectory = optionalPath.get();
//    FileUtil.ensureDirectoryExists(tmpSnapshotDirectory);
//
//    final var snapshotFile = tmpSnapshotDirectory.resolve(chunkName);
//    if (Files.exists(snapshotFile)) {
//      logger.debug("Received a snapshot chunk which already exist '{}'.", snapshotFile);
//      return false;
//    }
//
//    logger.debug("Consume snapshot chunk {} of snapshot {}", chunkName, snapshotId);
//    return writeReceivedSnapshotChunk(snapshotChunk, snapshotFile);
//  }
//
//  private boolean writeReceivedSnapshotChunk(
//      final SnapshotChunk snapshotChunk, final Path snapshotFile) throws IOException {
//    Files.write(snapshotFile, snapshotChunk.getContent(), CREATE_NEW, StandardOpenOption.WRITE);
//    logger.trace("Wrote replicated snapshot chunk to file {}", snapshotFile);
//    return true;
//  }
// }
