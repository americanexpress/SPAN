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
package com.americanexpress.span.codegenerator;

import com.americanexpress.span.annotation.Field;
import com.americanexpress.span.core.SPANConfigHolder;
import com.americanexpress.span.core.SPANInitialization;
import com.americanexpress.span.core.database.connection.SPANDataSource;
import com.americanexpress.span.models.SPUserDefineKey;
import com.americanexpress.span.utility.PropertyConfiguration;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class SPANCodeGenerator {

    private static final String EOL = "\n";
    private static final String EOL_2 = EOL + EOL;
    private static final String POJO_TEMPLATE = "%PACKAGE%" + EOL +
            EOL +
            "%IMPORTS%" +
            EOL_2 +
            "public class %CLASS_NAME% {" +
            EOL +
            "%FIELDS%" +
            EOL +
            "%RESULTSET%" +
            EOL +
            "%FIELDSGETSET%" +
            EOL +
            "%TO_STRING_OVERRIDE%" +
            EOL +
            "}";
    private static final String FIELD_TEMPLATE = "    @Field(name = \"%ORIG_NAME%\")" + EOL +
            "    private %FIELD_TYPE% %FIELD_NAME%;" + EOL;

    private static final String GETTER_TEMPLATE = "    public %FIELD_TYPE% get%METHOD_NAME%() {" + EOL +
            "        return %FIELD_NAME%;" + EOL +
            "    }";

    private static final String SETTER_TEMPLATE = "    public void set%METHOD_NAME%(%FIELD_TYPE% %FIELD_NAME%) {" + EOL +
            "        this.%FIELD_NAME% = %FIELD_NAME%;" + EOL +
            "    }";

    private static final String RS_TEMPLATE = "    @ResultSet%SEQ_NUM%" + EOL +
            "    List<SPResultSet%COUNT%> listRSObj%COUNT%;";

    private static final String TO_STRING = EOL + "    @java.lang.Override"
            + EOL +
            "    public java.lang.String toString() {"
            + EOL +
            "        return \"%CLASS_NAME% {\" +"
            + EOL +
            "%TO_STRING_FIELD%"
            + EOL +
            "                '}';"
            + EOL +
            "    }" + EOL;
    private static final String TO_STRING_FIELD = "                \"%FIELD_NAME%='\" + %FIELD_NAME% + '\\\\\\\\'' +";


    // Constants for Template objects. These constants will be used to replace with actual values inside a tempalte.
    private static final String FIELD_TYPE = "%FIELD_TYPE%";
    private static final String FIELD_NAME = "%FIELD_NAME%";
    private static final String METHOD_NAME = "%METHOD_NAME%";
    private static final String ORIG_NAME = "%ORIG_NAME%";
    private static final String PACKAGE = "%PACKAGE%";
    private static final String RESULT_SET = "%RESULTSET%";
    private static final String RESULT_SET_GETSET = "%RESULTSETGETSET%";


    //Other Constants
    private static final String SPAN_OUTPUT_FOLDER = "./output/";
    private static final String SRC_MAIN_JAVA = "src/main/java/";
    private static final String PROJECT = "project/";
    private static final String LIST_RS_OBJ = "listRSObj";
    private static final String SEQ_NUM = "%SEQ_NUM%";
    private static final String COUNT = "%COUNT%";


    //Values derived from program input. These are used as global variables in this class.
    private static String outputCodeFolder = null;
    private static String configFile = null;
    private static String packageName = null;
    private static String inputFileGenerationMode = null;
    private static String storedProcId = null;
    private static String inputFileName;


    //Stored Procedure and ResultSet metadata.
    private static List<FieldDetails> inList = new ArrayList<>();
    private static List<FieldDetails> outList = new ArrayList<>();
    private static List<List<FieldDetails>> listOfRSMetaData = new ArrayList<>();
    private static List<List<List<FieldDetailsWithValues>>> resultSetAllData = new ArrayList<>();

    @SuppressWarnings("PMD")
    private static final PrintStream STD_ERR = System.err;
    @SuppressWarnings("PMD")
    private static final PrintStream STD_OUT = System.out;


    /**
     * Private Constructor
     */
    private SPANCodeGenerator() {

    }

    /**
     * Represent metadata of each field from Input, output, and resultset objects.
     */
    private static class FieldDetails {
        private final String origColumnName;
        private final String columnName;
        private final Class columnDataType;

        @Override
        public String toString() {
            return "FieldDetails{" +
                    "origColumnName='" + origColumnName + '\'' +
                    ", columnName='" + columnName + '\'' +
                    ", columnDataType='" + columnDataType + '\'' +
                    '}';
        }

        FieldDetails(final String origColumnName, final Class columnDataType) {
            this.origColumnName = origColumnName;
            this.columnName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, origColumnName.replaceAll("^O_", "").replaceAll("^I_", ""));
            this.columnDataType = columnDataType;


        }
    }

    /**
     * Represent metadata and values for each field from Resultset.
     */
    private static class FieldDetailsWithValues extends FieldDetails {

        private final Object value;

        FieldDetailsWithValues(final String origColumnName, final Class columnDataType, final Object value) {
            super(origColumnName, columnDataType);
            this.value = value;
        }

        @Override
        public String toString() {
            return super.toString() + ". FieldDetailsWithValues{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }


    public static void main(final String[] args) {
        boolean parseSuccess = parseCommandLineParameters(args);
        try {
            if (parseSuccess) {
                outputCodeFolder = SPAN_OUTPUT_FOLDER + PROJECT + SRC_MAIN_JAVA + packageName.replaceAll("\\.", "/") + "/";
                boolean folderCreated = new File(outputCodeFolder).mkdirs();


                if (folderCreated) {
                    STD_ERR.println(outputCodeFolder + " created...");
                }
                SPANInitialization.initialize(new PropertyConfiguration() {
                    public String getSPANConfigFileName() {
                        return configFile;
                    }
                });
                createSPInputOutput(storedProcId);

                String pojoInput = createGetterSetterPojo(inList, new ArrayList<>(), "SPInput");
                pojoInput = pojoInput.replaceAll(RESULT_SET, "");
                pojoInput = pojoInput.replaceAll(RESULT_SET_GETSET, "");
                try (FileWriter fileWriter = new FileWriter(outputCodeFolder + "SPInput.java")) {
                    fileWriter.write(pojoInput);
                }

                STD_OUT.println(outputCodeFolder + "SPInput.java has been created to use with SPAN Framework...");

                String pojoOutput = createGetterSetterPojo(outList, listOfRSMetaData, "SPOutput");
                try (FileWriter fileWriter = new FileWriter(outputCodeFolder + "SPOutput.java")) {
                    fileWriter.write(pojoOutput);
                }

                STD_OUT.println(outputCodeFolder + "SPOutput.java has been created to use with SPAN Framework. Don't use it directly, " +
                        "please generate ResultSet POJOs before using it");


                if (!"Y".equalsIgnoreCase(inputFileGenerationMode)) {
                    createResultSetPOJO(storedProcId);

                    pojoOutput = createGetterSetterPojo(outList, listOfRSMetaData, "SPOutput");
                    try (FileWriter fileWriter = new FileWriter(outputCodeFolder + "SPOutput.java")) {
                        fileWriter.write(pojoOutput);
                    }

                    for (int i = 0; i < listOfRSMetaData.size(); i++) {
                        List<FieldDetails> rsMetaData = listOfRSMetaData.get(i);
                        pojoOutput = createGetterSetterPojo(rsMetaData, new ArrayList<>(), "SPResultSet");

                        if (i == 0) {
                            try (FileWriter fileWriter = new FileWriter(outputCodeFolder + "SPResultSet.java")) {
                                fileWriter.write(pojoOutput);
                            }
                        } else {
                            try (FileWriter fileWriter = new FileWriter(outputCodeFolder + "SPResultSet" + i + ".java")) {
                                fileWriter.write(pojoOutput);
                            }
                        }
                        int columnSize = 30;
                        STD_OUT.println(EOL + "\nStored Procedure Output ResultSet.... " + (i + 1));
                        STD_OUT.println("-----------------------------------------" + EOL);
                        for (FieldDetails resultSetData : listOfRSMetaData.get(i)) {
                            STD_OUT.format("%" + columnSize + "s", resultSetData.origColumnName);
                        }
                        STD_OUT.println();
                        STD_OUT.println(String.join("", Collections.nCopies(columnSize * listOfRSMetaData.get(i).size(), "-")));

                        for (List<FieldDetailsWithValues> resultSetData : resultSetAllData.get(i)) {
                            for (FieldDetailsWithValues fieldDetailsWithValues : resultSetData) {
                                if (fieldDetailsWithValues.value != null) {
                                    String fieldVal = String.valueOf(fieldDetailsWithValues.value);
                                    STD_OUT.format("%" + columnSize + "s",
                                            fieldVal.length() > columnSize ? fieldVal.substring(0, columnSize - 1) : fieldVal);
                                } else {
                                    STD_OUT.format("%" + columnSize + "s", "");
                                }
                            }
                            STD_OUT.println();
                        }
                    }

                    createMain();
                    createPOM();
                    createConfig();
                    STD_OUT.println();
                    STD_OUT.println(String.format("SPAN files have been created in folder \"" + outputCodeFolder + "\" and are ready to use.."));
                    STD_OUT.println();
                    STD_OUT.println();


                } else {
                    STD_OUT.println(String.format("%s has been created. " +
                            "Please populate values in the file and execute \"./GenerateSPResultSet.sh " + configFile + " " + storedProcId + " " + packageName
                            + "\" to generate ResultSet code.....", inputFileName));
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    private static void createMain() throws IOException {
        String mainTemplate = readClasspathFileToString("/MainTemplate.java");
        String mainCode = mainTemplate.replaceAll("%SP_ID%", storedProcId);
        Map<String, String> inputMap = readFileToMap(inputFileName);

        List<String> spInputs = new ArrayList<>();
        inList.stream().forEach(fieldDetails -> {
            spInputs.add("        spInput.set" + capitalize(fieldDetails.columnName) + "(\"" + inputMap.get(fieldDetails.origColumnName) + "\");");
        });

        mainCode = mainCode.replaceAll("%SPINPUT_FIELDS%", String.join(EOL, spInputs));

        mainCode = mainCode.replaceAll("%PACKAGE_NAME%", packageName);

        try (PrintWriter printWriter = new PrintWriter(new FileWriter(outputCodeFolder + "/Main.java"))) {
            printWriter.write(mainCode);
        }

        STD_OUT.println();
        STD_OUT.println();
        STD_OUT.println("Follow the steps below to use SPAN autogenerated classes.");
        STD_OUT.println("==============================================");
        STD_OUT.println();
        STD_OUT.println("//Initialize SPAN Framework. This should be done only once...");
        STD_OUT.println("SPANInitialization.initialize(\"SPANConfig.yaml\");");
        STD_OUT.println();
        STD_OUT.println("//Create and populate Input Object");
        STD_OUT.println("SPInput spInput = new SPInput()");
        STD_OUT.println("spInput.set...");
        STD_OUT.println();
        STD_OUT.println("//Execute the Stored Procedure");
        STD_OUT.println("SPOutput spOutput = new SPExecutor().execute(SP_ID, spInput, SPOutput.class);");
        STD_OUT.println();
        STD_OUT.println("//Fetch the Results");
        STD_OUT.println("spOutput.get...");
        STD_OUT.println("spOutput.getListRSObj()");

    }

    private static void createPOM() throws IOException {
        String pom = readClasspathFileToString("/pomTemplate.xml");
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(SPAN_OUTPUT_FOLDER + PROJECT + "/pom.xml"))) {
            printWriter.write(pom);
        }

    }

    private static void createConfig() throws IOException {
        String pom = readRelativePathFileToString(configFile);
        new File(SPAN_OUTPUT_FOLDER + PROJECT + "src/main/resources/").mkdirs();
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(SPAN_OUTPUT_FOLDER + PROJECT + "src/main/resources/SPANConfig.yaml"))) {
            printWriter.write(pom);
        }

    }


    private static void createSPInputOutput(final String spIdentifier) throws SQLException, IOException {
        DataSource dataSource = SPANDataSource.getInstance().getDataSource(spIdentifier);
        SPUserDefineKey spUserDefineKey = SPANConfigHolder.getInstance().getSPUserDefinedKey(spIdentifier);

        try (Connection conn = dataSource.getConnection()) {
            try (ResultSet resultSet = conn.getMetaData().getProcedureColumns(conn.getCatalog(), spUserDefineKey.getSchema(),
                    spUserDefineKey.getProcedure(), null)) {

                while (resultSet.next()) {

                    String columnName = resultSet.getString("COLUMN_NAME");
                    short columnType = resultSet.getShort("COLUMN_TYPE");
                    String columnDataTypeName = resultSet.getString("TYPE_NAME");
                    int columnByteLength = resultSet.getInt("LENGTH");
                    int columnPrecision = resultSet.getInt("PRECISION");

                    if (columnType == 1) {
                        inList.add(new FieldDetails(columnName, getClassType(columnDataTypeName, columnByteLength, columnPrecision)));
                    }
                    if (columnType == 4) {
                        outList.add(new FieldDetails(columnName, getClassType(columnDataTypeName, columnByteLength, columnPrecision)));
                    }

                }

                inputFileName = SPAN_OUTPUT_FOLDER + spIdentifier + ".input";
                if ("Y".equalsIgnoreCase(inputFileGenerationMode)) {
                    try (PrintWriter printWriter = new PrintWriter(new FileWriter(inputFileName))) {
                        inList.stream().forEach(fieldDetails -> {
                            printWriter.println(fieldDetails.origColumnName + "=");
                        });
                    }
                }

                String paramList = String.join(",", Collections.nCopies(inList.size() + outList.size(), "?"));
                String query = "{call " + spUserDefineKey.getSchema() + "." + spUserDefineKey.getProcedure() +
                        "(" + paramList + ")}";
                STD_OUT.println(EOL_2);
                STD_OUT.println("MetaData for SP: " + query);
                STD_OUT.println("-----------------------------------" + EOL + "\n");

                STD_OUT.println("Input Parameters");
                STD_OUT.println("----------------");
                inList.stream().forEach(fieldDetails -> {
                    STD_OUT.println(fieldDetails.origColumnName);
                });
                STD_OUT.println("-----------------------------------" + EOL + "\n");


                STD_OUT.println("Output Parameters");
                STD_OUT.println("----------------");
                outList.stream().forEach(fieldDetails -> {
                    STD_OUT.println(fieldDetails.origColumnName);
                });
                STD_OUT.println("-----------------------------------" + EOL + "\n");


            }

        }
    }


    private static String createGetterSetterPojo(List<FieldDetails> listForPOJO, List<List<FieldDetails>> listOfRSMetaData, String className) {
        String pojo = POJO_TEMPLATE;
        if (!StringUtils.isBlank(packageName)) {
            pojo = pojo.replaceAll(PACKAGE, "package " + packageName + ";");
        } else {
            pojo = pojo.replaceAll(PACKAGE, "");
        }


        List<String> fieldDefinitions = new ArrayList<>();
        List<String> getterSetters = new ArrayList<>();
        Set<String> imports = new HashSet<>();
        List<String> toStringFields = new ArrayList<>();
        if (!listForPOJO.isEmpty()) {
            imports.add("import " + Field.class.getCanonicalName() + ";");
        }
        listForPOJO.stream().forEach(fieldDetails -> {

            if (fieldDetails.columnDataType != String.class) {
                imports.add("import " + fieldDetails.columnDataType.getCanonicalName() + ";");
            }

            String fieldDefinition = FIELD_TEMPLATE;
            fieldDefinition = fieldDefinition.replaceAll(ORIG_NAME, fieldDetails.origColumnName);
            fieldDefinition = fieldDefinition.replaceAll(FIELD_TYPE, fieldDetails.columnDataType.getSimpleName());
            fieldDefinition = fieldDefinition.replaceAll(FIELD_NAME, fieldDetails.columnName);
            fieldDefinitions.add(fieldDefinition);

            String getterDefinition = GETTER_TEMPLATE;
            getterDefinition = getterDefinition.replaceAll(FIELD_TYPE, fieldDetails.columnDataType.getSimpleName());
            getterDefinition = getterDefinition.replaceAll(FIELD_NAME, fieldDetails.columnName);
            getterDefinition = getterDefinition.replaceAll(METHOD_NAME, capitalize(fieldDetails.columnName));
            getterSetters.add(getterDefinition);

            String setterDefinition = SETTER_TEMPLATE;
            setterDefinition = setterDefinition.replaceAll(FIELD_TYPE, fieldDetails.columnDataType.getSimpleName());
            setterDefinition = setterDefinition.replaceAll(FIELD_NAME, fieldDetails.columnName);
            setterDefinition = setterDefinition.replaceAll(METHOD_NAME, capitalize(fieldDetails.columnName));
            getterSetters.add(setterDefinition);

            String toStringField = TO_STRING_FIELD.replaceAll(FIELD_NAME, fieldDetails.columnName);
            toStringFields.add(toStringField);

        });

        if (!listOfRSMetaData.isEmpty()) {
            imports.add("import " + List.class.getCanonicalName() + ";");
            imports.add("import " + com.americanexpress.span.annotation.ResultSet.class.getCanonicalName() + ";");
        }

        for (int i = 0; i < listOfRSMetaData.size(); i++) {

            if (i == 0) {
                String fieldDefinition = RS_TEMPLATE;
                fieldDefinition = fieldDefinition.replaceAll(SEQ_NUM, "");
                fieldDefinition = fieldDefinition.replaceAll(COUNT, "");
                fieldDefinition = fieldDefinition.replaceAll(FIELD_TYPE, "List<SPResultSet>");
                fieldDefinition = fieldDefinition.replaceAll(FIELD_NAME, LIST_RS_OBJ);
                fieldDefinitions.add(fieldDefinition);

                String setterDefinition = GETTER_TEMPLATE;
                setterDefinition = setterDefinition.replaceAll(FIELD_TYPE, "List<SPResultSet>");
                setterDefinition = setterDefinition.replaceAll(FIELD_NAME, LIST_RS_OBJ);
                setterDefinition = setterDefinition.replaceAll(METHOD_NAME, capitalize(LIST_RS_OBJ));
                getterSetters.add(setterDefinition);

                String getterDefinition = SETTER_TEMPLATE;
                getterDefinition = getterDefinition.replaceAll(FIELD_TYPE, "List<SPResultSet>");
                getterDefinition = getterDefinition.replaceAll(FIELD_NAME, LIST_RS_OBJ);
                getterDefinition = getterDefinition.replaceAll(METHOD_NAME, capitalize(LIST_RS_OBJ));
                getterSetters.add(getterDefinition);
            } else {
                String fieldDefinition = RS_TEMPLATE;
                fieldDefinition = fieldDefinition.replaceAll(SEQ_NUM, "(seqNum = " + i + ")");
                fieldDefinition = fieldDefinition.replaceAll(COUNT, String.valueOf(i));
                fieldDefinition = fieldDefinition.replaceAll(FIELD_TYPE, "List<SPResultSet>");
                fieldDefinition = fieldDefinition.replaceAll(FIELD_NAME, LIST_RS_OBJ);
                fieldDefinitions.add(fieldDefinition);

                String setterDefinition = GETTER_TEMPLATE;
                setterDefinition = setterDefinition.replaceAll(FIELD_TYPE, "List<SPResultSet" + i + ">");
                setterDefinition = setterDefinition.replaceAll(FIELD_NAME, LIST_RS_OBJ + i);
                setterDefinition = setterDefinition.replaceAll(METHOD_NAME, capitalize(LIST_RS_OBJ + i));
                getterSetters.add(setterDefinition);

                String getterDefinition = SETTER_TEMPLATE;
                getterDefinition = getterDefinition.replaceAll(FIELD_TYPE, "List<SPResultSet" + i + ">");
                getterDefinition = getterDefinition.replaceAll(FIELD_NAME, LIST_RS_OBJ + i);
                getterDefinition = getterDefinition.replaceAll(METHOD_NAME, capitalize(LIST_RS_OBJ + i));
                getterSetters.add(getterDefinition);

            }

        }

        pojo = pojo.replaceAll(RESULT_SET, "");

        pojo = pojo.replaceAll("%FIELDS%", String.join(EOL_2, fieldDefinitions));

        pojo = pojo.replaceAll("%FIELDSGETSET%", String.join(EOL_2, getterSetters));

        pojo = pojo.replaceAll("%IMPORTS%", String.join(EOL, imports));

        String toString = TO_STRING.replaceAll("%TO_STRING_FIELD%", String.join(EOL, toStringFields));

        pojo = pojo.replaceAll("%TO_STRING_OVERRIDE%", toString);

        pojo = pojo.replaceAll("%CLASS_NAME%", className);

        return pojo;


    }


    private static void createResultSetPOJO(final String spIdentifier) throws SQLException, IOException {
        DataSource dataSource = SPANDataSource.getInstance().getDataSource(spIdentifier);
        SPUserDefineKey spUserDefineKey = SPANConfigHolder.getInstance().getSPUserDefinedKey(spIdentifier);

        try (Connection conn = dataSource.getConnection()) {
            String paramList = String.join(",", Collections.nCopies(inList.size() + outList.size(), "?"));
            Map<String, String> inputMap = readFileToMap(inputFileName);


            String query = "{call " + spUserDefineKey.getSchema() + "." + spUserDefineKey.getProcedure() +
                    "(" + paramList + ")}";
            STD_OUT.println("Executing Query: " + query + EOL_2);

            STD_OUT.println("Input Parameters");
            STD_OUT.println("----------------");
            try (CallableStatement stmt = conn.prepareCall(query)) {
                inList.stream().forEach(fieldDetails -> {
                    try {
                        STD_OUT.println(fieldDetails.origColumnName + ": " + inputMap.get(fieldDetails.origColumnName));
                        stmt.setObject(fieldDetails.origColumnName, inputMap.get(fieldDetails.origColumnName));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                STD_OUT.println("----------------------------------------------" + EOL + "\n");
                outList.stream().forEach(fieldDetails -> {
                    try {
                        stmt.registerOutParameter(fieldDetails.origColumnName, getDataType(fieldDetails.columnDataType));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });


                boolean moreResults = stmt.execute();

                STD_OUT.println("Stored Procedure Output.....");
                STD_OUT.println("Output Parameters");
                STD_OUT.println("-----------------");
                outList.stream().forEach(fieldDetails -> {
                    try {
                        STD_OUT.println(fieldDetails.origColumnName + ": " + stmt.getObject(fieldDetails.origColumnName));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                STD_OUT.println("----------------------------------------------" + EOL + "\n");

                int updateCount = stmt.getUpdateCount();
                while (moreResults || updateCount != -1) {
                    List<FieldDetails> rsMetaData = new ArrayList<>();
                    listOfRSMetaData.add(rsMetaData);
                    List<List<FieldDetailsWithValues>> singleResultSetRows = new ArrayList<>();
                    resultSetAllData.add(singleResultSetRows);
                    try (ResultSet resultSet = stmt.getResultSet()) {
                        if (resultSet != null) {
                            ResultSetMetaData metaData = resultSet.getMetaData();

                            for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                                rsMetaData.add(new FieldDetails(metaData.getColumnLabel(i), getClassType(metaData, i)));
                            }

                            while (resultSet.next()) {
                                List<FieldDetailsWithValues> resultSetData = new ArrayList<>();
                                singleResultSetRows.add(resultSetData);
                                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                                    resultSetData.add(new FieldDetailsWithValues(metaData.getColumnLabel(i), getClassType(metaData, i), resultSet.getObject(i)));
                                }
                            }
                        }
                        moreResults = stmt.getMoreResults();
                        updateCount = stmt.getUpdateCount();
                    }

                }

                if (listOfRSMetaData.size() != 0) {
                    STD_OUT.println("ResultSet Columns");
                    STD_OUT.println("--------------");
                    listOfRSMetaData.stream().forEach(rsDetails -> rsDetails.stream().forEach(fieldDetails -> {
                        STD_OUT.println(fieldDetails.origColumnName + ". Type:" + fieldDetails.columnDataType);
                    }));
                    STD_OUT.println("----------------------------------------------" + EOL + "\n");
                } else {
                    STD_OUT.println("There is no ResultSet returned form the Stored Procedure. " +
                            "Please check response codes from your stored Procedure if you were expecting a ResultSet");
                }


            }
        }
    }


    public static String capitalize(String str) {
        if (str == null) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static boolean parseCommandLineParameters(final String[] args) {
        String arg;
        int index = 0;
        while (index < args.length && args[index].startsWith("-")) {
            arg = args[index++];

            if (arg.equals("-configFile")) {
                if (index < args.length) {
                    configFile = args[index++];
                } else {
                    STD_ERR.println("-configFile is mandatory");
                    return false;
                }
            }

            if (arg.equals("-package")) {
                if (index < args.length) {
                    packageName = args[index++];
                } else {
                    STD_ERR.println("-packageName is mandatory");
                    return false;
                }
            }

            if (arg.equals("-storedProcId")) {
                if (index < args.length) {
                    storedProcId = args[index++];
                } else {
                    STD_ERR.println("-storedProcId is mandatory. It is identifier for Stored Procedure in SPAN Configuration file.");
                    return false;
                }
            }


            if (arg.equals("-inputFileGenerationMode")) {
                if (index < args.length) {
                    inputFileGenerationMode = args[index++];
                } else {
                    STD_ERR.println("-inputFileGenerationMode requires a <Y|N>");
                    return false;
                }
            }
        }
        if (configFile == null || packageName == null || inputFileGenerationMode == null || storedProcId == null) {
            STD_ERR.println("Usage: SPANCodeGenerator -configFile <configFile> -storedProcId <storedProcId> -packageName <packageName> -inputFileGenerationMode <Y|N>");
            return false;
        }
        return true;
    }


    private static Class getClassType(ResultSetMetaData metaData, int columnIndex) throws SQLException {
        String dataType = metaData.getColumnTypeName(columnIndex);
        Class dataTypeClass = String.class;
        if ("DECIMAL".equals(dataType)) {
            if (metaData.getPrecision(columnIndex) > 8) {
                dataTypeClass = BigDecimal.class;
            } else {
                if (metaData.getScale(columnIndex) > 0) {
                    dataTypeClass = Float.class;
                } else {
                    dataTypeClass = Integer.class;
                }
            }
        }
        return dataTypeClass;
    }

    private static Class getClassType(String dataType, int precision, int scale) {
        Class dataTypeClass = String.class;
        if ("DECIMAL".equals(dataType)) {
            if (precision > 8) {
                dataTypeClass = BigDecimal.class;
            } else {
                if (scale > 0) {
                    dataTypeClass = Float.class;
                } else {
                    dataTypeClass = Integer.class;
                }
            }
        }
        return dataTypeClass;
    }

    private static int getDataType(Class columnDataType) {
        if (columnDataType == String.class) {
            return Types.CHAR;
        }
        if (columnDataType == BigDecimal.class) {
            return Types.FLOAT;
        }
        if (columnDataType == Integer.class) {
            return Types.INTEGER;
        }
        return 0;
    }

    private static Map<String, String> readFileToMap(String inputFileName) throws IOException {
        Map<String, String> inputMap = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!Strings.isNullOrEmpty(line)) {
                    String[] keyVal = line.split("=");
                    if (keyVal.length == 2) {
                        inputMap.put(keyVal[0], keyVal[1]);
                    } else {
                        inputMap.put(keyVal[0], "");
                    }
                }
            }
        }
        return inputMap;
    }


    private static String readClasspathFileToString(String inputFileName) throws IOException {
        InputStream inputStream = SPANCodeGenerator.class.getResourceAsStream(inputFileName);

        String returnVal = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                returnVal += line + EOL;
            }
        }
        return returnVal;
    }

    private static String readRelativePathFileToString(String inputFileName) throws IOException {
        String returnVal = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName))) {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                returnVal += line + EOL;
            }
        }
        return returnVal;
    }

}