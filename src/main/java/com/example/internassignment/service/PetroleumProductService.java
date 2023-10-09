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
import java.util.TreeMap;

@Service
public class PetroleumProductService {

    @Autowired
    private PeteroleumRepo peteroleumRepo;



    public List<PetroleumProduct> fetchDataAndStoreInDatabase() {
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
        return null;
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

    public List<PetroleumProduct> list(){
        List<PetroleumProduct> productList = peteroleumRepo.findAll();
        return productList;
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

    public List<Map.Entry<String, Long>> getBottom3CountriesByTotalSales() {
        List<PetroleumProduct> petroleumProducts = peteroleumRepo.findAll();

        Map<String, Long> totalSalesByCountry = new HashMap<>();

        // Calculate the total sales for each country using Java streams
        petroleumProducts.stream()
                .collect(Collectors.groupingBy(PetroleumProduct::getCountry,
                        Collectors.summingLong(PetroleumProduct::getSale))) // Use summingLong to get a long value
                .forEach((country, totalSale) -> totalSalesByCountry.put(country, totalSale));

        List<Map.Entry<String, Long>> sortedEntries = totalSalesByCountry.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue())
                .limit(3)
                .collect(Collectors.toList());

        return sortedEntries;
    }

    public List<Object[]> getAvergageSaleByProdutFor4YearIntervals() {
        List<PetroleumProduct> allData = peteroleumRepo.findAll();

        // Use Java Stream API to group and calculate average sales
        Map<String, Map<String, Double>> averageSales = allData.stream()
                .filter(data -> data.getSale() > 0) // Exclude zero sales
                .collect(Collectors.groupingBy(
                        PetroleumProduct::getPetroleum_product,
                        Collectors.groupingBy(
                                data -> calculate4YearInterval(data.getYear()),
                                TreeMap::new, // Use TreeMap to sort years
                                Collectors.averagingDouble(data -> data.getSale())
                        )
                ));

        // Convert the result into the desired format with sorted years
        List<Object[]> result = averageSales.entrySet().stream()
                .flatMap(productEntry -> productEntry.getValue().entrySet().stream()
                        .map(intervalEntry -> new Object[]{
                                productEntry.getKey(),
                                intervalEntry.getKey(),
                                intervalEntry.getValue()
                        }))
                .collect(Collectors.toList());

        return result;
    }

    private String calculate4YearInterval(Integer year) {
        int startYear = year;
        int endYear = startYear + 3;
        return startYear + "-" + endYear;
    }
}







