package com.hnu.fk.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.hnu.fk.FkApplication;
import com.hnu.fk.domain.MaterialConsumptionDetail;
import com.hnu.fk.domain.MaterialConsumptionHeader;
import com.hnu.fk.domain.MaterialConsumptionItem;
import com.hnu.fk.service.MaterialConsumptionHeaderService;
import com.hnu.fk.service.MaterialConsumptionItemService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 17:05 2018/8/21
 * @Modified By:
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FkApplication.class)
@WebAppConfiguration
public class MaterialConsumptionHeaderControllerTest {

    @Autowired
    private MaterialConsumptionHeaderService materialConsumptionHeaderService;

    @Autowired
    private MaterialConsumptionItemService materialConsumptionItemService;

    @Test
    public void exportByStartDateAndEndDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2018-08-01");
        Date endDate = sdf.parse("2018-09-01");

        // 查询表头
        List<MaterialConsumptionItem> items = materialConsumptionItemService.findAll();
        if (items == null || items.size() == 0) {
            System.out.println("字段为空");
            return;
        }

        // 查询数据
        List<MaterialConsumptionHeader> headers = materialConsumptionHeaderService.getByStartDateAndEndDate(startDate, endDate);
        if (headers == null || headers.size() == 0) {
            System.out.println("数据为空");
            return;
        }

        // 创建表
        List<ExcelExportEntity> entities = new ArrayList<>();

        // 【1、写表头】
        // 1.日期
        ExcelExportEntity entity = new ExcelExportEntity("日期", "date");
        entity.setFormat("yyyy-MM-dd");
        entities.add(new ExcelExportEntity("日期", "date"));

        // 2.实际用量
        entity = new ExcelExportEntity("实际用量(t)", "realConsuption");
        List<ExcelExportEntity> list = new ArrayList<>();
        for (MaterialConsumptionItem materialConsumptionItem : items) {
            list.add(new ExcelExportEntity(materialConsumptionItem.getName(), materialConsumptionItem.getId()));
        }
        entity.setList(list);
        entities.add(entity);

        // 3.录入时间
        entity = new ExcelExportEntity("录入时间", "enterTime");
        entity.setFormat("yyyy-MM-dd HH:mm:ss");
        entities.add(entity);

        // 4.录入人
        entities.add(new ExcelExportEntity("录入人", "enterUser"));

        // 5.修改时间
        entity = new ExcelExportEntity("修改时间", "modifyTime");
        entity.setFormat("yyyy-MM-dd HH:mm:ss");
        entities.add(entity);

        // 6.修改人
        entities.add(new ExcelExportEntity("修改人", "modifyUser"));

        // 【2、写数据】
        List<Map<Object, Object>> datas = new ArrayList<>();
        for (MaterialConsumptionHeader header : headers) {
            Map<Object, Object> map = new HashMap<>();

            if (header.getDate() != null) {
                map.put("date", header.getDate());
            }

            if (header.getEnterTime() != null) {
                map.put("enterTime", header.getEnterTime());
            }

            if (header.getModifyTime() != null) {
                map.put("modifyTime", header.getModifyTime());
            }

            if (header.getEnterUser() != null && header.getEnterUser().getName() != null) {
                map.put("enterUser", header.getEnterUser().getName());
            }

            if (header.getModifyUser() != null && header.getModifyUser().getName() != null) {
                map.put("modifyUser", header.getModifyUser().getName());
            }

            if(header.getMaterialConsumptionDetails() == null || header.getMaterialConsumptionDetails().size() == 0){
                continue;
            }

            Map<Object, Object> realConsuptionMap = new HashMap<>();
            for(MaterialConsumptionDetail detail : header.getMaterialConsumptionDetails()){
                if(detail.getItem() == null || detail.getItem().getId() == null || detail.getValue() == null){
                    continue;
                }

                realConsuptionMap.put(detail.getItem().getId(), detail.getValue());
            }

            map.put("realConsuption", Arrays.asList(realConsuptionMap));
            datas.add(map);
        }

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("标题", "sheet名", ExcelType.XSSF), entities, datas);
        FileOutputStream fos = new FileOutputStream("测试1.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();

    }

    @Test
    public void nativePOI() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));

        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("第一列");
        row0.createCell(1).setCellValue("第二三列");
        row0.createCell(3).setCellValue("第四列");

        XSSFFont font = workbook.createFont();
        font.setBold(true);

        // 居中
        XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
        xssfCellStyle.setFont(font);
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        row0.setRowStyle(xssfCellStyle);

        XSSFRow row1 = sheet.createRow(1);
        XSSFCell cell = row1.createCell(1);
        cell.setCellStyle(xssfCellStyle);
        cell.setCellValue("第二列");
        row1.createCell(2).setCellValue("第三列");
        row1.setRowStyle(xssfCellStyle);

        for (int i = 2; i <= 4; i++) {
            XSSFRow row = sheet.createRow(i);
            for (int j = 0; j < 4; j++) {
                row.createCell(j).setCellValue(j);
            }
        }

        FileOutputStream fos = new FileOutputStream("测试2.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();
        System.out.println("完成");
    }

    @Test
    public void dynaCol() {
        try {
            List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
            ExcelExportEntity colEntity = new ExcelExportEntity("商品名称", "title");
            colEntity.setNeedMerge(true);
            colList.add(colEntity);

            colEntity = new ExcelExportEntity("供应商", "supplier");
            colEntity.setNeedMerge(true);
            colList.add(colEntity);

            ExcelExportEntity deliColGroup = new ExcelExportEntity("得力", "deli");
            List<ExcelExportEntity> deliColList = new ArrayList<ExcelExportEntity>();
            deliColList.add(new ExcelExportEntity("市场价", "orgPrice"));
            deliColList.add(new ExcelExportEntity("专区价", "salePrice"));
            deliColGroup.setList(deliColList);
            colList.add(deliColGroup);

            ExcelExportEntity jdColGroup = new ExcelExportEntity("京东", "jd");
            List<ExcelExportEntity> jdColList = new ArrayList<ExcelExportEntity>();
            jdColList.add(new ExcelExportEntity("市场价", "orgPrice"));
            jdColList.add(new ExcelExportEntity("专区价", "salePrice"));
            jdColGroup.setList(jdColList);
            colList.add(jdColGroup);


            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> valMap = new HashMap<String, Object>();
                valMap.put("title", "名称." + i);
                valMap.put("supplier", "供应商." + i);

                List<Map<String, Object>> deliDetailList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < 3; j++) {
                    Map<String, Object> deliValMap = new HashMap<String, Object>();
                    deliValMap.put("orgPrice", "得力.市场价." + j);
                    deliValMap.put("salePrice", "得力.专区价." + j);
                    deliDetailList.add(deliValMap);
                }
                valMap.put("deli", deliDetailList);

                List<Map<String, Object>> jdDetailList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < 2; j++) {
                    Map<String, Object> jdValMap = new HashMap<String, Object>();
                    jdValMap.put("orgPrice", "京东.市场价." + j);
                    jdValMap.put("salePrice", "京东.专区价." + j);
                    jdDetailList.add(jdValMap);
                }
                valMap.put("jd", jdDetailList);

                list.add(valMap);
            }

            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("价格分析表", "数据"), colList,
                    list);
            FileOutputStream fos = new FileOutputStream("测试3.xls");
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}