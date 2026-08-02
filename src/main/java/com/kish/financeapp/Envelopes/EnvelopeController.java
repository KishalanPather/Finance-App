package com.kish.financeapp.Envelopes;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kish.financeapp.Envelopes.dtos.CreateEnvelopeRequestDto;
import com.kish.financeapp.Envelopes.dtos.EnvelopeResponseDto;
import com.kish.financeapp.Envelopes.dtos.FundRequestDto;

@RestController
@RequestMapping("/api/v1/envelope")
public class EnvelopeController {
    private final EnvelopeService envelopeService;

    public EnvelopeController(EnvelopeService envelopeService){
        this.envelopeService = envelopeService;
    }


    @PostMapping()
    public EnvelopeResponseDto createEnvelope(@RequestBody CreateEnvelopeRequestDto envelopeRequest){
        return envelopeService.createEnvelope(envelopeRequest);
    }

    @PostMapping("/{id}/funding")
    public EnvelopeResponseDto fundEnvelope(
        @PathVariable("id") Integer envelopeId,
        @RequestBody FundRequestDto fundRequest
    ){
        return envelopeService.fundEnvelope(envelopeId, fundRequest);
    }

    @GetMapping()
    public List<EnvelopeResponseDto> getAllEnvelopes(){
        return envelopeService.getAllEnvelopes();
    }
}
