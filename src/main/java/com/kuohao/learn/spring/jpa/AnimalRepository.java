package com.kuohao.learn.spring.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/3
 * Time: 下午3:46
 * CopyRight: taobao
 * Descrption:定义成接口,不需要实现类
 */

@Transactional
public interface AnimalRepository extends CrudRepository<Animal, Long> {

    Animal findById(Long id);


    Animal findByName(String name);

//    @Query("select a from Animal a where a.name=:name")
//    Animal findAnimal(@Param("name") String name);

}
