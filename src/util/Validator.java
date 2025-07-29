// package util;

public class Validator {

    // Cek apakah string kosong atau null
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Cek apakah string merupakan bilangan bulat positif
    public static boolean isPositiveInteger(String value) {
        try {
            int number = Integer.parseInt(value.trim());
            return number >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validasi email sederhana
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$");
    }

    // Gabungan validasi umum untuk input teks dan angka
    public static boolean validateInput(String nama, String email, String poin) {
        return !isEmpty(nama) && isValidEmail(email) && isPositiveInteger(poin);
    }

    // Khusus validasi jumlah transaksi (jumlah > 0)
    public static boolean isValidTransactionQuantity(String value) {
        try {
            int jumlah = Integer.parseInt(value.trim());
            return jumlah > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
