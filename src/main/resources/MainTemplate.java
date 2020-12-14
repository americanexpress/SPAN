/**
 * Copyright 2020 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package %PACKAGE_NAME%;

import com.com.americanexpress.span.core.SPANInitialization;
import com.com.americanexpress.span.core.database.SPExecutor;

import java.sql.SQLException;

public class Main {

    private static final String SPAN_CONFIG = "SPANConfig.yaml";
    private static final String SP_ID = "%SP_ID%";

    public static void main(String[] args) throws SQLException {
        SPANInitialization.initialize(new PropertyConfiguration() {
            public String getSPANConfigFileName() {
                return SPAN_CONFIG;
            }
        });

        SPInput spInput = new SPInput();
%SPINPUT_FIELDS%

                SPOutput spOutput = new SPExecutor().execute(SP_ID, spInput, SPOutput.class);

        System.out.println("Stored Procedure Output...");
        System.out.println("--------------------------");
        System.out.println(spOutput);
        System.out.println();

        System.out.println("Stored Procedure ResultSet..");
        System.out.println("----------------------------");
        spOutput.getListRSObj().stream().forEach(spResultSetRow -> {
            System.out.println(spResultSetRow);
        });


    }
}