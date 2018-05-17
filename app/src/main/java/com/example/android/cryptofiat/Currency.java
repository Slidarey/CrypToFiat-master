package com.example.android.cryptofiat;

/**
 * Created by Zahir on 11/11/2017.
 */

public class Currency {

    private double mValueOfBTC;
    private double mValueOfETH;
    private String mCurrencyDescription;

    public Currency(double valueOfBTC, double valueOfETH, String currencyDescription){
        mValueOfBTC = valueOfBTC;
        mValueOfETH = valueOfETH;
        mCurrencyDescription = currencyDescription;
    }

    public void setValueOfBTC(double valueOfBTC) {
        this.mValueOfBTC = valueOfBTC;
    }

    public double getValueOfBTC() {
        return mValueOfBTC;
    }

    public void setValueOfETH(double valueOfETH) {
        this.mValueOfETH = valueOfETH;
    }

    public double getValueOfETH() {
        return mValueOfETH;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.mCurrencyDescription = currencyDescription;
    }

    public String getCurrencyDescription() {
        return mCurrencyDescription;
    }
}
