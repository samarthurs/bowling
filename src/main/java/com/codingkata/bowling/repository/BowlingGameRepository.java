package com.codingkata.bowling.repository;

import com.codingkata.bowling.model.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BowlingGameRepository extends JpaRepository<Frame, Long> {
}
