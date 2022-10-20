package com.ooredoo.customerreport.service;

import com.ooredoo.customerreport.dto.CustomerSearchDto;
import com.ooredoo.customerreport.model.CustomerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aalbediwy
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerReadService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerInfo>> getCustomers(final CustomerSearchDto searchDto) {

        final String sql = "select x.*,y.co_id,y.cs_activ_date,y.dn_num from (" +
                "select a.customer_id,a.custcode,a.customer_id_high,a.paymntresp,a.lbc_date,a.cscurbalance,b.cssocialsecno,b.ccline2,b.ccline3,b.ccline4 " +
                "from customer_all a,ccontact_all b " +
                "where a.customer_id = b.customer_id and b.ccbill is not null " +
                ") x,wtk_custall y " +
                "where x.customer_id = y.customer_id " +
                "and x.customer_id in ( "+searchDto.getCustomerIds()+" )";

        final List<CustomerInfo> results = jdbcTemplate.queryForList(sql).stream()
                .map(m ->
                        CustomerInfo.builder()
                                .id(m.get("CUSTCODE").toString())
                                .customerId(m.get("CUSTOMER_ID").toString())
                                .name(m.get("CCLINE2").toString())
                                .civilId(m.get("CSSOCIALSECNO").toString())
                                .msisdn(m.get("DN_NUM").toString())
                                .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }

}
