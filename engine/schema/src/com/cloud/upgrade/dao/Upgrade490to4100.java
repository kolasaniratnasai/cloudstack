// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.cloud.upgrade.dao;

import com.cloud.utils.exception.CloudRuntimeException;
import com.cloud.utils.script.Script;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Connection;

public class Upgrade490to4100 implements DbUpgrade {
    final static Logger LOG = Logger.getLogger(Upgrade490to4100.class);

    @Override
    public String[] getUpgradableVersionRange() {
        return new String[] {"4.9.0", "4.10.0"};
    }

    @Override
    public String getUpgradedVersion() {
        return "4.10.0";
    }

    @Override
    public boolean supportsRollingUpgrade() {
        return false;
    }

    @Override
    public File[] getPrepareScripts() {
        String script = Script.findScript("", "db/schema-490to4100.sql");
        if (script == null) {
            throw new CloudRuntimeException("Unable to find db/schema-490to4100.sql");
        }
        return new File[] {new File(script)};
    }

    @Override
    public void performDataMigration(Connection conn) {
    }

    @Override
    public File[] getCleanupScripts() {
        String script = Script.findScript("", "db/schema-490to4100-cleanup.sql");
        if (script == null) {
            throw new CloudRuntimeException("Unable to find db/schema-490to4100-cleanup.sql");
        }
        return new File[] {new File(script)};
    }
}
