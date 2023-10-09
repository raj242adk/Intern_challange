package com.example.internassignment.controller;

import com.example.internassignment.repo.PeteroleumRepo;
import com.example.internassignment.service.PetroleumProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class PetroleumController {
    @Autowired
    private PetroleumProductService petroleumProductService;
    @Autowired
    private PeteroleumRepo peteroleumRepo;

    @GetMapping("/fetchandsave")
    public String fetchAndStoreData(){
        petroleumProductService.fetchDataAndStoreInDatabase();
        return "Data Fetched and Store in the Database";
    }

    @GetMapping("/totalsale")
    public String listTotalSaleOfPetroleumProducts(Model model) {
        Map<String, Double> total = petroleumProductService.listTotalSaleOfPetroleumProducts();
        return total.toString();

    }

    @GetMapping("/top3")
    public List<Map.Entry<String, Long>> getTop3CountriesByTotalSales() {
        return petroleumProductService.getTop3CountriesByTotalSale();
    }
    @GetMapping("/button3")
    public List<Map.Entry<String, Long>> getButton3CountriesByTotalSales() {
        return petroleumProductService.getBottom3CountriesByTotalSales();
    }

    @GetMapping("/averageSales")
    public List<Object[]> getAverageSalesByProductFor4YearIntervals() {
        return petroleumProductService.getAvergageSaleByProdutFor4YearIntervals();
    }




}

