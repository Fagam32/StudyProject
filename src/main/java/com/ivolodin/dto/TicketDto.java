package com.ivolodin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDto {
    public String userName;
    public String trainName;
    public String fromStation;
    public String toStation;
    public LocalDateTime departure;
    public LocalDateTime arrival;
}
