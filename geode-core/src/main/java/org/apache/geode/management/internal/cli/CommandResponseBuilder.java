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
package org.apache.geode.management.internal.cli;

import org.apache.geode.management.cli.CliMetaData;
import org.apache.geode.management.internal.cli.json.GfJsonException;
import org.apache.geode.management.internal.cli.json.GfJsonObject;
import org.apache.geode.management.internal.cli.remote.CommandExecutionContext;
import org.apache.geode.management.internal.cli.result.CommandResult;

/**
 * 
 * @since GemFire 7.0
 */
public class CommandResponseBuilder {

  public static CommandResponse prepareCommandResponse(String memberName, CommandResult result) {
    GfJsonObject content;
    content = result.getContent();
    return new CommandResponse(memberName, getType(result), result.getStatus().getCode(), "1/1",
        CliMetaData.ANNOTATION_NULL_VALUE, getDebugInfo(result), result.getHeader(), content,
        result.getFooter(), result.failedToPersist(), result.getFileToDownload());
  }

  // De-serializing to CommandResponse
  public static CommandResponse prepareCommandResponseFromJson(String jsonString) {
    GfJsonObject jsonObject;
    try {
      jsonObject = new GfJsonObject(jsonString);
    } catch (GfJsonException e) {
      jsonObject = GfJsonObject.getGfJsonErrorObject(CliUtil.stackTraceAsString(e));
    }
    return new CommandResponse(jsonObject);
  }

  public static String getCommandResponseJson(CommandResponse commandResponse) {
    return new GfJsonObject(commandResponse).toString();
  }

  public static String createCommandResponseJson(String memberName, CommandResult result) {
    return getCommandResponseJson(prepareCommandResponse(memberName, result));
  }

  private static String getType(CommandResult result) {
    return result.getType();
  }

  private static String getDebugInfo(CommandResult result) {
    String debugInfo = "";
    if (CommandExecutionContext.isSetWrapperThreadLocal()) {
      CommandResponseWriter responseWriter = CommandExecutionContext.getCommandResponseWriter();
      debugInfo = responseWriter.getResponseWritten();
    }

    return debugInfo;
  }
}
