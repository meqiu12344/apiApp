package com.example.apiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apiapp.api.CurrencyResponse;
import com.example.apiapp.api.Rate;
import com.example.apiapp.services.NbpApiService;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView rateView;
    private TextView buyAndSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rateView = findViewById(R.id.rateView);
        buyAndSell = findViewById(R.id.textView1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nbp.pl/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        NbpApiService nbpApiService = retrofit.create(NbpApiService.class);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String code = spinner.getSelectedItem().toString();
                code = code.substring(0,3);
                nbpApiService.getExchangeRate(code).enqueue(new Callback<CurrencyResponse>() {
                    @Override
                    public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                        CurrencyResponse currencyResponse = response.body();
                        if (currencyResponse != null && currencyResponse.getRates() != null && !currencyResponse.getRates().isEmpty()){
                            double exchangeRate = currencyResponse.getRates().get(0).getMid();
                            String currencyName = currencyResponse.getCurrency();
                            rateView.setText(String.format("%s %f", currencyName, exchangeRate));
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrencyResponse> call, Throwable throwable) {
                        Log.e("API error", "Błąd podczas pobierania danych: " + throwable.getMessage());
                        rateView.setText("Nie udało się pobrać danych. XD beka z cb 🧖🏿‍♂️🧑🏿‍🦽");
                    }
                });

                nbpApiService.getSellAndBuyRates(code).enqueue(new Callback<CurrencyResponse>() {
                    @Override
                    public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                        CurrencyResponse currencyResponse = response.body();
                        if (currencyResponse != null && currencyResponse.getRates() != null && !currencyResponse.getRates().isEmpty()){
                            double ask = currencyResponse.getRates().get(0).getAsk();
                            double bid = currencyResponse.getRates().get(0).getBid();
                            String currencyName = currencyResponse.getCurrency();
                            buyAndSell.setText(String.format("Asked price for %s: %.2f and bidded: %.2f", currencyName, ask, bid));
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrencyResponse> call, Throwable throwable) {
                        Log.e("API error", "Błąd podczas pobierania danych: " + throwable.getMessage());
                        rateView.setText("Nie udało się pobrać danych. XD beka z cb 🧖🏿‍♂️🧑🏿‍🦽");
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }
}