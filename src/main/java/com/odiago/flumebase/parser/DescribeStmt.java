/**
 * Licensed to Odiago, Inc. under one or more contributor license
 * agreements.  See the NOTICE.txt file distributed with this work for
 * additional information regarding copyright ownership.  Odiago, Inc.
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.odiago.flumebase.parser;

import com.odiago.flumebase.plan.DescribeNode;
import com.odiago.flumebase.plan.PlanContext;

/**
 * DESCRIBE statement.
 */
public class DescribeStmt extends SQLStatement {
  /** The object to describe. */
  private String mIdentifier;

  public DescribeStmt(String identifier) {
    mIdentifier = identifier;
  }

  public String getIdentifier() {
    return mIdentifier;
  }

  @Override
  public void format(StringBuilder sb, int depth) {
    pad(sb, depth);
    sb.append("DESCRIBE mId=[" + mIdentifier + "]\n");
  }

  @Override
  public PlanContext createExecPlan(PlanContext planContext) {
    planContext.getFlowSpec().addRoot(new DescribeNode(mIdentifier));
    return planContext;
  }
}
