package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.TokensApi;
import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController implements TokensApi {

    private final TokenService tokenService;

   @Override
   public String generateApplicantToken(NewApplicantTaskDto applicantTaskDto){
       return tokenService.generateApplicantToken(applicantTaskDto);
   }

   }
