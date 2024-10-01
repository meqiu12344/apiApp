package com.example.apiapp.services;

import com.example.apiapp.api.CurrencyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NbpApiService {

    @GET("exchangerates/rates/A/{currency}/")
    Call<CurrencyResponse> getExchangeRate(@Path("currency") String currencyCode);

    @GET("exchangerates/rates/C/{currency}/")
    Call<CurrencyResponse> getSellAndBuyRates(@Path("currency") String currencyCode);

}
