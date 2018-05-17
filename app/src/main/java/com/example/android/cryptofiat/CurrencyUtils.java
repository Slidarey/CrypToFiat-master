package com.example.android.cryptofiat;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper methods related to requesting and receiving currency data from CryptoCompare.
 */
public final class CurrencyUtils {

    public static final String LOG_TAG = CurrencyUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link CurrencyUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name CurrencyUtils (and an object instance of CurrencyUtils is not needed).
     */
    private CurrencyUtils() {
    }

    public static List<Currency> fetchCurrencyData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException i) {
            i.printStackTrace();
        }

        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not perform HTTP request", e);
        }
        List<Currency> currency = extractCurrencies(jsonResponse);
        return currency;
    }

    public static String fetchConversionCurrencyData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not perform HTTP request", e);
        }
        return jsonResponse;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException m) {
            Log.e(LOG_TAG, "Error creating url", m);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            // it is better to use HttpURLConnection.HTTP_OK than hardcoded 200
            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Http request unsuccessful", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Currency} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Currency> extractCurrencies(String currencyJsonResponse) {

        if (TextUtils.isEmpty(currencyJsonResponse)) {
            //it is better to return Collection.emptyList() than to return null in this case
            return Collections.emptyList();
        }

        // Create an empty ArrayList that we can start adding currency data to
        List<Currency> currencies = new ArrayList<>();


        // Try to parse the JSON response String. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseResponseObject = new JSONObject(currencyJsonResponse);
            JSONObject btcObject = baseResponseObject.getJSONObject("BTC");
            JSONObject ethObject = baseResponseObject.getJSONObject("ETH");

            String[] currencyConversion = new String[]{"AED", "AUD", "BRL", "CAD", "CHF", "EUR", "GBP",
                    "IDR", "INR", "JPY", "KES", "KRW", "NGN", "PLN", "RUB", "THB", "TRY", "TZS", "UAH", "USD"};

            int i;
            for (i = 0; i < currencyConversion.length; i++) {
                Double btcValue = btcObject.getDouble(currencyConversion[i]);
                Double ethValue = ethObject.getDouble(currencyConversion[i]);

                Currency currency = new Currency(btcValue, ethValue, currencyConversion[i]);
                currencies.add(currency);
            }

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Currency objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing t89+659he earthquake JSON results", e);
        }

        // Return the list of currencies
        return currencies;
    }

}


