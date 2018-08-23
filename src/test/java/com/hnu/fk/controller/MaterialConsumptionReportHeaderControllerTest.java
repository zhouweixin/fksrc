package com.hnu.fk.controller;

import com.hnu.fk.FkApplication;
import com.hnu.fk.domain.MaterialConsumptionReportHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 14:04 2018/8/23
 * @Modified By:
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FkApplication.class)
@WebAppConfiguration
public class MaterialConsumptionReportHeaderControllerTest {

    @Autowired
    private MaterialConsumptionReportHeaderController materialConsumptionReportHeaderController;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

    @Test
    public void generateReport() throws ParseException {
        materialConsumptionReportHeaderController.generateReport(3, sdf.parse("2018-08"));
    }
}