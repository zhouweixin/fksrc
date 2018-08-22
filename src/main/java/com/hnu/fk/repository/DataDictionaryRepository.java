package com.hnu.fk.repository;

import com.hnu.fk.domain.DataDictionary;
import com.hnu.fk.domain.DataDictionaryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @Author: huXuDong
 * @Description: 数据字典
 * @Date: Created in 10:20 2018/8/15
 * @Modified By:
 */
@Repository
public interface DataDictionaryRepository extends JpaRepository<DataDictionary, Long> {

    /**
     * 根据字典值和外键查询
     */
    DataDictionary findFirstByDicContentAndDataDictionaryTypeId(String dicContent,Long id);

    /**
     * 排除传入id后根据字典值和外键查询
     */
    DataDictionary findFirstByIdNotAndDicContentAndDataDictionaryTypeId(Long id,String dicContent,Long typeId);

    /**
     * 根据外键查询所有
     */
    List<DataDictionary> findAllByDataDictionaryTypeId(Long id);
    List<DataDictionary> findAllByDataDictionaryTypeIdIn(Collection<Long> ids);
    Page<DataDictionary> findAllByDataDictionaryTypeId(Long id,Pageable pageable);
    Page<DataDictionary> findAllByDataDictionaryTypeIdAndDicNameLike(Long id,String dicName,Pageable pageable);
    /**
     * 查出rank字段最大值
     *
     * @return
     */
    @Query(value = "select max(d.rank) from DataDictionary d")
    Integer maxRank();

    /**
     * 批量删除
     */
    void deleteByIdIn(Collection<Long> ids);

}
