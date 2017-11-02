package com.lxzl.erp;

import com.lxzl.se.common.util.StringUtil;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyTest {

    public static void main(String[] args) throws Exception {
        test("erp_stock_order");
    }

    private static String URL = "jdbc:mysql://192.168.10.205:3306/lxzl_erp?useUnicode=true&amp;characterEncoding=UTF-8";
    private static String USER = "lxzldev";
    private static String PASSWORD = "lxzldev";

    public static void test(String tableName) throws Exception{

        Class.forName("com.mysql.jdbc.Driver");
        Connection con= DriverManager.getConnection(URL, USER, PASSWORD);

        DatabaseMetaData databaseMetaData = con.getMetaData();
        ResultSet tableRet = databaseMetaData.getColumns(null, "%", tableName, "%");

        String poName = "";
        String dataImport = "";
        String bigDecimalImport = "";

        List<NameAndType> nameAndTypeList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        while(tableRet.next()){
            nameList.add(tableRet.getString("COLUMN_NAME"));
            typeList.add(tableRet.getString("TYPE_NAME"));
        }
        int size = nameList.size();
        boolean haveDate = false;
        boolean haveBigDecimal = false;
        for(int i = 0 ; i<size ;i++){
            NameAndType nameAndType = new NameAndType(nameList.get(i),typeList.get(i),tableName);
            if(nameAndType.haveDate){
                haveDate = nameAndType.haveDate;
            }
            if(nameAndType.haveBigDecimal){
                haveBigDecimal = nameAndType.haveBigDecimal;
            }
            nameAndTypeList.add(nameAndType);
        }
        if(haveDate){
            dataImport = "import java.util.Date;";
        }
        if(haveBigDecimal){
            bigDecimalImport = "import java.math.BigDecimal;";
        }
        Table table = new Table(tableName);
        String poString = getPoString(dataImport,bigDecimalImport,nameAndTypeList,table);
        String doString = getDoString(dataImport,bigDecimalImport,nameAndTypeList,table);
        String xmlString = getXmlString(nameAndTypeList,table);
        String mapperString = getMapperString(table);

        writeToFile(poString,doString,xmlString,mapperString,table);
    }
    public static String getPoString(String dataImport , String bigDecimalImport , List<NameAndType> nameAndTypeList , Table table){
        StringBuffer poSb = new StringBuffer(
                "import org.codehaus.jackson.annotate.JsonIgnoreProperties;\n" +
                        "import java.io.Serializable;\n" +
                        dataImport+"\n" +bigDecimalImport+"\n" +
                        "\n" +
                        "@JsonIgnoreProperties(ignoreUnknown = true)\n" +
                        "public class "+ table.poTableName+" implements Serializable {");
        appendAllPOParam(nameAndTypeList,poSb);
        appendPOSetterAndSetter(nameAndTypeList,poSb);
        poSb.append("\n}");
        return poSb.toString();
    }
    public static String getDoString(String dataImport , String bigDecimalImport , List<NameAndType> nameAndTypeList , Table table){
        StringBuffer doSb = new StringBuffer(
                        "import com.lxzl.se.dataaccess.mysql.domain.BaseDO;\n"+
                        dataImport+"\n" +bigDecimalImport+"\n" +
                        "\n" +
                        "public class "+ table.poTableName+"DO "+" extends BaseDO {");
        appendAllDOParam(nameAndTypeList,doSb);
        appendDOSetterAndSetter(nameAndTypeList,doSb);
        doSb.append("\n}");
        return doSb.toString();
    }
    public static String getMapperString(Table table){
        StringBuffer mapperSb = new StringBuffer("import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;\n\n");

        String mapperName = table.poTableName+"Mapper";
                mapperSb.append("public interface "+mapperName+" extends BaseMysqlDAO<"+table.doTableName+"> {\n");
        mapperSb.append("\n}");
        return mapperSb.toString();
    }

    public static String getXmlString(List<NameAndType> nameAndTypeList , Table table){
        String mapperName = table.poTableName+"Mapper";
        StringBuffer xmlSb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlSb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        xmlSb.append("<mapper namespace=\""+mapperName+"\">\n\n");
        xmlSb.append("\t<resultMap id=\""+table.doTableName+"\" type=\""+table.doTableName+"\">\n");
        for(NameAndType nameAndType : nameAndTypeList){
            if("id".equals(nameAndType.trueDoName)){
                xmlSb.append("\t\t<id column=\"id\" jdbcType=\"INTEGER\" property=\"id\" />\n");
            }else{
                xmlSb.append("\t\t<result column=\""+nameAndType.sqlName+"\" jdbcType=\""+nameAndType.sqlType+"\" property=\""+nameAndType.trueDoName+"\" />\n");
            }
        }
        xmlSb.append("\t</resultMap>\n");
        xmlSb.append("\t<sql id=\"column_List\">\n");
        xmlSb.append("\t\t"+getSimpleString(table,nameAndTypeList)+"\n");
        xmlSb.append("\t</sql>\n\n");
        xmlSb.append("\t<sql id=\"set_column_sql\">\n");
        xmlSb.append("\t\t<set>\n");
        for(NameAndType nameAndType : nameAndTypeList){
            xmlSb.append("\t\t\t<if test=\""+nameAndType.trueDoName+" != null\">\n");
            xmlSb.append("\t\t\t\t"+nameAndType.sqlName+" = #{"+nameAndType.trueDoName+",jdbcType="+nameAndType.sqlType+"},\n");
            xmlSb.append("\t\t\t</if>\n");
        }
        xmlSb.append("\t\t</set>\n");
        xmlSb.append("\t</sql>\n");
        xmlSb.append("\t<insert id=\"save\" keyProperty=\"id\" useGeneratedKeys=\"true\"\n");
        xmlSb.append("\t\t<insert id=\"save\" keyProperty=\"id\" useGeneratedKeys=\"true\" parameterType=\""+table.doTableName+"\">\n");
        xmlSb.append("\t</insert>\n");
        xmlSb.append("\n");
        xmlSb.append("\t<update id=\"update\" parameterType=\""+table.doTableName+"\">\n");
        xmlSb.append("\t\tupdate "+table.sqlTableName+" <include refid=\"set_column_sql\"/> WHERE id = #{id, jdbcType=INTEGER}\n");
        xmlSb.append("\t</update>\n");
        xmlSb.append("</mapper>");
        return xmlSb.toString();
    }

    public static String getSimpleString(Table table,List<NameAndType> nameAndTypeList){
        String[] ss = table.sqlTableName.split("_");
        StringBuffer simpleSb = new StringBuffer();
        for(String s : ss){
            simpleSb.append(s.substring(0,1));
        }
        StringBuffer sql = new StringBuffer();
        for(NameAndType nameAndType : nameAndTypeList){
            sql.append(simpleSb.toString()).append(".").append(nameAndType.sqlName).append(",");
        }
        return sql.toString().substring(0,sql.length()-1);
    }
    public static String getDomainTableName(String tableName){
        return tableName.substring(3, tableName.length());
    }

    public static String getUp(String s){
        String first = s.substring(0,1);
        String last = s.substring(1,s.length());
        return first.toUpperCase()+last;
    }
    static class Table{
        private String poTableName;
        private String doTableName;
        private String sqlTableName;

        public Table(String tableName){
            this.sqlTableName = tableName;
            this.poTableName = getUp(getDomainTableName(covertString(tableName)));
            this.doTableName = getUp(getDomainTableName(covertString(tableName)))+"DO";
        }
    }

    static class NameAndType{
        private String sqlTableName;
        private String tableName;
        private String sqlName;
        private String doName;
        private String trueDoName;
        private String poName;
        private String type;
        private String sqlType;
        private boolean haveDate = false;
        private boolean haveBigDecimal = false;
        public NameAndType(String name, String type,String tableName) {
            this.sqlTableName = tableName;
            this.tableName = getUp(getDomainTableName(tableName));
            this.sqlName = name;
            this.doName = convertDoName(name);
            this.trueDoName = convertTrueDoName(name);
            this.poName = convertPoName(name);
            if("INT".equals(type)){
                this.sqlType = "INTEGER";
            }else{
                this.sqlType = type;
            }

            this.type = convertType(type);
        }
        public String convertDoName(String name){
            if("create_time".equals(name)||"create_user".equals(name)||"update_time".equals(name)||"update_user".equals(name)){
                return null;
            }
            return covertString(name);
        }
        public String convertTrueDoName(String name){
            return covertString(name);
        }
        public String convertPoName(String name){
            if("id".equals(name)){
                return covertString(tableName)+"Id";
            }
            return covertString(name);
        }



        public String convertType(String type){
            if("INT".equals(type)){
                return "Integer";
            }
            if("DECIMAL".equals(type)){
                this.haveBigDecimal=true;
                return "BigDecimal";
            }
            if("DATETIME".equals(type)){
                this.haveDate = true;
                return "Date";
            }
            if("VARCHAR".equals(type)){
                return "String";
            }
            if("BIGINT".equals(type)){
                return "Long";
            }
            return "";
        }
    }

    public static String covertString(String name){

        String[] parts = name.split("_");
        StringBuffer newName = new StringBuffer();
        for(int i = 0 ; i < parts.length ; i++){
            if(i==0){
                newName.append(parts[i]);
            }else if(i<parts.length){
                newName.append(getUp(parts[i]));
            }
        }
        return newName.toString();
    }
    public static void appendAllPOParam(List<NameAndType> nameAndTypeList , StringBuffer sb ){
        sb.append("\n");
        for(NameAndType nameAndType : nameAndTypeList){
            String s = "\n\tprivate " + nameAndType.type +" " + nameAndType.poName+";";
            sb.append(s);
        }
    }
    public static void appendAllDOParam(List<NameAndType> nameAndTypeList , StringBuffer sb ){
        sb.append("\n");
        for(NameAndType nameAndType : nameAndTypeList){
            if(nameAndType.doName!=null){
                String s = "\n\tprivate " + nameAndType.type +" " + nameAndType.doName+";";
                sb.append(s);
            }
        }
    }
    public static void appendPOSetterAndSetter(List<NameAndType> nameAndTypeList , StringBuffer sb){
        sb.append("\n");
        for(NameAndType nameAndType : nameAndTypeList){
            String  getter = "\n\tpublic "+nameAndType.type+" get"+getUp(nameAndType.poName)+"(){\n\t\treturn "+nameAndType.poName+";\n\t}\n";
            String  setter = "\n\tpublic void set"+getUp(nameAndType.poName)+"("+nameAndType.type+" "+ nameAndType.poName+"){\n\t\tthis."+nameAndType.poName+" = "+nameAndType.poName+";\n\t}\n";
            sb.append(getter).append(setter);
        }
    }
    public static void appendDOSetterAndSetter(List<NameAndType> nameAndTypeList , StringBuffer sb){
        sb.append("\n");
        for(NameAndType nameAndType : nameAndTypeList){
            if(nameAndType.doName!=null){
                String  getter = "\n\tpublic "+nameAndType.type+" get"+getUp(nameAndType.doName)+"(){\n\t\treturn "+nameAndType.doName+";\n\t}\n";
                String  setter = "\n\tpublic void set"+getUp(nameAndType.doName)+"("+nameAndType.type+" "+ nameAndType.doName+"){\n\t\tthis."+nameAndType.doName+" = "+nameAndType.doName+";\n\t}\n";
                sb.append(getter).append(setter);
            }
        }
    }


    private static void  writeToFile(String poString , String doString  , String xmlString ,String mapperString , Table table) throws IOException {


        String dirName = table.poTableName;
        File dir = new File("d:/tmp/"+dirName);
        if(!dir.isDirectory()){
            dir.mkdirs();
        }
        if(StringUtil.isNotEmpty(poString)){
            String poFileName = table.poTableName +".java";
            File file = new File("d:/tmp/"+dirName+"/"+poFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(poString.getBytes());
        }
        if(StringUtil.isNotEmpty(poString)){
            String doFileName = table.doTableName +".java";
            File file = new File("d:/tmp/"+dirName+"/"+doFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(doString.getBytes());
        }
        if(StringUtil.isNotEmpty(xmlString)){
            String xmlFileName = table.sqlTableName +".xml";
            File file = new File("d:/tmp/"+dirName+"/"+xmlFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(xmlString.getBytes());
        }
        if(StringUtil.isNotEmpty(mapperString)){
            String mapperFileName = table.poTableName +"Mapper.java";
            File file = new File("d:/tmp/"+dirName+"/"+mapperFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(mapperString.getBytes());
        }
    }
}
