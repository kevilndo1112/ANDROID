package Hieu_iceTea.CurrencyConvertor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnConvert_onClick(View view) {

        hideKeyboard(view);

        EditText txtAmount = findViewById(R.id.txtAmount);
        TextView lblResult = findViewById(R.id.lblResult);

        String strAmountUSD = txtAmount.getText().toString();

        if (strAmountUSD.isEmpty()) {
            lblResult.setText("ERROR: Amount cannot be left blank");
            lblResult.setTextColor(Color.parseColor("#FF0000"));
            return;
        }

        double amountUSD = Double.parseDouble(strAmountUSD);
        double USDToVNDExchangeRate = 22930.1; //Tỉ giá cập nhật lúc 16:00 2021-12-08
        double amountVND = amountUSD * USDToVNDExchangeRate;

        //https://stackoverflow.com/questions/4885254/string-format-to-format-double-in-java
        String strFormatAmountUSD = String.format("%1$,.2f", amountUSD);
        String strFormatAmountVND = String.format("%1$,.2f", amountVND);

        lblResult.setText(strFormatAmountUSD + " USD convert to " + strFormatAmountVND + " VND");
        lblResult.setTextColor(Color.parseColor("#000000"));

    }

    private void hideKeyboard(View view) {
        //https://www.tutorialspoint.com/how-to-close-or-hide-the-virtual-keyboard-on-android

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
    }

}