package com.hnu.fk.utils;

import com.hnu.fk.domain.ActionLog;
import com.hnu.fk.domain.User;
import com.hnu.fk.repository.ActionLogRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 说明:操作日志记录工具类
 *
 * @author WaveLee
 * 日期: 2018/8/7
 */
@Component
public class ActionLogUtil {

    private static  ActionLogRepository actionLogRepository;

    @Autowired
    public ActionLogUtil(ActionLogRepository actionLogRepository) {
        ActionLogUtil.actionLogRepository = actionLogRepository;
    }

    /**
     * 修改 操作记录
     * @param <T>
     * @param object    操作对象
     * @param oldData   旧对象
     * @param newData   新对象
     * @return
     */
    public static <T> void log(String object, T oldData, T newData) {
        Date currentTime = new Date();
        ActionLog actionLog = new ActionLog();
        actionLog.setObject(object);
        actionLog.setTime(currentTime);
        actionLog.setType("修改");
        User user = (User) SecurityUtils.getSubject().getSession(true).getAttribute("user");
        actionLog.setUser(user);
        actionLog.setDescription(LogDescriptionUtil.log(oldData,newData));
        actionLogRepository.save(actionLog);
    }

    /**
     * 新增/删除 操作记录
     * @param <T>
     * @param object    操作对象
     * @param type  0新增/1删除
     * @param data  对象
     */
    public static <T> void log(String object, Integer type, T data) {
        Date currentTime = new Date();
        ActionLog actionLog = new ActionLog();
        actionLog.setObject(object);
        actionLog.setTime(currentTime);
        String type1 = null;
        if(type == 0)
            type1 = "新增";
        else if(type == 1)
            type1 = "删除";
        actionLog.setType(type1);

        // TODO 开发阶段此功能先注释
//        User user = (User) SecurityUtils.getSubject().getSession(true).getAttribute("user");
//        actionLog.setUser(user);

        actionLog.setDescription(LogDescriptionUtil.log(data));
        actionLogRepository.save(actionLog);
    }

    /**
     * 批量新增/删除
     * @param object  操作对象
     * @param type  0批量新增/1批量删除
     * @param data  对象
     * @param <T>
     */

    public static <T> void log(String object, Integer type, List<T> data) {
        for (T t: data) {
            log(object,type,t);
        }
    }

    public static <T> void log(String object){
        ActionLog actionLog = new ActionLog();
        Date currentTime = new Date();
        actionLog.setTime(currentTime);
        actionLog.setObject(object);
        actionLog.setType("导出EXCEL");

        actionLogRepository.save(actionLog);
    }
}
