package com.hnu.fk.repository;

import com.hnu.fk.domain.DataDictionaryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface DataDictionaryTypeRepository extends JpaRepository<DataDictionaryType,Long> {
    /**
     * 根据主键查询
     */
    DataDictionaryType findFirstById(Long id);
    DataDictionaryType findFirstByTypeValue(String name);
    DataDictionaryType findByIdIn(Collection<Long> ids);
    /**
     * 根据主键批量删除
     */
    void deleteByIdIn(Collection<Long> ids);

    /**
     * 查出rank字段最大值
     *
     * @return
     */
    @Query(value = "select max(d.rank) from DataDictionaryType d")
    Integer maxRank();

    /**
     * 分页模糊查询
     */
    Page<DataDictionaryType> findByTypeNameLike(String name,Pageable pageable);
}
