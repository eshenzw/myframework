package com.myframework.autocode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.myframework.autocode.entity.ColumnInfo;
import com.myframework.autocode.entity.TableInfo;

public class CodeGeneratorUtils
{

	public final static String DB_DEFINE_FILE = "\\doc\\数据表设计.xlsx";
	public final static String AUTOCODE_PATH = "../template";
	public final static String OUTPUT_PATH = "/doc/AutoCodeGenerate/";
	public final static String OUTPUT_PACKAGE = "com.parttime.app";
	public final static String DB_PREFIX = "pt_";

	/**
	 * 读取xlsx配置文件用
	 * <p>
	 * 读取指定的文件并遍历所有页签，找到符合规则的页签，读取其中的数据表信息
	 */
	public static List<TableInfo> readXlsx(File xlsxFile) throws IOException
	{
		InputStream is = new FileInputStream(xlsxFile);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		int sheetNum = wb.getNumberOfSheets();
		Pattern pattern = Pattern.compile("^(.+)\\(([0-9a-zA-Z_]+)\\)$"); // 页签名称正则表达式，例如“用户信息表(user)”
		List<TableInfo> tableList = new ArrayList<TableInfo>();
		for (int i = 0; i < sheetNum; i++)
		{
			XSSFSheet sheet = wb.getSheetAt(i);
			Matcher matcher = pattern.matcher(sheet.getSheetName().toString());
			if (matcher.matches())
			{
				TableInfo tableInfo = new TableInfo();
				tableInfo.setTableName(matcher.group(1).trim()); // 第一部分是表名称
				tableInfo.setTableDbName(matcher.group(2).replace(DB_PREFIX,"").trim()); // 第二部分是数据库内的表名称
				if(matcher.group(2).replace(DB_PREFIX,"").contains("_")){
					String moduleName = matcher.group(2).replace(DB_PREFIX,"").substring(0,matcher.group(2).indexOf("_"));
					tableInfo.setModuleName(moduleName.toLowerCase());
				}else{
					tableInfo.setModuleName(matcher.group(2).replace(DB_PREFIX,"").toLowerCase());
				}
				List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();
				for (int j = 1; j <= sheet.getLastRowNum(); j++)
				{
					XSSFRow row = sheet.getRow(j);
					if (row == null || row.getCell(0) == null || "".equals(row.getCell(0).getStringCellValue()))
					{
						continue;
					}
					ColumnInfo column = new ColumnInfo();
					column.setName(row.getCell(0).getStringCellValue()); // 表格第一列：元素名称
					column.setKey("是".equals(row.getCell(1).getStringCellValue().trim()));// 表格第二列：是否主键
					column.setNotNull("是".equals(row.getCell(2).getStringCellValue().trim())); // 表格第三列：是否非空
					column.setType(row.getCell(3).getStringCellValue()); // 表格第四列：类型
					column.setLength((int) (row.getCell(4).getNumericCellValue())); // 表格第五列：长度
					column.setDescription(row.getCell(5).getStringCellValue()); // 表格第六列：描述

					// 表个第七列：示例
					XSSFCell xssfCell = row.getCell(6);
					String value = "";
					if (xssfCell != null)
					{
						switch (xssfCell.getCellType())
						{
							case XSSFCell.CELL_TYPE_STRING:
								value = xssfCell.getStringCellValue();
								break;
							case XSSFCell.CELL_TYPE_NUMERIC:
								value = String.valueOf((int) xssfCell.getNumericCellValue());
							default:
						}
					}
					column.setExample(value);

					columnList.add(column);
				}
				tableInfo.setColumns(columnList);
				tableList.add(tableInfo);
			}
		}
		return tableList;
	}

	public static void main(String[] args) throws IOException
	{
		File file = new File(System.getProperty("user.dir") + CodeGeneratorUtils.DB_DEFINE_FILE);
		List<TableInfo> list = CodeGeneratorUtils.readXlsx(file);
		System.out.println(list);
	}
}
