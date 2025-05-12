package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.PoliciesDTO;
import com.hotelJB.hotelJB_API.models.entities.Policies;
import com.hotelJB.hotelJB_API.models.responses.PoliciesResponse;
import com.hotelJB.hotelJB_API.repositories.PoliciesRepositoy;
import com.hotelJB.hotelJB_API.services.PoliciesService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoliciesServiceImpl implements PoliciesService{
    @Autowired
    private PoliciesRepositoy policiesRepositoy;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(PoliciesDTO data) throws Exception {
        try{
            Policies policies = new Policies(data.getWarrantyEs(),data.getWarrantyEn(),data.getCancellationEs(), data.getCancellationEn());
            policiesRepositoy.save(policies);
        }catch(Exception e){
            throw new Exception("Error save policies");
        }
    }

    @Override
    public void update(PoliciesDTO data, int policiesId) throws Exception {
        Policies policies = policiesRepositoy.findById(policiesId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Policies"));

        policies.setWarrantyEs(data.getWarrantyEs());
        policies.setWarrantyEn(data.getWarrantyEn());
        policies.setCancellationEs(data.getCancellationEs());
        policies.setCancellationEn(data.getCancellationEn());
        policiesRepositoy.save(policies);
    }

    @Override
    public void delete(int policiesId) throws Exception {
        Policies policies = policiesRepositoy.findById(policiesId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Policies"));
        policiesRepositoy.delete(policies);
    }

    @Override
    public List<Policies> getAll() {
        return policiesRepositoy.findAll();
    }

    @Override
    public Optional<PoliciesResponse> findById(int policiesId, String lang) {
        Optional<Policies> policies = policiesRepositoy.findById(policiesId);

        return policies.map(value -> new PoliciesResponse(
                value.getPoliciesId(),
                lang.equals("es") ? value.getWarrantyEs() : value.getWarrantyEn(),
                lang.equals("es") ? value.getCancellationEs() : value.getCancellationEn()
        ));
    }

    @Override
    public List<PoliciesResponse> findByLanguage(String language) {
        List<Policies> policies = policiesRepositoy.findAll();

        return policies.stream().map(value -> new PoliciesResponse(
                value.getPoliciesId(),
                language.equals("es") ? value.getWarrantyEs() : value.getWarrantyEn(),
                language.equals("es") ? value.getCancellationEs() : value.getCancellationEn()
        )).toList();
    }
}