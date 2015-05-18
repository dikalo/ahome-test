/*
 * Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.ahome.server.rpc.commands

import groovy.transform.CompileStatic

import org.springframework.stereotype.Service

import com.ait.tooling.json.JSONObject
import com.ait.tooling.server.core.pubsub.IPubSubDescriptor
import com.ait.tooling.server.core.pubsub.IPubSubMessageReceivedHandler
import com.ait.tooling.server.core.pubsub.MessageReceivedEvent
import com.ait.tooling.server.core.pubsub.PubSubChannelType
import com.ait.tooling.server.core.pubsub.PubSubNextEventActionType
import com.ait.tooling.server.rpc.IJSONRequestContext
import com.ait.tooling.server.rpc.JSONCommandSupport

@Service
@CompileStatic
public class GetLastEventCommand extends JSONCommandSupport
{
    private IPubSubDescriptor   m_pubsub

    private JSONObject          m_return = new JSONObject()

    @Override
    public JSONObject execute(final IJSONRequestContext context, final JSONObject object) throws Exception
    {
        if (null == m_pubsub)
        {
            m_pubsub = context.getServerContext().getPubSubDescriptorProvider().getPubSubDescriptor("CoreServerEvents", PubSubChannelType.EVENT)

            if (m_pubsub)
            {
                m_pubsub.addMessageReceivedHandler(new IPubSubMessageReceivedHandler()
                        {
                            @Override
                            public PubSubNextEventActionType onMesageReceived(final MessageReceivedEvent event)
                            {
                                GetLastEventCommand.this.setValue(event.getValue())

                                PubSubNextEventActionType.CONTINUE
                            }
                        })
            }
        }
        getValue()
    }
    
    public void setValue(JSONObject value)
    {
        m_return = value
    }
    
    public JSONObject getValue()
    {
        return m_return
    }
}
