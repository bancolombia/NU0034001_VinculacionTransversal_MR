package co.com.bancolombia.commonsvnt.usecase.util.constants;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;

public enum Numbers {
    ZERO("0","ZERO"),
    ONE("1","PRIMERA"),
    TWO("2","SEGUNDA"),
    THREE("3","TERCERA"),
    FOUR("4","CUARTA"),
    FIVE("5","QUINTA"),
    SIX("6","SEXTA"),
    SEVEN("7","SEPTIMA"),
    EIGHT("8","OCTAVA"),
    NINE("9","NOVENA"),
    TEN("10","DECIMA"),
    ELEVEN("11","DECIMO PRIMERA"),
    TWELVE("12","DECIMO SEGUNDA"),
    THIRTEEN("13",""),
    TWENTY("20", ""),
    THIRTY("30", ""),
    FORTY("40", ""),
    FIFTY("50", ""),
    SIXTY("60",""),
    SEVENTY("70", ""),
    EIGHTY("80", ""),
    NINETY("90", ""),
    ONE_HUNDRED("100", ""),
    TWO_HUNDRED("200", ""),
    THREE_HUNDRED("300", ""),
    FOUR_HUNDRED("400", ""),
    FIVE_HUNDRED("500", ""),
    SIX_HUNDRED("600", ""),
    SEVEN_HUNDRED("700", ""),
    EIGHT_HUNDRED("800", ""),
    NINE_HUNDRED("900", ""),
    THOUSAND("1000", "");

    private String number;
    private String literal;

    Numbers(String number, String literal) {
        this.number = number;
        this.literal = literal;
    }

    public String getNumber() {
        return number;
    }
    public int getIntNumber() {
        return Integer.parseInt(number);
    }

    public String getLiteral(){
        return literal;
    }

    public static String findByNumber(int number) {
        for(Numbers item : values()) {
            if(item.getIntNumber()==number){
                return item.literal;
            }
        }
        return EMPTY;
    }
}