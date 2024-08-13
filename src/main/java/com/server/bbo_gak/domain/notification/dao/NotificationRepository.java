package com.server.bbo_gak.domain.notification.dao;

import com.server.bbo_gak.domain.notification.entity.Notification;
import com.server.bbo_gak.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByUserAndIsReadFalse(User user);

    List<Notification> findAllByUserOrderByCreatedDateDesc(User user);

    List<Notification> findAllByUserAndIsReadFalse(User user);

}

