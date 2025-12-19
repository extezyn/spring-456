package com.example.demo.repository;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findByLesson(Lesson lesson);
}


