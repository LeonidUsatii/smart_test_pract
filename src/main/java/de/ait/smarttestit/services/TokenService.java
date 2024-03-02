package de.ait.smarttestit.services;

import de.ait.smarttestit.models.Token;
import jakarta.validation.constraints.NotBlank;

public interface TokenService {
    Token getByCodeOrThrow(@NotBlank final String tokenCode);
}
