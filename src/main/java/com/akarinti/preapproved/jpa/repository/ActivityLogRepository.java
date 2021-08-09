package com.akarinti.preapproved.jpa.repository;

import com.akarinti.preapproved.jpa.entity.ActivityLog;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends BaseRepository<ActivityLog>, QuerydslPredicateExecutor<ActivityLog> {
}
