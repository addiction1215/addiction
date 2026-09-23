package com.addiction.smokefree.service;

import com.addiction.smokefree.service.request.SmokeFreeConfirmationServiceRequest;
import com.addiction.smokefree.service.response.SmokeFreeConfirmationResponse;

public interface SmokeFreeConfirmationService {

    SmokeFreeConfirmationResponse confirm(SmokeFreeConfirmationServiceRequest request);
}
