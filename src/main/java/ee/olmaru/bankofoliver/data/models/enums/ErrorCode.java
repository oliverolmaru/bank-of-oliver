package ee.olmaru.bankofoliver.data.models.enums;

public enum ErrorCode {
    CUS404, // Customer does not exist
    ACC404, // Account does not exist
    BAL404, // Balance does not exist
    TRA404, // Transaction does not exist
    TRA405, // Insufficient funds
    VAL400, // Invalid field value (invalid currency, invalid direction, amount, etc)
}
