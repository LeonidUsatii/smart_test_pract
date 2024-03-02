package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Token;
import de.ait.smarttestit.repositories.TokensRepository;
import de.ait.smarttestit.services.TokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokensRepository tokensRepository;
    @Override
    public Token getByCodeOrThrow(String tokenCode) {
        return tokensRepository.findByCode(tokenCode)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with code: " + tokenCode));
    }
}
