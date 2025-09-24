package edu.ccrm.domain;

/**
 * Custom exception for exceeding maximum credit limit
 */
public class MaxCreditLimitExceededException extends Exception {
    private final int currentCredits;
    private final int attemptedCredits;
    private final int maxLimit;

    public MaxCreditLimitExceededException(int currentCredits, int attemptedCredits, int maxLimit) {
        super(String.format("Credit limit exceeded: Current=%d, Attempted=%d, Max=%d", 
                           currentCredits, attemptedCredits, maxLimit));
        this.currentCredits = currentCredits;
        this.attemptedCredits = attemptedCredits;
        this.maxLimit = maxLimit;
    }

    public int getCurrentCredits() { return currentCredits; }
    public int getAttemptedCredits() { return attemptedCredits; }
    public int getMaxLimit() { return maxLimit; }
}