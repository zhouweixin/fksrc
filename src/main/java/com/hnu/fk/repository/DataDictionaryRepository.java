package com.hnu.fk.repository;

import com.hnu.fk.domain.DataDictionary;
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
     * 根据字典编码和父编码查询
     */
    DataDictionary findByDicIdAndDicParentId(Integer dicId,Integer parentId);

    /**
     * 根据字典编码查询
     *
     * @param dicId
     * @return
     */
    DataDictionary findFirstByDicId(Integer dicId);

    /**
     * 根据字典名字查询
     *
     * @param dicName
     * @return
     */
    DataDictionary findFirstByDicName(String dicName);

    /**
     * 根据字典值查询
     *
     * @param dicContent
     * @return
     */
    DataDictionary findFirstByDicContent(String dicContent);

    /**
     * 排除传入id后根据字典编码查询
     *
     * @param dicId
     * @return
     */
    DataDictionary findFirstByIdNotAndDicId(Long id, Integer dicId);

    /**
     * 排除传入id后根据字典名查询
     *
     * @param id
     * @param dicName
     * @return
     */
    DataDictionary findFirstByIdNotAndDicName(Long id, String dicName);

    /**
     * 排除传入id后根据字典值查询
     *
     * @param id
     * @param dicContent
     * @return
     */
    DataDictionary findFirstByIdNotAndDicContent(Long id, String dicContent);

    /**
     * 根据父编码查询所有子数据
     *
     * @param dicId
     * @return
     */
    List<DataDictionary> findAllByDicParentId(Integer dicId);

    /**
     * 根据父编码分页查询所有子数据
     *
     * @param pageable
     * @param dicId
     * @return
     */
    Page<DataDictionary> findAllByDicParentId(Pageable pageable, Integer dicId);

    /**
     * 分页模糊查询子数据
     *
     * @param pageable
     * @param name
     * @param dicParentId
     * @return
     */
    Page<DataDictionary> findByDicNameLikeAndDicParentId(Pageable pageable, String name, Integer dicParentId);

    /**
     * 查出rank字段最大值
     *
     * @return
     */
    @Query(value = "select max(d.rank) from DataDictionary d")
    Integer maxRank();

    /**
     * 通过主键批量删除
     *
     * @param ids
     */
    @Transactional
    void deleteByIdIn(Collection<Long> ids);
}
