package com.edu.college.common.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ExcelUtil implements AutoCloseable {
    private InputStream inputStream;
    private Map<String, Method> methodMap = new HashMap<>();
    private Map<String, Field> fieldMap = new HashMap<>();
    private Executor executor;

    /**
     * 创建一个excel工具对象，不开启多线程
     *
     * @param inputStream excel文件输入流
     */
    public ExcelUtil(InputStream inputStream) {
        this(inputStream, null);
    }

    /**
     * 创建一个excel工具对象，开启多线程
     *
     * @param inputStream excel文件输入流
     * @param executor    自定义线程池
     */
    public ExcelUtil(InputStream inputStream, Executor executor) {
        this.inputStream = new BufferedInputStream(inputStream);
        this.executor = executor;
    }

    /**
     * 分段读取excel并进行操作
     *
     * @param sheetIndex sheet下标
     * @param tClass     要将每行数据封装成的类型
     * @param maxRow     分段最大行数
     * @param function   对分段读取的list做什么操作
     * @return 处理的行数
     */
    public <T> int readAndOperate(int sheetIndex, Class<T> tClass, int maxRow, Consumer<List<T>> function) throws IOException {
        prepare(tClass);
        final Constructor<T> constructor = getConstructor(tClass);
        final Workbook workbook = getWorkbook();
        final Sheet sheet = workbook.getSheetAt(sheetIndex);
        final int firstRowNum = sheet.getFirstRowNum();
        final Row titleRow = sheet.getRow(firstRowNum);
        Map<Integer, String> titleMap = titleMap(titleRow);
        final int lastRowNum = sheet.getLastRowNum();
        final AtomicInteger totalLine = new AtomicInteger(0);
        List<T> list = new ArrayList<>(maxRow);
        for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
            final Row row = sheet.getRow(i);
            T t = toObject(row, constructor, titleMap);
            if (t != null) {
                list.add(t);
            }
            if (list.size() == maxRow) {
                final List<T> finalList = new ArrayList<>(list);
                if (executor == null) {
                    function.accept(finalList);
                } else {
                    executor.execute(() -> function.accept(finalList));
                }
                totalLine.addAndGet(list.size());
                list = new ArrayList<>(maxRow);
            }
        }
        function.accept(list);
        final int total = totalLine.addAndGet(list.size());
        list.clear();
        return total;
    }

    /**
     * 读取excel并封装成对象集合
     *
     * @param sheetIndex sheet下标
     * @param tClass     要将每行数据封装成的类型
     */
    public <T> List<T> read(int sheetIndex, Class<T> tClass) throws IOException {
        prepare(tClass);
        final Constructor<T> constructor = getConstructor(tClass);
        final Workbook workbook = getWorkbook();
        final Sheet sheet = workbook.getSheetAt(sheetIndex);
        final int firstRowNum = sheet.getFirstRowNum();
        final Row titleRow = sheet.getRow(firstRowNum);
        Map<Integer, String> titleMap = titleMap(titleRow);
        final int lastRowNum = sheet.getLastRowNum();
        final int totalRow = lastRowNum - firstRowNum + 2;
        final int batch = totalRow % 5000 == 0 ? totalRow / 5000 : totalRow / 5000 + 1;
        List<T> list = new ArrayList<>(totalRow);
        for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
            final Row row = sheet.getRow(i);
            T t = toObject(row, constructor, titleMap);
            list.add(t);
        }
        return list;
    }

    private <T> Constructor<T> getConstructor(final Class<T> tClass) {
        final Constructor<T> constructor;
        try {
            constructor = tClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("目标类没有公共的无参构造器");
        }
        return constructor;
    }

    private <T> void prepare(final Class<T> tClass) {
        final Field[] fields = tClass.getDeclaredFields();
        for (final Field field : fields) {
            field.setAccessible(true);
            try {
                final ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                if (annotation != null) {
                    if (!methodMap.containsKey(field.getName())) {
                        final String setMethodName = getSetMethodName(field.getName());
                        final Method setter = tClass.getMethod(setMethodName, field.getType());
                        methodMap.put(field.getName(), setter);
                    }
                    if (!fieldMap.containsKey(field.getName())) {
                        final String value = annotation.value();
                        fieldMap.put(value, field);
                    }
                }
            } catch (NoSuchMethodException ignored) {
            }
        }
    }

    private <T> T toObject(final Row row, Constructor<T> constructor, final Map<Integer, String> titleMap) {
        if (row == null || row.getPhysicalNumberOfCells() == 0) {
            return null;
        }
        final short firstCellNum = row.getFirstCellNum();
        final short lastCellNum = row.getLastCellNum();
        final T t;
        try {
            t = constructor.newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("目标类没有公共的无参构造器");
        }
        for (int i = firstCellNum; i < lastCellNum; i++) {
            final Cell cell = row.getCell(i);
            final String title = titleMap.get(i);
            final Field field = fieldMap.get(title);
            if (field == null || cell == null) {
                continue;
            }
            final Object value = getValue(cell, field);
            final Method setter = methodMap.get(field.getName());
            try {
                setter.invoke(t, value);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }
        return t;
    }

    private Object getValue(final Cell cell, final Field field) {
        final String typeName = field.getType().getSimpleName();
        final Object value;
        switch (typeName) {
            case "Integer":
            case "int":
                value = (int) cell.getNumericCellValue();
                break;
            case "Long":
            case "long":
                value = (long) cell.getNumericCellValue();
                break;
            case "Double":
            case "double":
                value = cell.getNumericCellValue();
                break;
            case "Date":
                value = cell.getDateCellValue();
                break;
            case "Boolean":
            case "boolean":
                value = cell.getBooleanCellValue();
                break;
            default:
            case "String":
                value = cell.getStringCellValue();
                break;
        }
        return value;
    }

    private String getSetMethodName(String name) {
        String firstChar = name.substring(0, 1);
        return "set" + firstChar.toUpperCase() + name.substring(1);
    }

    private Map<Integer, String> titleMap(final Row titleRow) {
        final short firstCellNum = titleRow.getFirstCellNum();
        final short lastCellNum = titleRow.getLastCellNum();
        final Map<Integer, String> map = new HashMap<>(lastCellNum - firstCellNum + 1);
        for (int i = firstCellNum; i < lastCellNum; i++) {
            final String title = titleRow.getCell(i).getStringCellValue();
            map.put(i, title);
        }
        return map;
    }

    private Workbook getWorkbook() throws IOException {
        final FileMagic fileMagic = FileMagic.valueOf(inputStream);
        Workbook wb;
        if (fileMagic.equals(FileMagic.OLE2)) {
            wb = new HSSFWorkbook(inputStream);
        } else if (FileMagic.OOXML.equals(fileMagic)) {
            wb = new XSSFWorkbook(inputStream);
        } else {
            throw new RuntimeException("文件格式不支持");
        }
        return wb;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
        this.methodMap.clear();
        this.fieldMap.clear();
    }
}
