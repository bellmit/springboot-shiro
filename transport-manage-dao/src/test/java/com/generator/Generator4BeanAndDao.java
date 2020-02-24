/*
package com.generator;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by shaoxiangfei on 2016/4/28.
 *//*

public class Generator4BeanAndDao {

    private static final String METHOD_GET = "get";

    private static final String METHOD_SET = "set";

    private boolean idExist;

    private static final String ID = "ID";

    private static final String COLUMNS = "COLUMNS";

    private static final String COLUMNS_ALL = "COLUMNS_ALL";

    public static void main(String[] args) throws IOException {
        Generator4BeanAndDao generator = new Generator4BeanAndDao();
//        try {
//            generator.generateCode("10.154.157.12", "3307", "letv_boss", "dict", "boss_test", "YmY4MzRlYzk1Yjc", "com", "D:/");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println("------------done----------");
    }

    */
/**
     * 代码生成
     *
     * @param ip
     * @param port
     * @param database
     * @param table
     * @param uname
     * @param pwd
     * @param pk
     * @param filePath
     * @throws IOException
     *//*

    protected void generateCode(String ip, String port, String database, String table, String uname, String pwd, String pk, String filePath) throws Exception {
        Map<String, Integer> columnMap = this.getColumnFromDBTable(ip, port, database, table, uname, pwd);

        //生成Bean文件
        TypeSpec bean = TypeSpec.classBuilder(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table))
                .addModifiers(Modifier.PUBLIC)
                .addFields(this.generateBeanFields(columnMap))
                .addMethods(this.generateBeanMethods(columnMap))
                .addSuperinterface(Serializable.class)
                .build();
        JavaFile javaFile = JavaFile.builder(pk, bean).build();
        javaFile.writeTo(new File(filePath));

        //生成Dao文件
        AnnotationSpec annotationSpec = AnnotationSpec.builder(DB.class)
                .addMember("dataSource", "DatabasesConstant.MDB_CAR_MANAGE")
                .addMember("table", "$S", table)
                .build();
        TypeSpec dao = TypeSpec.interfaceBuilder(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table) + "Dao")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(annotationSpec)
                .addFields(generateDaoFields(columnMap))
                .addMethods(generateDaoMethods(columnMap, table, pk))
                .build();
        javaFile = JavaFile.builder(pk, dao).build();
        javaFile.writeTo(new File(filePath));
    }

    */
/**
     * 生成Dao methods
     *
     * @param table
     * @return
     *//*

    private List<MethodSpec> generateDaoMethods(Map<String, Integer> columnMap, String table, String pk) {
        List<MethodSpec> methodList = new ArrayList<MethodSpec>();

        //构建select语句
        methodList.add(this.buildDaoSelectObjectMethod(table, pk));
        methodList.add(this.buildDaoSelectMethod(table, pk));

        //构建delete语句
        methodList.add(this.buildDaoDeleteMethod(table));

        //构建insert语句
        methodList.add(this.buildDaoInsertMethod(columnMap, table, pk));

        //构建update语句
        methodList.add(this.buildDaoUpdateMethod(columnMap, table, pk));

        return methodList;
    }

    */
/**
     * 构建Dao update接口
     *
     * @param columnMap
     * @param table
     * @return
     *//*

    private MethodSpec buildDaoUpdateMethod(Map<String, Integer> columnMap, String table, String pk) {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SQL.class).addMember("value", this.buildDaoUpdateSql(columnMap)).build();
        TypeName tableType = ClassName.get(pk, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table));
        return MethodSpec.methodBuilder("update" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(annotationSpec)
                .addParameter(ParameterSpec.builder(tableType, table).build())
                .returns(int.class).build();
    }

    private String buildDaoUpdateSql(Map<String, Integer> columnMap) {
        StringBuilder update = new StringBuilder("\"update #table set ");
        StringBuilder sb = new StringBuilder("");
        int i = 1;
        int max = columnMap.size();
        for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
            if (ID.toLowerCase().equalsIgnoreCase(entry.getKey())) {
                idExist = true;
                max--;
                continue;
            }

            if (i == max) {
                sb.append(entry.getKey()).append("=").append(":1.").append(entry.getKey());
            } else {
                sb.append(entry.getKey()).append("=").append(":1.").append(entry.getKey()).append(", ");
            }

            i++;
        }

        update.append(sb.toString());

        if (idExist) {
            update.append(" where id=:1.id\"");
        } else {
            update.append(" where 1=1 limit 1\"");
        }

        return update.toString();
    }

    */
/**
     * 构建Dao insert接口
     *
     * @param columnMap
     * @param table
     * @return
     *//*

    private MethodSpec buildDaoInsertMethod(Map<String, Integer> columnMap, String table, String pk) {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SQL.class).addMember("value", this.buildDaoInsertSql(columnMap)).build();
        TypeName tableType = ClassName.get(pk, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table));
        return MethodSpec.methodBuilder("insert" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(annotationSpec)
                .addParameter(ParameterSpec.builder(tableType, table).build())
                .returns(int.class).build();
    }

    private String buildDaoInsertSql(Map<String, Integer> columnMap) {
        StringBuilder insert = new StringBuilder("\"insert into #table( \" + COLUMNS + \") values(");
        StringBuilder sb = new StringBuilder("");
        int i = 1;
        int max = columnMap.size();
        for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
            if (ID.toLowerCase().equalsIgnoreCase(entry.getKey())) {
                idExist = true;
                max--;
                continue;
            }

            if (i == max) {
                sb.append(":1.").append(entry.getKey());
            } else {
                sb.append(":1.").append(entry.getKey()).append(", ");
            }

            i++;
        }

        insert.append(sb.toString()).append(")\"");

        return insert.toString();
    }

    */
/**
     * 构建Dao delete接口
     *
     * @param table
     * @return
     *//*

    private MethodSpec buildDaoDeleteMethod(String table) {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SQL.class).addMember("value", this.buildDaoDelSql()).build();
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("delete" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(annotationSpec)
                .returns(int.class);
        if (idExist) {
            methodSpecBuilder.addParameter(ParameterSpec.builder(long.class, ID.toLowerCase()).build());
        }

        return methodSpecBuilder.build();
    }

    */
/**
     * 构建Dao select Object 接口
     *
     * @param table
     * @return
     *//*

    private MethodSpec buildDaoSelectObjectMethod(String table, String pk) {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SQL.class).addMember("value", this.buildDaoSelectSql()).build();
        TypeName tableType = ClassName.get(pk, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table));
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("select" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(annotationSpec)
                .returns(tableType);
        if (idExist) {
            methodSpecBuilder.addParameter(ParameterSpec.builder(long.class, ID.toLowerCase()).build());
        }

        return methodSpecBuilder.build();
    }

    */
/**
     * 构建Dao select接口
     *
     * @param table
     * @return
     *//*

    private MethodSpec buildDaoSelectMethod(String table, String pk) {
        AnnotationSpec annotationSpec = AnnotationSpec.builder(SQL.class).addMember("value", this.buildDaoSelectSql()).build();
        ClassName list = ClassName.get("java.util", "List");
        TypeName tableType = ClassName.get(pk, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table));
        TypeName typeName = ParameterizedTypeName.get(list, tableType);
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("select" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, table) + "List")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(annotationSpec)
                .returns(typeName);
        if (idExist) {
            methodSpecBuilder.addParameter(ParameterSpec.builder(long.class, ID.toLowerCase()).build());
        }

        return methodSpecBuilder.build();
    }

    private String buildDaoDelSql() {
        StringBuilder del = new StringBuilder("\"delete from #table");
        if (idExist) {
            del.append(" where id=:1\"");
        } else {
            del.append(" where 1=1 limit 1\"");
        }

        return del.toString();
    }

    private String buildDaoSelectSql() {
        StringBuilder select = new StringBuilder("\"select \" + ");
        if (idExist) {
            select.append("COLUMNS_ALL + ");
        } else {
            select.append("COLUMNS + ");
        }
        select.append("\" from #table");
        if (idExist) {
            select.append(" where id=:1\"");
        } else {
            select.append(" where 1=1 limit 1\"");
        }

        return select.toString();
    }

    */
/**
     * 生成Dao属性
     *
     * @param columnMap
     * @return
     *//*

    private List<FieldSpec> generateDaoFields(Map<String, Integer> columnMap) {
        List<FieldSpec> member = new ArrayList<FieldSpec>();
        StringBuilder sb = new StringBuilder("");
        int i = 1;
        int max = columnMap.size();
        for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
            if (ID.toLowerCase().equalsIgnoreCase(entry.getKey())) {
                idExist = true;
                max--;
                continue;
            }

            if (i == max) {
                sb.append(entry.getKey());
            } else {
                sb.append(entry.getKey()).append(", ");
            }

            i++;
        }

        FieldSpec cloumns = FieldSpec.builder(String.class, COLUMNS, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", sb.toString()).build();

        if (idExist) {
            FieldSpec id = FieldSpec.builder(String.class, ID, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$S", ID.toLowerCase()).build();

            FieldSpec cloumnsAll = FieldSpec.builder(String.class, COLUMNS_ALL, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer(ID + " + \",\" + " + COLUMNS).build();

            member.add(id);
            member.add(cloumns);
            member.add(cloumnsAll);
        } else {
            member.add(cloumns);
        }

        return member;
    }

    */
/**
     * 生成Bean get/set方法
     *
     * @param columnMap
     * @return
     *//*

    private List<MethodSpec> generateBeanMethods(Map<String, Integer> columnMap) {
        List<MethodSpec> methods = new ArrayList<MethodSpec>();
        for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
            if (entry.getValue() == Types.VARCHAR || entry.getValue() == Types.CHAR) {
                methods.add(this.createGetMethodSpec(String.class, entry.getKey()));
                methods.add(this.createSetMethodSpec(String.class, entry.getKey()));
            } else if (entry.getValue() == Types.INTEGER || entry.getValue() == Types.TINYINT) {
                methods.add(this.createGetMethodSpec(Integer.class, entry.getKey()));
                methods.add(this.createSetMethodSpec(Integer.class, entry.getKey()));
            } else if (entry.getValue() == Types.BIGINT) {
                methods.add(this.createGetMethodSpec(Long.class, entry.getKey()));
                methods.add(this.createSetMethodSpec(Long.class, entry.getKey()));
            } else if (entry.getValue() == Types.DATE || entry.getValue() == Types.TIME || entry.getValue() == Types.TIMESTAMP) {
                methods.add(this.createGetMethodSpec(java.util.Date.class, entry.getKey()));
                methods.add(this.createSetMethodSpec(java.util.Date.class, entry.getKey()));
            } else if (entry.getValue() == Types.FLOAT || entry.getValue() == Types.DOUBLE) {
                methods.add(this.createGetMethodSpec(Double.class, entry.getKey()));
                methods.add(this.createSetMethodSpec(Double.class, entry.getKey()));
            } else if (entry.getValue() == Types.DECIMAL) {
                methods.add(this.createGetMethodSpec(BigDecimal.class, entry.getKey()));
                methods.add(this.createSetMethodSpec(BigDecimal.class, entry.getKey()));
            }
        }

        return methods;
    }

    */
/**
     * 创建SetMethodSpec
     *
     * @param clz
     * @param columnName
     * @return
     *//*

    private MethodSpec createSetMethodSpec(Class clz, String columnName) {
        return MethodSpec.methodBuilder(METHOD_SET + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, columnName))
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(clz, columnName)
                .addStatement("this. " + columnName + " = " + columnName)
                .build();
    }

    */
/**
     * 创建GetMethodSpec
     *
     * @param clz
     * @param columnName
     * @return
     *//*

    private MethodSpec createGetMethodSpec(Class clz, String columnName) {
        return MethodSpec.methodBuilder(METHOD_GET + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, columnName))
                .addModifiers(Modifier.PUBLIC)
                .returns(clz)
                .addStatement("return " + columnName)
                .build();
    }

    */
/**
     * 生成Bean属性内容
     *
     * @param columnMap
     * @return
     *//*

    private List<FieldSpec> generateBeanFields(Map<String, Integer> columnMap) {
        List<FieldSpec> fields = new ArrayList<FieldSpec>();
        for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
            if (entry.getValue() == Types.VARCHAR || entry.getValue() == Types.CHAR) {
                fields.add(this.createFieldSpec(String.class, entry.getKey()));
            } else if (entry.getValue() == Types.INTEGER || entry.getValue() == Types.TINYINT) {
                fields.add(this.createFieldSpec(Integer.class, entry.getKey()));
            } else if (entry.getValue() == Types.BIGINT) {
                fields.add(this.createFieldSpec(Long.class, entry.getKey()));
            } else if (entry.getValue() == Types.DATE || entry.getValue() == Types.TIME || entry.getValue() == Types.TIMESTAMP) {
                fields.add(this.createFieldSpec(java.util.Date.class, entry.getKey()));
            } else if (entry.getValue() == Types.FLOAT || entry.getValue() == Types.DOUBLE) {
                fields.add(this.createFieldSpec(Double.class, entry.getKey()));
            } else if (entry.getValue() == Types.DECIMAL) {
                fields.add(this.createFieldSpec(BigDecimal.class, entry.getKey()));
            }
        }

        return fields;
    }

    */
/**
     * 创建FieldSpec
     *
     * @param clz
     * @param columnName
     * @return
     *//*

    private FieldSpec createFieldSpec(Class clz, String columnName) {
        return FieldSpec.builder(clz, columnName, Modifier.PRIVATE).build();
    }

    */
/**
     * 从db获取指定table的属性字段及其类型
     *
     * @param ip
     * @param port
     * @param database
     * @param table
     * @param uname
     * @param pwd
     * @return
     *//*

    private Map<String, Integer> getColumnFromDBTable(String ip, String port, String database, String table, String uname, String pwd) throws Exception {
        Map<String, Integer> columnMap = new HashMap<String, Integer>();
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database, uname, pwd);
            PreparedStatement statement = conn.prepareStatement("select * from " + table + " limit 1");
            ResultSetMetaData rsm = statement.getMetaData();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                columnMap.put(rsm.getColumnName(i), rsm.getColumnType(i));
            }

            statement.close();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return columnMap;
    }
}
*/
