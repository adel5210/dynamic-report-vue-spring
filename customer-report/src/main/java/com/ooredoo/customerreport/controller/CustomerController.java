package com.ooredoo.customerreport.controller;

import com.ooredoo.customerreport.dto.CustomerSearchDto;
import com.ooredoo.customerreport.model.CustomerInfo;
import com.ooredoo.customerreport.service.CustomerReadService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author aalbediwy
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8186")
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerReadService customerReadService;

    @PostMapping
    public ResponseEntity<List<CustomerInfo>> getCustomersFromIds(@RequestBody CustomerSearchDto searchDto){
        return customerReadService.getCustomers(searchDto);
    }

}
