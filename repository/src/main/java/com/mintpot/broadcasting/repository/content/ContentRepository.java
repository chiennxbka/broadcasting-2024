package com.mintpot.broadcasting.repository.content;

import com.mintpot.broadcasting.common.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long>, JpaSpecificationExecutor<Content> {
  Set<Content> findContentByIdIn(List<Long> ids);
}
