package com.hnu.fk.utils;

import com.hnu.fk.domain.ActionLog;
import com.hnu.fk.domain.User;
import com.hnu.fk.repository.ActionLogRepository;
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
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Date currentTime = new Date();
        ActionLog actionLog = new ActionLog();
        actionLog.setObject(object);
        actionLog.setTime(currentTime);
        actionLog.setType("修改");
        if (request.getSession().getAttribute("user") != null) {
            actionLog.setUser((User) request.getSession().getAttribute("user"));
        }
        actionLog.setDescription(LogDescriptionUtil.log(oldData,newData));
        actionLogRepository.save(actionLog);
    }

    /**
     * 新增/删除 操作记录
     * @param <T>
     * @param object    操作对象
     * @param type  0新增/1删除
     * @param data  对象
     * @return
     */
    public static <T> void log(String object, Integer type, T data) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
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
        if (request.getSession().getAttribute("user") != null) {
            actionLog.setUser((User) request.getSession().getAttribute("user"));
        }
        actionLog.setDescription(LogDescriptionUtil.log(data));
        actionLogRepository.save(actionLog);
    }

    /**
     * 批量删除
     * @param object
     * @param data
     * @param <T>
     */
    public static <T> void log(String object, List<T> data) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Date currentTime = new Date();
        ActionLog actionLog = new ActionLog();
        actionLog.setObject(object);
        actionLog.setTime(currentTime);
        actionLog.setType("批量删除");
        if (request.getSession().getAttribute("user") != null) {
            actionLog.setUser((User) request.getSession().getAttribute("user"));
        }
        actionLog.setDescription(LogDescriptionUtil.log(data));
        actionLogRepository.save(actionLog);
    }
}
