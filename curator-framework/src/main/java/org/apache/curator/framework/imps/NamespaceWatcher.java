/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.curator.framework.imps;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

class NamespaceWatcher implements Watcher
{
    private final CuratorFrameworkImpl client;
    private final Watcher actualWatcher;
    private final CuratorWatcher curatorWatcher;

    NamespaceWatcher(CuratorFrameworkImpl client, Watcher actualWatcher)
    {
        this.client = client;
        this.actualWatcher = actualWatcher;
        this.curatorWatcher = null;
    }

    NamespaceWatcher(CuratorFrameworkImpl client, CuratorWatcher curatorWatcher)
    {
        this.client = client;
        this.actualWatcher = null;
        this.curatorWatcher = curatorWatcher;
    }

    @Override
    public void process(WatchedEvent event)
    {
        if ( actualWatcher != null )
        {
            actualWatcher.process(new NamespaceWatchedEvent(client, event));
        }
        else if ( curatorWatcher != null )
        {
            try
            {
                curatorWatcher.process(new NamespaceWatchedEvent(client, event));
            }
            catch ( Exception e )
            {
                client.logError("Watcher exception", e);
            }
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamespaceWatcher that = (NamespaceWatcher) o;

        if (actualWatcher != null ? !actualWatcher.equals(that.actualWatcher) : that.actualWatcher != null)
            return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (curatorWatcher != null ? !curatorWatcher.equals(that.curatorWatcher) : that.curatorWatcher != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = client != null ? client.hashCode() : 0;
        result = 31 * result + (actualWatcher != null ? actualWatcher.hashCode() : 0);
        result = 31 * result + (curatorWatcher != null ? curatorWatcher.hashCode() : 0);
        return result;
    }
}
