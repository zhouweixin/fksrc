package com.hnu.fk.repository;

import com.hnu.fk.domain.MaintenanceSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/21
 */
public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule,Integer> {
    /**
     * 通过检修设备和故障描述模糊查询(二者均有值，某一者为空则使用单一查询）
     * @param name
     * @param description
     * @return
     */
    //@Query(value = "SELECT * FROM produce_maintenance_schedule where equipment_code =(SELECT id FROM produce_equipment_info WHERE name LIKE ?1) AND description LIKE ?2 AND flag =0 order by entering_time desc", nativeQuery = true)
    Page<MaintenanceSchedule> findAllByEquipmentInfoId_NameLikeAndDescriptionLikeAndFlag(String name, String description,Pageable pageable,Integer flag);

    /**
     * 通过检修设备模糊查询
     * @param name
     * @param pageable
     * @return
     */
    Page<MaintenanceSchedule> findAllByEquipmentInfoId_NameLikeAndFlag(String name,Pageable pageable,Integer flag);

    /**
     * 通过故障描述模糊查询
     * @param description
     * @param pageable
     * @return
     */
    Page<MaintenanceSchedule> findAllByDescriptionLike(String description,Pageable pageable);

    /**
     * 通过完成状态查询
     * @param flag
     * @param pageable
     * @return
     */
    Page<MaintenanceSchedule> findAllByFlag(Integer flag,Pageable pageable);
}
