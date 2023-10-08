package com.example.internassignment.service;


import com.example.internassignment.entity.PetroleumProduct;
import com.example.internassignment.repo.PeteroleumRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PetroleumProductService {

    @Autowired
    private PeteroleumRepo peteroleumRepo;


    public void fetchDataAndStoreInDatabase() {
        String apiEndPoint = "https://raw.githubusercontent.com/younginnovations/internship-challenges/master/programming/petroleum-report/data.json";
        // Make an API call to fetch data from the provided URL
        ResponseEntity<String> response = new RestTemplate().getForEntity(
                apiEndPoint, String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Parse the JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                List<PetroleumProduct> productList = objectMapper.readValue(response.getBody(), new TypeReference<List<PetroleumProduct>>() {
                });
                peteroleumRepo.saveAll(productList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Map<String, Double> listTotalSaleOfPetroleumProducts() {
        List<PetroleumProduct> petroleumProducts = peteroleumRepo.findAll();
        // Calculate the total sale for each petroleum product
        Map<String, Double> totalSales = new HashMap<>();
        for (PetroleumProduct product : petroleumProducts) {
            String productName = product.getPetroleum_product();
            double sale = product.getSale();
            totalSales.put(productName, totalSales.getOrDefault(productName, 0.0) + sale);
        }
        return totalSales;
    }

    public List<Map.Entry<String,Long>> getTop3CountriesByTotalSale(){
        List<PetroleumProduct> petroleumProducts=peteroleumRepo.findAll();

        Map<String,Long> totalSalesByCountry=new HashMap<>();

        petroleumProducts.stream()
                .collect(Collectors.groupingBy(PetroleumProduct::getCountry,Collectors.summingLong(PetroleumProduct::getSale)))
                .forEach((country,totalSale)->totalSalesByCountry.put(country,totalSale));

        List<Map.Entry<String,Long>> sortedEntries = totalSalesByCountry.entrySet()
                .stream()
                .sorted(Map.Entry.<String,Long> comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        return sortedEntries;
    }






}
