/*
 * Copyright © 2020  camunda services GmbH (info@camunda.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.atomix.raft.snapshot;

import io.zeebe.util.CloseableSilently;
import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * Represents an snapshot chunk reader, which means it is used to chunk an {@link PersistedSnapshot}
 * and read it from it's persisted storage.
 */
public interface SnapshotChunkReader extends Iterator<SnapshotChunk>, CloseableSilently {

  /**
   * Skips all chunks up to the one with the given ID, inclusively, such that the next chunk would
   * be the chunk right after it (if any). If no ID is given then should not do anything.
   *
   * @param id the chunk ID to seek to; maybe null
   */
  void seek(ByteBuffer id);

  /**
   * Returns the next chunk ID; if {@link #hasNext()} should return false, then this will return
   * null.
   *
   * @return the next chunk ID
   */
  ByteBuffer nextId();
}
