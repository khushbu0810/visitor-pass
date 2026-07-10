package com.example.PassMicroservice.service.impl;

import com.example.PassMicroservice.config.VisitorClientConfig;
import com.example.PassMicroservice.model.Pass;
import com.example.PassMicroservice.repository.PassRepository;
import com.example.PassMicroservice.service.PassService;
import com.example.PassMicroservice.util.QRCodeGenerator;
import com.example.PassMicroservice.utils.VisitorDTO;
import com.example.core.enums.PassStatus;
import com.example.core.enums.VisitorStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PassServiceImpl implements PassService {
    private final PassRepository passRepository;
    private final VisitorClientConfig visitorClient;

    public PassServiceImpl(PassRepository passRepository, VisitorClientConfig visitorClient) {
        this.passRepository = passRepository;
        this.visitorClient = visitorClient;
    }

    @Override
    public Pass generatePassForVisitor(UUID residentId, UUID visitorId) {
        if (passRepository.findByVisitorId(visitorId).isPresent()) {
            throw new RuntimeException("Pass already generated for this visitor.");
        }

        Pass pass = new Pass();

        pass.setResidentId(residentId);
        pass.setVisitorId(visitorId);
        pass.setGeneratedAt(LocalDateTime.now());
        pass.setStatus(PassStatus.ACTIVE);

        String qrToken=UUID.randomUUID().toString();
        pass.setQrCode(qrToken);
        pass.setQrImage(QRCodeGenerator.generateQRCodeImage(qrToken));

        return passRepository.save(pass);
    }

    @Override
    public List<Pass> getAllPasses() {
        return passRepository.findAll();
    }

    @Override
    public Optional<Pass> getPassById(UUID passId) {
        return passRepository.findById(passId);
    }

    @Override
    public Optional<Pass> getPassForVisitor(UUID visitorId) {
        return passRepository.findByVisitorId(visitorId);
    }

    @Override
    public Pass verifyPass(String qrCode) {
        Pass pass = passRepository.findByQrCode(qrCode)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Invalid QR Code."
                        ));

        switch (pass.getStatus()) {
            case USED ->
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Pass has already been used."
                    );

            case CANCELLED ->
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Pass has been cancelled."
                    );

            case EXPIRED ->
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Pass has expired."
                    );

            case ACTIVE -> {
            }
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        VisitorDTO visitor = visitorClient.getVisitorById(pass.getVisitorId(), token);

        if (!visitor.getVisitDate().equals(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Visitor is not scheduled for today."
            );
        }

        switch (visitor.getStatus()) {
            case PENDING -> {
                return pass;
            }

            case APPROVED ->
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Visitor has already been approved."
                    );

            case ENTERED ->
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Visitor has already entered."
                    );

            case EXITED ->
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Visitor has already exited."
                    );

            case CANCELLED ->
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Visitor has been cancelled."
                    );

            case EXPIRED ->
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Visitor has expired."
                    );
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to verify visitor."
        );
    }

    @Override
    public boolean deletePass(UUID passId) {
        if (!passRepository.existsById(passId)) {
            return false;
        }
        passRepository.deleteById(passId);
        return true;
    }

    @Override
    public Pass markPassUsed(UUID passId) {
        Pass pass = passRepository.findById(passId)
                .orElseThrow(() -> new RuntimeException("Pass not found"));
        if (pass.getStatus() == PassStatus.USED) {
            throw new RuntimeException("Pass already used.");
        }
        if (pass.getStatus() != PassStatus.ACTIVE) {
            throw new RuntimeException("Only ACTIVE passes can be used.");
        }
        pass.setStatus(PassStatus.USED);
        return passRepository.save(pass);
    }

    @Override
    public Optional<Pass> cancelPass(UUID passId) {
        Optional<Pass> optPass = passRepository.findById(passId);
        if (optPass.isEmpty()) {
            return Optional.empty();
        }
        Pass pass = optPass.get();
        if (pass.getStatus() == PassStatus.CANCELLED) {
            throw new RuntimeException("Pass already cancelled.");
        }
        if (pass.getStatus() == PassStatus.USED) {
            throw new RuntimeException("Used pass cannot be cancelled.");
        }
        if (pass.getStatus() == PassStatus.EXPIRED) {
            throw new RuntimeException("Expired pass cannot be cancelled.");
        }
        pass.setStatus(PassStatus.CANCELLED);
        return Optional.of(passRepository.save(pass));
    }

    @Override
    public void expirePass(UUID passId) {
        Pass pass = passRepository.findById(passId)
                .orElseThrow(() -> new RuntimeException("Pass not found"));
        if (pass.getStatus() != PassStatus.ACTIVE) {
            return;
        }
        pass.setStatus(PassStatus.EXPIRED);
        passRepository.save(pass);
    }
}
