package com.medilabo.patient_service.models.DTO.requests;

import com.medilabo.patient_service.models.entities.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {
    private String street;
    private String city;
    private String zipCode;

    public Address toAddress() {
        return Address.builder()
                .street(this.street)
                .city(this.city)
                .zipCode(this.zipCode)
                .build();
    }
}
