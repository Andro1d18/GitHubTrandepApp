package com.example.pet.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DateTimeKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    // свойства главного класса страницы
    public MainCreditDataForm mainForm; // наш подкласс для работы с интерфейсом

    // стартовый метод главного класса
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // инициализируем интерфейсные элементы через наш отдельный класс
        mainForm = new MainCreditDataForm();
        mainForm.initiateMainForm();

        //Настраиваем для поля ввода слушателя изменений в тексте TextChangedListener:
        //mEditText.addTextChangedListener(new NumberTextWatcher());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // слой интерфейса
    class MainCreditDataForm {
        // свойства
        private EditText etPayment;
        private EditText etCreditMonthLenght;
        private EditText etCreditAmount;
        private EditText etRate;
        private EditText etOverPayment;
        private EditText etFullCreditCost;
        private EditText etPrimePayment;
        private EditText etApartmentCost;
        private EditText etStartDate;
        public int deltaDirection;      // направление изменений
        public double delta;            // дельта приращения значения реквизитов
        public int AnchorID;            // ИД пользовательского сценария с якорями

        // инициализация текстовых элементов для использования в дальнейших расчетах
        public void initiateMainForm() {
            //Инициализируем наши текстовые элементы:
            etPayment = (EditText) findViewById(R.id.editText1);            // 1
            etCreditMonthLenght = (EditText) findViewById(R.id.editText2);  // 2
            etCreditAmount = (EditText) findViewById(R.id.editText3);       // 3
            etRate = (EditText) findViewById(R.id.editText4);               // 4
            etOverPayment = (EditText) findViewById(R.id.editText5);        // 5
            etFullCreditCost = (EditText) findViewById(R.id.editText6);     // 6
            etPrimePayment = (EditText) findViewById(R.id.editText7);       // 7
            etApartmentCost = (EditText) findViewById(R.id.editText8);      // 8
            etStartDate = (EditText) findViewById(R.id.editText9);          // 9

            // отключить лишние кнопки
            this.disableButtons();
        }

        // считать данные с формы и заполнить кредит
        public void getCreditFromForm(Credit credit) {
            // P
            try {
                credit.Payment = Double.parseDouble(etPayment.getText().toString().trim());
            } catch(NumberFormatException ex)
            {
                credit.Payment = 0;
            }
            // CML
            try {
                credit.CreditMonthLenght = Double.parseDouble(etCreditMonthLenght.getText().toString().trim());
            } catch(NumberFormatException ex)
            {
                credit.CreditMonthLenght = 0;
            }
            // CA
            try {
                credit.CreditAmount = Double.parseDouble(etCreditAmount.getText().toString().trim());
            } catch(NumberFormatException ex)
            {
                credit.CreditAmount = 0;
            }
            // R
            try {
                credit.setRate(Double.parseDouble(etRate.getText().toString().trim()));
            } catch(NumberFormatException ex)
            {
                credit.setRate(0);
            }
            // OP
            try {
                credit.OverPayment = Double.parseDouble(etOverPayment.getText().toString().trim());
            } catch(NumberFormatException ex)
            {
                credit.OverPayment = 0;
            }
            // F
            try {
                credit.FullCreditCost = Double.parseDouble(etFullCreditCost.getText().toString().trim());
            } catch(NumberFormatException ex)
            {
                credit.FullCreditCost = 0;
            }
            // PP
            try {
                credit.PrimePayment = Double.parseDouble(etPrimePayment.getText().toString().trim());
            } catch(NumberFormatException ex)
            {
                credit.PrimePayment = 0;
            }
            // AC
            try {
                credit.ApartmentCost = Double.parseDouble(etApartmentCost.getText().toString().trim());
            } catch(NumberFormatException ex)
            {
                credit.ApartmentCost = 0;
            }
        }

        // наполнить форму из свойств кредита
        public void setFormFromCredit(Credit credit) {
            // считать из свойств текущего кредита
            double P = credit.Payment;
            double CML = credit.CreditMonthLenght;
            double CA = credit.CreditAmount;
            double R = credit.Rate;
            double OP = credit.OverPayment;
            double F = credit.FullCreditCost;
            double PP = credit.PrimePayment;
            double AC = credit.ApartmentCost;
            // преобразовать в строку и записать в поля формы
            etPayment.setText(String.valueOf(P));
            etCreditMonthLenght.setText(String.valueOf(CML));
            etCreditAmount.setText(String.valueOf(CA));
            etRate.setText(String.valueOf(R));
            etOverPayment.setText(String.valueOf(OP));
            etFullCreditCost.setText(String.valueOf(F));
            etPrimePayment.setText(String.valueOf(PP));
            etApartmentCost.setText(String.valueOf(AC));
            // TODO добавить обработку формата данных
        }

        // Отключить кнопки
        public void disableButtons() {
            Button bt = (Button) findViewById(R.id.button51);
            bt.setEnabled(false);
            Button bt1 = (Button) findViewById(R.id.button52);
            bt1.setEnabled(false);
            Button bt2 = (Button) findViewById(R.id.button61);
            bt2.setEnabled(false);
            Button bt3 = (Button) findViewById(R.id.button62);
            bt3.setEnabled(false);
        }

        // возвратить edittext в зависимости от текущей кнопки
        // метода также заполняет свойства deltaDirection и delta чтобы два раза не писать поиск по кнопке, хотя можно сделать красивше
        // тут же заданы константы для дельт по каждому полю формы
        public EditText findEditTextOnButton(View v) {
            EditText et;
            // на случай если ничего не найдется по кесу TODO посмотреть как правильно это делать
            et = etPayment;
            deltaDirection = 0;
            AnchorID = 0;
            // конец
            switch (v.getId())
            {
                // проверяем нажатие "левых" кнопок
                case R.id.button11:
                    et = etPayment;
                    deltaDirection = -1;
                    delta = 5000;
                    AnchorID = 1;
                    break;
                case R.id.button21:
                    et = etCreditMonthLenght;
                    deltaDirection = -1;
                    delta = 12;
                    AnchorID = 2;
                    break;
                case R.id.button31:
                    et = etCreditAmount;
                    deltaDirection = -1;
                    delta = 100000;
                    AnchorID = 3;
                    break;
                case R.id.button41:
                    et = etRate;
                    deltaDirection = -1;
                    delta = 0.01;
                    AnchorID = 4;
                    break;
                case R.id.button71:
                    et = etPrimePayment;
                    deltaDirection = -1;
                    delta = 10000;
                    AnchorID = 5;
                    break;
                case R.id.button81:
                    et = etApartmentCost;
                    deltaDirection = -1;
                    delta = 100000;
                    AnchorID = 6;
                    break;
                // проверить нажатие "правых" кнопок
                case R.id.button12:
                    et = etPayment;
                    deltaDirection = 1;
                    delta = 5000;
                    AnchorID = 1;
                    break;
                case R.id.button22:
                    et = etCreditMonthLenght;
                    deltaDirection = 1;
                    delta = 12;
                    AnchorID = 2;
                    break;
                case R.id.button32:
                    et = etCreditAmount;
                    deltaDirection = 1;
                    delta = 100000;
                    AnchorID = 3;
                    break;
                case R.id.button42:
                    et = etRate;
                    deltaDirection = 1;
                    delta = 0.01;
                    AnchorID = 4;
                    break;
                case R.id.button72:
                    et = etPrimePayment;
                    deltaDirection = 1;
                    delta = 10000;
                    AnchorID = 5;
                    break;
                case R.id.button82:
                    et = etApartmentCost;
                    deltaDirection = 1;
                    delta = 100000;
                    AnchorID = 6;
                    break;
            }
            return et;
        }

        // Метод изменяет значение в контролях вида EditText на заданную дельту и по заданному направлению +1 -увеличиваем или -1 -уменьшаем. Имитация бегунка
        // при изменении значения в EditText ак же активируется перестроение чек-боксов якорей
        public void editTextValueChange(int buttonType, double delta, EditText et, int AnchorID) {
            // Если увеличить значение в реквизите, то buttonType будет +1
            // Если уменьшить значение в реквизите, то buttonType будет -1
            // double P = buttonType + delta; // в процентах на сколько изменить. Пример 1 + 0.1 = 1.1 или 110% от текущего значения
            // получить текуще значение из eidttext
            String etValue = et.getText().toString().trim();
            double d_etValue;
            try {
                d_etValue = Double.parseDouble(etValue);
            } catch(NumberFormatException ex)
            {
                d_etValue = 0;
            }
            // изменить значение на дельту и записать обратно в реквизит
            d_etValue = d_etValue + delta * buttonType; // пример: 40 000 + 5 000 * -1
            etValue = String.valueOf(d_etValue);
            // записать значение в edittext
            et.setText(etValue);
            // переставить галочки якорей
            this.setViewAutoAnchor(AnchorID); // TODO доделать хранилище супрякорей
        }

        // выставление чек-боксов в зависимости от приоритетов якорей
        public void setViewAutoAnchor(int AnchorID) {
        /* таблица якорей
        id = 1
        от Payment
        автоякори и приоритеты по снятию (сначала снимаем те что слева): R, CML

        id = 2
        от CML
        автоякори: P, R

        id = 3
        от CA
        автоякори: CML, R

        id = 4
        от R
        автоякори: CML, CA

        id = 5
        от PP
        автоякори: AC, R, P

        id = 6
        от AC
        автоякори: PP, R, CML
        */
            if (AnchorID == 1) {
                // сбросить чек боксы
                this.dropCheckBoxes();
                // заполнить нужные нам
                CheckBox cbMain = (CheckBox) findViewById(R.id.checkBox1);
                CheckBox cbAuto2 = (CheckBox) findViewById(R.id.checkBox4);
                CheckBox cbAuto3 = (CheckBox) findViewById(R.id.checkBox2);
                cbMain.setChecked(true);
                cbAuto2.setChecked(true);
                cbAuto3.setChecked(true);
            }
            if (AnchorID == 2) {
                // сбросить чек боксы
                this.dropCheckBoxes();
                // заполнить нужные нам
                CheckBox cbMain = (CheckBox) findViewById(R.id.checkBox2);
                CheckBox cbAuto2 = (CheckBox) findViewById(R.id.checkBox4);
                CheckBox cbAuto3 = (CheckBox) findViewById(R.id.checkBox1);
                cbMain.setChecked(true);
                cbAuto2.setChecked(true);
                cbAuto3.setChecked(true);
            }
            if (AnchorID == 3) {
                // сбросить чек боксы
                this.dropCheckBoxes();
                // заполнить нужные нам
                CheckBox cbMain = (CheckBox) findViewById(R.id.checkBox3);
                CheckBox cbAuto2 = (CheckBox) findViewById(R.id.checkBox4);
                CheckBox cbAuto3 = (CheckBox) findViewById(R.id.checkBox2);
                cbMain.setChecked(true);
                cbAuto2.setChecked(true);
                cbAuto3.setChecked(true);
            }
            if (AnchorID == 4) {
                // сбросить чек боксы
                this.dropCheckBoxes();
                // заполнить нужные нам
                CheckBox cbMain = (CheckBox) findViewById(R.id.checkBox4);
                CheckBox cbAuto2 = (CheckBox) findViewById(R.id.checkBox3);
                CheckBox cbAuto3 = (CheckBox) findViewById(R.id.checkBox2);
                cbMain.setChecked(true);
                cbAuto2.setChecked(true);
                cbAuto3.setChecked(true);
            }
            if (AnchorID == 5) {
                // сбросить чек боксы
                this.dropCheckBoxes();
                // заполнить нужные нам
                CheckBox cbMain = (CheckBox) findViewById(R.id.checkBox7);
                CheckBox cbAuto2 = (CheckBox) findViewById(R.id.checkBox4);
                CheckBox cbAuto3 = (CheckBox) findViewById(R.id.checkBox1);
                cbMain.setChecked(true);
                cbAuto2.setChecked(true);
                cbAuto3.setChecked(true);
            }
            if (AnchorID == 6) {
                // сбросить чек боксы
                this.dropCheckBoxes();
                // заполнить нужные нам
                CheckBox cbMain = (CheckBox) findViewById(R.id.checkBox8);
                CheckBox cbAuto2 = (CheckBox) findViewById(R.id.checkBox4);
                CheckBox cbAuto3 = (CheckBox) findViewById(R.id.checkBox2);
                cbMain.setChecked(true);
                cbAuto2.setChecked(true);
                cbAuto3.setChecked(true);
            }
        }

        public void dropCheckBoxes() {     // TODO сделать нормальный цикл
            CheckBox cb1 = (CheckBox) findViewById(R.id.checkBox1);
            CheckBox cb2 = (CheckBox) findViewById(R.id.checkBox2);
            CheckBox cb3 = (CheckBox) findViewById(R.id.checkBox3);
            CheckBox cb4 = (CheckBox) findViewById(R.id.checkBox4);
            CheckBox cb5 = (CheckBox) findViewById(R.id.checkBox5);
            CheckBox cb6 = (CheckBox) findViewById(R.id.checkBox6);
            CheckBox cb7 = (CheckBox) findViewById(R.id.checkBox7);
            CheckBox cb8 = (CheckBox) findViewById(R.id.checkBox8);
            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(false);
            cb4.setChecked(false);
            cb5.setChecked(false);
            cb6.setChecked(false);
            cb7.setChecked(false);
            cb8.setChecked(false);
        }
    }

    /*// Отслеживаем изменения в текстовом поле. Цифры будут вводиться с десятых долей т.е. 0,01 0,12 1,23 и т.д.
    class NumberTextWatcher implements TextWatcher {

        boolean mEditing;

        public NumberTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {
            if (!mEditing) {
                mEditing = true;
                // Strip symbols
                String digits = s.toString().replaceAll("\\D", "");
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMinimumFractionDigits(2);
                try {
                    String formatted = nf.format(Double.parseDouble(digits) / 100);
                    s.replace(0, s.length(), formatted);
                } catch (NumberFormatException nfe) {
                    s.clear();
                }
                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }*/

    // ряд кнопок " < " и " > " на главном экране, уменьшают и увеличивают значение в реквизите
    public void onClick_right(View v) {
        // свойства метода
        int buttonType;     // если +1 будем увеличивать значение в реквизите, если -1 будем уменьшать на шаг
        double delta;       // в долях процентов на сколько будем изменять значение в + или в - сторону
        int AnchorID = 0;       // ID пользовательской ситуации, см описание в setViewAutoAnchor()

        /* алгоритм
        1. определить кнопку
        2. определить направление
        3. определить дельту
        4. определить тип пользовательского действия
        5. создать кредит и вызвать перерасчет
        */

        // определить нужный EditText в зависимости от нажатой сейчас кнопки
        // он же обозначет delta deltadirection и AnchorID
        EditText et = mainForm.findEditTextOnButton(v);
        // вызывать метод, который заполняет значение в editText, параметры - направление изменения +/-, дельта смещения, ссылка на контроль, ИД сценария
        mainForm.editTextValueChange(mainForm.deltaDirection, mainForm.delta, et, mainForm.AnchorID);
        // пересчитываем кредит после изменения данных на форме
        Credit MyCredit = new Credit();
        // заполним кредит данными с формы
        mainForm.getCreditFromForm(MyCredit);
        // расчитаем изменения по методу AnchorID
        MyCredit.creditCalculate(mainForm.AnchorID);
        // заполним форму данными с кредита
        mainForm.setFormFromCredit(MyCredit);
    }

    // Вывести диалог при нажатии кнопки CALCULATE
    public void onClick(View v) {   //TODO нормальное название для метода кнопки расчета
        Credit MyCredit = new Credit();
        MyCredit.dropCredit();
        mainForm.setFormFromCredit(MyCredit);
        mainForm.dropCheckBoxes();
        /*
        // РАБОЧИЙ ДИАЛОГ
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ваш расчет")
                .setMessage("ежемесячный платеж: " + MyCredit.getPayment() + "\n" +
                        "срок кредита: " + MyCredit.getCreditMonthLenght() + "\n"+
                        "сумма кредита: " + MyCredit.getCreditAmount() + "\n" +
                        "процентная ставка: " + MyCredit.getRate() + "\n"+
                        "сумма переплаты: " + MyCredit.getOverPayment() +"\n"+
                        "общая стоимость кредита: " + MyCredit.getFullCreditCost() +"\n"+
                        "первоначальный взнос: " +  MyCredit.getPrimePayment()  + "\n" +
                        "max стоимость квартиры: " + MyCredit.getApartmentCost() + "\n"+
                        "Начало выплат: 22 сентября 2015\n" +
                        "Окончание выплат: 22 августа 2020\n")
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
        */
    }

    // Наш класс кредитов
    public class Credit {
        // свойства класса кредитов
        char CreditName;
        public double Payment;          // ежемесячный платеж в рублях. Пока константа, TODO зависимость от дохода
        public double CreditMonthLenght;  // длительность кредита в месяцах
        public double ApartmentCost;    // стоимость недвижимости в рублях
        public double PrimePayment;     // первоначальный взнос в рублях
        public double Rate;             // 13,5% годовая процентная ставка по кредиту. Пока константа, в будущем сделать анализатор ставок
        public double RateMonth;     // процентная ставка по кредиту в месяц
        public double FullCreditCost;  // Общая стоимость кредита (срок*платеж или сумма кредита+переплата)
        public double K;                // Коэфициент ануитета
        public double CreditAmount; // сумма кредита в рублях
        public double OverPayment; // сумма переплаты по кредиту в рублях
        public int PrimePaymentLenght;  // TODO поидее убрать это значение нужно
        public DateFormat StartDate;

        // set и get методы свойств класса
        // ежемесячный платеж в рублях
//        public String getPayment() {
//            return NumberFormat.getCurrencyInstance().format(Payment);
//        }
//        public void setPayment(double aPayment) {
//            Payment = aPayment;
//            FullCreditCost = aPayment * CreditMonthLenght;
//            PrimePayment = aPayment * PrimePaymentLenght; // TODO перенести это в get или set методы свойства PrimePayment
//            K = RateMonth * Math.pow((1 + RateMonth), CreditMonthLenght) / (Math.pow((1 + RateMonth), CreditMonthLenght)-1);
//            CreditAmount = FullCreditCost / (CreditMonthLenght * K);
//            OverPayment = FullCreditCost - CreditAmount;
//            ApartmentCost = CreditAmount + PrimePayment;
//        }
//
//        // длительность кредита
//        public double getCreditMonthLenght() {
//            return CreditMonthLenght;
//        }
//        public void setCreditMonthLenght(int aCreditMonthLenght) {
//            CreditMonthLenght = aCreditMonthLenght;
//        }
//
//        // стоимость недвижимости в руб
//        public double getApartmentCost() {
//            //return NumberFormat.getCurrencyInstance().format(ApartmentCost);
//            return ApartmentCost;
//        }
//        public void setApartmentCost(double aApartmentCost) {
//            ApartmentCost = aApartmentCost;
//        }
//
//        // первичный платеж в рублях
//        public double getPrimePayment() {
//            // return NumberFormat.getCurrencyInstance().format(PrimePayment);
//            return PrimePayment;
//        }
//        public void setPrimePayment(double aPrimePayment) {
//            PrimePayment = aPrimePayment;
//        }

        // процентная ставка. ставка подается в десятичном формате 0.135
        public double getRate() {
            return Rate;
        }
        public void setRate(double aRate) {
            Rate = aRate;
            RateMonth = aRate / 12; // при задании ставки, сразу расчитываем месячную ставку
        }

        // Сбросить кредит
        public void  dropCredit() {
            Payment = 0;
            CreditMonthLenght = 0;
            CreditAmount = 0;
            this.setRate(0);
            OverPayment = 0;
            FullCreditCost = 0;
            PrimePayment = 0;
            ApartmentCost = 0;
        }

        // Расчет кредита
        public void creditCalculate(int AnchorID) {
            // таблица якорей
            if (AnchorID == 1) {
                // id = 1
                // от Payment
                // автоякори и приоритеты по снятию (сначала снимаем те что слева): R, AC
                FullCreditCost = Payment * CreditMonthLenght;
                K = RateMonth * Math.pow((1 + RateMonth), CreditMonthLenght) / (Math.pow((1 + RateMonth), CreditMonthLenght)-1);
                CreditAmount = FullCreditCost / (CreditMonthLenght * K);
                OverPayment = FullCreditCost - CreditAmount;
                ApartmentCost = CreditAmount/(1-0.25);
                PrimePayment = 0.25 * ApartmentCost;
            }

            if (AnchorID == 2) {
                // id = 2
                // от CML
                // автоякори: P, R
                FullCreditCost = Payment * CreditMonthLenght;
                K = RateMonth * Math.pow((1 + RateMonth), CreditMonthLenght) / (Math.pow((1 + RateMonth), CreditMonthLenght)-1);
                CreditAmount = FullCreditCost / (CreditMonthLenght * K);
                OverPayment = FullCreditCost - CreditAmount;
                ApartmentCost = CreditAmount/(1-0.25);
                PrimePayment = 0.25 * ApartmentCost;
            }

            if (AnchorID == 3) {
                // id = 3
                // от CA
                // автоякори: CML, R
                K = RateMonth * Math.pow((1 + RateMonth), CreditMonthLenght) / (Math.pow((1 + RateMonth), CreditMonthLenght)-1);
                Payment = CreditAmount * K;
                FullCreditCost = Payment * CreditMonthLenght;
                OverPayment = FullCreditCost - CreditAmount;
                ApartmentCost = CreditAmount/(1-0.25);
                PrimePayment = 0.25 * ApartmentCost;
            }

            if (AnchorID == 4) {
                // id = 4
                // от R
                // автоякори: CML, CA
                K = RateMonth * Math.pow((1 + RateMonth), CreditMonthLenght) / (Math.pow((1 + RateMonth), CreditMonthLenght)-1);
                Payment = CreditAmount * K;
                FullCreditCost = Payment * CreditMonthLenght;
                OverPayment = FullCreditCost - CreditAmount;
                ApartmentCost = CreditAmount/(1-0.25);
                PrimePayment = 0.25 * ApartmentCost;
            }

            if (AnchorID == 5) {
                //  id = 5
                // от PP
                // автоякори: R, P
                ApartmentCost = PrimePayment / 0.25;
                CreditAmount = ApartmentCost - PrimePayment;
                K = Payment / CreditAmount;
                CreditMonthLenght = Math.log(K / (K - RateMonth)) / Math.log(1 + RateMonth); // log_x_от_y = ln(че надо)/ln(какое надо основание);
                FullCreditCost = Payment * CreditMonthLenght;
                OverPayment = FullCreditCost - CreditAmount;
            }

            if (AnchorID == 6) {
                // id = 6
                // от AC
                // автоякори: R, CML
                PrimePayment = ApartmentCost * 0.25;
                CreditAmount = ApartmentCost - PrimePayment;
                K = RateMonth * Math.pow((1 + RateMonth), CreditMonthLenght) / (Math.pow((1 + RateMonth), CreditMonthLenght)-1);
                Payment = CreditAmount * K;
                FullCreditCost = Payment * CreditMonthLenght;
                OverPayment = FullCreditCost - CreditAmount;
            }


//        /*// РАСЧЕТ ОТ ДОХОДА
//        // Объявляем свойства для кредита
//        MyCredit.PrimePaymentLenght = 6;
//
//        // Запонить кредит данными с формы
//        MyCredit.CreditMonthLenght = Double.valueOf(etCreditMonthLenght.getText().toString());;
//        // заполняем зависимые свойства ставка и ежемесячный платеж
//        MyCredit.setRate(Double.valueOf(etRate.getText().toString())); // FAIL
//        MyCredit.setPayment(Double.valueOf(etPayment.getText().toString()));
//
//        // Запонить форму данными кредита
//        etCreditAmount.setText(MyCredit.getCreditAmount());
//        etOverPayment.setText(MyCredit.getOverPayment());
//        etFullCreditCost.setText(MyCredit.getFullCreditCost());
//        etApartmentCost.setText(MyCredit.getApartmentCost());
//        etPrimePayment.setText(MyCredit.getPrimePayment());*/
        }
    }
}
