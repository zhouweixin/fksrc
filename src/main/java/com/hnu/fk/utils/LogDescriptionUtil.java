package com.hnu.fk.utils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 说明:记录描述生成工具类
 *
 * @author WaveLee
 * 日期: 2018/8/7
 */
public class LogDescriptionUtil {
    /**
     * 修改
     * @param oldData
     * @param newData
     * @param <T>
     * @return
     */
    public static <T>String log(T oldData,T newData) {
        Field   fields[]   =   oldData.getClass().getDeclaredFields();
        Field.setAccessible(fields,true);
        String description = null;
        try {
            description = fields[0].getName()+ ":" + fields[0].get(oldData)+ "|";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Field field : fields) {
            try {
                if(field.get(oldData)!= null && field.get(newData)!=null) {
                    if (!field.get(oldData).toString().equals(field.get(newData).toString())) {
                        description += field.getName() + ":" + field.get(oldData) + "->" + field.get(newData) + "|";
                    }
                }
                else if (field.get(oldData) != field.get(newData)){
                    description += field.getName() + ":" + field.get(oldData) + "->" + field.get(newData) + "|";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return description;
    }

    /**
     * 新增，删除
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String log(T data) {
        String description = "";
        Field   fields[]   =   data.getClass().getDeclaredFields();
        Field.setAccessible(fields,true);
        for (Field field : fields) {
            try {
                description += field.getName() + ":" + field.get(data) + "|";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return description;
    }

    /**
     * 批量删除
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String log(List<T> data){
        String description = "";
        Iterator it = data.iterator();
        while(it.hasNext()) {
            description += it.next() + "|";
        }
        return description;
    }
}
