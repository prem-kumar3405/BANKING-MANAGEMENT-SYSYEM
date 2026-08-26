package com.prem.banking_management_system.exceptions;

import java.time.LocalDateTime;

public record ApiErrorResponse(int status, String message, LocalDateTime timestamp)
{

}
