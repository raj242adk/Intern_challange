package com.example.internassignment.repo;

import com.example.internassignment.entity.PetroleumProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PeteroleumRepo extends JpaRepository<PetroleumProduct,Integer> {



    @Query(value = "SELECT country, SUM(sale) as total_sales " +
            "FROM petroleum_product " +
            "GROUP BY country " +
            "ORDER BY total_sales DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTop3CountriesByTotalSales();


    @Query(value = "SELECT country, SUM(sale) as total_sales " +
            "FROM petroleum_product " +
            "GROUP BY country " +
            "ORDER BY total_sales ASC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findButton3CountriesByTotalSales();

    // Query to calculate the average sale within a specified year range (excluding zero sales)
    @Query(value = "SELECT petroleum_product, AVG(sale) as average_sale " +
            "FROM petroleum_product " +
            "WHERE year BETWEEN :intervalStart AND :intervalEnd AND sale > 0 " +
            "GROUP BY petroleum_product", nativeQuery = true)
    Object[] calculateAverageSaleWithinInterval(@Param("intervalStart") int intervalStart, @Param("intervalEnd") int intervalEnd);

}
