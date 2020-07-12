package com.shivendra.codeserver.repository;

import com.shivendra.codeserver.entity.SdlcSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SdlcSystemRepository extends JpaRepository<SdlcSystem, Long>{

}
