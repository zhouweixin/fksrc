package com.hnu.fk.repository;

import com.hnu.fk.domain.DataDictionary;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;


@Repository
public interface DataDictionaryRepository  extends JpaRepository<DataDictionary,Long> {
    /**
     * 新增查重
     * @param dicId
     * @return
     */
    //根据字典编号查询
    DataDictionary findByDicId(Integer dicId);
    //根据字典名查询
    DataDictionary findByDicName(String dicName);
    //根据字典值查询
    DataDictionary findByDicContent(String dicContent);

    /**
     * 更新查重
     * @param dicId
     * @return
     */
    //排除当前id外根据字典编号查询
    DataDictionary findFirstByIdNotAndDicId(Long id,Integer dicId);
    //同上,根据字典名查询
    DataDictionary findFirstByIdNotAndDicName(Long id,String dicName);
    //同上,根据字典值查询
    DataDictionary findFirstByIdNotAndDicContent(Long id,String dicContent);

    //根据父类Dic id查询
    List<DataDictionary> findAllByDicParentId(Integer dicId);
    Page<DataDictionary> findAllByDicParentId(Pageable pageable,Integer dicId);
    //分页模糊查询子类
    Page<DataDictionary> findByDicNameLikeAndDicParentId(Pageable pageable,String name,Integer dicParentId);
    @Query(value = "select max(d.rank) from DataDictionary d")
    Integer maxRank();
    //通过主键批量删除
    @Transactional
    void deleteByIdIn(Collection<Long> ids);
}
