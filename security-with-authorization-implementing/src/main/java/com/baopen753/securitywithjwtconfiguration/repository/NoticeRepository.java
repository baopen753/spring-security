package com.baopen753.securitywithauthorizationimplementing.repository;


import com.baopen753.securitywithauthorizationimplementing.model.Notice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {

    @Query(value = "from Notice n where CURDATE() BETWEEN  n.noticBegDt AND n.noticEndDt")
    List<Notice> findAllActiveNotices();

}
