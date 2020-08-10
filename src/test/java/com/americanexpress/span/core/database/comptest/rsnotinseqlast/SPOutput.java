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
package com.americanexpress.span.core.database.comptest.rsnotinseqlast;

import com.americanexpress.span.annotation.ResultSet;

import java.util.List;

public class SPOutput {


    @ResultSet(seqNum = 1)
    List<SPResultSet> listRSObj;

    @ResultSet(seqNum = 2)
    List<SPResultSet> listRSObj2;

    @ResultSet(seqNum = 4)
    List<SPResultSet> listRSObj3;


    public List<SPResultSet> getListRSObj() {
        return listRSObj;
    }

    public void setListRSObj(List<SPResultSet> listRSObj) {
        this.listRSObj = listRSObj;
    }

    public List<SPResultSet> getListRSObj2() {
        return listRSObj2;
    }

    public void setListRSObj2(List<SPResultSet> listRSObj2) {
        this.listRSObj2 = listRSObj2;
    }

    public List<SPResultSet> getListRSObj3() {
        return listRSObj3;
    }

    public void setListRSObj3(List<SPResultSet> listRSObj3) {
        this.listRSObj3 = listRSObj3;
    }

    @Override
    public String toString() {
        return "SPOutput{" +
                "listRSObj=" + listRSObj +
                ", listRSObj2=" + listRSObj2 +
                ", listRSObj3=" + listRSObj3 +
                '}';
    }
}
