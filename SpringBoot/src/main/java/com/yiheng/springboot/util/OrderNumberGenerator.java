package com.yiheng.springboot.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderNumberGenerator {
    private AtomicInteger sequence = new AtomicInteger(0);

    public String generateOrderNumber() {
        LocalDate currentDate = LocalDate.now();
        String datePart = currentDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = sequence.getAndIncrement();
        String seqPart = String.format("%03d", seq);
        return "ORDER" + datePart + seqPart;
    }
}
