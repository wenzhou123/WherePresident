package com.example.presidenttracker.mapper;

import com.example.presidenttracker.model.Leader;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LeaderMapper {
    List<Leader> findAll();

    void insert(Leader leader);

    Leader findById(Long id);

    Leader findByName(String name);

    void update(Leader leader);
}
