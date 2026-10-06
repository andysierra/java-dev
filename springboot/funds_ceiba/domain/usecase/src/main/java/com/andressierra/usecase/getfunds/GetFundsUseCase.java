package com.andressierra.usecase.getfunds;

import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.gateways.FundRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetFundsUseCase {

    private final FundRepository fundRepository;

    public Flux<Fund> getAll() {
        return fundRepository.findAll();
    }
}
