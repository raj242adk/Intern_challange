package com.example.internassignment.repo;

import com.example.internassignment.entity.PetroleumProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PeteroleumRepo extends JpaRepository<PetroleumProduct,Integer> {

    @Query(value = "SELECT country, SUM(sale) AS totalSales " +
            "FROM  " +
            "GROUP BY country " +
            "ORDER BY totalSales DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTop3CountriesByTotalSales();

    @Query("SELECT p.country, SUM(p.sale) AS tolalSales FROM PetroleumProduct p GROUP BY p.country ORDER BY tolalSales ASC ")
    List<Object[]> findButton3CountriesByTotalSales();

}
