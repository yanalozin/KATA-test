import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("Input:");

        String result;

        try {
            result = calc(input.nextLine());

            System.out.println("Output:");

            System.out.println(result);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static String calc(String input) throws Exception {

        Input currentInput = new Input();

        String currentOperator = currentInput.getOperator(input); // получение текущего оператора

        String[] strings = input.split(currentInput.getSeparator(currentOperator), 2);

        Operand operand1 = new Operand();
        Operand operand2 = new Operand();

        operand1.value = strings[0].trim(); // получение первого операнда
        operand2.value = strings[1].trim(); // получение второго операнда

        // проверка на вхождение дополнительных операторов
        for (String operator : currentInput.operators) {
            if(operand2.value.contains(operator)) {
                throw new Exception("Формат математической операции не удовлетворяет заданию");
            }
        }

        int intOperand1;
        int intOperand2;

        boolean isArabian;

        boolean isRoman1 = operand1.isRoman();
        boolean isRoman2 = operand2.isRoman();

        if (isRoman1 && isRoman2) { // проверка операндов, являются ли римскими цифрами
            Roman romanOperand1 = Roman.valueOf(operand1.value);
            Roman romanOperand2 = Roman.valueOf(operand2.value);

            intOperand1 = romanOperand1.getValue();
            intOperand2 = romanOperand2.getValue();

            isArabian = false;
        } else if (!isRoman1 && !isRoman2) { // проверка операндов, являются ли арабскими цифрами и приведение к числу
            intOperand1 = operand1.stringToInt();
            intOperand2 = operand2.stringToInt();

            isArabian = true;
        }
        else {
            throw new Exception("Используются одновременно разные системы счисления");
        }

        // проверка операндов на вхождение в диапазон от 1 до 10
        operand1.checkOperand(intOperand1);
        operand2.checkOperand(intOperand2);

        // вычисление
        int currentResult = 0;
        switch (currentOperator) {
            case "+":
                currentResult = intOperand1 + intOperand2;
                break;
            case "-":
                currentResult = intOperand1 - intOperand2;
                break;
            case "*":
                currentResult = intOperand1 * intOperand2;
                break;
            case "/":
                currentResult = intOperand1 / intOperand2;
                break;
        }

        // перевод результата вычисления в строку
        if (isArabian) {
            return Integer.toString(currentResult);
        } else {
            return currentInput.arabianToRoman(currentResult);
        }

    }
}

class Input {

    String[] operators = {"+", "-", "*", "/"};

    // получить оператор
    String getOperator(String input) throws Exception {

        String getOperator = new String();

        for (String operator : operators) {
            if (input.contains(operator)) {
                getOperator = operator;
                break;
            }
        }
        if (getOperator.isEmpty()) {
            throw new Exception("Строка не является математической операцией");
        }
        return getOperator;
    }

    // получить сепаратор
    String getSeparator(String currentOperator) {

        String separator = new String();

        switch (currentOperator) {
            case "+":
                separator = "\\+";
                break;
            case "-":
                separator = "-";
                break;
            case "*":
                separator = "\\*";
                break;
            case "/":
                separator = "/";
                break;
        }

        return separator;
    }

    // перевести арабское число в римское
    String arabianToRoman (int value) throws Exception {

        if (value < 1) {
            throw new Exception("В римской системе нет отрицательных чисел");
        }

        String result = new String();
        if(value == 100) {
            result = "C";
            value -= 100;
        } else if (value >= 90) {
            result = "XC";
            value -= 90;
        } else if (value >= 80) {
            result = "LXXX";
            value -= 80;
        } else if (value >= 70) {
            result = "LXX";
            value -= 70;
        } else if (value >= 60) {
            result = "LX";
            value -= 60;
        } else if (value >= 50) {
            result = "L";
            value -= 50;
        } else if (value >= 40) {
            result = "XL";
            value -= 40;
        } else if (value >= 30) {
            result = "XXX";
            value -= 30;
        } else if (value >= 20) {
            result = "XX";
            value -= 20;
        } else if (value >= 10) {
            result = "X";
            value -= 10;
        }

        if(value == 1) {
            result += "I";
        } else if (value == 2) {
            result += "II";
        } else if (value == 3) {
            result += "III";
        } else if (value == 4) {
            result += "IV";
        } else if (value == 5) {
            result += "V";
        } else if (value == 6) {
            result += "VI";
        } else if (value == 7) {
            result += "VII";
        } else if (value == 8) {
            result += "VIII";
        } else if (value == 9) {
            result += "IX";
        }
        return result;
    }
}

class Operand {

    String value;

    // перевести операнд из строки в целове число
    int stringToInt() throws Exception {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            throw new Exception("Не удалось преобразовать строку к целому числу");
        }
    }

    // проверить, является ли операнд римским числом
    boolean isRoman() {
        String[] romans = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        for (String roman : romans) {

            if (value.equals(roman)) {
                return true;
            }
        }
        return false;
    }

    // проверить, входит ли операнд в диапазон от 1 до 10
    void checkOperand (int operand) throws Exception {
        if (operand < 1 || operand > 10) {
            throw new Exception("Число должно быть от 1 до 10");
        }
    }
}