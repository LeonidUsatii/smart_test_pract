package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.mail.EmailRequestDto;

public interface EmailService {

    void sendSimpleMessage(EmailRequestDto emailRequest);
}
