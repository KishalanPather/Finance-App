package com.kish.financeapp.Envelopes;

import java.math.BigDecimal;

import com.kish.financeapp.Envelopes.enums.EnvelopeGroup;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter 
@Setter
public class Envelope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer envelopeId;

    private String name;
    
    private EnvelopeGroup envelopeGroup;

    private BigDecimal envelopeLimit;

    private BigDecimal envelopeBalance;
}
