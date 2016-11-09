package com.myframework.autocode.generator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myframework.autocode.entity.ClassInfo;
import com.myframework.autocode.entity.ColumnInfo;
import com.myframework.autocode.entity.PropertyInfo;
import com.myframework.autocode.entity.TableInfo;
import com.myframework.autocode.util.CodeGeneratorUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GeneratorTest
{

	public static void main(String[] args) throws IOException, TemplateException
	{
		File xlsxFile = new File(System.getProperty("user.dir") + CodeGeneratorUtils.DB_DEFINE_FILE);
		List<TableInfo> tableInfoList = CodeGeneratorUtils.readXlsx(xlsxFile);
		List<ClassInfo> classInfoList = new ArrayList<ClassInfo>();
		for (TableInfo tableInfo : tableInfoList)
		{
			ClassInfo classInfo = new ClassInfo(tableInfo);
			classInfoList.add(classInfo);
		}

		System.out.println(tableInfoList);
		System.out.println(classInfoList);

		//

		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(GeneratorTest.class, CodeGeneratorUtils.AUTOCODE_PATH);

		String basePath = System.getProperty("user.dir") + CodeGeneratorUtils.OUTPUT_PATH;
		// 清空basePath下的文件
		clearBasePathFiles(basePath);

		for (int i = 0; i < tableInfoList.size(); i++)
		{
			TableInfo tableInfo = tableInfoList.get(i);
			ClassInfo classInfo = classInfoList.get(i);

			// 模板内使用的参数Map
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("tableName", tableInfo.getTableName());
			dataMap.put("tableDbName", tableInfo.getTableDbName());
			dataMap.put("tableDbNameLc", tableInfo.getTableDbName().toLowerCase());
			dataMap.put("prefixTableDbName", CodeGeneratorUtils.DB_PREFIX + tableInfo.getTableDbName());
			//
			dataMap.put("classPackage", CodeGeneratorUtils.OUTPUT_PACKAGE + "."
					+ tableInfo.getModuleName());
			dataMap.put("className", classInfo.getClassName());
			dataMap.put("classNameWithoutEntity", classInfo.getClassNameWithoutEntity());
			dataMap.put("classObjectName", classInfo.getClassObjectName());
			dataMap.put("iDaoName", classInfo.getIDaoName());
			dataMap.put("daoName", classInfo.getDaoName());

			List<Map<String, Object>> pros = new ArrayList<Map<String, Object>>();
			List<ColumnInfo> columnList = tableInfo.getColumns();
			List<PropertyInfo> propertyList = classInfo.getProperties();
			for (int j = 0; j < columnList.size(); j++)
			{
				ColumnInfo columnInfo = columnList.get(j);
				PropertyInfo propertyInfo = propertyList.get(j);
				Map<String, Object> proMap = new HashMap<String, Object>();
				proMap.put("proDescription", columnInfo.getDescription());
				proMap.put("proColumnName", columnInfo.getName());
				proMap.put("proColumnNameLc", columnInfo.getName().toLowerCase());
				proMap.put("proDbColumnType", columnInfo.getDbColumnType("sqlserver"));
				proMap.put("proDbColumnOracleType", columnInfo.getDbColumnType("oracle"));
				proMap.put("proNotNull", columnInfo.isNotNull());
				proMap.put("proType", propertyInfo.getPropertyType());
				proMap.put("proName", propertyInfo.getPropertyName());
				proMap.put("proMethodName", propertyInfo.getMethodName());
				pros.add(proMap);
			}
			dataMap.put("properties", pros);

			// Entity生成
			File pathFile = new File(basePath + tableInfo.getModuleName() +"/entity/" + classInfo.getClassName() + ".java");
			pathFile.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(pathFile);
			Writer writer = new OutputStreamWriter(fos, "UTF-8");
			Template t = cfg.getTemplate("EntityGeneratorTemplate"); // 要生成的目标文件的模板文件
			t.process(dataMap, writer);
			fos.flush();
			fos.close();
			writer.close();
			System.out.println("导出成功：" + pathFile.getAbsolutePath());

			if(CodeGeneratorUtils.DAO_TYPE == "1"){

				if(CodeGeneratorUtils.DB_TYPE == "1"){
					// Mapper生成
					pathFile = new File(basePath + tableInfo.getModuleName() +"/mapper/" + classInfo.getClassNameWithoutEntity() + "Mapper.xml");
					pathFile.getParentFile().mkdirs();
					fos = new FileOutputStream(pathFile);
					writer = new OutputStreamWriter(fos, "UTF-8");
					t = cfg.getTemplate("MapperGeneratorTemplate");
					t.process(dataMap, writer);
					fos.flush();
					fos.close();
					writer.close();
					System.out.println("导出成功：" + pathFile.getAbsolutePath());
				}else if(CodeGeneratorUtils.DB_TYPE == "2"){
					// OracleMapper生成
					pathFile = new File(basePath + tableInfo.getModuleName() +"/mapper/oracle/" + classInfo.getClassNameWithoutEntity() + "Mapper.xml");
					pathFile.getParentFile().mkdirs();
					fos = new FileOutputStream(pathFile);
					writer = new OutputStreamWriter(fos, "UTF-8");
					t = cfg.getTemplate("MapperOracleGeneratorTemplate");
					t.process(dataMap, writer);
					fos.flush();
					fos.close();
					writer.close();
					System.out.println("导出成功：" + pathFile.getAbsolutePath());
				}

				// IDao生成
				pathFile = new File(basePath + tableInfo.getModuleName() +"/dao/" + classInfo.getIDaoName() + ".java");
				pathFile.getParentFile().mkdirs();
				fos = new FileOutputStream(pathFile);
				writer = new OutputStreamWriter(fos, "UTF-8");
				t = cfg.getTemplate("IDaoGeneratorTemplate");
				t.process(dataMap, writer);
				fos.flush();
				fos.close();
				writer.close();
				System.out.println("导出成功：" + pathFile.getAbsolutePath());

				// Dao生成
				pathFile = new File(basePath + tableInfo.getModuleName() +"/dao/" + classInfo.getDaoName() + "Impl.java");
				pathFile.getParentFile().mkdirs();
				fos = new FileOutputStream(pathFile);
				writer = new OutputStreamWriter(fos, "UTF-8");
				t = cfg.getTemplate("DaoGeneratorTemplate");
				t.process(dataMap, writer);
				fos.flush();
				fos.close();
				writer.close();
				System.out.println("导出成功：" + pathFile.getAbsolutePath());
			}else if(CodeGeneratorUtils.DAO_TYPE == "2"){
				//
				if(CodeGeneratorUtils.DB_TYPE == "1"){
					// Mapper生成
					pathFile = new File(basePath + tableInfo.getModuleName() +"/mapper/" + classInfo.getClassNameWithoutEntity() + "Mapper.xml");
					pathFile.getParentFile().mkdirs();
					fos = new FileOutputStream(pathFile);
					writer = new OutputStreamWriter(fos, "UTF-8");
					t = cfg.getTemplate("MapperGeneratorTemplate2");
					t.process(dataMap, writer);
					fos.flush();
					fos.close();
					writer.close();
					System.out.println("导出成功：" + pathFile.getAbsolutePath());
				}else if(CodeGeneratorUtils.DB_TYPE == "2"){
					// OracleMapper生成
					pathFile = new File(basePath + tableInfo.getModuleName() +"/mapper/oracle/" + classInfo.getClassNameWithoutEntity() + "Mapper.xml");
					pathFile.getParentFile().mkdirs();
					fos = new FileOutputStream(pathFile);
					writer = new OutputStreamWriter(fos, "UTF-8");
					t = cfg.getTemplate("MapperOracleGeneratorTemplate2");
					t.process(dataMap, writer);
					fos.flush();
					fos.close();
					writer.close();
					System.out.println("导出成功：" + pathFile.getAbsolutePath());
				}
				// IDao生成
				pathFile = new File(basePath + tableInfo.getModuleName() +"/dao/" + classInfo.getIDaoName() + ".java");
				pathFile.getParentFile().mkdirs();
				fos = new FileOutputStream(pathFile);
				writer = new OutputStreamWriter(fos, "UTF-8");
				t = cfg.getTemplate("IDaoGeneratorTemplate2");
				t.process(dataMap, writer);
				fos.flush();
				fos.close();
				writer.close();
				System.out.println("导出成功：" + pathFile.getAbsolutePath());
			}
		}
	}

	private static void clearBasePathFiles(String basePath)
	{
		File dir = new File(basePath);
		dir.mkdirs();
		clearBasePathFiles(dir);
	}

	private static void clearBasePathFiles(File file)
	{
		if (file.isDirectory())
		{
			for (File child : file.listFiles())
			{
				clearBasePathFiles(child);
			}
		}
		else
		{
			file.delete();
		}
	}

}
