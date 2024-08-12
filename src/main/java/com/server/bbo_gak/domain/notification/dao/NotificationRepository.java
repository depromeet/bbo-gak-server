package com.server.bbo_gak.domain.notification.dao;

import com.server.bbo_gak.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
