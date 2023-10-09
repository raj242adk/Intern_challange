package com.example.internassignment.controller;


import com.example.internassignment.entity.PetroleumProduct;
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

@Controller
@RequestMapping("/test")
public class ViewController {

    @Autowired
    private PetroleumProductService petroleumProductService;

    @GetMapping("/home")
    public String home(){
        return "Index";
    }

    @GetMapping("/all-products")
    public String getAllProducts(Model model) {
        List<PetroleumProduct> allProductDisplay = petroleumProductService.list();
        model.addAttribute("AllProductDisplay", allProductDisplay);
        return "ALL";
    }

    @GetMapping("/top3")
    public String getTop3CountriesByTotalSales(Model model) {
         List<Map.Entry<String, Long>> top3=petroleumProductService.getTop3CountriesByTotalSale();
         model.addAttribute("topThree",top3);
         return "topThree.html";
    }
    @GetMapping("/button3")
    public String getButton3Countries(Model model){
        List<Map.Entry<String, Long>> top3=petroleumProductService.getBottom3CountriesByTotalSales();
        model.addAttribute("buttonThree",top3);
        return "buttonThree.html";
    }

    @GetMapping("/average")
    public String getAverageSaleByProductFor4YearIntervals(Model model) {
        List<Object[]> averageSales = petroleumProductService.getAvergageSaleByProdutFor4YearIntervals();
        model.addAttribute("averageSales", averageSales);
        return "Average";
    }
}

