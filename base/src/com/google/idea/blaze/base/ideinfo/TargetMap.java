/*
 * Copyright 2016 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.idea.blaze.base.ideinfo;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.devtools.intellij.model.ProjectData;
import javax.annotation.Nullable;

/** Map of configured targets (and soon aspects). */
public final class TargetMap implements ProtoWrapper<ProjectData.TargetMap> {
  private final ImmutableMap<TargetKey, TargetIdeInfo> targetMap;

  public TargetMap(ImmutableMap<TargetKey, TargetIdeInfo> targetMap) {
    this.targetMap = targetMap;
  }

  public static TargetMap fromProto(ProjectData.TargetMap proto) {
    return new TargetMap(
        proto.getTargetsList().stream()
            .map(TargetIdeInfo::fromProto)
            .collect(ImmutableMap.toImmutableMap(TargetIdeInfo::getKey, Functions.identity())));
  }

  @Override
  public ProjectData.TargetMap toProto() {
    ProjectData.TargetMap.Builder builder = ProjectData.TargetMap.newBuilder();
    targetMap.values().stream().map(TargetIdeInfo::toProto).forEach(builder::addTargets);
    return builder.build();
  }

  @Nullable
  public TargetIdeInfo get(TargetKey key) {
    return targetMap.get(key);
  }

  public boolean contains(TargetKey key) {
    return targetMap.containsKey(key);
  }

  public ImmutableCollection<TargetIdeInfo> targets() {
    return targetMap.values();
  }

  public ImmutableMap<TargetKey, TargetIdeInfo> map() {
    return targetMap;
  }
}
