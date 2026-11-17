package com.amrmedhat.qa.data;

/** Public demo account shown on https://the-internet.herokuapp.com/login (not a secret). */
public final class Credentials {

    private Credentials() {
    }

    public static final String VALID_USERNAME = System.getProperty("loginUser", "tomsmith");
    public static final String VALID_PASSWORD = System.getProperty("loginPassword", "SuperSecretPassword!");
}
