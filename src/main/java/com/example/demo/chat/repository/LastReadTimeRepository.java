package com.example.demo.chat.repository;

import com.example.demo.entity.LastReadTime;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface LastReadTimeRepository extends CrudRepository<LastReadTime, LastReadTimeId> {
    List<LastReadTime> findAllBySiteUserId(String siteUser);
}
