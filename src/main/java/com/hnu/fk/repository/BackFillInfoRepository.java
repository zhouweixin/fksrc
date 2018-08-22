package com.hnu.fk.repository;

import com.hnu.fk.domain.BackFillInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
public interface BackFillInfoRepository extends JpaRepository<BackFillInfo,Integer> {
    /**
     * 通过时间段查询
     * @param startDate
     * @param endDate
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM produce_back_fill_info where maintenance_schedule_code in (SELECT id FROM produce_maintenance_schedule WHERE entering_time>=?1 and entering_time <=Dateadd(dd,1,?2) AND equipment_code LIKE ?3) order by finish_time desc", nativeQuery = true)
    public Page<BackFillInfo> findByEnteringTimeAndEquipment(String startDate, String endDate,String equipmentCode, Pageable pageable);

    @Query(value = "SELECT * FROM produce_back_fill_info where maintenance_schedule_code in (SELECT id FROM produce_maintenance_schedule WHERE entering_time>=?1 and entering_time <=Dateadd(dd,1,?2) AND equipment_code LIKE ?3) order by finish_time desc", nativeQuery = true)
    public List<BackFillInfo> findByEnteringTimeAndEquipment(String startDate, String endDate,String equipmentCode);
}
