package com.eqca.repository.mapper.a.master;

import com.eqca.repository.dto.entity.MdBiz;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BizMapper {

    /**
     * 비즈니스 목록 조회
     * @return List
     */
    List<MdBiz> findAllBiz();

}
