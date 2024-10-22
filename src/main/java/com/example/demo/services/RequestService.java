package com.example.demo.services;

import com.example.demo.models.Request;
import com.example.demo.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    // Crear una nueva solicitud
    public Request createRequest(Long retailerId, Long wholesalerId) {
        Request request = new Request(retailerId, wholesalerId, "PENDING");
        return requestRepository.save(request);
    }

    // Aceptar o denegar una solicitud
    public void updateRequestStatus(Long requestId, String status) {
        Optional<Request> requestOpt = requestRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            Request request = requestOpt.get();
            request.setStatus(status);
            requestRepository.save(request);
        }
    }

    // Verificar si el minorista tiene acceso
    public boolean hasAccess(Long retailerId, Long wholesalerId) {
        List<Request> requests = requestRepository.findByRetailerIdAndWholesalerId(retailerId, wholesalerId);
        return requests.stream().anyMatch(r -> r.getStatus().equals("ACCEPTED"));
    }
}

