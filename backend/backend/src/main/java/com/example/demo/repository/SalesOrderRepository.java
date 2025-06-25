package com.example.demo.repository;

import com.example.demo.entity.SalesOrder;
import com.example.demo.enums.SalesOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>,
        JpaSpecificationExecutor<SalesOrder> {
    Long countByOrderStatus(SalesOrderStatus status);

    @Query("SELECT FUNCTION('DATE', so.orderDate), SUM(so.totalAmount) " +
            "FROM SalesOrder so " +
            "WHERE so.orderStatus = 'SHIPPED' AND so.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', so.orderDate) " +
            "ORDER BY FUNCTION('DATE', so.orderDate)")
    List<Object[]> findDailySalesTotalBetween(LocalDate startDate, LocalDate endDate);
}
